package tr.edu.gunelEyyubova;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements  View.OnClickListener {

    Button sign_out_button;
    ImageButton new_question_button, list_button, create_button, settings_button;
    private String currentUsername;
    ImageView myImage;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        new_question_button = findViewById(R.id.newQuestionButton);
        list_button = findViewById(R.id.listButton);
        create_button = findViewById(R.id.createButton);
        settings_button = findViewById(R.id.settingsButton);
        sign_out_button = findViewById(R.id.signOutButton);
        myImage = findViewById(R.id.userImage);
        defineListeners();
        currentUsername = getIntent().getStringExtra("username");
        handleImage();

    }



    public void handleImage(){
     SQLiteDatabase db = openOrCreateDatabase("USERS.db",MODE_PRIVATE,null);
     Cursor cursor = db.rawQuery("SELECT image FROM users WHERE username = ?" ,new String[] {currentUsername});
     int imageIx = cursor.getColumnIndex("image");

     if(cursor.moveToFirst()){
         byte[] bytes = cursor.getBlob(imageIx);
         if(bytes!=null){
             Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
             myImage.setImageBitmap(reSizeImage(bitmap));
         }
        else{
              BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.userlogo);
              Bitmap bitmap = drawable.getBitmap();
              myImage.setImageBitmap(reSizeImage(bitmap));
         }

     }


     cursor.close();
     db.close();

    }
    public Bitmap reSizeImage(Bitmap image){
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio= ((float) width / (float) height);

        if(ratio > 1){
            ratio = ratio/2;
            width = (int) ( width * ratio);
            height = (int) ( height * ratio);
        }
        else{
            ratio = ratio*2;
            height =(int) (height/ratio);
            width = (int) ( width/ratio);

        }

        return Bitmap.createScaledBitmap(image,width,height,true);

    }

   public void defineListeners(){
        new_question_button.setOnClickListener(this);
       list_button.setOnClickListener(this);
       create_button.setOnClickListener(this);
       settings_button.setOnClickListener(this);
       sign_out_button.setOnClickListener(this);

   }




    @Override
    public void onClick(View view) {

            Intent intent;
        switch(view.getId()){

            case R.id.newQuestionButton:
            intent = new Intent(MenuActivity.this, NewQuestionActivity.class);
            intent.putExtra("username",currentUsername);
            startActivity(intent);
            break;

            case R.id.listButton:
                intent = new Intent(MenuActivity.this, RecyclerViewActivity.class);
                intent.putExtra("username",currentUsername);
                startActivity(intent);
                break;

            case R.id.createButton:

                break;

            case R.id.settingsButton:
                intent = new Intent(MenuActivity.this, Settings.class);
                startActivity(intent);

                break;

            case R.id.signOutButton:
                finish();
                break;
        }


    }



}