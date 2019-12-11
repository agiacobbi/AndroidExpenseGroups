package com.example.ledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class JoinGroupActivity extends AppCompatActivity {
    String groupName = "";
    String groupKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);


        final EditText groupEditText = findViewById(R.id.joinGroupText);
        final TextView existsNote = findViewById(R.id.existsTextView);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference groupRef = reference.child("group");
        final DatabaseReference groupInRef = reference.child("group-in");
        final DatabaseReference inGroupRef = reference.child("in-group");
        final DatabaseReference newGroupRef = inGroupRef.push();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        final Button joinButton = findViewById(R.id.joinGroup);
        joinButton.setEnabled(false);
        final Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinGroupActivity.this.finish();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inGroupRef.child(groupKey).child(userId).setValue(userName);
                groupInRef.child(userId).child(groupKey).setValue(groupName);
                System.out.println(groupName);
                finish();
            }
        });


        groupEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String groupKey = groupEditText.getText().toString();
                JoinGroupActivity.this.groupKey = groupKey;
                DatabaseReference groupCheck = reference.child("group");
                final DatabaseReference inGroup = reference.child("group-in");
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                groupCheck.orderByKey().equalTo(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean isInGroup = false;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean groupExists = false;
                        if (dataSnapshot.exists()) {
                            existsNote.setText("");
                            alreadyInGroup();
                            if (!isInGroup) {
                                  joinButton.setEnabled(true);
                                  groupName = dataSnapshot.child(groupKey).child("groupName").getValue().toString();
//                                inGroupRef.child(groupKey).child(userId).setValue(userName);
//                                groupInRef.child(userId).child(groupKey).setValue(dataSnapshot.child(groupKey).child("groupName").getValue().toString());
                            } else {
                                joinButton.setEnabled(false);
                            }
                        } else {

                            //existsNote.setText("Invalid group code");
                            existsNote.setText("Invalid group code");
                            joinButton.setEnabled(false);
                        }
                    }

                    private void alreadyInGroup() {
                        inGroup./*orderByChild(userId)*/orderByKey().equalTo(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    System.out.println(dataSnapshot.toString());
                                    isInGroup = true;
                                } else {
                                    System.out.println("NOT IN GROUP");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
