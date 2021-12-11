package com.bogdan.drew.finalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class House {

    private DrewList chores;
    private DrewList groceries;
    private DrewList debts;
    private DrewList users;

    private String name;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference HouseListDatabaseReference;

    public House() {
        HouseListDatabaseReference = database.getReference().child("House");
        chores = new ChoreList(HouseListDatabaseReference);
        groceries = new GroceryList(HouseListDatabaseReference);
        debts = new DebtList(HouseListDatabaseReference);
        users = new UserList(HouseListDatabaseReference);
    }

    public House(String houseName) {
        HouseListDatabaseReference = database.getReference().child(houseName);
        chores = new ChoreList(HouseListDatabaseReference);
        groceries = new GroceryList(HouseListDatabaseReference);
        debts = new DebtList(HouseListDatabaseReference);
        users = new UserList(HouseListDatabaseReference);
    }


}
