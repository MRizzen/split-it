package com.example.splititapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText fullname, email, password, confirmpassword, birthdate;
    Button signupbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        birthdate = findViewById(R.id.birthdate);
        signupbtn = findViewById(R.id.signupbtn);

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final java.util.Calendar c = java.util.Calendar.getInstance();
                int year = c.get(java.util.Calendar.YEAR);
                int month = c.get(java.util.Calendar.MONTH);
                int day = c.get(java.util.Calendar.DAY_OF_MONTH);

                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                        Register.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        birthdate.setText(date);
            }, year, month, day);
                datePickerDialog.show();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(fullname.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || birthdate.getText().toString().isEmpty()){
                    Toast.makeText(Register.this,"Please fill all fields!",Toast.LENGTH_SHORT).show();
                }else if(!password.getText().toString().equals(confirmpassword.getText().toString())){
                    Toast.makeText(Register.this,"Password do not match!",Toast.LENGTH_SHORT).show();
                }else {
                    sendDataToXAMPP();
                }
            }
        });
    }
    private void sendDataToXAMPP(){
        String url = "http://10.0.2.2/split_it/register.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(Register.this,response,Toast.LENGTH_SHORT).show();
                    if(response.contains("Successful")){
                        finish();
                    }
                },
                error -> {
                    Toast.makeText(Register.this, "Error connecting to XAMPP",Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            protected Map <String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("fullname",fullname.getText().toString());
                params.put("email",email.getText().toString());
                params.put("password",password.getText().toString());
                params.put("birthdate",birthdate.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}
