package edu.sjsu.yduan.resume;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class Fragment_ListView extends Fragment {
    //private static final String ARG_PARAM1 = "lv_param1";
    private static final String ARG_PARAM2 = "lv_param2";
    private RecyclerView mRecyclerView=null;
    private RecycleViewAdapter adapter=null;
    private ArrayList<ResumeItem> Resume;
    //private int Key;

    public Fragment_ListView(){}
    public static Fragment_ListView newInstance(int param1, ArrayList<ResumeItem> param2) {
        Fragment_ListView fragment = new Fragment_ListView();
        Bundle args = new Bundle();
        //args.putInt(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            //Key = getArguments().getInt(ARG_PARAM1);
            Resume = (ArrayList<ResumeItem> )getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter = new RecycleViewAdapter(Resume,view.getContext());
        mRecyclerView.setAdapter(adapter);
        return view;
    }

}
