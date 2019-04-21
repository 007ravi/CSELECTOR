package com.ravi.cslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.text_view_question)
    TextView mQuestion;
    @BindView(R.id.button_confirm_next)
    Button next;
    @BindView(R.id.radio_group)
    RadioGroup choice;
    @BindView(R.id.optionA)
    RadioButton oA;
    @BindView(R.id.optionB)
    RadioButton oB;
    @BindView(R.id.optionC)
    RadioButton oC;
    @BindView(R.id.optionD)
    RadioButton oD;
    @BindView(R.id.score)
    TextView mScore;
    @BindView(R.id.btnend)
    Button end;

    Network mNetwork;

    String mcq;
    String coption;
    String[] woption = new String[3];
    int score = 0;
    int answer;
    int correct;
    JSONArray questions;
    int questionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ques);

        ButterKnife.bind(this);

        mNetwork = Network.getInstance();

         getMcq();

         end.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
              startActivity(new Intent(MainActivity.this,feedBack.class));
             }
         });

         choice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
               answer = checkedId;
             }
         });

         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 validate();
                 questionIndex++;
                 setDisplay();
             }
         });

    }

    private void getMcq() {
        String url = "https://opentdb.com/api.php?amount=100&type=multiple";
        mNetwork.makeApiGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Exception Caught", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.body() != null) {
                    mcq = response.body().string();
                    parseData();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDisplay();
                    }
                });
            }
        });
    }

    private void validate(){
        if(answer == correct) {
            score+=4;
        }
        else {
            score-=1;
        }
    }

    private void parseData() {
        Log.i(TAG,"Work");
        try {
            JSONObject jsonMcq = new JSONObject(mcq);
            questions = jsonMcq.getJSONArray("results");
        } catch (JSONException e) {
            Log.e(TAG, "Exception Caught", e);
        }
    }

    private void setDisplay() {
        JSONObject ques = null;
        try {
            ques = questions.getJSONObject(questionIndex);

            JSONArray incorrectArray = ques.getJSONArray("incorrect_answers");

            woption[0] = incorrectArray.getString(0);
            woption[1] = incorrectArray.getString(1);
            woption[2] = incorrectArray.getString(2);

            coption = ques.getString("correct_answer");
            Log.i(TAG, ques.getString("question"));

            String[] options = new String[4];
            int random = (int)(Math.random() * (4-1) + 1);

            options[random] = coption+"correct";

            int k=0;
            for(int i=0;i<4;i++){
                if(i != random) {
                    options[i] = woption[k];
                    k++;
                }
                else
                {
                    switch (i)
                    {
                        case 0 :correct = oA.getId();
                                break;
                        case 1 : correct = oB.getId();
                                break;
                        case 2 :correct = oC.getId();
                                break;
                        case 3 :correct = oD.getId();
                                break;
                    }
                }
            }

            mQuestion.setText(ques.getString("question"));

            mScore.setText("Score : " + score );
            oA.setText(options[0]);
            oB.setText(options[1]);
            oC.setText(options[2]);
            oD.setText(options[3]);


            Log.i(TAG, ques.getString("question"));
            mQuestion.setText(ques.getString("question"));
        } catch (JSONException e) {
            Log.e(TAG, "Exception Caught", e);
        }
    }
}