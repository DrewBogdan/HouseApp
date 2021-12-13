package com.bogdan.drew.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HouseActivity extends AppCompatActivity {

    CustomListAdapter recyclerAdapter = new CustomListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        Intent receive_intent = getIntent();
        int code = receive_intent.getIntExtra("code", -1);
        House house = new House(code);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String item = spinner.getSelectedItem().toString();

        TextView textView = findViewById(R.id.house_title);
        textView.setText("House " + code);

        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        house.setCurrentSelected(item);

    }

    class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.CustomListViewHolder> {


        class CustomListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            public CustomListViewHolder(@NonNull View itemView) {
                super(itemView);
                title = findViewById(R.id.houseCardTitle);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                //House house = houseList.get(getAdapterPosition());
                // house.get id
                // TODO: start intent to open house main activity using the code
            }

            @Override
            public boolean onLongClick(View view) {

                return true;
            }

            public void updateView(House house) {
                //TODO: update the house with main attributes
                //TODO: include code
//                title.setText();
//                address.setText(place.getVicinity());
            }


        }

        @NonNull
        @Override
        public CustomListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HouseActivity.this)
                    .inflate(R.layout.house_card_view, parent, false);
            return new CustomListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomListViewHolder holder, int position) {
           // House house = houseList.get(position);
            //holder.updateView(house);
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }


}
