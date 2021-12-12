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
        // TODO: include house code to create object
        //House house = new House(code);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String item = spinner.getSelectedItem().toString();

        // TO REMOVE
        House house = new House();
        list = list(item, house);
        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    public Object object(String item, House house) {
        if(item.compareTo("Debts") == 0) {
            List<Debt> listDebts = new ArrayList<>();
            // TODO: get list from house object
            return listDebts;
        }
        else if (item.compareTo("Chores") == 0) {
            List<Chore> choreList = new ArrayList<>();
            // TODO: get list from house object
            return choreList;
        }
        else {
            List<Grocery> groceryList = new ArrayList<>();
            // TODO: get list from house object
            return groceryList;
        }
    }

    class CustomListAdapter extends RecyclerView.Adapter<MainActivity.CustomAdapter.CustomViewHolder> {


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
                House house = houseList.get(getAdapterPosition());
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
        public MainActivity.CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.house_card_view, parent, false);
            return new MainActivity.CustomAdapter.CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.CustomAdapter.CustomViewHolder holder, int position) {
            House house = houseList.get(position);
            holder.updateView(house);
        }

        @Override
        public int getItemCount() {
            return houseList.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.house_add:
                Intent intent = new Intent(MainActivity.this, HouseActivity.class);
                launcher.launch(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
}