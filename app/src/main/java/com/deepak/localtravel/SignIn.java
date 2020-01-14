package com.deepak.localtravel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.deepak.localtravel.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class SignIn extends AppCompatActivity {

    TextInputEditText edtPhone, edtPassword;
    Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignIn= findViewById(R.id.btnSignIn);


        //firebase instance made and called to check for user details
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               if(edtPhone.getText().length() == 0 || edtPhone.getText().length() < 10 ){
                    Toast.makeText(SignIn.this,"Please Enter Correct Phone Number", Toast.LENGTH_SHORT).show();
                }
                else if(edtPassword.getText().length() == 0){
                    Toast.makeText(SignIn.this,"Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else{

                   final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                   mDialog.setMessage("Please Waiting.....");
                   mDialog.show();

                   table_user.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           String phone_num=edtPhone.getText().toString();
                           //get user information
                           User user = dataSnapshot.child(phone_num).getValue(User.class);

                           //  if(user.getPassword().equals(edtPassword.getText().toString()))
                           if(user.getPassword().equals(edtPassword.getText().toString()))
                           {
                               mDialog.dismiss();
                               SharedPreferences sharePref = getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharePref.edit();

                               //Store true if user is already login
                               editor.putBoolean("active", true);
                               editor.putString("googleLogin","no");
                               editor.putString("name_shared_preferance", user.getName());
                               editor.putString("phone_number_shared_preferance",edtPhone.getText().toString());
                               editor.putString("email_shared_preferance",user.getEmail());
                               editor.commit();

                              // Intent mumbai_route = new Intent(SignIn.this,MapActivity.class);
                               Intent mumbai_route = new Intent(SignIn.this,ActivityPunePage.class);
                               mumbai_route.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               startActivity(mumbai_route);
                               finish();
                           }
                           else{
                               mDialog.dismiss();
                               Toast.makeText(SignIn.this,"SignIn Failed!", Toast.LENGTH_SHORT).show();

                               SharedPreferences sharePref = getSharedPreferences("user_login_details", Context.MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharePref.edit();
                               // to store data if user login
                               editor.putBoolean("active", false);
                               editor.commit();
                           }

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

               }



            }
        });
    }

}
