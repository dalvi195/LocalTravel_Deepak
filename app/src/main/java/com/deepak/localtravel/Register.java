package com.deepak.localtravel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.deepak.localtravel.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    TextInputEditText edtPhone, edtPassword , edtName , edtEmail;
    Button btnSignUp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignUp = findViewById(R.id.btnSignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdd= edtEmail.getText().toString().trim();
                edtEmail.setTextColor(getResources().getColor(R.color.emailRight));
                if(edtName.getText().length() == 0 ){
                    Toast.makeText(Register.this,"Please Enter Name",Toast.LENGTH_SHORT).show();
                }
                else if(edtPhone.getText().length() < 10 ){
                    Toast.makeText(Register.this,"Phone Number should be 10 digit.",Toast.LENGTH_SHORT).show();
                }
                else if(!emailAdd.matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    edtEmail.setTextColor(getResources().getColor(R.color.emailWrong));
                }
                else if(edtPassword.getText().length() <=6){
                    Toast.makeText(Register.this,"Password length should be more then 6",Toast.LENGTH_SHORT).show();
                }
                else{
                    final ProgressDialog mDialog = new ProgressDialog(Register.this);
                    mDialog.setMessage("Please Waiting.....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String phone_number= edtPhone.getText().toString();
                            if(dataSnapshot.child(phone_number).exists()){
                                mDialog.dismiss();
                                Toast.makeText(Register.this,"User is already Exist",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mDialog.dismiss();
                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString() , edtEmail.getText().toString());
                                table_user.child(phone_number).setValue(user);
                                Toast.makeText(Register.this,"SignUp successfully !! ",Toast.LENGTH_SHORT).show();
                                finish();

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

