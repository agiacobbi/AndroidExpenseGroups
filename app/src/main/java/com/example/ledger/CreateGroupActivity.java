package com.example.ledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupActivity extends AppCompatActivity {

    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference groupRef = reference.child("group");
        final DatabaseReference groupInRef = reference.child("group-in");
        final DatabaseReference inGroupRef = reference.child("in-group");
        final DatabaseReference newGroupRef = inGroupRef.push();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final String groupKey = newGroupRef.getKey();
        group = new Group();

        //need to find a way to invite members
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        final TextView groupIdView = findViewById(R.id.GroupIDLabel);
        final EditText groupName = findViewById(R.id.createGroupName);
        Button createButton = findViewById(R.id.createButton);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroupActivity.this.finish();
            }
        });
        groupIdView.setText("Group ID: " + groupKey);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupName.getText().toString().equals("")){
                    Toast.makeText(CreateGroupActivity.this, "Please add a name to this group", Toast.LENGTH_SHORT).show();
                }
                else{
                    group.setGroupName(groupName.getText().toString());
                    groupRef.child(groupKey).setValue(group);
                    groupInRef.child(userId).child(groupKey).setValue(group.getGroupName());
                    inGroupRef.child(groupKey).child(userId).setValue(userName);
                    CreateGroupActivity.this.finish();
                }
            }
        });

         
    }
}
