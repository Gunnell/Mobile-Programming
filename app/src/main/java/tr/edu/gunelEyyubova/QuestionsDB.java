package tr.edu.gunelEyyubova;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class QuestionsDB extends SQLiteOpenHelper {
    public static final String DBNAME = "QUESTIONS.db";
    public QuestionsDB(Context context) {
        super(context, "QUESTIONS.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, question VARCHAR, answerA VARCHAR, answerB VARCHAR," +
                  " answerC VARCHAR, answerD VARCHAR, answerE VARCHAR, rightAnswer VARCHAR, attachment BLOB)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions");

    }


    public Boolean insertData( String username,String question, String answerA,String answerB,String answerC,String answerD,
                               String answerE,String rightAnswer,byte[] attachment){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("question", question);
        contentValues.put("answerA", answerA);
        contentValues.put("answerB", answerB);
        contentValues.put("answerC", answerC);
        contentValues.put("answerD", answerD);
        contentValues.put("answerE", answerE);
        contentValues.put("rightAnswer", rightAnswer);
        contentValues.put("attachment", attachment);
        long result = MyDB.insert("questions", null, contentValues);

        if(result==-1) return false;

        else return true;

    }
public boolean deleteOneQuestion(String id){
    SQLiteDatabase MyDB = this.getWritableDatabase();
    long result = MyDB.delete("questions", "id = ?", new String[] {id});
    if(result == -1)
        return false;

     return true;
}


public boolean updateQuestion(String id, String question, String answerA, String answerB, String answerC, String answerD, String answerE,
                                String rightAnswer, byte[] image){
    SQLiteDatabase MyDB = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("question", question);
    contentValues.put("answerA", answerA);
    contentValues.put("answerB", answerB);
    contentValues.put("answerC", answerC);
    contentValues.put("answerD", answerD);
    contentValues.put("answerE", answerE);
    contentValues.put("rightAnswer", rightAnswer);
    contentValues.put("attachment", image);

    long result= MyDB.update("questions",contentValues,"id = ?", new String[] {id});
    if(result == -1)
        return false;

        return true;
}

   public void deleteAllQuestions(){
    SQLiteDatabase MyDB = this.getWritableDatabase();
    MyDB.execSQL("DELETE FROM questions");


      }



}
