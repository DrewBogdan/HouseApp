package com.bogdan.drew.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    int code;
    CustomGroceryAdapter groceryAdapter;
     CustomDebtAdapter debtAdapter;
    CustomChoresAdapter choresAdapter;
    House house;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        Intent receive_intent = getIntent();
        code = receive_intent.getIntExtra("code", -1);
        house = new House(code);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView textView = findViewById(R.id.house_title);
        textView.setText(getString(R.string.housename) + code);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().compareTo("Debt") == 0) {
                    house.setCurrentSelected("Debt");
                     debtAdapter = new CustomDebtAdapter(house.getAll());
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(debtAdapter);
                }
                else if(spinner.getSelectedItem().toString().compareTo("Chore") == 0) {
                    house.setCurrentSelected("Chore");
                     choresAdapter = new CustomChoresAdapter(house.getAll());
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(choresAdapter);
                }
                else if(spinner.getSelectedItem().toString().compareTo("Grocery") == 0) {
                    house.setCurrentSelected("Grocery");
                     groceryAdapter = new CustomGroceryAdapter(house.getAll());
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

        public CustomDebtAdapter(ArrayList<ListPiece> debtList) {
            this.debtList = debtList;
        }

        ArrayList<ListPiece> debtList;
        class CustomDebtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            ImageView imageView;
            public CustomDebtViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.houseCardTitle);
                imageView = itemView.findViewById(R.id.houseCardImage);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Debt debt = (Debt) debtList.get(getAdapterPosition());
                double amount = debt.getAmount();
                int id = debt.getId();
                String description = debt.getDescription();
                String userOwed = debt.getUserOwed().getName();
                String userOwing = debt.getUserOwing().getName();
                String date = debt.getDate();
                Intent intent = new Intent(HouseActivity.this, ItemActivity.class);
                intent.putExtra("owed", userOwed);
                intent.putExtra("amount", amount);
                intent.putExtra("id", id);
                intent.putExtra("description", description);
                intent.putExtra("owing", userOwing);
                intent.putExtra("date", date);
                intent.putExtra("type", "Debt");
                intent.putExtra("editing", true);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete Debt").setMessage("You are about to delete a debt").setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                house.setCurrentSelected("Debt");
                                house.deleteSingle(debtList.get(getAdapterPosition()).getId());
                            }
                        });
                builder.show();
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
           Debt debt = (Debt) debtList.get(position);
           holder.updateView(debt);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "debt");
            return debtList.size();
        }
    }

    class CustomChoresAdapter extends RecyclerView.Adapter<CustomChoresAdapter.CustomChoresViewHolder> {

        public CustomChoresAdapter(ArrayList<ListPiece> choreList) {
            this.choreList = choreList;
        }

        ArrayList<ListPiece> choreList;
        class CustomChoresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            ImageView imageView;
            public CustomChoresViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.houseCardTitle);
                imageView = itemView.findViewById(R.id.houseCardImage);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                // TODO: start intent to open house main activity using the code
                Chore chore = (Chore) choreList.get(getAdapterPosition());
                String choreToDo = chore.getChore();
                String userAdded = chore.getUserAdded().getName();
                String date = chore.getDate();

                Intent intent = new Intent(HouseActivity.this, ItemActivity.class);
                intent.putExtra("chore", choreToDo);
                intent.putExtra("user", userAdded);
                intent.putExtra("date", date);
                intent.putExtra("id", chore.getId());
                intent.putExtra("type", "Chore");
                intent.putExtra("editing", true);
                startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete chore").setMessage("You are about to delete a chore").setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                house.setCurrentSelected("Chore");
                                house.deleteSingle(getAdapterPosition());
                            }
                        });
                builder.show();
                return true;
            }

            public void updateView(Chore chore) {
                title.setText(chore.getChore());
                // "https://www.flaticon.com/authors/smashicons"
                imageView.setImageResource(R.drawable.bucket);
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
            Chore chore = (Chore) choreList.get(position);
            holder.updateView(chore);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "chore");
            return choreList.size();
        }
    }

    class CustomGroceryAdapter extends RecyclerView.Adapter<CustomGroceryAdapter.CustomGroceryViewHolder> {
        public CustomGroceryAdapter(ArrayList<ListPiece> groceryList) {
            this.groceryList = groceryList;
        }

        ArrayList<ListPiece> groceryList;

        class CustomGroceryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            ImageView imageView;
            public CustomGroceryViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.houseCardTitle);
                imageView = itemView.findViewById(R.id.houseCardImage);
                Log.d(TAG, "grocery");
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                // TODO: start intent to open house main activity using the code
                Grocery grocery = (Grocery) groceryList.get(getAdapterPosition());
                Intent intent = new Intent(HouseActivity.this, ItemActivity.class);
                intent.putExtra("grocery", grocery.getGrocery());
                intent.putExtra("user", grocery.getUserAdded().getName());
                intent.putExtra("editing", true);
                intent.putExtra("type", "Grocery");

                startActivity(intent);

            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete grocery").setMessage("You are about to delete a grocery").setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                house.setCurrentSelected("Grocery");
                                house.deleteSingle(getAdapterPosition());
                            }
                        });
                builder.show();
                return true;
            }

            public void updateView(Grocery grocery) {
                title.setText(grocery.getGrocery());
                // "https://www.flaticon.com/authors/smashicons"
                imageView.setImageResource(R.drawable.grocery);
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
            Grocery grocery = (Grocery) groceryList.get(position);
            holder.updateView(grocery);
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "grocery" + groceryList.size());
            return groceryList.size();
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
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String selected = spinner.getSelectedItem().toString();
                Intent intent = new Intent(HouseActivity.this, ItemActivity.class);
                if(selected.compareTo("Debt") == 0)
                    intent.putExtra("type", "Debt");
                else if(selected.compareTo("Chore") == 0)
                    intent.putExtra("type", "Chore");
                else if(selected.compareTo("Grocery") == 0)
                    intent.putExtra("type", "Grocery");
                intent.putExtra("houseId", code);
                intent.putExtra("editing", false);
                startActivity(intent);

                break;
            case R.id.deleteMenuItem:
                spinner = (Spinner) findViewById(R.id.spinner);
                selected = spinner.getSelectedItem().toString();
                House house = new House(code);

                if(selected.compareTo("Debt") == 0)
                    house.setCurrentSelected("Debt");
                else if(selected.compareTo("Chore") == 0)
                    house.setCurrentSelected("Chore");
                else if(selected.compareTo("Grocery") == 0)
                    house.setCurrentSelected("Grocery");
                ArrayList<ListPiece> listPieceArrayList = house.getAll();
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete " + selected).setMessage("You are about to delete a " + selected).setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                house.deleteAll();
//                                if(selected.compareTo("Debt") == 0)
//                                    debtAdapter.notifyDataSetChanged();
//                                else if(selected.compareTo("Chore") == 0)
//                                    choresAdapter.notifyDataSetChanged();
//                                else if(selected.compareTo("Grocery") == 0)
//                                    groceryAdapter.notifyDataSetChanged();
                            }
                        });
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
