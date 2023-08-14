package com.inulvizta.myvizta;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LoginActivity extends AppCompatActivity {
    Integer isLanjutLogin = 0;
    String tokenOTP = "",
            url = "http://"+SplashActivity.serverIP+"/vizta/pusat/loginapp";
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private void cekLogin(String nomor){
        mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        mStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(android.R.id.content), "Tidak dapat membuka koneksi ke server", Snackbar.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hp", nomor);
                return params;
            }
        };
        mRequestQueue.add(mStringRequest);
    }

    private void parseData(String data){
        TextView txtlogin = (TextView) findViewById(R.id.tvLogin);
        TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.outlinedNomorHP);
        LinearLayout otpLayout = (LinearLayout) findViewById(R.id.OTPLayout);
        MaterialButton btnNext = (MaterialButton) findViewById(R.id.btnNextLogin);
        TextView txtRegister = (TextView) findViewById(R.id.tvRegister);

        try{
            JSONObject jsonObject = new JSONObject(data);
            String indexUser = jsonObject.getString("id");
            String namaUser = jsonObject.getString("nm");
            String hpUser = jsonObject.getString("ha");

            if(indexUser.toString().equals("0")){ //nomor HP tidak ada
                Snackbar.make(findViewById(android.R.id.content), "Nomor HP tidak terdaftar", Snackbar.LENGTH_LONG).show();
            }
            else{ //nomor HP ada ditemukan

                SharedPreferences sharedPreferences = getSharedPreferences("INVIZ_PREF", MODE_PRIVATE);
                SplashActivity.indexUser = indexUser;
                SplashActivity.nomorHandphone = hpUser;
                SplashActivity.namaUser = namaUser;

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("INDEX_USER", indexUser);
                editor.putString("MOBILE_NUMBER", hpUser);
                editor.putString("CUSTOMER_NAME", namaUser);
                editor.apply();

                Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intentHome);
                finish();

                //create random OTP
                /*tokenOTP = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

                txtlogin.setText("Kami sudah mengirimkan OTP ke Whatsapp anda.\nSilahkan masukkan kode OTP dibawah");
                inputLayout.setVisibility(View.INVISIBLE);
                otpLayout.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);
                txtRegister.setVisibility(View.GONE);*/
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* script buat bikin fullscreen mulai disini */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* script buat bikin fullscreen selesai disini */

        setContentView(R.layout.activity_login);

        EditText txtOTP1 = (EditText) findViewById(R.id.txtOTP1);
        EditText txtOTP2 = (EditText) findViewById(R.id.txtOTP2);
        EditText txtOTP3 = (EditText) findViewById(R.id.txtOTP3);
        EditText txtOTP4 = (EditText) findViewById(R.id.txtOTP4);
        EditText txtOTP5 = (EditText) findViewById(R.id.txtOTP5);
        EditText txtOTP6 = (EditText) findViewById(R.id.txtOTP6);

        TextView txtlogin = (TextView) findViewById(R.id.tvLogin);
        txtlogin.setText("silahkan login untuk dapat menikmati semua fiturnya");

        TextInputLayout inputLayout = (TextInputLayout) findViewById(R.id.outlinedNomorHP);
        inputLayout.setVisibility(View.VISIBLE);

        LinearLayout otpLayout = (LinearLayout) findViewById(R.id.OTPLayout);
        otpLayout.setVisibility(View.GONE);

        TextView txtRegister = (TextView) findViewById(R.id.tvRegister);
        txtRegister.setMovementMethod(LinkMovementMethod.getInstance());
        txtRegister.setVisibility(View.VISIBLE);

        MaterialButton btnNext = (MaterialButton) findViewById(R.id.btnNextLogin);
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText txtNomorHP = (TextInputEditText) findViewById(R.id.txtNomorHP);
                String NomorHP = txtNomorHP.getText().toString();

                if(TextUtils.isEmpty(NomorHP.trim())){
                    Snackbar.make(findViewById(android.R.id.content), "Nomor HP harus diisi", Snackbar.LENGTH_LONG).show();
                }
                else{
                    /* validasi nomor hp dimulai disini */
                    cekLogin(NomorHP);
                    /* validasi nomor hp selesai disini */


                    /*txtlogin.setText("Kami sudah mengirimkan OTP ke Whatsapp anda.\nSilahkan masukkan kode OTP dibawah");
                    inputLayout.setVisibility(View.INVISIBLE);
                    otpLayout.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.GONE);
                    txtRegister.setVisibility(View.GONE);*/
                }
            }
        });

        txtOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    txtOTP2.requestFocus();
                }
            }
        });

        txtOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    txtOTP3.requestFocus();
                }
                else{
                    txtOTP1.requestFocus();
                }
            }
        });

        txtOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    txtOTP4.requestFocus();
                }
                else{
                    txtOTP2.requestFocus();
                }
            }
        });

        txtOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    txtOTP5.requestFocus();
                }
                else{
                    txtOTP3.requestFocus();
                }
            }
        });

        txtOTP5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    txtOTP6.requestFocus();
                }
                else{
                    txtOTP4.requestFocus();
                }
            }
        });

        txtOTP6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    //lakukan validasi disini
                }
                else{
                    txtOTP5.requestFocus();
                }
            }
        });
    }
}