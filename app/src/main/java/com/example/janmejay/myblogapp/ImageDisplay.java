package com.example.janmejay.myblogapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImageDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);


        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image =  findViewById(R.id.display);

        image.setImageBitmap(bmp);

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
}
/*if(getIntent().hasExtra("byteArray")) {
    ImageView previewThumbnail = new ImageView(this);
    Bitmap b = BitmapFactory.decodeByteArray(
        getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
    previewThumbnail.setImageBitmap(b);
}*/