package com.bogdan.drew.finalproject;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Scanner;

public class Debt extends ListPiece {

    User userOwed;
    User userOwing;

    double amount;

    String description;
    String date;


    public Debt(DatabaseReference ref) {
        userOwed = null;
        userOwing = null;
        amount = 0.0;
        description = null;
        date = null;
        reference = ref;
    }

    public Debt(User userOwed, User userOwing, double amount, String description, String date, DatabaseReference ref) {
        this.userOwing = userOwing;
        this.userOwed = userOwed;
        this.amount = amount;
        this.description = description;
        this.date = date;
        reference = ref;
    }

    @Override
    protected void addData(DatabaseReference ref) {
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
                        DatabaseReference tempRef = ref.child("Debt" + id);
                        tempRef.push().setValue(userOwed);
                        tempRef.push().setValue(userOwing);
                        tempRef.push().setValue(amount);
                        tempRef.push().setValue(description);
                        tempRef.push().setValue(date);
                    }
                }
            });
        }
        else {
            tempRef = ref.child("Debt" + id);
            tempRef.push().setValue(userOwed);
            tempRef.push().setValue(userOwing);
            tempRef.push().setValue(amount);
            tempRef.push().setValue(description);
            tempRef.push().setValue(date);
        }

    }
}
