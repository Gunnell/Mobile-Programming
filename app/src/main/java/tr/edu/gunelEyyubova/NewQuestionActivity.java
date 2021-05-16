package tr.edu.gunelEyyubova;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NewQuestionActivity extends AppCompatActivity {
     String currentUsername;
     EditText questionText, answerA, answerB, answerC, answerD,  answerE;
     EditText right_answer;
     Button attachment;
     FloatingActionButton add_button;
     Bitmap selectedImage = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        currentUsername = getIntent().getStringExtra("username");
        questionText = findViewById(R.id.questionText);
        answerA = findViewById(R.id.answer_A);
        answerB = findViewById(R.id.answer_B);
        answerC = findViewById(R.id.answer_C);
        answerD = findViewById(R.id.answer_D);
        answerE = findViewById(R.id.answer_E);
        right_answer = findViewById(R.id.right_answer);
        attachment = findViewById(R.id.attachment_button);

        add_button = (FloatingActionButton) findViewById(R.id.add_ButtoN);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             create(v);
            }
        });



    }


    public void getAttachment(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},3);


        } else{

            Intent intentToPhotos=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToPhotos,4);


        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 3){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToPhotos=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentToPhotos.setType("image/*");
                startActivityForResult(intentToPhotos,4);

            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 4 && resultCode == RESULT_OK && data!=null){

            Uri imgData = data.getData();
            try {
                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imgData);
                    selectedImage = ImageDecoder.decodeBitmap(source);

                }
                else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imgData);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    public boolean checkFields(){

        if(questionText.getText().toString().length()<2){
            Toast.makeText(getBaseContext(),"Question should contain at least 2 characters !",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(answerA.getText().toString().matches("") || answerB.getText().toString().matches("")
        || answerC.getText().toString().equals("") || answerD.getText().toString().equals("")
        || answerE.getText().toString().equals("") || right_answer.getText().toString().equals("")){
            Toast.makeText(getBaseContext(),"All answer fields must be filled , please check again!",
                    Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(right_answer.getText().toString().matches("")){
            Toast.makeText(getBaseContext(),"Right answer must be filled!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!(right_answer.getText().toString().equals("A") || right_answer.getText().toString().equals("B") ||
                right_answer.getText().toString().equals("C") || right_answer.getText().toString().equals("D") ||
                right_answer.getText().toString().equals("E") )){
            Toast.makeText(getBaseContext(),"Right answer must be one of the options above!",
                    Toast.LENGTH_SHORT).show();
                return false;
        }




        return true;
}

public void create(View view) {
     boolean result = false;
    if (checkFields()) {
        String qText = questionText.getText().toString();
        String asrA = answerA.getText().toString();
        String asrB = answerB.getText().toString();
        String asrC = answerC.getText().toString();
        String asrD = answerD.getText().toString();
        String asrE = answerE.getText().toString();
        String right = right_answer.getText().toString();
        byte[] imgByTes = null;
        if(selectedImage != null) {
            Bitmap smallImage = resizeImage(selectedImage,250);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            imgByTes = outputStream.toByteArray();

        }

        try {
            QuestionsDB db = new QuestionsDB(this);
            result =  db.insertData(currentUsername,qText,asrA,asrB,asrC,asrD,asrE,right,imgByTes);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



        Intent intent = new Intent(NewQuestionActivity.this,NewQuestionActivity.class);
        intent.putExtra("username",currentUsername);
        if(result)
        Toast.makeText(getBaseContext(),"Question has been added successfully!",
                Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getBaseContext(),"Question could not be added !",
                    Toast.LENGTH_SHORT).show();
        clearFields();
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // startActivity(intent);

         }

     }


     public void clearFields(){
         questionText.setText("");
         answerA.setText("");
         answerB.setText("");
         answerC.setText("");
         answerD.setText("");
         answerE.setText("");
         right_answer.setText("");
         selectedImage = null;

     }
    public Bitmap resizeImage(Bitmap image,int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio= (float) width / (float) height;

        if(ratio > 1){
            width = maxSize;
            height = (int) ( width/ratio);
        }
        else{
            height = maxSize;
            width = (int) ( height*ratio);

        }

        return Bitmap.createScaledBitmap(image,width,height,true);

    }



}