package com.example.teamclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teamclone.databinding.ActivityLoginBinding;
import com.example.teamclone.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    String email="",password="",confirmPassword="";
    private ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatedata();
            }
        });


    }

    private void validatedata() {
        email=binding.emailEt.getText().toString();
        password=binding.passwordEt.getText().toString();
        confirmPassword=binding.confirmpasswordEt.getText().toString();


        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //email format is invalid
            binding.emailEt.setError("Invalid email format");

        } else if(TextUtils.isEmpty(password)){
            //no password is entered
            binding.passwordEt.setError("Enter password");
        }else if(password.length()<6){
            //password length less than 6
            binding.emailEt.setError("Password must atleast 6 characters long");
        }else if(!confirmPassword.equals(password)){
            //password and confirm password is not same
            binding.confirmpasswordEt.setError("Confirm password and Password must be same");
        }
        else{
            //data is valid
            firebaseSignUp();
        }



    }

    private void firebaseSignUp() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign up fails
                            Toast.makeText(signupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            Toast.makeText(signupActivity.this, "Succecsfully login", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(signupActivity.this,profileActivity.class);
            intent.putExtra("name",user.getDisplayName());
            startActivity(intent);
        }
    }

}