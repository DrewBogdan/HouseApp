package com.bogdan.drew.finalproject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

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
                    Log.d(TAG, "onComplete: " + task.getResult().getValue());
//                    Scanner parser = new Scanner(String.valueOf(task.getResult().getValue()));
//                    while(parser.hasNext()) {
//                        parseGrocery(parser.next());
//                    }
                }
            }
        });
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

    private void parseGrocery(String toParse) {
        Grocery gry;
        User user = new User(baseReference);
        Scanner parser = new Scanner(toParse).useDelimiter(Pattern.compile("="));
        String idS = parser.next();
        String data = parser.next();
        parser = new Scanner(idS).useDelimiter("Grocery");
        parser.next();
        int id = parser.nextInt();
        parser = new Scanner(data).useDelimiter(",");
        String info = parser.next();
        String us1 = parser.next();
        parser = new Scanner(info).useDelimiter("=");
        parser.next();
        info = parser.next();
        parser = new Scanner(us1).useDelimiter("=");
        parser.next();
        us1 = parser.next();
        parser = new Scanner(us1).useDelimiter("}}");
        us1 = parser.next();
        user.setName(us1);
        gry = new Grocery(user, info, baseReference);
        gry.setId(id);
        list.add(gry);
    }

}
