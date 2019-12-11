package com.example.ledger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JoinGroupActivity extends AppCompatActivity {
    EditText inputGroupId = (EditText)findViewById(R.id.groupId);
    Button joinGroupButton = (Button)findViewById(R.id.joinGroup);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputGroupId.getText().toString().equals("")){
                    Toast.makeText(JoinGroupActivity.this, "Please enter a groupId", Toast.LENGTH_SHORT).show();
                }
                else{
                    
                }
            }
        });
    }
}
