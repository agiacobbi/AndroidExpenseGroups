package com.example.ledger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.PrivateKey;

public class CreateGroupActivity extends AppCompatActivity {

    private Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText groupName = (EditText)findViewById(R.id.createGroupName);

        //need to find a way to invite members
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Button createButton = (Button)findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupName.getText().toString().equals("")){
                    Toast.makeText(CreateGroupActivity.this, "Please add a name to this group", Toast.LENGTH_SHORT);
                }
                else{
                    group.setGroupName(groupName.getText().toString());
                }
            }
        });
    }
}
