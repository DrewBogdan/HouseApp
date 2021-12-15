package com.bogdan.drew.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GroceryList extends DrewList {

    ArrayList<Grocery> list;

    private final static String TAG = "ListTag";

    public GroceryList(DatabaseReference HouseReference) {
        super(HouseReference);
        list = new ArrayList<>();
        currentReference = HouseReference.child("GroceryList");
        currentReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: failed");
                }
                else {
                    Log.d(TAG, "onComplete KEY: " + task.getResult().getValue());
                    updateData(String.valueOf(task.getResult().getValue()));
                    attachListener();
                }
            }
        });
    }

    private void attachListener() {
        ValueEventListener changeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    updateData(dataSnapshot.getValue().toString());
                    Log.d(TAG, "onDataChange: " + dataSnapshot.getKey());
                    firePropertyChange();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        baseReference.child("GroceryList").addValueEventListener(changeListener);
    }

    @Override
    public void insert(ListPiece item) {
        list.add((Grocery) item);
        item.addData(currentReference);
    }

    @Override
    public void update(int id, ListPiece item) {
        int toDelete = -1;
        for(int count = 0; count < list.size(); count++) {
            if(list.get(count).getId() == id) {
                list.get(count).deleteData();
                toDelete = count;
                item.setId(id);
            }
        }
        if(toDelete != -1) {
            list.remove(toDelete);
            list.add(toDelete,(Grocery) item);
            item.addData(currentReference);
        }
    }

    @Override
    public void deleteAll() {
        currentReference.removeValue();
    }

    @Override
    public void deleteSingle(int id) {
        int toDelete = -1;
        for(int count = 0; count < list.size(); count++) {
            if(list.get(count).getId() == id) {
                list.get(count).deleteData();
                toDelete = count;
            }
        }
        if(toDelete != -1) {
            list.remove(toDelete);
        }

    }

    @Override
    public void deleteSelected(ArrayList<Integer> idList) {
        ArrayList<Integer> toDelete = new ArrayList<>();
        for(int count = 0; count < list.size(); count++) {
            if(idList.contains(list.get(count).getId())) {
                list.get(count).deleteData();
                toDelete.add(count);
            }
        }
        if(!toDelete.isEmpty()) {
            for(Integer index : toDelete) {
                list.remove(index);
            }
        }
    }

    @Override
    public ArrayList<ListPiece> getAll() {
        ArrayList<ListPiece> returning = new ArrayList<>();
        for(Grocery gry : list) {
            returning.add(gry);
        }
        return returning;
    }

    @Override
    public ListPiece getSingle(int id) {
        for(int count = 0; count < list.size(); count++) {
            if(list.get(count).getId() == id) {
                return list.get(count);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    private void updateData(String result) {
        list.clear();
        Scanner parser = new Scanner(String.valueOf(result)).useDelimiter("Grocery");
        parser.next();
        while(parser.hasNext()) {
            parseGrocery(parser.next());
        }
    }

    private void parseGrocery(String toParse) {
        if (toParse.length() > 20) {
            Log.d(TAG, "parseDebt: debtPArse " + toParse);
            ArrayList<String> strings = new ArrayList<>();
            Scanner parser = new Scanner(toParse).useDelimiter("=");
            User usr = new User(baseReference);
            Grocery gry = new Grocery(baseReference);
            gry.setId(parser.nextInt());
            parser = new Scanner(toParse).useDelimiter("-M");
            parser.next();
            while (parser.hasNext()) {
                strings.add(parser.next().substring(10));
            }
            Log.d(TAG, "parseDebt: strngs : " + strings);
            for (String string : strings) {
                Log.d(TAG, "parseDebt: start" + string);
                if (!string.contains("=")) {
                    Log.d(TAG, "parseDebt: no good" + string);
                } else if (string.contains("-0")) {
                    Log.d(TAG, "parseDebt: userMake" + string);
                    parser = new Scanner(string).useDelimiter("=");
                    parser.next();
                    String temp = parser.next();
                    Log.d(TAG, "parseDebt: user " + temp);
                    parser = new Scanner(temp.substring(0, temp.length() - 2)).useDelimiter("-");
                    usr.setName(parser.next());
                    Log.d(TAG, "parseDebt: user " + usr.getName());
                    usr.setId(parser.nextInt());
                    Log.d(TAG, "parseDebt: user " + usr.getId());
                } else {
                    Log.d(TAG, "parseDebt: dext" + string);
                    parser = new Scanner(string).useDelimiter("=");
                    parser.next();
                    String temp = parser.next();
                    Log.d(TAG, "parseDebtTTEmp: " + temp);
                    Log.d(TAG, "parseDebt: " + string);
                    String newTemp = "";
                    for (int x = 0; x < temp.length(); x++) {
                        if (temp.charAt(x) != '}') {
                            newTemp += temp.charAt(x);
                        }
                    }
                    gry.setGrocery(newTemp.substring(0, newTemp.length() - 2));
                }
            }
            gry.setNewUser(usr);
            list.add(gry);
        }
    }

}
