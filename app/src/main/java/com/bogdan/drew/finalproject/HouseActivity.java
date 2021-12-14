package com.bogdan.drew.finalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
    CustomGroceryAdapter  groceryAdapter = new CustomGroceryAdapter();;
    CustomDebtAdapter debtAdapter = new CustomDebtAdapter();
    CustomChoresAdapter choresAdapter = new CustomChoresAdapter();
    ArrayList<ListPiece> groceryList = new ArrayList<>();
    ArrayList<ListPiece> debtList = new ArrayList<>();
    ArrayList<ListPiece> choreList= new ArrayList<>();
    House house;
    ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        Intent receive_intent = getIntent();
        code = receive_intent.getIntExtra("code", -1);
        house = new House(code);
        setTitle(getString(R.string.houseTitle) + code);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.spinner_items, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = findViewById(R.id.house_title);
        textView.setText(getString(R.string.house_title) + code);
//        spinner.setSelection(0);
//        RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(debtAdapter);
//        debtList = house.getAll();
//        debtAdapter.notifyDataSetChanged();
        house.givePropertyChange(HouseActivity.this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) adapterView.getChildAt(0)).setTextSize(24);
                if(spinner.getSelectedItem().toString().compareTo("Debt") == 0) {
                    house.setCurrentSelected("Debt");
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(debtAdapter);
                    house.givePropertyChange(HouseActivity.this);
                }
                else if(spinner.getSelectedItem().toString().compareTo("Chore") == 0) {
                    house.setCurrentSelected("Chore");
                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(choresAdapter);
                    house.givePropertyChange(HouseActivity.this);
                }
                else if(spinner.getSelectedItem().toString().compareTo("Grocery") == 0) {
                    house.setCurrentSelected("Grocery");

                    RecyclerView recyclerView = findViewById(R.id.listRecyclerView);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HouseActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(groceryAdapter);
                    house.givePropertyChange(HouseActivity.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {

                            Intent data = result.getData();
                            assert data != null;
                            String type = data.getStringExtra("type");
                            Log.d(TAG, type);
                            if(type.compareTo("Debt") == 0) {
                                spinner.setSelection(0);
                                house.setCurrentSelected("Debt");
                                house.givePropertyChange(HouseActivity.this);

                            }
                            else if(type.compareTo("Chore") == 0) {
                                spinner.setSelection(1);
                                house.setCurrentSelected("Chore");
                                choreList = house.getAll();
                                house.givePropertyChange(HouseActivity.this);

                            }
                            else if(type.compareTo("Grocery") == 0) {
                                spinner.setSelection(2);
                                house.setCurrentSelected("Grocery");
                                house.givePropertyChange(HouseActivity.this);
                            }

                        }
                    }
                });
    }

    public void dataSetChanged() {

        debtList.clear();
        choreList.clear();
        groceryList.clear();
        house.setCurrentSelected("Debt");
        debtList = house.getAll();
        house.setCurrentSelected("Chore");
        choreList = house.getAll();
        house.setCurrentSelected("Grocery");
        groceryList = house.getAll();
        debtAdapter.notifyDataSetChanged();
        groceryAdapter.notifyDataSetChanged();
        choresAdapter.notifyDataSetChanged();
    }

    class CustomDebtAdapter extends RecyclerView.Adapter<CustomDebtAdapter.CustomDebtViewHolder> {

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
                launcher.launch(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete Debt").setMessage(R.string.alertm).setNegativeButton(R.string.dismiss, null)
                        .setPositiveButton(R.string.deletei, new DialogInterface.OnClickListener() {
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
                launcher.launch(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete chore").setMessage("You are about to delete a chore").setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                house.setCurrentSelected("Chore");
                                house.deleteSingle(choreList.get(getAdapterPosition()).getId());
                                Log.d(TAG, choreList.get(getAdapterPosition()).getId() + "");
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
                Grocery grocery = (Grocery) groceryList.get(getAdapterPosition());
                Intent intent = new Intent(HouseActivity.this, ItemActivity.class);
                intent.putExtra("grocery", grocery.getGrocery());
                intent.putExtra("user", grocery.getUserAdded().getName());
                intent.putExtra("editing", true);
                intent.putExtra("type", "Grocery");

                launcher.launch(intent);

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
            case android.R.id.home:
                this.finish();
                return true;
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
                launcher.launch(intent);

                break;
            case R.id.deleteMenuItem:
                spinner = (Spinner) findViewById(R.id.spinner);
                selected = spinner.getSelectedItem().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(HouseActivity.this);
                builder.setTitle("Delete " + selected).setMessage("You are about to delete a " + selected).setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(selected.compareTo("Debt") == 0) {
                                    house.setCurrentSelected("Debt");
                                    debtList.clear();
                                    debtAdapter.notifyDataSetChanged();
                                }
                                else if(selected.compareTo("Chore") == 0) {
                                    house.setCurrentSelected("Chore");
                                    choreList.clear();
                                    choresAdapter.notifyDataSetChanged();
                                }
                                else if(selected.compareTo("Grocery") == 0) {
                                    house.setCurrentSelected("Grocery");
                                    groceryList.clear();
                                    groceryAdapter.notifyDataSetChanged();
                                }
                                house.deleteAll();
                                house.givePropertyChange(HouseActivity.this);

                            }
                        });
                builder.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
