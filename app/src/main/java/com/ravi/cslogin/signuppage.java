package com.ravi.cslogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class signuppage extends AppCompatActivity {
    private static final String TAG = signuppage.class.getSimpleName();

    @BindView(R.id.txtfname)
    EditText mfname;
    @BindView(R.id.txtemail)
    EditText memail;
    @BindView(R.id.txtphone)
    EditText mphone;
    @BindView(R.id.txtpass)
    EditText mpass;
    @BindView(R.id.btnreg)
    Button mbtnReg;
    @BindView(R.id.btnbacklogin)
    TextView mbtnbacklogin;
    @BindView(R.id.msgBox)
    TextView mMsgBox;

    private User mUser;
    private Network mNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mUser = User.getInstance();
        mNetwork = Network.getInstance();

        mbtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setUserName(mfname.getText().toString());
                mUser.setUserEmail(memail.getText().toString());
                mUser.setUserPhone(mphone.getText().toString());
                String sPass = mpass.getText().toString();
                signuppage.this.createUser(sPass);
            }
        });

        mbtnbacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signuppage.this, loginpage.class));
            }
        });
    }

    private void createUser(String sPass) {
        String loginUrl = "http://192.168.43.194:3000/api/signup";
        JSONObject json = new JSONObject();

        try {
            json.put("userName", mUser.getUserName());
            json.put("userEmail", mUser.getUserEmail());
            json.put("userPassword", sPass);
            json.put("phoneno", mUser.getUserPhone());

        } catch (JSONException e) {
            Log.e(TAG, "Exception Caught", e);
        }

        if (mNetwork.isNetworkAvailable(this)) {
            try {
                mNetwork.makeApiPostRequest(loginUrl, json.toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String token = response.body().string();
                            if (token.length() > 0) {
                                mNetwork.setApiToken(token);
                                Log.i(TAG,token);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(signuppage.this, loginpage.class));
                                    }
                                });
                            }
                        }
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "Exception Caught", e);
            }
        }
    }
}