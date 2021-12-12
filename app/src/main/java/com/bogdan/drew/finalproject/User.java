package com.bogdan.drew.finalproject;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.Scanner;

public class User extends ListPiece {

    private String name;

    /**
     * DVC
     */
    public User(DatabaseReference ref) {
        name = "Empty Name";
        reference = ref;
    }

    /**
     * EVC
     * @param username
     */
    public User(String username, DatabaseReference ref) {
        name = username;
        reference = ref;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                        DatabaseReference tempRef = ref.child("User" + id);
                        tempRef.push().setValue(name);
                    }
                }
            });
        }
        else {
            tempRef = ref.child("User" + id);
            tempRef.push().setValue(name);
        }
    }

    public String toString() {
        return name + "-" + id;
    }
}
