package com.bogdan.drew.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase FirebaseDatabase;
    private DatabaseReference GroceryListDatabaseReference;
    private DatabaseReference DebtListDatabaseReference;
    private DatabaseReference ChoresListDatabaseReference;
    private DatabaseReference DebtNameListDatabaseReference;
    private DatabaseReference HouseListDatabaseReference;
    public final String TAG = "demoTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase = FirebaseDatabase.getInstance();
        HouseListDatabaseReference = FirebaseDatabase.getReference().child("House");
        GroceryListDatabaseReference = HouseListDatabaseReference.child("GroceryList");
        DebtListDatabaseReference = HouseListDatabaseReference.child("DebtList");
        ChoresListDatabaseReference = HouseListDatabaseReference.child("ChoresList");
        DebtNameListDatabaseReference = DebtListDatabaseReference.child("DebtName");

        HouseListDatabaseReference.child("ID").push().setValue("1");

        GroceryList listTest = new GroceryList(HouseListDatabaseReference);

        Button button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String text = editText.getText().toString();
//                DebtNameListDatabaseReference.push().setValue(text);
                listTest.update(1, new Grocery(new User("Drew", HouseListDatabaseReference), "george", HouseListDatabaseReference.getRef()));
            }
        });

        Button button2 = findViewById(R.id.button2);
        EditText editText2 = findViewById(R.id.editText2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText2.getText().toString();
                Grocery gry1 = new Grocery(new User("Drew", HouseListDatabaseReference), text, HouseListDatabaseReference.getRef());
                listTest.insert(gry1);
            }
        });

        Button button3 = findViewById(R.id.button3);
        EditText editText3 = findViewById(R.id.editText3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String text = editText3.getText().toString();
//                ChoresListDatabaseReference.push().setValue(text);
                listTest.deleteAll();
            }
        });

//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String message = "no input";
//                message = dataSnapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        };
//        GroceryListDatabaseReference.addChildEventListener(childEventListener);

    }
}