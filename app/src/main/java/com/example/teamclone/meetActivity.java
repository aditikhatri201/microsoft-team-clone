package com.example.teamclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class meetActivity extends AppCompatActivity {
    Button joinButton,shareButton;
    EditText code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);
        joinButton=findViewById(R.id.joinbtn);
        code=findViewById(R.id.codeboxEt);
        shareButton=findViewById(R.id.sharebtn);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        URL serverURL;
        //MEETING CODE
        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions =
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //MEETING STARTED
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString()!=null &&
                        code.getText().toString().length()>0){
                    JitsiMeetUserInfo info=new JitsiMeetUserInfo();
                    info.setDisplayName(user.getDisplayName());
                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(code.getText().toString())
                            .setWelcomePageEnabled(false)
                            .setAudioMuted(true)
                            .setUserInfo(info)
                            .setVideoMuted(true)
                            .build();
                    JitsiMeetActivity.launch(meetActivity.this,options);

                }
                else {
                    Toast.makeText(meetActivity.this, "Please provide code first.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //SHARE BUTTON
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String sendString="Meeting code is  "+code.getText().toString()+"\nYou can use this code join the meeting.";

                if(code.getText().toString()!=null && code.getText().toString().length()>0){
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sendString);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);


                }else {

                    Toast.makeText(meetActivity.this, "Please provide code first.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //BOTTOM MENU
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        navView.setSelectedItemId(R.id.meet);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.person:
                        startActivity(new Intent(meetActivity.this,profileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.meet:
                        return true;


                    case R.id.chat:
                        startActivity(new Intent(meetActivity.this, chatActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.logout:

                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();

                        Intent logout = new Intent(meetActivity.this, loginActivity.class);
                        startActivity(logout);
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

}