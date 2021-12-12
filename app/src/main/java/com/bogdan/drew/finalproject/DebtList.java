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

public class DebtList extends DrewList {

    private static final String TAG = "debtlist";

    ArrayList<Debt> list;

    public DebtList(DatabaseReference HouseReference) {
        super(HouseReference);
        list = new ArrayList<>();
        currentReference = HouseReference.child("DebtList");
        currentReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: failed");
                }
                else {
                    Log.d(TAG, "onComplete: " + task.getResult().getValue());
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
                    list.clear();
                    updateData(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        baseReference.child("DebtList").addValueEventListener(changeListener);
    }

    @Override
    public void insert(ListPiece item) {
        list.add((Debt) item);
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
            list.add(toDelete,(Debt) item);
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
        for(Debt dbt : list) {
            returning.add(dbt);
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
        Scanner parser = new Scanner(String.valueOf(result)).useDelimiter("Debt");
        parser.next();
        while(parser.hasNext()) {
            parseDebt(parser.next());
        }
    }

    private void parseDebt(String debtParse) {
        if(debtParse.length() > 20) {
            Log.d(TAG, "parseDebt: debtPArse " + debtParse);
            ArrayList<String> strings = new ArrayList<>();
            Scanner parser = new Scanner(debtParse).useDelimiter("-Mq");
            User usr = new User(baseReference);
            Debt dbt = new Debt(baseReference);
            String idString = parser.next();
            Log.d(TAG, "parseDebt: idString " + idString);
            Scanner idParse = new Scanner(idString).useDelimiter("=");
            dbt.setId(idParse.nextInt());
            while (parser.hasNext()) {
                strings.add(parser.next().substring(10));
            }
            Log.d(TAG, "parseDebt: strngs : " + strings);
            for (String string : strings) {
                Log.d(TAG, "parseDebt: start" + string);
                if(!string.contains("=")) {
                    Log.d(TAG, "parseDebt: no good" + string);
                }
                else if (string.contains("-0") || string.contains("-1")) {
                    Log.d(TAG, "parseDebt: userMake" + string);
                    parser = new Scanner(string).useDelimiter("=");
                    parser.next();
                    String temp = parser.next();
                    Log.d(TAG, "parseDebt: user " + temp);
                    parser = new Scanner(temp.substring(0,temp.length()-2)).useDelimiter("-");
                    usr.setName(parser.next());
                    Log.d(TAG, "parseDebt: user " + usr.getName());
                    usr.setId(parser.nextInt());
                    Log.d(TAG, "parseDebt: user " + usr.getId());
                    String idS = parser.next();
                    int tester;
                    if(idS.contains("}")) {
                        parser = new Scanner(idS.substring(0,idS.length()-1));
                        tester = parser.nextInt();
                    }
                    else {
                        parser = new Scanner(idS.substring(0,idS.length()));
                        tester = parser.nextInt();

                    }
                    if (tester == 0) {
                        dbt.setUserOwed(usr);
                    } else {
                        dbt.setUserOwing(usr);
                    }
                    usr = new User(baseReference);
                } else if (string.contains("/")) {
                    Log.d(TAG, "parseDebt: date" + string);
                    parser = new Scanner(string).useDelimiter("=");
                    parser.next();
                    String temp = parser.next();
                    if (temp.contains("}")) {
                        String newGry = "";
                        for (int x = 0; x < temp.length(); x++) {
                            if (temp.charAt(x) != '}') {
                                newGry += temp.charAt(x);
                            }
                        }
                        temp = newGry;
                    } else {
                        temp = temp.substring(0, temp.length() - 2);
                    }
                    dbt.setDate(temp);
                } else if (string.contains("$")) {
                    Log.d(TAG, "parseDebt: ammount" + string);
                    parser = new Scanner(string).useDelimiter("=");
                    Log.d(TAG, "parseDebt: " + parser.next());
                    Log.d(TAG, "parseDebt: " + string);
                    String temp = parser.next();
                    String newTemp = "";
                    for(int x = 0; x < temp.length(); x++) {
                        if(temp.charAt(x) != '}' && temp.charAt(x) != '$' && temp.charAt(x) != ',') {
                            newTemp += temp.charAt(x);
                        }
                    }
                    parser = new Scanner(newTemp);
                    Log.d(TAG, "parseDebt: " + newTemp);
                    double val = parser.nextDouble();
                    dbt.setAmount(val);

                } else {
                    Log.d(TAG, "parseDebt: dext" + string);
                    parser = new Scanner(string).useDelimiter("=");
                    parser.next();
                    String temp = parser.next();
                    Log.d(TAG, "parseDebtTTEmp: " + temp);
                    Log.d(TAG, "parseDebt: " + string);
                    String newTemp = "";
                    for(int x = 0; x < temp.length(); x++) {
                        if(temp.charAt(x) != '}') {
                            newTemp += temp.charAt(x);
                        }
                    }
                    dbt.setDescription(newTemp.substring(0,newTemp.length()-2));
                }
            }
            list.add(dbt);
        }



    }
}
