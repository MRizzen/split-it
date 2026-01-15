package com.example.splititapp;

import static android.content.Intent.getIntent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ForOptionCustomize extends AppCompatActivity {

    LinearLayout containerLayout;
    double totalBill = 0.0;
    ImageButton backBtn;
    Button saveBtn, addBtn;
    TextView tvRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_option_customize);

        // 1. Initialize Views
        containerLayout = findViewById(R.id.containerLayout);
        backBtn = findViewById(R.id.imageButton7);
        saveBtn = findViewById(R.id.button3);
        addBtn = findViewById(R.id.button4);
        tvRemaining = findViewById(R.id.tvRemaining);

        String amountStr = getIntent().getStringExtra("total_amount");
        if (amountStr != null) {
            try {
                totalBill = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                totalBill = 0.0;
            }
        }


        backBtn.setOnClickListener(v -> finish());

        addBtn.setOnClickListener(v -> addNewRow());

        saveBtn.setOnClickListener(v -> validateAndSave());

        addNewRow();
    }

    private void addNewRow() {
        View rowView = getLayoutInflater().inflate(R.layout.row_person, null);
        EditText amountInput = rowView.findViewById(R.id.splitAmount);

        amountInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                updateRemainingText();
            }
        });

        containerLayout.addView(rowView);
    }
    private void updateRemainingText() {
        double currentSum = 0;
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View row = containerLayout.getChildAt(i);
            EditText amountInput = row.findViewById(R.id.splitAmount);
            String val = amountInput.getText().toString();
            if (!val.isEmpty()) {
                try { currentSum += Double.parseDouble(val); } catch (Exception e) {}
            }
        }

        double remaining = totalBill - currentSum;
        tvRemaining.setText(String.format("Remaining: ₱%.2f", remaining));

        // Change color to green if it matches perfectly
        if (Math.abs(remaining) < 0.01) {
            tvRemaining.setTextColor(android.graphics.Color.GREEN);
        } else {
            tvRemaining.setTextColor(android.graphics.Color.RED);
        }
    }

    private void validateAndSave() {
        double currentSum = 0;
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> amounts = new ArrayList<>();

        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            View row = containerLayout.getChildAt(i);
            EditText nameInput = row.findViewById(R.id.personName);
            EditText amountInput = row.findViewById(R.id.splitAmount);

            String name = nameInput.getText().toString().trim();
            String amtStr = amountInput.getText().toString().trim();

            if (!amtStr.isEmpty()) {
                try {
                    double val = Double.parseDouble(amtStr);
                    currentSum += val;
                    amounts.add(amtStr);
                    names.add(name.isEmpty() ? "Person " + (i + 1) : name);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid amount in a row", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }


        if (Math.abs(currentSum - totalBill) > 0.01) {
            Toast.makeText(this, "The total must be " + totalBill + ". Current total: " + currentSum, Toast.LENGTH_LONG).show();
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("split_names", names);
            resultIntent.putStringArrayListExtra("split_amounts", amounts);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}

