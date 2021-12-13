package com.bogdan.drew.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HouseActivity extends AppCompatActivity{

    public static final String TAG = "houseTag";
    List<ListPiece> list;
    House house;
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

        TextView textView = findViewById(R.id.house_title);
        textView.setText("House " + code);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().compareTo("Debt") == 0) {
                    house.setCurrentSelected("Debt");
                    CustomDebtAdapter debtAdapter = new CustomDebtAdapter(house.getAll());
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(debtAdapter);
                }
                else if(spinner.getSelectedItem().toString().compareTo("Chore") == 0) {
                    CustomChoresAdapter choresAdapter = new CustomChoresAdapter();
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(choresAdapter);
                }
                else if(spinner.getSelectedItem().toString().compareTo("Grocery") == 0) {
                    CustomGroceryAdapter groceryAdapter = new CustomGroceryAdapter();
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(groceryAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    class CustomDebtAdapter extends RecyclerView.Adapter<CustomDebtAdapter.CustomDebtViewHolder> {

        public CustomDebtAdapter(ListPiece debtList) {
            this.debtList = (DebtList) debtList;
        }

        DebtList debtList;
        class CustomDebtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            ImageView imageView;
            public CustomDebtViewHolder(@NonNull View itemView) {
                super(itemView);
                title = findViewById(R.id.houseCardTitle);
                imageView = findViewById(R.id.houseCardImage);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Debt debt = debtList.list.get(getAdapterPosition());
                double amount = debt.getAmount();
                int id = debt.getId();
                String description = debt.getDescription();
                String userOwed = debt.getUserOwed().toString();
                String usedOwing = debt.getUserOwing().toString();

                Intent intent = new Intent()

                // house.get id
                // TODO: start intent to open house main activity using the code
            }

            @Override
            public boolean onLongClick(View view) {

                return true;
            }

            public void updateView(Debt debt) {
                title.setText(debt.getDescription());
                // "https://www.flaticon.com/authors/dimitry-miroliubov"
                imageView.setImageResource(R.drawable.money);
            }


        }

        @NonNull
        @Override
        public CustomDebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HouseActivity.this)
                    .inflate(R.layout.house_card_view, parent, false);
            return new CustomDebtViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomDebtViewHolder holder, int position) {
           Debt debt = debtList.list.get(position);
           holder.updateView(debt);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "debt");
            return debtList.list.size();
        }
    }

    class CustomChoresAdapter extends RecyclerView.Adapter<CustomChoresAdapter.CustomChoresViewHolder> {
//        ArrayList<ListPiece> choreList = house.getAll();
        class CustomChoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            public CustomChoresViewHolder(@NonNull View itemView) {
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

            public void updateView(ListPiece piece) {
                //TODO: update the house with main attributes
                //TODO: include code
              //title.setText();
//                address.setText(place.getVicinity());
            }


        }

        @NonNull
        @Override
        public CustomChoresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HouseActivity.this)
                    .inflate(R.layout.house_card_view, parent, false);

            return new CustomChoresViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomChoresViewHolder holder, int position) {
            ListPiece listPiece = list.get(position);
            holder.updateView(listPiece);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "chore");
            return 0;
        }
    }

    class CustomGroceryAdapter extends RecyclerView.Adapter<CustomGroceryAdapter.CustomGroceryViewHolder> {
        List<Grocery> debtList;

        class CustomGroceryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            public CustomGroceryViewHolder(@NonNull View itemView) {
                super(itemView);
                title = findViewById(R.id.houseCardTitle);
                Log.d(TAG, "grocery");
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
        public CustomGroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(HouseActivity.this)
                    .inflate(R.layout.house_card_view, parent, false);
            return new CustomGroceryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomGroceryViewHolder holder, int position) {
            ListPiece piece= list.get(position);

            //holder.updateView(house);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "grocery");
            return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.house_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMenuItem:
                break;
            case R.id.deleteMenuItem:
        }
        return super.onOptionsItemSelected(item);
    }

}
