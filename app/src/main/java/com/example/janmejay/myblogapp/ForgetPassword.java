package com.example.janmejay.myblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ForgetPassword extends AppCompatActivity {
private EditText registeredMail;
private Button   sendLink;
private Button   changePassword;
private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        registeredMail=findViewById(R.id.registered_email);
        sendLink=findViewById(R.id.send_link);
        changePassword=findViewById(R.id.change_password);
        firebaseAuth=FirebaseAuth.getInstance();
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ForgetPassword.this,Change_Password.class);
                startActivity(intent);
            }
        });
        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLinkToUser();
            }
        });
    }
    public void sendLinkToUser(){
        final ProgressDialog progressDialog = new ProgressDialog(ForgetPassword.this);
        progressDialog.setMessage("varifying..");
        progressDialog.show();
        String providedMail=registeredMail.getText().toString();

        firebaseAuth.sendPasswordResetEmail(providedMail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent=new Intent(ForgetPassword.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Password reset link is sent",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPassword.this, "invalid mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
