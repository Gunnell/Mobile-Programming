package tr.edu.gunelEyyubova;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class signUpActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Button signUpButton;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText userName;
    EditText passWord;
    EditText passWord2;
    TextView login;
    ImageView userImage;

    Bitmap selectedImage;


    UsersDB users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initDatePicker();
         dateButton = findViewById(R.id.datePickerButton);
         signUpButton=findViewById(R.id.sign_up);
         firstName =findViewById(R.id.first_name);
         lastName=findViewById(R.id.last_name);
         phoneNumber=findViewById(R.id.phone_number);
         userName=findViewById(R.id.username);
         passWord=findViewById(R.id.passWord);
         passWord2=findViewById(R.id.passWord2);
         login=findViewById(R.id.login);
         userImage=findViewById(R.id.userImg);
         users = new UsersDB(this);
         phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        selectedImage = null;


    }



    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);


    }

    private String makeDateString(int day, int month, int year)
    {
        if(month<10){
            if(day<10)
                return " 0"+ month+ "/0"+ day + "/" + year;
        }
        else if(day<10)
            return " "+ month+ "/0"+ day + "/" + year;
        return " "+ month+ "/"+ day + "/" + year;
    }


    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void backToMain(View view){

        Intent intent=new Intent(signUpActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }
    public void selectImage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);


        } else{

           Intent intentToPhotos=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
           startActivityForResult(intentToPhotos,2);


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToPhotos=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentToPhotos.setType("image/*");
                startActivityForResult(intentToPhotos,2);

            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data!=null){

            Uri imgData = data.getData();
            try {
                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imgData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    userImage.setImageBitmap(selectedImage);
                }
                else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imgData);
                    userImage.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }






        super.onActivityResult(requestCode, resultCode, data);
    }




    public boolean checkFields(){

        if(firstName.getText().toString().length()<2){
            Toast.makeText(getBaseContext(),"FirstName should contain at least 2 characters !",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!firstName.getText().toString().matches("^[A-Za-z]+$")){
            Toast.makeText(getBaseContext(),"FirstName should not contain special characters or numbers!",
                    Toast.LENGTH_SHORT).show();
            firstName.setText("");
            return false;

        }
       else  if(lastName.getText().toString().length()<2){
            Toast.makeText(getBaseContext(),"LastName should contain at least 2 characters !",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!lastName.getText().toString().matches("^[A-Za-z]+$")){
            Toast.makeText(getBaseContext(),"LastName should not contain special characters or numbers!",
                    Toast.LENGTH_SHORT).show();
            lastName.setText("");
            return false;

        }
        else if (dateButton.getText().toString().equals(" Date Of Birth")){


            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String strDate= formatter.format(date);


            AlertDialog.Builder alert_date= new AlertDialog.Builder(signUpActivity.this);
            alert_date.setCancelable(false);
            alert_date.setTitle("Are you sure of your birth date ?");
            alert_date.setMessage(strDate);
            alert_date.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dateButton.setText(strDate);
                    Toast.makeText(signUpActivity.this,"Date is saved",Toast.LENGTH_SHORT).show();
                    return ;
                }

            });
            alert_date.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });

            alert_date.show();
            return false;
        }
     /*  else if(phoneNumber.getText().toString().length()<15 ||phoneNumber.getText().toString().length()>17 || !phoneNumber.getText().toString().matches("^[+][0-9]{10,13}$")==false) {
            Toast.makeText(getBaseContext(), "Please enter " + "\n" + " valid phone number", Toast.LENGTH_SHORT).show();
            phoneNumber.setText("");
                  phoneNumber.setHint("+00(123)1234567");
                   return false;
        } */
       else if (userName.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userName.getText().toString()).matches() ) {
            Toast.makeText(getBaseContext(), "Please enter valid username!",Toast.LENGTH_SHORT).show();

            userName.setText("");
            return false;
        }
       else if(users.checkUsername(userName.getText().toString())){
            Toast.makeText(getBaseContext(), "Please enter different username, this username already taken!",Toast.LENGTH_SHORT).show();
           return false;
        }

       else if(passWord.getText().toString().length()<8){
            Toast.makeText(signUpActivity.this,"Password should contain at least 8 characters !",
                    Toast.LENGTH_SHORT).show();

            return false;
        }
        else if(passWord2.getText().toString().length()<8){
            Toast.makeText(signUpActivity.this,"Password should contain at least 8 characters !",
                    Toast.LENGTH_SHORT).show();

            return false;
        }
       else if(!passWord.getText().toString().equals(passWord2.getText().toString())){
            Toast.makeText(signUpActivity.this,"Passwords do not match, please check and enter again !",
                    Toast.LENGTH_SHORT).show();

            passWord2.setText("");
            return false;
        }



        return true;
    }





  public void signUp(View view){

            if(checkFields()) {

            String first_name = firstName.getText().toString();
            String last_name = lastName.getText().toString();
            String birth_date = dateButton.getText().toString();
            String phone_number = phoneNumber.getText().toString();
            String user_name = userName.getText().toString();
            String pass_word = passWord.getText().toString();
            byte[] imageBytes;
            if(selectedImage!=null){
                Bitmap smallImage = resizeImage(selectedImage,250);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                imageBytes = outputStream.toByteArray();
            }
            else
                imageBytes = null;



            users.insertData(user_name,pass_word,first_name,last_name,phone_number,birth_date,imageBytes);
            users.close();

                Intent intent=new Intent(view.getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



            }



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