package com.example.ledger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGroupActivity extends AppCompatActivity {

    String currentUserName;
    String currentUserId;
    Map<String, Double> memberSpending;
    String groupId;
    Map<String, Double> amountOwed;
    List<Cost> expenseList = new ArrayList<>();
    Map<String, String> userCodeDisplayMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        TextView balanceView = findViewById(R.id.balanceText);
        final ListView membersListView = findViewById(R.id.memberList);


        Button invite = findViewById(R.id.button);
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String message = "UoMe! Join our group using this code: " + groupId;
                // send a simple text message with some activity
                intent.setType("text/plain"); // mime type (media)
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            groupId = intent.getStringExtra("groupId");
        } else {
            balanceView.setText("Error loading info");
            return;
        }

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        memberSpending = new HashMap<>();
        amountOwed = new HashMap<>();
        userCodeDisplayMap = new HashMap<>();

        DatabaseReference usersInGroupRef = FirebaseDatabase.getInstance().getReference().child("in-group");
        usersInGroupRef.child(groupId).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        userCodeDisplayMap.put(s.getKey(), s.getValue().toString());
                        memberSpending.put(s.getKey(), 0.0);
                    }
                }
                sumCosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sumCosts() {
        DatabaseReference costRef = FirebaseDatabase.getInstance().getReference().child("cost");
        costRef.orderByKey().equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        for (DataSnapshot cost : s.getChildren()) {
                            double amt = Double.parseDouble(cost.child("amountCost").getValue().toString());
                            String uid = cost.child("userId").getValue().toString();
                            System.out.println("putting " + amt);
                            memberSpending.put(uid, memberSpending.get(uid) + amt);
                        }
                    }
                }
                calculateBalances();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateBalances() {
        int numMembers = memberSpending.size();
        System.out.println(memberSpending);
        for (String user : memberSpending.keySet()) {
            if (!user.equals(currentUserId)) {
                double balanceOwed = (memberSpending.get(user) / numMembers) - (memberSpending.get(currentUserId) / numMembers);
                amountOwed.put(user, balanceOwed);
            }
        }

        updateView();
    }

    private void updateView() {
        List<String> messages = new ArrayList<>();
        double overallBalance = 0;
        DecimalFormat df = new DecimalFormat("$0.00");


        for (String k : amountOwed.keySet()) {
            overallBalance += amountOwed.get(k);
            if (amountOwed.get(k) > 0) {
                messages.add("You owe " + userCodeDisplayMap.get(k) + " " + df.format(amountOwed.get(k)));
            } else {
                messages.add(userCodeDisplayMap.get(k) + " owes you " + df.format((-1 * amountOwed.get(k))));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                messages
        );
        ListView groupMembers = findViewById(R.id.memberList);
        groupMembers.setAdapter(adapter);

        TextView balance = findViewById(R.id.balanceText);
        if (overallBalance < 0) {
            balance.setText(df.format((-1 * overallBalance)));
            balance.setTextColor(getResources().getColor(R.color.green));
        } else {
            balance.setText(df.format((overallBalance)));
            balance.setTextColor(getResources().getColor(R.color.red));
        }
    }

}
