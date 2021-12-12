package com.bogdan.drew.finalproject;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public abstract class DrewList {

    protected DatabaseReference baseReference;
    protected DatabaseReference currentReference;

    protected House house;

    public DrewList(DatabaseReference HouseReference) {
        baseReference = HouseReference;
    }

    public void givePropertyChange(House house) {
        this.house = house;
    }

    public void firePropertyChange() {
        house.propertyChange();
    }

    abstract protected void insert(ListPiece item);
    abstract protected void update(int id, ListPiece item);
    abstract protected void deleteAll();
    abstract protected void deleteSingle(int id);
    abstract protected void deleteSelected(ArrayList<Integer> list);
    abstract protected ArrayList<ListPiece> getAll();
    abstract protected ListPiece getSingle(int id);
    abstract public String toString();






}
