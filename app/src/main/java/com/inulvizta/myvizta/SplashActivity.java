package com.inulvizta.myvizta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    public static String serverIP = "", indexUser = "", nomorHandphone = "", namaUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* script buat bikin fullscreen mulai disini */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* script buat bikin fullscreen selesai disini */

        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("INVIZ_PREF", MODE_PRIVATE);
        serverIP = sharedPreferences.getString("SERVER_IP","");
        indexUser = sharedPreferences.getString("INDEX_USER", "");
        nomorHandphone = sharedPreferences.getString("MOBILE_NUMBER", "");
        namaUser = sharedPreferences.getString("CUSTOMER_NAME", "");

        if(TextUtils.isEmpty(serverIP)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SERVER_IP","192.168.4.45");
            editor.apply();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(indexUser)) { //kalau belum pernah login, bawa ke halaman LOGIN
                    Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                else{ //kalau sudah pernah login sebelumnya, bawa ke halaman HOME
                    Intent intentHome = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                }
                finish();
            }
        }, 3000);
    }
}