package com.bogdan.drew.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    private static final String TAG = "itemTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        boolean editing = intent.getBooleanExtra("editing", false);
        int houseId = intent.getIntExtra("houseId", -1);
        int position = intent.getIntExtra("position", -1);
        House house = new House(houseId);
        if(type.compareTo("Debt") == 0) {
            setContentView(R.layout.debt_item);

        }
        else if(type.compareTo("Chore") == 0) {
            setContentView(R.layout.chore_item);
            Spinner spinner = (Spinner) findViewById(R.id.dateSpinner);
            ArrayAdapter<CharSequence> adapter =  ArrayAdapter.createFromResource(this, R.array.dates, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            EditText choreEdit = findViewById(R.id.choreEdit);
            EditText userEdit = findViewById(R.id.dateEdit);
            Button add = findViewById(R.id.choreButton);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = spinner.getSelectedItem().toString();
                    String chore = choreEdit.getText().toString();
                    String userText = userEdit.getText().toString();
                    house.setCurrentSelected("Chore");
                    User userObject = new User(userText, house.getHouseListDatabaseReference());
                    if(!editing)
                        // add chore
                        house.insert(new Chore(chore, date, userObject, house.getHouseListDatabaseReference()));
                    else
                        house.update(position, new Chore(chore, date, userObject, house.getHouseListDatabaseReference()));
                }
            });
        }
        else if(type.compareTo("Grocery") == 0) {
            setContentView(R.layout.grocery_item);
            EditText groceryEdit = findViewById(R.id.groceryTitleEdit);
            EditText userEdit = findViewById(R.id.userTitleEdit);

            Button add = findViewById(R.id.groceryButton);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = groceryEdit.getText().toString();
                    String user = userEdit.getText().toString();
                    house.setCurrentSelected("Grocery");
                    User userObject = new User(user, house.getHouseListDatabaseReference());
                    if(!editing)
                        // add grocery
                        house.insert(new Grocery(userObject, title, house.getHouseListDatabaseReference()));
                    else
                        house.update(position, new Grocery(userObject, title, house.getHouseListDatabaseReference()));
                    ItemActivity.this.finish();
                }
            });
        }

    }
}