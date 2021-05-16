package tr.edu.gunelEyyubova;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    String time,number;
    int score;
    EditText timeText, numberText;
    TextView scores;
    Spinner spinner;
    SharedPreferences sharedPreferences;
    Button saveButton,cancelButton;
    ArrayAdapter adapter;
    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timeText = findViewById(R.id.timeText);
        numberText = findViewById(R.id.numberText);
        scores = findViewById(R.id.scoresText);
        saveButton = findViewById(R.id.savee_button);
        cancelButton = findViewById(R.id.canceel_button);

        spinner = findViewById(R.id.dif_level_spinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        sharedPreferences = this.getSharedPreferences("tr.edu.gunelEyyubova", Context.MODE_PRIVATE);
        String storedTime = sharedPreferences.getString("storedTime","0");
        String storedNumber =sharedPreferences.getString("storedNumber","0");
        int storedScore = sharedPreferences.getInt("storedScore",0);
        int storedPosition = sharedPreferences.getInt("storedPosition",0);


        setStoredData(storedTime,storedNumber,storedScore,storedPosition);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] levels = getResources().getStringArray(R.array.levels);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });




    }


public void setStoredData(String time,String num,int score,int position){


        timeText.setText(time);
        numberText.setText(num);
        scores.setText(Integer.toString(score));
        spinner.setSelection(position,true);


}

public void cancel(View view){
  finish();

}


public boolean checkFields(){
         time = timeText.getText().toString();
         number = numberText.getText().toString();
        if(time.matches("")){
            Toast.makeText(getBaseContext(),"Please enter the duration of the exam in minutes!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(time.contains(":")){
            Toast.makeText(getBaseContext(),"Time can not contain any symbol!",
                    Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(Integer.parseInt(time)>240 || Integer.parseInt(time)<10){
            Toast.makeText(getBaseContext(),"Exam must be set in the 10-240 minute interval!",
                    Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(number.contains(":")){
            Toast.makeText(getBaseContext(),"Number can not contain any symbol!",
                    Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(Integer.parseInt(number)>100 || Integer.parseInt(number)<5){
            Toast.makeText(getBaseContext(),"Number of questions must be in the 5-100 interval!",
                    Toast.LENGTH_SHORT).show();
            return false;

        }



        return true;
}
public void save(View view){
        if(checkFields()){
            selectedPosition = spinner.getSelectedItemPosition();
            sharedPreferences.edit().putString("storedTime",time).apply();
            sharedPreferences.edit().putString("storedNumber",number).apply();
            sharedPreferences.edit().putInt("storedScore",score).apply();
            sharedPreferences.edit().putInt("storedPosition",selectedPosition).apply();
            finish();
        }
        else
            Toast.makeText(getBaseContext(),"Please fill all the fields!",
                    Toast.LENGTH_SHORT).show();

}
   public void calculate(View view){
       int number = Integer.parseInt(numberText.getText().toString());
       score = 100 /number;
       scores.setText(Integer.toString(score));

    }

}