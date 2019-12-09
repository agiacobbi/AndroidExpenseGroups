package com.example.ledger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogCost extends AppCompatActivity {

//    final EditText userInputCost = findViewById(R.id.cost_name);
//    final EditText userDescription = findViewById(R.id.cost_description);
    final String TAG = "LogCostActivity";
    private Cost cost = new Cost();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_cost);
        Intent intent = new Intent();
        /*if(intent != null){
            cost = (Cost)intent.getSerializableExtra("cost");
        }*/
        Button finishButton = findViewById(R.id.finish_log);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userInputCost = findViewById(R.id.cost_name);
                EditText userDescription = findViewById(R.id.cost_description);
                Log.d(TAG, "in finishLogListener");
                if (userInputCost.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(LogCost.this, "Cost is empty please enter an amount for cost", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    double costAmount = Double.parseDouble(userInputCost.getText().toString());
                    Log.d(TAG, "cost amount: " + costAmount);
                    cost.setAmountCost(costAmount);
                    cost.setCostDescription(userDescription.getText().toString());
                    Intent intent =  new Intent(LogCost.this, MainActivity.class);
                    intent.putExtra("cost", cost);
                    setResult(RESULT_OK, intent);
                    LogCost.this.finish();
                }
            }
        });
    }
}