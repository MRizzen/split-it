package com.example.splititapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ForOptionEqually extends AppCompatActivity {

    LinearLayout containerLayout;
    double totalBill = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_option_equally);

        containerLayout = findViewById(R.id.containerLayout);
        Button addButton = findViewById(R.id.button4);
        Button saveButton = findViewById(R.id.button3);

        String amount = getIntent().getStringExtra("total_amount");
        try {
            if (amount != null) {
                totalBill = Double.parseDouble(amount);
            }
        } catch (NumberFormatException e) {
            totalBill = 0.0;
        }

        addButton.setOnClickListener(v -> addNewRow());

        saveButton.setOnClickListener(v -> saveAndReturn());

        addNewRow();
    }

    private void addNewRow() {
        View rowView = getLayoutInflater().inflate(R.layout.row_person, null);
        EditText amountInput = rowView.findViewById(R.id.splitAmount);
        amountInput.setFocusable(false);
        amountInput.setEnabled(false);
        amountInput.setCursorVisible(false);

        containerLayout.addView(rowView);
        calculateEqually();
    }

    private void calculateEqually() {
        int personCount = containerLayout.getChildCount();
        if (personCount == 0) return;

        double share = totalBill / personCount;
        for (int i = 0; i < personCount; i++) {
            View row = containerLayout.getChildAt(i);

            EditText amountDisplay = row.findViewById(R.id.splitAmount);

            if (amountDisplay != null) {
                amountDisplay.setText(String.format("%.2f", share));
            }
        }
    }

    private void saveAndReturn() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> amounts = new ArrayList<>(); // 1. Create the amounts list

        int personCount = containerLayout.getChildCount();
        if (personCount == 0) return;

        // Calculate the share again just to be 100% sure it's accurate
        double share = totalBill / personCount;
        String shareString = String.format("%.2f", share);

        for (int i = 0; i < personCount; i++) {
            View row = containerLayout.getChildAt(i);

            // Collect Name
            EditText nameInput = row.findViewById(R.id.personName);
            String name = nameInput.getText().toString().trim();
            names.add(name.isEmpty() ? "Person " + (i + 1) : name);

            // 2. Add the share to our amounts list
            amounts.add(shareString);
        }

        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("split_names", names);
        // 3. Send the amounts list back to AddPage
        resultIntent.putStringArrayListExtra("split_amounts", amounts);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}