package com.example.janmejay.myblogapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;



//this is edit activity

public class Edit_Activity extends AppCompatActivity {
    private TextView gData,mDes,gTime;
    private ImageView gImage;
private DatabaseReference databaseRef,rDatabase;
private static final int GALLERY_INTENT=2;
private boolean isImageFitToScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_);
        gData = findViewById(R.id.textView);
        mDes = findViewById(R.id.textView2);
        gImage=findViewById(R.id.imageView);
        gTime=findViewById(R.id.time1);
        databaseRef=FirebaseDatabase.getInstance().getReference();

        ActionBar b=getSupportActionBar();
        assert b!=null;
        b.setHomeButtonEnabled(true);
        b.setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        String Des = bundle.getString("description");
        String title=bundle.getString("title");
        String time=bundle.getString("time");
        String image=bundle.getString("image");
        Glide.with(this).load(image).into(gImage);
        gData.setText(Des);
        mDes.setText(title);
        gTime.setText(time);
        gImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.blog);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Intent intent = new Intent(Edit_Activity.this, ImageDisplay.class);
                intent.putExtra("picture", byteArray);
                startActivity(intent);
            }
        });

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
}
   /* Bitmap b; // your bitmap
    ByteArrayOutputStream bs = new ByteArrayOutputStream();
b.compress(Bitmap.CompressFormat.PNG, 50, bs);
        i.putExtra("byteArray", bs.toByteArray());
        startActivity(i);*/