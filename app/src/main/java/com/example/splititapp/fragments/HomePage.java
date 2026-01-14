package com.example.splititapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.splititapp.Bill;
import com.example.splititapp.BillAdapter;
import com.example.splititapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {

    private RecyclerView recyclerView;
    private BillAdapter adapter;
    private List<Bill> billList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBills);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tells the adapter that these bills ARE payable and deletable.
        adapter = new BillAdapter(billList, false);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBillsFromDB();
    }

    private void loadBillsFromDB() {
        String url = "http://10.0.2.2/split_it/get_bills.php";

        // IMPORTANT: Clear the list and refresh UI BEFORE the request.
        billList.clear();
        adapter.notifyDataSetChanged();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            billList.add(new Bill(
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
                        android.util.Log.e("JSON_ERROR", "Error: " + e.getMessage());
                    }
                },
                error -> Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(requireContext()).add(stringRequest);
    }
}