package com.bogdan.drew.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class ListPiece {
    protected DatabaseReference reference = null;
    protected DatabaseReference thisRef = null;
    protected int id = 0;

    protected abstract void addData(DatabaseReference ref);

    protected void deleteData() {
        if(thisRef != null) {
            Log.d("listpiece", "deleteData: what is this" + thisRef.toString());
            thisRef.removeValue();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
