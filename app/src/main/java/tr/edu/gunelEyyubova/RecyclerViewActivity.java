package tr.edu.gunelEyyubova;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {
    String currentUsername;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    SQLiteDatabase MyDB;
    ArrayList<String>  questionL, answerAL, answerBL, answerCL, answerDL, answerEL, rightAnswerL,questionIdL;
    ArrayList<byte[]> imagesL;
    QuestionAdapter adp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        currentUsername = getIntent().getStringExtra("username");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());

        questionIdL = new ArrayList<>();
        questionL = new ArrayList<>();
        answerAL = new ArrayList<>();
        answerBL = new ArrayList<>();
        answerCL = new ArrayList<>();
        answerDL = new ArrayList<>();
        answerEL = new ArrayList<>();
        rightAnswerL = new ArrayList<>();
        imagesL = new ArrayList<>();
        getAndStoreData();
        adp = new QuestionAdapter(RecyclerViewActivity.this,this,questionIdL,questionL,answerAL,answerBL,answerCL,answerDL,answerEL,rightAnswerL,imagesL);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            recreate();
        }
    }

    public void getAndStoreData(){
        Cursor cursor;
     try{
         MyDB = openOrCreateDatabase("QUESTIONS.db",MODE_PRIVATE,null);
         cursor = MyDB.rawQuery("SELECT * FROM questions WHERE username = ?", new String[] {String.valueOf(currentUsername)});
         if(cursor.getCount() == 0){
             Toast.makeText(this,"No questions to be shown! ",Toast.LENGTH_SHORT).show();

         }
         else{
             while(cursor.moveToNext()){
                 questionIdL.add(cursor.getString(0));
                 questionL.add(cursor.getString(2));
                 answerAL.add(cursor.getString(3));
                 answerBL.add(cursor.getString(4));
                 answerCL.add(cursor.getString(5));
                 answerDL.add(cursor.getString(6));
                 answerEL.add(cursor.getString(7));
                 rightAnswerL.add(cursor.getString(8));
                 imagesL.add(cursor.getBlob(9));
             }

         }

     }
     catch (Exception e){
         e.printStackTrace();

     }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete All");
            builder.setMessage("Are you sure to delete all of the questions ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    QuestionsDB db = new QuestionsDB(getApplicationContext());
                    db.deleteAllQuestions();
                    //recreate();  fazla intent cagırmak kotu birşey mi ?
                    Intent intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                    startActivity(intent);
                    finish();
                }


            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();

        }

        return super.onOptionsItemSelected(item);
    }


}