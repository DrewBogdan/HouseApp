package com.bogdan.drew.finalproject;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class UserList extends DrewList {

    ArrayList<User> list;

    public UserList(DatabaseReference HouseReference) {
        super(HouseReference);
        list = new ArrayList<>();
        currentReference = HouseReference.child("UserList");

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
}
