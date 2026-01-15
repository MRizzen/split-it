package com.example.splititapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Added for debugging
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginButton, createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextTextEmailAddress3);
        password = findViewById(R.id.editTextTextPassword5);
        loginButton = findViewById(R.id.logInbtn);
        createAccountButton = findViewById(R.id.createbtn);

        loginButton.setOnClickListener(v -> loginUser());

        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });
    }

    public void loginUser() {
        String url = "http://10.0.2.2/split_it/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String message = obj.getString("message");

                        if (message.toLowerCase().contains("success")) {
                            String userId = obj.getString("id");

                            Log.d("LOGIN_DEBUG", "Saving User ID: " + userId);

                            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("user_id", userId);
                            editor.apply();

                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Login.this, "Login Failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, "Server Response Error: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(Login.this, "Connection Error: Check XAMPP/Network", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}