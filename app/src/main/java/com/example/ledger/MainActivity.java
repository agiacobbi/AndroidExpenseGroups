package com.example.ledger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivityTag";
    static final int SIGN_IN_REQUEST = 1;
    static final int LOGIN_REQUEST_CODE = 1;
    private String userName = "Anonymous";
    private String email = "Anonymous";
    ChildEventListener usersAddedListener;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference databaseReference;
    private Cost newCost;
    private User signedInUser = new User();
    ArrayAdapter adapter;
    List<String> groupIdList;
    List<Cost> costList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //when this is clicked the user will be able to add a cost
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, LogCost.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
                Log.d(TAG, "in fab button listener");

            }
        });

        setupFirebase();
        populateListView();
    }

    private void populateListView() {
        ListView costListView = findViewById(R.id.expensesList);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference groupInRef = reference.child("group-in");
        groupIdList = new ArrayList<>();
        costList = new ArrayList<>();

        adapter = new ArrayAdapter<Cost>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                costList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // inflates simple_list_item_1 for this position contact

                Cost cost = costList.get(position);
                // set value(s) for the view and its subviews
                TextView tv1 = view.findViewById(android.R.id.text1);
                tv1.setText(cost.getUser()); // cloning what ArrayAdapter has been doing for us

                // task: set the name for text1 and set the phone number for text2
                TextView tv2 = view.findViewById(android.R.id.text2);
                DecimalFormat df = new DecimalFormat("$0.00");
                tv2.setText(df.format(cost.getAmountCost()));

                return view;
            }
        };
        costListView.setAdapter(adapter);

        groupInRef.orderByKey().equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s: dataSnapshot.getChildren()) {
                        for (DataSnapshot g : s.getChildren()) {
                            System.out.println("ADDING: " + g.getKey());
                            groupIdList.add(g.getKey());
                        }
                    }
                }

                fillCosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fillCosts() {
        DatabaseReference costRef = FirebaseDatabase.getInstance().getReference().child("cost");
        costRef.orderByKey().limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    System.out.println("LOADING COSTS");
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        if (groupIdList.contains(s.getKey())) {
                            for (DataSnapshot cost : s.getChildren()) {
                                double val = Double.parseDouble(cost.child("amountCost").getValue().toString());
                                String desc = cost.child("costDescription").getValue().toString();
                                String user = cost.child("user").getValue().toString();
                                costList.add(new Cost(val, desc, user));
                                adapter.notifyDataSetChanged();
                                System.out.println("NOTIFIED");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupFirebase(){
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDataBase.getReference().child("users");
        usersAddedListener = new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "user: " + signedInUser.getEmail());
                //User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    setupSignedInUser(user);
                }
                else {
                    Intent intent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    )
                            ).build();
                    startActivityForResult(intent, SIGN_IN_REQUEST);
                }
            }
        };
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
    private void setupSignedInUser(FirebaseUser user) {
        // get the user's name
        userName = user.getDisplayName();
        email =  user.getEmail();
        signedInUser.setUsername(userName);
        signedInUser.setEmail(email);
        databaseReference.addChildEventListener(usersAddedListener);
        databaseReference.push().setValue(signedInUser);
        // listen for database changes with childeventlistener
        // wire it up!
        //mMessagesDatabaseReference
        //.addChildEventListener(mMessagesChildEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_signout:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.createGroup:
                Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_viewgroups:
                Intent viewIntent = new Intent(MainActivity.this, ViewAllGroupsActivity.class);
                startActivity(viewIntent);
                return true;
            case R.id.action_joingroup:
                Intent joinIntent = new Intent(MainActivity.this, JoinGroupActivity.class);
                startActivity(joinIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(this, "You are now signed in", Toast.LENGTH_SHORT).show();

            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                // they backed out of the sign in activity
                // let's exit
                finish();
            }
        }
        //Does not work after user signs in
        //trying to get data from cost
        /*if(data != null){
            newCost = (Cost)data.getSerializableExtra("cost");
            Log.d(TAG, "Cost amount: " + newCost.getAmountCost()+ " Description: " + newCost.getCostDescription());
        }*/
    }
}

