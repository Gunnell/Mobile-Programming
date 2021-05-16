package tr.edu.gunelEyyubova;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText userName ;
    EditText password;
    Button loginBtn;
    Button signUpBtn;
    int attempt;

    UsersDB peopleList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        loginBtn =findViewById(R.id.loginButton);
        signUpBtn =findViewById(R.id.registerButton);
        attempt=0;
        peopleList = new UsersDB(this);

    }





   public void login(View view){
       if (userName.getText().toString().isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(userName.getText().toString()).matches()) ) {
           Toast.makeText(MainActivity.this,"Please enter valid username!",
                   Toast.LENGTH_SHORT).show();
           userName.setText("");

       }
       else if(password.getText().toString().matches("")){
           Toast.makeText(MainActivity.this,"Please enter the password !",
                   Toast.LENGTH_SHORT).show();
           password.setText("");


       }
       else if(password.getText().toString().length()<8) {
           Toast.makeText(MainActivity.this, "Password should contain at least 8 characters !",
                   Toast.LENGTH_SHORT).show();
       }




          else  if(peopleList.checkUsernamePassword(userName.getText().toString(),password.getText().toString())){
                Intent intent=new Intent(view.getContext(),MenuActivity.class);
                Toast.makeText(MainActivity.this,"Signing in...!", Toast.LENGTH_SHORT).show();
                intent.putExtra("username",userName.getText().toString());
                startActivity(intent);
                userName.setText("");
                password.setText("");




        } else{
           Toast.makeText(MainActivity.this,"Please check your password and username, no account have been found!",
                   Toast.LENGTH_SHORT).show();
            attempt++;
            if(attempt>=3){
                Toast.makeText(MainActivity.this,"You have entered the wrong information 3 times, Sign up for login!",
                        Toast.LENGTH_SHORT).show();
               // loginBtn.setEnabled(false);
                Intent intent=new Intent(MainActivity.this,signUpActivity.class);
                startActivity(intent);

                attempt=0;
                userName.setText("");
                password.setText("");
            }

        }



    }



            public void signUp(View view){

                Intent intent=new Intent(MainActivity.this,signUpActivity.class);
                userName.setText("");
                password.setText("");
                startActivity(intent);

                               }


}