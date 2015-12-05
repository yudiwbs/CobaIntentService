package yudiwbs.cs.upi.edu.cobaintentservice2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {

    Intent serviceIntent;
    TextView tvHasil;

    //konstanta yang mendefinisikan aksi
    public static final String  ACTION_TERIMA = "aksi-cobaservice";

    //definisikan aksi jika mendapat broadcast
    private  class MyBroadcastReceiver extends BroadcastReceiver {

        //terima pesan, update UI
        //tapi jika app berada di background, tidak akan terupdate
        @Override
        public void onReceive(Context context, Intent intent) {
            // ambil extra data yang ada Intent
            String pesan = intent.getStringExtra("PESAN");
            //tulis pesan di textview
            tvHasil.setText(pesan);
        }
    }

    private MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHasil = (TextView) findViewById(R.id.tvHasil);
        // handler untuk menerima Intents.
        mReceiver = new MyBroadcastReceiver();
    }

    public void klikMulai(View v) {
        //buat intent
        serviceIntent = new Intent(this, CobaIntentService.class);
        serviceIntent.putExtra("PESAN", "hello world"); //data yang dikirim
        startService(serviceIntent); //mulai jalankan services
    }

    @Override
    protected void onResume() {
        // onResume juga dipanggil saat app dimulai
        // daftar broadcastreciver untuk menerima intent.
        // tapi khusus untuk action ACTION_TERIMA yang sudah kita definisikan
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mReceiver, new IntentFilter(ACTION_TERIMA));
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Unregister karena activity dipause.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onPause();
    }


}
