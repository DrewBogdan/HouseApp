package com.bogdan.drew.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Scanner;

public class Chore extends ListPiece {

    private String chore;
    private String date;

    private User userAdded;

    /**
     * DVC
     * @param ref
     */
    public Chore(DatabaseReference ref) {
        reference = ref;
        userAdded = null;
        chore = null;
        date = null;
    }

    public Chore(String chore, String date, User userAdded, DatabaseReference ref) {
        this.chore = chore;
        this.date = date;
        this.userAdded = userAdded;
        reference = ref;
    }

    public String getChore() {
        return chore;
    }

    public void setChore(String chore) {
        this.chore = chore;
    }

    public User getUserAdded() {
        return userAdded;
    }

    public void setUserAdded(User userAdded) {
        this.userAdded = userAdded;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
                        DatabaseReference tempRef = ref.child("Chore" + id);
                        tempRef.push().setValue(userAdded + "-0");
                        tempRef.push().setValue(chore);
                        tempRef.push().setValue(date);
                    }
                }
            });
        }
        else {
            tempRef = ref.child("Chore" + id);
            tempRef.push().setValue(userAdded + "-0");
            tempRef.push().setValue(chore);
            tempRef.push().setValue(date);
        }
    }

    public String toString() {
        return userAdded + " | " + date + " | " + chore + " | " + id;
    }
}
