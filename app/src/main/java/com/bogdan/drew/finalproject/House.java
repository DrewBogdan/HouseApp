package com.bogdan.drew.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Scanner;

public class House {

    private DrewList chores;
    private DrewList groceries;
    private DrewList debts;
    private DrewList users;

    private String name;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference HouseListDatabaseReference;

    /**
     * for testing purposes this is here
     * this will not be used in release
     */
    public House() {
        HouseListDatabaseReference = database.getReference().child("House");
        chores = new ChoreList(HouseListDatabaseReference);
        groceries = new GroceryList(HouseListDatabaseReference);
        debts = new DebtList(HouseListDatabaseReference);
        users = new UserList(HouseListDatabaseReference);
        chores.givePropertyChange(this);
        groceries.givePropertyChange(this);
        debts.givePropertyChange(this);
        users.givePropertyChange(this);
        checkId();
    }

    public DatabaseReference getHouseListDatabaseReference() {
        return HouseListDatabaseReference;
    }

    /**
     * will take in a house code and set the referenaces
     * to that code. if that code does not exist a new
     * house is born with said code.
     * @param houseCode
     */
    public House(int houseCode) {
        HouseListDatabaseReference = database.getReference().child(""+houseCode);
        chores = new ChoreList(HouseListDatabaseReference);
        groceries = new GroceryList(HouseListDatabaseReference);
        debts = new DebtList(HouseListDatabaseReference);
        users = new UserList(HouseListDatabaseReference);
        chores.givePropertyChange(this);
        groceries.givePropertyChange(this);
        debts.givePropertyChange(this);
        users.givePropertyChange(this);
        checkId();
    }

    public void givePropertyChange() {
        /**
         * give the reference to the recycler view
         * object here and make a method this can
         * accsess to setup the property change listeners
         */
    }

    public void propertyChange() {
        /**
         * Fill this with some sort
         * of function to give the list
         * a message to update its recylerview
         * when this method is called.
         * this will be called alot when the database is changed
         * if that makes sense
         */
    }

    private void checkId() {
        HouseListDatabaseReference.child("ID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", "onComplete: " + task.getResult().getValue());
                    String result = String.valueOf(task.getResult().getValue());
                    if(result.length() < 20) {
                        HouseListDatabaseReference.child("ID").push().setValue("1");
                    }
                }
            }
        });
    }

    /**
     * Database Functions
     * ***********************************
     */

    private DrewList currentSelected = null;

    /**
     * when the selection is made for which list
     * to see, this should be run and given the string
     * of the list selected, that way it adjusts all the
     * methods within house to correctly work.
     * @param selected
     */
    public void setCurrentSelected(String selected) {
        if(selected == "Chore") {
            currentSelected = chores;
        }
        else if(selected == "Debt") {
            currentSelected = debts;
        }
        else if(selected == "Grocery") {
            currentSelected = groceries;
        }
        else if(selected == "User") {
            currentSelected = users;
        }
    }

    public void insert(ListPiece item) {
        /**
         * Item cannot contain the key words
         * Chore,Debt,Grocery,User
         * If it contains any of these the entire parse method
         * it also cannot contain the special characters
         * },{,=,-
         */
        if(currentSelected != null) {
            currentSelected.insert(item);
        }
    }

    public void update(int id, ListPiece item) {
        /**
         * same rules for insert apply here
         */
        if(currentSelected != null) {
            currentSelected.update(id, item);
        }
    }

    public void deleteAll() {
        if(currentSelected != null) {
            currentSelected.deleteAll();
        }
    }

    public void deleteSingle(int id) {
        if(currentSelected != null) {
            currentSelected.deleteSingle(id);
        }
    }

    public void deleteSelected(ArrayList<Integer> idList) {
        if(currentSelected != null) {
            currentSelected.deleteSelected(idList);
        }
    }

    public ArrayList<ListPiece> getAll() {
        if(currentSelected != null) {
            return currentSelected.getAll();
        }
        return null;
    }

    public ListPiece getSingle(int id) {
        if(currentSelected != null) {
            return currentSelected.getSingle(id);
        }
        return null;
    }



}
