package com.example.eatforwhat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class LoginMethodActivity extends AppCompatActivity {

    Button btn_login, btn_signup, btn_guest;
    FirebaseAuth fAuth;
    Activity loginmethod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginmethod);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        btn_guest = (Button)findViewById(R.id.btn_guest);
        fAuth=FirebaseAuth.getInstance();

        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMethodActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMethodActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginMethodActivity.this,MainActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }
}
