package com.applications.sample.firebasedatabasesamplejava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;

    private TextView tvUsername, tvMessage;
    static boolean isInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        setGreetingMessage(null);

        //If the Firebase is already initialized, setPersistenceEnabled throws error. Hence check.
        if (!isInitialized) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            isInitialized = true;
        }
        //message is the json root element
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("message");
        mFirebaseDatabase.keepSynced(true);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                tvMessage.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });

        //username is the json root element
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("username");
        mFirebaseDatabase.keepSynced(true);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                setGreetingMessage(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {

            }
        });


    }

    private void setGreetingMessage(String username) {
        Calendar calendar = Calendar.getInstance();
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Good ");
        if (0 < hr && hr < 12) {
            stringBuilder.append("Morning");
        } else if (12 <= hr && hr < 18) {
            stringBuilder.append("Afternoon");
        } else if (18 <= hr && hr < 24) {
            stringBuilder.append("Evening");
        }

        if (TextUtils.isEmpty(username)) {
            stringBuilder.append("!");
        } else {
            stringBuilder.append(" ").append(username).append("!");
        }

        tvUsername.setText(stringBuilder.toString());
    }
}
