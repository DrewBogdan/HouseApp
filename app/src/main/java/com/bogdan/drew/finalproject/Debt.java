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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public User getUserOwing() {
        return userOwing;
    }

    public void setUserOwing(User userOwing) {
        this.userOwing = userOwing;
    }

    public User getUserOwed() {
        return userOwed;
    }

    public void setUserOwed(User userOwed) {
        this.userOwed = userOwed;
    }

    public void setId(int id) {
        super.setId(id);
        thisRef = reference.child("DebtList").child("Debt" + id);
    }


    @Override
    public String toString() {
        return "Owed " + userOwed + " | Owing " + userOwing + " | " + amount + " | " + date + " | " + description + " | " + id;
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
                        Scanner parseNumber = new Scanner(end);
                        String temp = parseNumber.next();
                        parseNumber = new Scanner(temp.substring(0,temp.length()-1));
                        id = parseNumber.nextInt();
                        Log.d("firebasetag", "onComplete: " + id);
                        reference.child("ID").removeValue();
                        reference.child("ID").push().setValue(id+1);
                        DatabaseReference tempRef = ref.child("Debt" + id);
                        tempRef.push().setValue(userOwed.toString() + "-0");
                        tempRef.push().setValue(userOwing.toString() + "-1");
                        tempRef.push().setValue("$" + amount);
                        tempRef.push().setValue(description);
                        tempRef.push().setValue(date);
                        thisRef = tempRef;
                    }
                }
            });
        }
        else {
            tempRef = ref.child("Debt" + id);
            tempRef.push().setValue(userOwed.toString() + "-0");
            tempRef.push().setValue(userOwing.toString() + "-1");
            tempRef.push().setValue(amount);
            tempRef.push().setValue(description);
            tempRef.push().setValue(date);
            thisRef = tempRef;
        }

    }
}
