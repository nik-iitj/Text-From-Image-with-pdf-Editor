package com.example.textmachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.common.InputImage;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class CreatePdf extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText fileName,stringFile;
    Button convertPdf;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);

        stringFile = findViewById(R.id.edit_des_text);
        fileName = findViewById(R.id.fileName);
        convertPdf = findViewById(R.id.convertPdf);
        radioGroup = findViewById(R.id.radioGroup);
        spinner = findViewById(R.id.spinner);

        ArrayList<Integer>x = new ArrayList<Integer>();

        for(int i = 12;i<50;i++){
            x.add(i);
        }

        ArrayAdapter<Integer>adapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,x);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(this);

        convertPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePdf();

            }
        });









    }


    public void checkButton(View v){

        int radioId= radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        int startSelection=stringFile.getSelectionStart();
        int endSelection=stringFile.getSelectionEnd();
        String text = stringFile.getText().toString();
        SpannableString span = new SpannableString(text);


        if(radioButton.getText().equals("Bold")){




            span.setSpan(new StyleSpan(Typeface.BOLD), startSelection, endSelection ,  0);



        } else if(radioButton.getText().equals("Italic")){


            span.setSpan(new StyleSpan(Typeface.ITALIC), startSelection, endSelection , 0);

        }

        else if(radioButton.getText().equals("Underline")){
            span.setSpan(new UnderlineSpan(), startSelection, endSelection , 0);

        }

        else if(radioButton.getText().equals("Normal")){
            span.setSpan(new StyleSpan(Typeface.NORMAL), startSelection, endSelection , 0);

        }

        stringFile.setText(span, TextView.BufferType.SPANNABLE);




    }





    public void savePdf(){

        Document mDoc = new Document();
        String file = fileName.getText().toString();


        if(!file.isEmpty()){
            String mFilePath = Environment.getExternalStorageDirectory() +"/" + file + ".pdf";

            try{


                PdfWriter.getInstance(mDoc,new FileOutputStream(mFilePath));
                mDoc.open();


                String extText = stringFile.getText().toString();

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
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED  ){


                    savePdf();
                } else{

                    Toast.makeText(this, "Permissions not granted!", Toast.LENGTH_SHORT).show();

                }

            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //stringFile.setTextSize(parent.getItemIdAtPosition(position));


        int pos = (int) parent.getItemAtPosition(position);
        stringFile.setTextSize(TypedValue.COMPLEX_UNIT_SP,pos);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }
}