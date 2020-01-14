package com.deepak.localtravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    TextView user_info_title, user_info_name, name_display,user_info_number,
            number_display, user_info_email, email_display, password, passwordConfirm;
    EditText edtPassEnter, edtPassConfirm;

    Button passwordChange, homeGo, updatePass;

    String number_user_sh;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference table_user = database.getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user_info_title = findViewById(R.id.user_info_title);
        user_info_name= findViewById(R.id.user_info_name);
        name_display = findViewById(R.id.name_display);
        user_info_number = findViewById(R.id.user_info_number);
        number_display = findViewById(R.id.number_display);
        user_info_email =findViewById(R.id.user_info_email);
        email_display =findViewById(R.id.email_display);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        edtPassEnter =(EditText) findViewById(R.id.edtPassEnter);
        edtPassConfirm = (EditText) findViewById(R.id.edtPassConfirm);
        updatePass = findViewById(R.id.updatePass);

        password.setVisibility(View.GONE);
        passwordConfirm.setVisibility(View.GONE);
        edtPassEnter.setVisibility(View.GONE);
        edtPassConfirm.setVisibility(View.GONE);
        updatePass.setVisibility(View.GONE);




        passwordChange = (Button) findViewById(R.id.passwordChange);
        homeGo= findViewById(R.id.homeGo);

        SharedPreferences sharePref = getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
        Boolean value = sharePref.getBoolean("active", Boolean.parseBoolean(""));
        String googleLogin = sharePref.getString("googleLogin","");
        String name_user_sh = sharePref.getString("name_shared_preferance","");
        number_user_sh = sharePref.getString("phone_number_shared_preferance","");
        String email_user_sh = sharePref.getString("email_shared_preferance","");

        name_display.setText(name_user_sh);
        number_display.setText(number_user_sh);
        email_display.setText(email_user_sh);

        passwordChange.setOnClickListener(this);
        homeGo.setOnClickListener(this);
        updatePass.setOnClickListener(this);

        if(googleLogin == "yes"){
            passwordChange.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        //Define button and define in switch to use
        switch (v.getId()){
           case R.id.passwordChange:
               edtPassEnter.setText("");
               edtPassConfirm.setText("");
               password.setVisibility(View.VISIBLE);
               passwordConfirm.setVisibility(View.VISIBLE);
               edtPassEnter.setVisibility(View.VISIBLE);
               edtPassConfirm.setVisibility(View.VISIBLE);
               passwordChange.setVisibility(View.GONE);
               updatePass.setVisibility(View.VISIBLE);
               break;

            case R.id.homeGo:
                Intent goHomeIntent = new Intent(UserProfile.this,ActivityPunePage.class);
                startActivity(goHomeIntent);

                break;
            case R.id.updatePass:
                if( edtPassEnter.getText().equals("")||edtPassConfirm.getText().equals("")){
                    edtPassEnter.setError("Please enter Password");
                }
                else if(!edtPassEnter.getText().equals(edtPassConfirm.getText()) || !edtPassConfirm.getText().equals(edtPassEnter.getText())) {

                    edtPassConfirm.setError("Password not match");
                    edtPassEnter.setError("Password not match");
                }
                else {

                    String passwordUpdate = edtPassEnter.getText().toString();
//
                    Map<String, Object> taskMap = new HashMap<String, Object>();
                    taskMap.put("password", passwordUpdate);
                    table_user.child(number_user_sh).updateChildren(taskMap);

                    password.setVisibility(View.GONE);
                    passwordConfirm.setVisibility(View.GONE);
                    edtPassEnter.setVisibility(View.GONE);
                    edtPassConfirm.setVisibility(View.GONE);
                    passwordChange.setVisibility(View.VISIBLE);
                    updatePass.setVisibility(View.GONE);
                }

                    break;

        }

    }
}
