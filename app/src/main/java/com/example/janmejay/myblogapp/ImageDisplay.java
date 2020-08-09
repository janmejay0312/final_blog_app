package com.example.janmejay.myblogapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ImageDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        ImageView image =  findViewById(R.id.display);
        Bundle extras = getIntent().getExtras();
        String image1 = extras.getString("picture");
       // Bitmap bitmap = decodeImage(image1);
        ActionBar a=getSupportActionBar();
        a.setTitle("Picture");
       // Bitmap bmp = (Bitmap)getIntent().getParcelableArrayExtra("picture");
       // ImageView image =  findViewById(R.id.display);
        Glide.with(getApplicationContext()).load(image1).into(image);
       // image.setImageBitmap(bitmap);

    /*   if(getIntent().hasExtra("byteArray")){
           imageView =new ImageView(this);
           Bitmap bitmap= BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
           imageView.setImageBitmap(bitmap);/
       }
     /*   Bundle extras = new Bundle();
        extras.putParcelable("imagebitmap", image);
        intent.putExtras(extras);
        startActivity(intent);*/
    }

    private Bitmap decodeImage(String image1) {
        byte[] b = Base64.decode(image1, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bmp;
    }
}
/*if(getIntent().hasExtra("byteArray")) {
    ImageView previewThumbnail = new ImageView(this);
    Bitmap b = BitmapFactory.decodeByteArray(
        getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
    previewThumbnail.setImageBitmap(b);
}*/