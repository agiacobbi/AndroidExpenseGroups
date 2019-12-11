package com.example.ledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        final EditText groupEditText = findViewById(R.id.joinGroupText);
        final TextView existsNote = findViewById(R.id.existsTextView);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        groupEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String groupKey = groupEditText.getText().toString();
                DatabaseReference groupCheck = reference.child("group");
                final DatabaseReference inGroup = reference.child("group-in");
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                groupCheck.orderByKey().equalTo(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean isInGroup = false;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean groupExists = false;
                        if (dataSnapshot.exists()) {
                            alreadyInGroup();
                            if (!isInGroup) {

                            }
                        } else {
                            existsNote.setText("Invalid group code");
                        }
                    }

                    private void alreadyInGroup() {
                        inGroup.orderByChild(userId).orderByKey().equalTo(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
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
