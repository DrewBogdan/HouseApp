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
    User userAdded;

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
                        DatabaseReference tempRef = ref.child("Chore" + id);
                        tempRef.push().setValue(userAdded);
                        tempRef.push().setValue(chore);
                        tempRef.push().setValue(date);
                    }
                }
            });
        }
        else {
            tempRef = ref.child("Chore" + id);
            tempRef.push().setValue(userAdded);
            tempRef.push().setValue(chore);
            tempRef.push().setValue(date);
        }
    }
}
