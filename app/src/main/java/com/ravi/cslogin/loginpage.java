package com.ravi.cslogin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class loginpage extends AppCompatActivity {
    private static final String TAG = loginpage.class.getSimpleName();

    private Network mNetwork;
    private User mUser;

    @BindView(R.id.user_email) EditText mUserEmail;
    @BindView(R.id.user_password) EditText mPassword;
    @BindView(R.id.btn_login) Button mloginButton;
    @BindView(R.id.noacc)TextView mbtnNoacc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        ButterKnife.bind(this);

        mNetwork = Network.getInstance();
        mUser = User.getInstance();

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUserEmail.getText().toString();
                String password = mPassword.getText().toString();
                loginpage.this.login(email, password);
                //
                // startActivity(new Intent(loginpage.this,examQuiz.class));
            }
        });

        mbtnNoacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginpage.this,signuppage.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void login(String email, String password) {

        String loginUrl = "http://192.168.43.194:3000/api/login";
        JSONObject json = new JSONObject();

        try {
            json.put("userEmail", email);
            json.put("userPassword", password);
        }
        catch (JSONException e){
            Log.e(TAG, "Exception Caught", e);
        }

        if(mNetwork.isNetworkAvailable(this))
        {
            try {
                mNetwork.makeApiPostRequest(loginUrl, json.toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()) {
                            String token = response.body().string();
                            if(token.length() > 0) {
                                mNetwork.setApiToken(token);
                                runOnUiThread(() -> startActivity(new Intent(loginpage.this, StartActivity.class)));
                            }
                        }
                    }
                });
            }
            catch (IOException e)
            {
                Log.e(TAG, "Exception Caught", e);
            }
        }

        if (!validate()) {
            onLoginFailed();
            return;
        }

        mloginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(loginpage.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                () -> {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess();
                    // onLoginFailed();
                    progressDialog.dismiss();
                }, 3000);
    }

    private void onLoginSuccess() {
        mloginButton.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mloginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;
        return valid;
    }
}