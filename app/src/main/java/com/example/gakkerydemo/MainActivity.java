package com.example.gakkerydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView imageBox;
    Button btnNext, btnPrevious, btnAdd, btnReset;
    EditText edtURL;
    private int index = -1;
    private static final String FILE_NAME = "image_text.txt";
    ArrayList<String> listURLs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageBox = findViewById(R.id.imgBox);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnAdd = findViewById(R.id.btnAdd);
        edtURL = findViewById(R.id.edtURL);
        btnReset = findViewById(R.id.btnReset);

        //Load file text img open app
        try {
            loadImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Next image
        btnNext.setOnClickListener(v ->{
            int total = listURLs.size();
            if (total > 0){
                index++;
                if (index == total){
                    index = 0;
                }
                setImageFormURL();
            }
        });
        //Previous Image
        btnPrevious.setOnClickListener(v ->{
            int total = listURLs.size();
            if (total > 0){
                index--;
                if (index < 0){
                    index = total - 1;
                }
                setImageFormURL();
            }
        });

        //Add Image
        btnAdd.setOnClickListener(v ->{
            String URL = edtURL.getText().toString().trim();
            listURLs.add(URL);
            edtURL.setText("");

            //Store to file
            try {
                storeToFile(URL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Clear data
        btnReset.setOnClickListener(v -> {
            listURLs.clear();
            index = -1;
            imageBox.setImageResource(0); //R.id.image not found

            //delete current data file
            getApplicationContext().deleteFile(FILE_NAME);
        });

    }

    private void storeToFile(String url) throws IOException {
        FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(FILE_NAME, Context.MODE_APPEND);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(url);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        bufferedWriter.close();
        outputStreamWriter.close();
    }

    private void loadImage() throws IOException {
//        String URL1 = "https://i.pinimg.com/564x/95/c2/78/95c278725e13b989942ef8ebc809488a.jpg";
//        String URL2 = "https://i.pinimg.com/564x/ea/9c/99/ea9c99d888b6420a179e978be0c77163.jpg";
//        String URL3 = "https://i.pinimg.com/736x/f9/f8/42/f9f842378c4f966db31a4c2dbba00937.jpg";
//        listURLs.add(URL1);
//        listURLs.add(URL2);
//        listURLs.add(URL3);

        //READ URLs file text => add to list array
            FileInputStream fileInputStream = getApplicationContext().openFileInput(FILE_NAME);
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    listURLs.add(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
    }

    private void setImageFormURL(){
        Glide.with(this).load(listURLs.get(index)).into(imageBox);
    }
}