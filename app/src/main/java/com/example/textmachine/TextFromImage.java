package com.example.textmachine;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;

public class TextFromImage extends AppCompatActivity {

    Bitmap bitmap,mutable;
    EditText fileName,stringImg;
    InputImage image;
    TextView textImg;
    Button convertPdf,editTxt;
    TextInputLayout field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_from_image);
        String filePath=getIntent().getStringExtra("path");
        File file = new File(filePath);
        bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap k =bitmap.copy(Bitmap.Config.ARGB_8888,true);
        mutable = Bitmap.createScaledBitmap(k,1080,1575,false);


        stringImg = findViewById(R.id.edit_des_text);
        fileName = findViewById(R.id.fileName);
        textImg = findViewById(R.id.text);
        editTxt = findViewById(R.id.editTxt);
        convertPdf = findViewById(R.id.convertPdf);
        field = findViewById(R.id.txtField);

        image=InputImage.fromBitmap(mutable,0);

        executeClassification();

        editTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                field.setVisibility(View.VISIBLE);
                textImg.setVisibility(View.GONE);


            }
        });


        convertPdf.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permission,1000);

                } else{

                    savePdf();


                }




            }

        });



    }


    public void savePdf(){

        Document mDoc = new Document();
        String file = fileName.getText().toString();


        if(!file.isEmpty()){
            String mFilePath = Environment.getExternalStorageDirectory() +"/" + file + ".pdf";

            try{


                PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));
                mDoc.open();


                String extText = textImg.getText().toString();

                if(!extText.isEmpty()){

                    mDoc.add(new Paragraph(extText));


                    mDoc.close();

                    Toast.makeText(this, file+ ".pdf"+" is Saved :) to \n" + mFilePath, Toast.LENGTH_SHORT).show();



                } else{

                    Toast.makeText(this, "Can't leave blank", Toast.LENGTH_SHORT).show();

                }



            } catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }






        } else{

            Toast.makeText(this, "Please select a file name", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED  ){


                    savePdf();
                } else{

                    Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }

    public void executeClassification(){
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull @NotNull Text text) {
                String resultText = text.getText();
                textImg.setMovementMethod(new ScrollingMovementMethod());
                textImg.setText(resultText);
                stringImg.setText(resultText);

                if(!resultText.isEmpty()){
                    for(Text.TextBlock block : text.getTextBlocks()){
                        String blockText = block.getText();
                        Point[] blockCornerPoints = block.getCornerPoints();
                        Rect blockFrame = block.getBoundingBox();
                        for (Text.Line line : block.getLines()) {
                            String lineText = line.getText();
                            Point[] lineCornerPoints = line.getCornerPoints();
                            Rect lineFrame = line.getBoundingBox();
                            for (Text.Element element : line.getElements()) {
                                String elementText = element.getText();
                                Point[] elementCornerPoints = element.getCornerPoints();
                                Rect elementFrame = element.getBoundingBox();
                            }
                        }



                    }



                } else{

                    Toast.makeText(TextFromImage.this, "No text recognized!!", Toast.LENGTH_SHORT).show();

                }

            }
        });





    }




}