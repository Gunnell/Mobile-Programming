package tr.edu.gunelEyyubova;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
    Button cancel,update;
    EditText question,answer_a,answer_b,answer_c,answer_d,answer_e,right_answer;
    ImageButton attachment;
    Bitmap selectedImage;


    String questionI,answer_aI,answer_bI,answer_cI,answer_dI,answer_eI, right_answerI,questionIdI;
    byte[] imageI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        cancel = findViewById(R.id.cancel_buttonU);
        update = findViewById(R.id.update_buttonU);

        question = findViewById(R.id.questionTextU);
        answer_a = findViewById(R.id.answer_AU);
        answer_b = findViewById(R.id.answer_BU);
        answer_c = findViewById(R.id.answer_CU);
        answer_d = findViewById(R.id.answer_DU);
        answer_e = findViewById(R.id.answer_EU);
        right_answer = findViewById(R.id.right_answerU);

        attachment = findViewById(R.id.attachmentButtonU);
        getAndSetIntentData();

        selectedImage = null;

    }




   public  void cancel(View v){
        finish();
      }

    private void getAndSetIntentData(){
       //get
        questionIdI = getIntent().getStringExtra("question Id");
        questionI = getIntent().getStringExtra("question");
        answer_aI = getIntent().getStringExtra("answer A");
        answer_bI = getIntent().getStringExtra("answer B");
        answer_cI = getIntent().getStringExtra("answer C");
        answer_dI = getIntent().getStringExtra("answer D");
        answer_eI = getIntent().getStringExtra("answer E");
        right_answerI = getIntent().getStringExtra("right answer");
        imageI = getIntent().getByteArrayExtra("image");


        //set
        question.setText(questionI);
        answer_a.setText(answer_aI);
        answer_b.setText(answer_bI);
        answer_c.setText(answer_cI);
        answer_d.setText(answer_dI);
        answer_e.setText(answer_eI);
        right_answer.setText(right_answerI);



    }


    public void update(View view){
        String questiontxt, answer_A,answer_B,answer_D,answer_C,answer_E,right_Answer;
        questiontxt = question.getText().toString();
        answer_A = answer_a.getText().toString();
        answer_B = answer_b.getText().toString();
        answer_C = answer_c.getText().toString();
        answer_D = answer_d.getText().toString();
        answer_E = answer_e.getText().toString();
        right_Answer= right_answer.getText().toString();

        handleImage();

         if(checkFields(questiontxt,answer_A,answer_B,answer_C,answer_D,answer_E,right_Answer)){
            try{
                boolean result = false;
                QuestionsDB db = new QuestionsDB(this);
                result = db.updateQuestion(questionIdI,questiontxt,answer_A,answer_B,answer_C,answer_D,answer_E,right_Answer,imageI);

                if(result){
                    Toast.makeText(getBaseContext(),"Question has been updated !",
                            Toast.LENGTH_SHORT).show();
                    db.close();
                    finish();
                }
                else
                    Toast.makeText(getBaseContext(),"An error occurred, question could not be updated !",
                            Toast.LENGTH_SHORT).show();

            }
            catch(Exception e){
                e.printStackTrace();
            }




           }







    }


        public boolean checkFields(String questionText,String answerA, String answerB,String answerC,String answerD,String answerE,String right_answer){
            if(questionText.length()<2){
                Toast.makeText(getBaseContext(),"Question should contain at least 2 characters !",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(answerA.matches("") || answerB.matches("")
                    || answerC.equals("") || answerD.equals("")
                    || answerE.equals("") || right_answer.equals("")){
                Toast.makeText(getBaseContext(),"All answer fields must be filled , please check again!",
                        Toast.LENGTH_SHORT).show();
                return false;

            }
            else if(right_answer.matches("")){
                Toast.makeText(getBaseContext(),"Right answer must be filled!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            else if(!(right_answer.equals("A") || right_answer.equals("B") ||
                    right_answer.equals("C") || right_answer.equals("D") ||
                    right_answer.equals("E") )){
                Toast.makeText(getBaseContext(),"Right answer must be one of the options above!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }


            return true;
    }



                public void handleImage(){
                if (selectedImage!=null){
                    Bitmap small;
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    small= resizeimage(selectedImage,250);
                    small.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                    imageI = outputStream.toByteArray();
                }



                return ;
                }
    public Bitmap resizeimage(Bitmap image,int maxSize){
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
       public void getAttachment(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},5);


        } else{

            Intent intentToPhotos=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToPhotos,6);


        }

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 5){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToPhotos=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentToPhotos.setType("image/*");
                startActivityForResult(intentToPhotos,6);

            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 6 && resultCode == RESULT_OK && data!=null){

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



}