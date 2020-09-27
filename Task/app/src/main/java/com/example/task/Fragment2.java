package com.example.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task.Adapter.JsAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Fragment2 extends Fragment {

    RecyclerView recyclerView;

    ArrayList<String> list = new ArrayList<String>();
    JsAdapter jsAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_layout2, container, false);
        /*Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "This is a fragment2", Snackbar.LENGTH_LONG).show();
            }
        });*/

        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_interaction);
        addData();
        return rootView;
    }

    public void addData() {
        list.add("진선");
        list.add("진선1");
        list.add("진선2");
        list.add("진선3");
        list.add("진선4");
        list.add("진선5");
        list.add("진선6");
        list.add("진선7");
        list.add("진선8");
        list.add("진선9");
        list.add("진선10");
        list.add("진선11");
        list.add("진선12");
        list.add("진선13");
        list.add("진선14");
        list.add("진선15");
        list.add("진선16");
        list.add("진선17");
        list.add("진선18");
        list.add("진선19");

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);

        jsAdapter = new JsAdapter(list);
        recyclerView.setAdapter(jsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
