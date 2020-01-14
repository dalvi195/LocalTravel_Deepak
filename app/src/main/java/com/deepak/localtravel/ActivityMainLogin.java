package com.deepak.localtravel;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class ActivityMainLogin extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp, btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //Define button and define in switch to use
        switch (v.getId()){

            case R.id.btnSignIn:

                Intent signIn = new Intent(ActivityMainLogin.this, SignIn.class);
                startActivity(signIn);


                break;

            case R.id.btnSignUp:
                Intent signUp = new Intent(ActivityMainLogin.this,Register.class);
                startActivity(signUp);

                break;
        }

    }


}


