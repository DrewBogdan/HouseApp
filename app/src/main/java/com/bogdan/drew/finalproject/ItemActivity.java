package com.bogdan.drew.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        House house = new House(houseId);
        if(type.compareTo("Debt") == 0) {
            setContentView(R.layout.debt_item);
            EditText dateEdit = findViewById(R.id.dateSpinnerDebt);
            EditText owedEdit = findViewById(R.id.owedEdit);
            EditText owingEdit = findViewById(R.id.OwingEdit);
            EditText descriptionEdit = findViewById(R.id.descriptionEdit);
            EditText amountEdit = findViewById(R.id.amount);
            Button add = findViewById(R.id.debtButton);
            if(editing) {
                add.setEnabled(false);
                owedEdit.setText(intent.getStringExtra("owed"));
                owingEdit.setText(intent.getStringExtra("owing"));
                descriptionEdit.setText(intent.getStringExtra("description"));
                String text = String.valueOf(intent.getDoubleExtra("amount", 1.0));
                amountEdit.setText(text);
                String date = intent.getStringExtra("date");
                dateEdit.setText(date);
                add.setText("View Mode");
            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = dateEdit.getText().toString();
                    char[] arr = date.toCharArray();
                    StringBuilder newDate = new StringBuilder();
                    if(arr.length != 5 || arr[2] != '/' ) {
                        Toast.makeText(ItemActivity.this, "wrong date format", Toast.LENGTH_LONG).show();
                        date = "";
                    }
                    else {
                        newDate.append(arr[0]).append(arr[1]).append("/").append(arr[3]).append(arr[4]);

                    }
                    String owed = owedEdit.getText().toString();
                    String owing = owingEdit.getText().toString();
                    String description = descriptionEdit.getText().toString();
                    String amountText = amountEdit.getText().toString();
                    double amount = 0.0;
                    if(!amountText.equals(""))
                        amount = Double.valueOf(amountText);
                    if(date.compareTo("") == 0 || owed.compareTo("") == 0 ||owing.compareTo("") == 0 ||description.compareTo("") == 0 ||amountText.compareTo("") == 0)
                        Toast.makeText(ItemActivity.this, "missing input", Toast.LENGTH_LONG).show();
                    else {
                        User owedObject = new User(owed, house.getHouseListDatabaseReference());
                        User owingObject = new User(owing, house.getHouseListDatabaseReference());
                        house.setCurrentSelected("Debt");
                        if (!editing) {
                            house.insert(new Debt(owedObject, owingObject, amount, description, date, house.getHouseListDatabaseReference()));
                            intent.putExtra("type", "Debt");
                            ItemActivity.this.setResult(Activity.RESULT_OK, intent);
                            ItemActivity.this.finish();
                        }
                    }


                }
            });

        }
        else if(type.compareTo("Chore") == 0) {
            setContentView(R.layout.chore_item);
            EditText dateEdit = findViewById(R.id.dateSpinner);
            EditText choreEdit = findViewById(R.id.choreEdit);
            EditText userEdit = findViewById(R.id.dateEdit);
            Button add = findViewById(R.id.choreButton);
            if(editing) {
                add.setEnabled(false);
                choreEdit.setText(intent.getStringExtra("chore"));
                userEdit.setText(intent.getStringExtra("user"));
                String date = intent.getStringExtra("date");
                dateEdit.setText(date);
                add.setText("View Mode");

            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String date = dateEdit.getText().toString();
                    char[] arr = date.toCharArray();
                    StringBuilder newDate = new StringBuilder();
                    if(arr.length != 5 || arr[2] != '/' ) {
                        Toast.makeText(ItemActivity.this, "wrong date format", Toast.LENGTH_LONG).show();
                        date = "";
                    }
                    else {
                        newDate.append(arr[0]).append(arr[1]).append("/").append(arr[3]).append(arr[4]);

                    }
                    String chore = choreEdit.getText().toString();
                    String userText = userEdit.getText().toString();
                    house.setCurrentSelected("Chore");
                    if(date.compareTo("") == 0 || chore.compareTo("") == 0 ||userText.compareTo("") == 0)
                        Toast.makeText(ItemActivity.this, "missing input", Toast.LENGTH_LONG).show();
                    else {
                        User userObject = new User(userText, house.getHouseListDatabaseReference());
                        if (!editing) {// add chore
                            house.insert(new Chore(chore, date, userObject, house.getHouseListDatabaseReference()));
                            intent.putExtra("type", "Chore");
                            ItemActivity.this.setResult(Activity.RESULT_OK, intent);
                            ItemActivity.this.finish();
                        }

                    }
                }
            });
        }
        else if(type.compareTo("Grocery") == 0) {
            setContentView(R.layout.grocery_item);
            EditText groceryEdit = findViewById(R.id.groceryTitleEdit);
            EditText userEdit = findViewById(R.id.userTitleEdit);

            Button add = findViewById(R.id.groceryButton);
            if(editing) {
                add.setEnabled(false);
                add.setText("View Mode");
                groceryEdit.setText(intent.getStringExtra("grocery"));
                userEdit.setText(intent.getStringExtra("user"));

            }
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = groceryEdit.getText().toString();
                    String user = userEdit.getText().toString();
                    house.setCurrentSelected("Grocery");
                    if(title.compareTo("") == 0 || user.compareTo("") == 0)
                        Toast.makeText(ItemActivity.this, "missing input", Toast.LENGTH_LONG).show();
                    else {
                        User userObject = new User(user, house.getHouseListDatabaseReference());
                        if (!editing)
                        {
                            house.insert(new Grocery(userObject, title, house.getHouseListDatabaseReference()));
                            intent.putExtra("type", "Grocery");
                            Log.d(TAG, "here");
                            ItemActivity.this.setResult(Activity.RESULT_OK, intent);
                            ItemActivity.this.finish();
                        }
                    }

                }
            });
        }

    }
}