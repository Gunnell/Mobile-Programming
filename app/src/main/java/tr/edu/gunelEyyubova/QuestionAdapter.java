package tr.edu.gunelEyyubova;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    private  Context context;
    Activity activity;
    private ArrayList<String> question_,answer_A,answer_B,answer_C,answer_D,answer_E, right_answer,questionId_;
    private ArrayList<byte[]> images;
    private String id;


    QuestionAdapter(Activity activity,Context context,ArrayList<String> questionId_, ArrayList<String> question_, ArrayList<String> answer_A, ArrayList<String> answer_B,ArrayList<String> answer_C,
                         ArrayList<String> answer_D,ArrayList<String> answer_E, ArrayList<String> right_answer, ArrayList<byte[]> images ){ //en son <String> kaldırıp dene bi calısıyor mu
             this.context = context;
             this.activity = activity;
             this.questionId_ = questionId_;
             this.question_ = question_;
             this.answer_A = answer_A;
             this.answer_B = answer_B;
             this.answer_C = answer_C;
             this.answer_D = answer_D;
             this.answer_E = answer_E;
             this.right_answer = right_answer;
             this.images = images;




            }





    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageR;
        TextView question, answerA, answerB, answerC, answerD, answerE,rightAnswer;
        ImageButton delete, update, attach;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageR = itemView.findViewById(R.id.imageR);
            question = itemView.findViewById(R.id.questionIdR);
            answerA = itemView.findViewById(R.id.answerAR);
            answerB = itemView.findViewById(R.id.answerBR);
            answerC = itemView.findViewById(R.id.answerCR);
            answerD = itemView.findViewById(R.id.answerDR);
            answerE = itemView.findViewById(R.id.answerER);
            rightAnswer = itemView.findViewById(R.id.rightAnswerR);
            delete = itemView.findViewById(R.id.deleteButtonR);
            update = itemView.findViewById(R.id.updateButtonR);
            attach = itemView.findViewById(R.id.attachmentButtonR);

        }




    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.question.setText(question_.get(position));
        holder.answerA.setText(answer_A.get(position));
        holder.answerB.setText(answer_B.get(position));
        holder.answerC.setText(answer_C.get(position));
        holder.answerD.setText(answer_D.get(position));
        holder.answerE.setText(answer_E.get(position));
        holder.rightAnswer.setText(right_answer.get(position));
        id = questionId_.get(position);
        byte[] bytes = images.get(position);
        if(bytes != null){

        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        holder.imageR.setImageBitmap(bitmap); }
        else{

            String uri = "@drawable/selectimage";
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            Drawable res = context.getResources().getDrawable(imageResource);
            holder.imageR.setImageDrawable(res);

        }


        holder.update.setOnClickListener((v) -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.putExtra("question Id",String.valueOf(questionId_.get(position)));
            intent.putExtra("question",String.valueOf(question_.get(position)));
            intent.putExtra("answer A",String.valueOf(answer_A.get(position)));
            intent.putExtra("answer B",String.valueOf(answer_B.get(position)));
            intent.putExtra("answer C",String.valueOf(answer_C.get(position)));
            intent.putExtra("answer D",String.valueOf(answer_D.get(position)));
            intent.putExtra("answer E",String.valueOf(answer_E.get(position)));
            intent.putExtra("right answer",String.valueOf(right_answer.get(position)));
            intent.putExtra("image",images.get(position));

            activity.startActivityForResult(intent,10);



        });
        holder.delete.setOnClickListener((v) -> {
            try{

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete ?");
                builder.setMessage("Are you sure to delete this question ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = false;
                        QuestionsDB db =  new QuestionsDB(context);

                        result = db.deleteOneQuestion(id);
                        db.close();
                        if(result){
                            Toast.makeText(context, "Question has been deleted",Toast.LENGTH_SHORT).show();
                            activity.recreate();
                        }
                        else
                            Toast.makeText(context, "An error occurred, Question could not be deleted",Toast.LENGTH_SHORT).show();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();



            }
            catch(Exception e){
                e.printStackTrace();
            }




        });
        holder.attach.setOnClickListener((v) -> {

        });

    }

    @Override
    public int getItemCount() {
        return question_.size();
    }







}
