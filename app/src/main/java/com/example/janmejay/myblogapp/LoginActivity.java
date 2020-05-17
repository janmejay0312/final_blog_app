package com.example.janmejay.myblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
       private EditText fEmail,fPassword;
       private TextView newUser;
       private Button mLogin;
       private FirebaseAuth firebaseAuth;
       private   DatabaseReference databaseReference;
       private ProgressDialog mprogressBar;
       private Button forgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fEmail=findViewById(R.id.Email);
        fPassword=findViewById(R.id.password);
        newUser=findViewById(R.id.new_user);
        mLogin=findViewById(R.id.Login);
        forgotPassword=findViewById(R.id.forgot_password);
        mprogressBar=new ProgressDialog(this);
        //onBackPressed();
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("User");
        mLogin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  checkLogin();
              }
         });
        newUser.setOnClickListener(new View.OnClickListener() {
    @Override
             public void onClick(View v) {
                 Intent in=new Intent(LoginActivity.this,RegisterActivity.class);
                 startActivity(in);
                 finish();
             }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(intent);
          }
        });

    }

    private void checkLogin() {
        String s=fEmail.getText().toString().trim();
        String p=fPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(s)&&!TextUtils.isEmpty(p))
        {
            mprogressBar.setMessage("Loging in...");
            mprogressBar.show();
            firebaseAuth.signInWithEmailAndPassword(s,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExistence();
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });
            mprogressBar.dismiss();
        }
        else
        {
            if(TextUtils.isEmpty(s))
                fEmail.setError("fill email");
            if (TextUtils.isEmpty(p))
                fPassword.setError("fill password");
        }


    }

    private void checkUserExistence() {
      final   String q=firebaseAuth.getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(q)){
                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                    if(firebaseUser.isEmailVerified()) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(LoginActivity.this, "Please verify email first...", Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(LoginActivity.this,"does not exist",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
