package com.example.janmejay.myblogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {
    private ImageButton addImage;
    private Button submit, update;
    private EditText title;
    private EditText description;
    private Uri uri = null;
    private String string;
    private Intent intent;
    private FirebaseAuth firebaseAuth1;
    private static final int GALLERY_REQUEST = 1;
    private static final int GALLERY_REQUEST1 = 1;
    private DatabaseReference mDatabase, rdatabase;
    private StorageReference mstorage;
    private StorageReference storageReference;
    private ProgressDialog progressBar;
    private DatabaseReference databaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
     private String id2;
     public String id1,id4;
     private String s2;
    final int[] t = {0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setTitle("Post");
        addImage = findViewById(R.id.imageview);
        submit = findViewById(R.id.button);
        title = findViewById(R.id.editText);
        s2=user.getUid();
        firebaseAuth1=FirebaseAuth.getInstance();
        ActionBar a=getSupportActionBar();
        assert a != null;
        a.setHomeButtonEnabled(true);
        a.setDisplayHomeAsUpEnabled(true);
        description = findViewById(R.id.editText2);
        mstorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Your_Blog");
        intent = getIntent();
        if (intent != null) {
            if(getIntent().getStringExtra("rowId")!=null){
                submit.setText("update");
            }else {
                submit.setText("submit");
            }
            if (intent.getStringExtra("description") != null) {
                String Des = intent.getStringExtra("description");
                description.setText(Des);
            }
            if (intent.getStringExtra("title") != null) {
                String mTitle = intent.getStringExtra("title");
                title.setText(mTitle);
            }
            if (intent.getStringExtra("image") != null) {
                String image = intent.getStringExtra("image");
                Glide.with(this).load(image).into(addImage);
            }
        }
        progressBar = new ProgressDialog(this);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("rowId")!=null){
                   startUpdating();
                }else {
                    startPosting();
                }
            }

        });


    }

    private void startUpdating() {
        progressBar.setMessage("Updating...");
        progressBar.show();

        DatabaseReference ref=mDatabase.child(getIntent().getStringExtra("rowId"));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String time= formatter.format(date);
        ref.child("time").setValue(time);
        ref.child("description").setValue(description.getText().toString().trim());
        ref.child("title").setValue(title.getText().toString().trim());
        FirebaseStorage.getInstance().getReference().child("Photos").child(uri.getLastPathSegment()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FirebaseStorage.getInstance().getReference().child("Photos").child(uri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase.getInstance().getReference().child("Your_Blog").child(getIntent().getStringExtra("rowId")).child("image").setValue(uri.toString());
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
                uri = data.getData();
                addImage.setImageURI(uri);
            }


    }

    private void startPosting() {
        progressBar.setMessage("Uploading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.show();
        progressBarStatus = 0;
        fileSize = 0; new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {
                    // performing operation
                    progressBarStatus = doOperation();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // Updating the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }
                // performing operation if file is downloaded,
                if (progressBarStatus >= 100) {
                    // sleeping for 1 second after operation completed
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // close the progress bar dialog
                    progressBar.dismiss();
                }
            }
        }).start();
        final String s = title.getText().toString().trim();
        final String q = description.getText().toString().trim();
        if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(q) && uri != null) {
             storageReference = mstorage.child("Photos").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
 id1=UUID.randomUUID().toString();


databaseReference=mDatabase.child(id1);
            DatabaseReference       firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("User");
                    firebaseDatabase.child(s2).child("id").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            id2=snapshot.getValue(String.class);

                            databaseReference.child("user_id").setValue(id2);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });

      databaseReference.child("id").setValue(id1);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String time= formatter.format(date);
                    databaseReference.child("time").setValue(time);
                    databaseReference.child("title").setValue(s);
                    databaseReference.child("description").setValue(q);
                  databaseReference.child("like").setValue(0);
                    databaseReference.child("dislike").setValue(0);
                    progressBar.dismiss();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child("image").setValue(uri.toString());
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                }


            });


        }


    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }
    public int doOperation() {
        //The range of ProgressDialog starts from 0 to 10000
        while (fileSize <= 10000) {
            fileSize++;
            if (fileSize == 1000) {
                return 10;
            } else if (fileSize == 2000) {
                return 20;
            } else if (fileSize == 3000) {
                return 30;
            } else if (fileSize == 4000) {
                return 40; // you can add more else if
            }
         /* else {
                return 100;
            }*/
        }//end of while
        return 100;
    }//end of doOperation
}
