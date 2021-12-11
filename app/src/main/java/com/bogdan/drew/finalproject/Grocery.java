package com.bogdan.drew.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Scanner;

public class Grocery extends ListPiece {

    private User userAdded;

    private String grocery;

    static final String TAG = "groceryTest";
    /**
     * Empty Constructor
     */
    public Grocery(DatabaseReference ref) {
        userAdded = null;
        grocery = null;
        reference = ref;
    }

    /**
     * EVC
     * @param user
     * @param grocery
     */
    public Grocery(User user, String grocery, DatabaseReference ref) {
        userAdded = user;
        this.grocery = grocery;
        reference = ref;
    }


    public User getUserAdded() {
        return userAdded;
    }

    public void setNewUser(User userAdded) {
        this.userAdded = userAdded;
    }

    public String getGrocery() {
        return grocery;
    }

    public void setGrocery(String grocery) {
        this.grocery = grocery;
    }

    @NonNull
    public String toString() {
        return grocery + " | " + userAdded + " | " + id;
    }

    @Override
    public void addData(DatabaseReference listRef) {
        DatabaseReference tempRef;
        if(id == 0) {
            reference.child("ID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", "onComplete: " + task.getResult().getValue());
                        String result = String.valueOf(task.getResult().getValue());
                        Scanner parseEnd = new Scanner(result).useDelimiter("=");
                        parseEnd.next();
                        String end = parseEnd.next();
                        Scanner parseNumber = new Scanner(end).useDelimiter("");
                        id = parseNumber.nextInt();
                        Log.d("firebasetag", "onComplete: " + id);
                        reference.child("ID").removeValue();
                        reference.child("ID").push().setValue(id+1);
                        DatabaseReference tempRef = listRef.child("Grocery" + id);
                        tempRef.push().setValue(userAdded);
                        tempRef.push().setValue(grocery);
                        thisRef = tempRef;
                    }
                }
            });
        }
        else {
            tempRef = listRef.child("Grocery" + id);
            tempRef.push().setValue(userAdded);
            tempRef.push().setValue(grocery);
            thisRef = tempRef;
        }
    }
}
