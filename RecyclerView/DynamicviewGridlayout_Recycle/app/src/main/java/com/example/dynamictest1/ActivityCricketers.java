package com.example.dynamictest1;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityCricketers extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Cricketer> cricketerArrayList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crickerters);

        recyclerView = findViewById(R.id.recycler_cricketers);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);


        cricketerArrayList=(ArrayList<Cricketer>) getIntent().getExtras().getSerializable("list");

        Log.d("로그확인:",cricketerArrayList.get(0).getTeamName());
        Log.d("로그확인:",cricketerArrayList.get(0).getCricketerName());

        recyclerView.setAdapter(new CricketerAdapter(cricketerArrayList));

    }
}
