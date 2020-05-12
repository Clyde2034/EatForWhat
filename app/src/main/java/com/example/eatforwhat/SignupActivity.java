package com.example.eatforwhat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

public class SignupActivity extends AppCompatActivity {

    EditText mMail, mPassword, mName;
    Button mSignupbtn;
    RadioButton mRadiobtn_man, mRadiobtn_woman;
    RadioGroup mRadiogroup_gender;
    TextView mUserrules;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    String gender = "";
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        UIfindID();
        UserRules();

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRadiogroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobtn_man:
                        gender = mRadiobtn_man.getText().toString().trim();
                        break;
                    case R.id.radiobtn_woman:
                        gender = mRadiobtn_woman.getText().toString().trim();
                        break;
                }
            }
        });

        mSignupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mMail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String name = mName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mMail.setError("郵件不能為空白!");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("密碼不能為空白!");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    mName.setError("密碼不能為空白!");
                    return;
                }

                if (password.length() <= 6) {
                    mPassword.setError("密碼長度必須大於六碼!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //建立Firebase帳號驗證
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "User Created.", Toast.LENGTH_LONG).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("fName", name);
                            user.put("fEmail", email);
                            user.put("fPassword", password);
                            user.put("fGender", gender);
                            documentReference.set(user);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "User Created Error." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void UserRules() {
        mUserrules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SignupActivity.this)
                        .setMessage("用戶協議")
                        .setPositiveButton("確定", null)
                        .show();
            }
        });
        mUserrules.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void UIfindID() {
        mMail = findViewById(R.id.et_mail);
        mPassword = findViewById(R.id.et_password);
        mName = findViewById(R.id.et_name);
        mSignupbtn = findViewById(R.id.btn_signup);
        mRadiobtn_man = findViewById(R.id.radiobtn_man);
        mRadiobtn_woman = findViewById(R.id.radiobtn_woman);
        mUserrules = findViewById(R.id.tv_userrules);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mRadiogroup_gender = findViewById(R.id.radiogroup_gender);
    }
}
