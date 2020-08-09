package com.example.janmejay.myblogapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


//this is edit activity

public class Edit_Activity extends AppCompatActivity {
    private TextView gData,mDes,gTime;
    private ImageView gImage;
private DatabaseReference databaseRef,rDatabase;
private static final int GALLERY_INTENT=2;
private boolean isImageFitToScreen;
Context context;
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
        b.setTitle("description");
        assert b!=null;
        b.setHomeButtonEnabled(true);
        b.setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        String Des = bundle.getString("description");
        String title=bundle.getString("title");
        String time=bundle.getString("time");
        final String image=bundle.getString("image");
        gImage.setScaleType(ImageView.ScaleType.FIT_XY);
      /*  Bitmap bitmap=BitmapFactory.decode(image);
        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);*/
        Glide.with(this).load(image).into(gImage);
        gData.setText(Des);
        mDes.setText(title);
        gTime.setText(time);
        gImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.blog);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();*/
                //gImage.buildDrawingCache();
                //Bitmap bitmap=gImage.getDrawingCache();
                Intent intent = new Intent(Edit_Activity.this, ImageDisplay.class);
                intent.putExtra("picture", image);
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