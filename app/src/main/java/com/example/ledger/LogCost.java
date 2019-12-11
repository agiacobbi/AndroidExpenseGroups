package com.example.ledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogCost extends AppCompatActivity {

//    final EditText userInputCost = findViewById(R.id.cost_name);
//    final EditText userDescription = findViewById(R.id.cost_description);
    final String TAG = "LogCostActivity";
    private Cost cost = new Cost();
    private List<String> groupsList;
    private List<String> groupIds;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_cost);
        Intent intent = new Intent();


        Spinner groupSelect = findViewById(R.id.group_selector);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference groupInRef = reference.child("group-in");
        groupsList = new ArrayList<>();
        groupIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                groupsList
        );
        groupSelect.setAdapter(adapter);


        groupInRef.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s: dataSnapshot.getChildren()) {
                        for (DataSnapshot g : s.getChildren()) {
                            System.out.println("ADDING: " + g.getValue().toString());
                            groupIds.add(g.getKey());
                            System.out.println(groupIds);
                            groupsList.add(g.getValue().toString());
                            System.out.println(groupsList.toString());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Button finishButton = findViewById(R.id.finish_log);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText userInputCost = findViewById(R.id.cost_name);
                EditText userDescription = findViewById(R.id.cost_description);
                Spinner spinner = findViewById(R.id.group_selector);
                Log.d(TAG, "in finishLogListener");
                Button backButton = findViewById(R.id.backButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LogCost.this.finish();
                    }
                });


                if (userInputCost.getText().toString().equals("") || userDescription.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(LogCost.this, "Enter a cost and a description", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    String groupInsertId = groupIds.get(spinner.getSelectedItemPosition());
                    double costAmount = Double.parseDouble(userInputCost.getText().toString());

                    cost.setAmountCost(costAmount);
                    cost.setCostDescription(userDescription.getText().toString());
                    cost.setUser(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    cost.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    DatabaseReference insertRef = FirebaseDatabase.getInstance().getReference().child("cost").child(groupInsertId);
                    insertRef.push().setValue(cost);

                    LogCost.this.finish();
                }
            }
        });
    }
}