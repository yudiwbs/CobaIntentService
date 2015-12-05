package yudiwbs.cs.upi.edu.cobaintentservice2;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class CobaIntentService extends IntentService {


    //handler untuk berkomunikasi dengan UI thread
    Handler mHandler;


    public CobaIntentService() {
        super("cobaservice");  //untuk debug
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //ambil data dari intent

        String pesan;
        Bundle extras = intent.getExtras();
        if(extras == null) {
            pesan= null;
        } else {
            pesan=extras.getString("PESAN");
        }


        //proses background ada disini
        mHandler.post(new DisplayToast(this, "Service mulai.... Pesan="+pesan));

        //init notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        //notifikasi dengan progressbar
        NotificationCompat.Builder notifBuilderProgress =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Progress")
                        .setContentText("Sedang proses");

        //loop proses yang panjang
        for (int i = 0; i<100;i++) {
            try {
                Thread.sleep(50);  //simulasi proses yang panjang
                //max diisi 100, false artinya kita tahu selesainya kapan
                notifBuilderProgress.setProgress(100, i, false);
                // tampilkan progress bar
                // 998 adalah id, bisa diganti dengan nilai lain
                mNotificationManager.notify(998, notifBuilderProgress.build());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // update notif
        // buang progress bar
        notifBuilderProgress.setContentText("Proses selesai").setProgress(0,0,false);
        mNotificationManager.notify(998, notifBuilderProgress.build());

        mHandler.post(new DisplayToast(this, "Service selesai"));

        //kirim pesan ke activity
        //menggunakan LocalBroadcast
        Intent localIntent = new Intent(MainActivity.ACTION_TERIMA);
        localIntent.putExtra("PESAN", "Sudah selesai!");
        //menggunakan LocalBroadCastManager agar pengiriman dijamin tidak bisa dilihat app lain
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        //buat notifikasi yang jika di tap akan kembali ke activity

        //siapkan notif builder
        NotificationCompat.Builder notifBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Selesai! Tap untuk menampilkan app");

        // explisit intent untuk memulai activity
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Gunakan taskstack agar saat user tekan  tombol back tetap konsisten
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notifBuilder.setContentIntent(resultPendingIntent);
        notifBuilder.setAutoCancel(true);//agar setelah ditap tutup otomatis


        // 999 id untuk jika perlu modif nanti (bagusnya jadi konstanta).
        mNotificationManager.notify(999, notifBuilder.build());



        // Sets the progress indicator to a max value, the
        // current completion percentage, and "determinate"
        // state


    }
}
