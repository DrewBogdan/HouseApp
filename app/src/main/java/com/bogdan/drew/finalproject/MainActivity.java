package com.bogdan.drew.finalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    CustomAdapter adapter = new CustomAdapter();
    ArrayList<Integer> codeList;
    List<House> houseList = new ArrayList<>();
    public static final String TAG = "mainActivityTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("House main");
        if(savedInstanceState != null) {
            codeList = new ArrayList<>();
            codeList = savedInstanceState.getIntegerArrayList("codeList");
            Log.d(TAG, codeList.size() + "herelklkh");
            if(codeList.size()== 0)
                codeList = new ArrayList<>();
            else {
                for (int i = 0; i < codeList.size(); i++) {
                    houseList.add(new House(codeList.get(i)));
                }
            }

            Log.d(TAG, houseList.size() + "here");
            adapter.notifyDataSetChanged();
        }
        else
            codeList = new ArrayList<>();
        // TODO: assign codes to create house objects and insert them
        RecyclerView recyclerView = findViewById(R.id.houseRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        EditText editText = findViewById(R.id.codeEnter);
        Button addHouse = findViewById(R.id.addHouse);
        addHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if(text.compareTo("") != 0) {
                    int code = Integer.parseInt(text);
                    House house = new House(code);
                    houseList.add(house);
                    codeList.add(code);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putIntegerArrayList("codeList", codeList);
        Log.d(TAG, codeList.size()+ "");
        super.onSaveInstanceState(outState);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {


        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView title;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.houseCardTitle);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int code = codeList.get(getAdapterPosition());
                Intent intent = new Intent(MainActivity.this, HouseActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);

            }

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete house").setMessage("You are about to delete a house").setNegativeButton("Dismiss", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                houseList.remove(getAdapterPosition());
                                codeList.remove(getAdapterPosition());
                                adapter.notifyItemRemoved(getAdapterPosition());
                                codeList.clear();
                            }
                        });
                builder.show();
                return true;
            }

            public void updateView(House house) {
                title.setText("house " +  codeList.get(getAdapterPosition()));
            }


        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.house_card_view, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
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
                int code = generateCode();
                codeList.add(code);
                House house = new House(code);
                houseList.add(house);
                Log.d(TAG, code + "");
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int generateCode() {
        int code = 0;
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            code += (random.nextInt(10) * Math.pow(10,i));
        }
        return code;
    }
}