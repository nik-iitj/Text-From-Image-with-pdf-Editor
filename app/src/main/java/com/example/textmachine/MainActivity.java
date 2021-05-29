package com.example.textmachine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView cam,file,pdf;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen = false;
    Bitmap k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingBtn);
        cam = findViewById(R.id.camera);
        file = findViewById(R.id.fileImg);
        pdf = findViewById(R.id.filePdf);


        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward= AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();

            }
        });

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Intent intent = new Intent(getApplicationContext(),FromCamDetect.class);
                startActivity(intent);
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select an image"),100);


            }
        });


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CreatePdf.class);
                startActivity(intent);


            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK){
            if(data.getData()!=null){

                Uri uri = data.getData();

                try {
                    k= MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String filepath= tempFileImage(MainActivity.this,k,"photo");


                Intent intent = new Intent(MainActivity.this,TextFromImage.class);
                intent.putExtra("path",filepath);
                startActivity(intent);


            }



        }


    }


    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }




    private void animateFab(){

        if(isOpen){
            fab.startAnimation(rotateForward);
            cam.startAnimation(fabClose);
            file.startAnimation(fabClose);
            pdf.startAnimation(fabClose);
            cam.setClickable(false);
            file.setClickable(false);
            pdf.setClickable(false);
            isOpen=false;
        } else{

            fab.startAnimation(rotateBackward);
            cam.startAnimation(fabOpen);
            file.startAnimation(fabOpen);
            pdf.startAnimation(fabOpen);
            cam.setClickable(true);
            file.setClickable(true);
            pdf.setClickable(true);
            isOpen=true;




        }

    }
}