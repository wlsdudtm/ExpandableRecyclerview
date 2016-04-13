package com.lge.exapandablerecyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "[Simple][MainActivity]";
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomRecyclerView recyclerView = (CustomRecyclerView) findViewById(R.id.recyclerview);

        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new ItemLayoutManger(this));

    }
}
