package com.sabaq.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class Ongoing extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
   ArrayList<String> arr1=new ArrayList<>();
   ArrayList<String> arr2=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing);
        recyclerView=findViewById(R.id.recycler);
        databaseHelper=new DatabaseHelper(this);
        Cursor res=databaseHelper.getSabaq2();

        if (res.getCount()==0) {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (res.moveToNext())
            {
                arr1.add(res.getString(1));
                arr2.add(res.getString(2));

            }
            String s1[] = new String[arr1.size()];
            String s2[] = new String[arr1.size()];
            for (int i = 0; i < arr1.size(); i++) {
                s1[i] = arr1.get(i);
                s2[i] = arr2.get(i);
                My_Info myAdapter = new My_Info(getApplicationContext(), s1, s2);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

        }

    }
}
