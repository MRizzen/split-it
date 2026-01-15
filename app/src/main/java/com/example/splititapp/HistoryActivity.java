package com.example.splititapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BillAdapter adapter;
    private List<Bill> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.rvHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();

        adapter = new BillAdapter(historyList, true);
        recyclerView.setAdapter(adapter);

        fetchHistory();
    }

    private void fetchHistory() {
        String url = "http://10.0.2.2/split_it/get_history.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        historyList.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            historyList.add(new Bill(
                                    obj.getString("id"),
                                    obj.getString("title"),
                                    obj.getString("total_amount"),
                                    obj.getString("payer_name"),
                                    obj.getString("due_date"),
                                    obj.getInt("total_members"),
                                    obj.getInt("paid_members")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("HISTORY_ERROR", "JSON Parsing error: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("HISTORY_ERROR", "Volley error: " + error.toString());
                    Toast.makeText(this, "Network Error. Check connection.", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}