package com.example.ledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAllGroupsActivity extends AppCompatActivity {
    List<String> groupsList = new ArrayList<>();
    List<String> groupIdList = new ArrayList<>();
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_groups);

        final ListView groupListView = findViewById(R.id.viewGroupsList);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAllGroupsActivity.this.finish();
            }
        });

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference groupInRef = reference.child("group-in");
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                groupsList);

        groupListView.setAdapter(adapter);

        groupInRef.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s: dataSnapshot.getChildren()) {
                        for (DataSnapshot g : s.getChildren()) {
                            System.out.println("ADDING: " + g.getValue().toString());
                            groupsList.add(g.getValue().toString());
                            groupIdList.add(g.getKey());
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

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewAllGroupsActivity.this, ViewGroupActivity.class);
                intent.putExtra("groupId", groupIdList.get(i));

                startActivity(intent);
            }
        });
    }
}
