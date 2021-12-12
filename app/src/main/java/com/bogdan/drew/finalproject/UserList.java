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

public class UserList extends DrewList {

    private static final String TAG = "userlist";

    ArrayList<User> list;

    public UserList(DatabaseReference HouseReference) {
        super(HouseReference);
        list = new ArrayList<>();
        currentReference = HouseReference.child("UserList");
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
        baseReference.child("UserList").addValueEventListener(changeListener);

    }

    @Override
    public void insert(ListPiece item) {
        list.add((User) item);
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
            list.add(toDelete,(User) item);
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
        for(User usr : list) {
            returning.add(usr);
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
        Scanner parser = new Scanner(String.valueOf(result)).useDelimiter("User");
        parser.next();
        while(parser.hasNext()) {
            parseUser(parser.next());
        }
    }

    private void parseUser(String toParse) {
        if (toParse.length() > 20) {
            User usr = new User(baseReference);
            int usrId;
            String usrName;
            Scanner parser = new Scanner(toParse).useDelimiter("=");
            usrId = parser.nextInt();
            parser.next();
            usrName = parser.next();
            parser = new Scanner(usrName).useDelimiter(",");
            usrName = parser.next();
            usr.setId(usrId);
            if(usrName.contains("}}")) {
                usr.setName(usrName.substring(0, usrName.length() - 2));
            }else {
                usr.setName(usrName.substring(0, usrName.length() - 1));
            }
            list.add(usr);
        }
    }
}
