package com.example.janmejay.myblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.PrivateKey;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
private EditText yName,yEmail,yPassword;
private Button register;
private FirebaseAuth firebaseAuth;
private DatabaseReference firebaseDatabase;
private ProgressDialog mprogressBar;
   public String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        yName=findViewById(R.id.name);
        yEmail=findViewById(R.id.Email);
        yPassword=findViewById(R.id.password);
        register=findViewById(R.id.register);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("User");
        ActionBar c=getSupportActionBar();
        assert c!=null;
        c.setHomeButtonEnabled(true);
        c.setDisplayHomeAsUpEnabled(true);
        mprogressBar=new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startForRegister();
            }
        });
    }

    private void startForRegister() {
        final String sName=yName.getText().toString();
        String sEmail=yEmail.getText().toString();
        String sPassword=yPassword.getText().toString();


        if(!TextUtils.isEmpty(sName)&&!TextUtils.isEmpty(sEmail)&&!TextUtils.isEmpty(sPassword))
        {  mprogressBar.setMessage("signing up");
        mprogressBar.show();
            firebaseAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final String id1= UUID.randomUUID().toString();
                        s1=firebaseAuth.getCurrentUser().getUid();
     DatabaseReference databaseReference=firebaseDatabase.child(s1);

     databaseReference.child("name").setValue(sName);
     databaseReference
             .child("id").setValue(id1);
     mprogressBar.dismiss();
    Intent lIntent=new Intent(RegisterActivity.this,MainActivity.class);
    lIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(lIntent);
    finish();
                    }
                }
            });

        }
    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, LoginActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
    public  String register1(){
        return s1;
    }
}
