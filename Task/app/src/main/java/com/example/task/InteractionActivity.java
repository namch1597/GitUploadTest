package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.task.Adapter.JsAdapter;
import com.example.task.databinding.ActivityInteractionBinding;

import java.util.ArrayList;

public class InteractionActivity extends AppCompatActivity {

    ActivityInteractionBinding binding;

    ArrayList<String>list = new ArrayList<String>();
    JsAdapter jsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_interaction);

        addData();

        FirstViewSetting();
    }

    public void addData() {
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");
        list.add("진선");

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        binding.rvInteraction.setLayoutManager(mLinearLayoutManager);

        jsAdapter = new JsAdapter(list);
        binding.rvInteraction.setAdapter(jsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.rvInteraction.getContext(),
                mLinearLayoutManager.getOrientation());
        binding.rvInteraction.addItemDecoration(dividerItemDecoration);
    }

    public void FirstViewSetting() {
        Toolbar toolbar = (Toolbar) binding.tbToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.tlTablayout.addTab(binding.tlTablayout.newTab().setText("left"));
        binding.tlTablayout.addTab(binding.tlTablayout.newTab().setText("right"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}