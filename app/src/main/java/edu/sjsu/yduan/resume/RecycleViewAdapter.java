package edu.sjsu.yduan.resume;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView.Adapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.util.Log;
import android.widget.Filterable;
import android.widget.Filter;
public class RecycleViewAdapter
        extends Adapter<RecyclerViewHolder>
        implements Filterable{
    String TAG = RecycleViewAdapter.class.getSimpleName();
    ArrayList<ResumeItem> fullDataList;
    ArrayList<ResumeItem> filtedDataList;
    SearchFilter sfilter;
    //int key=-1;
    Context context;

    RecycleViewAdapter(ArrayList<ResumeItem> fl,Context c){
        fullDataList = fl;
        filtedDataList = new ArrayList<>(fl);
        context = c;
        //key = k;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //int i=R.layout.listview_item;
        //if(key==R.string.Time_View) i = R.layout.listview_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.bind(filtedDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return filtedDataList.size();
    }

    @Override
    public Filter getFilter() {
        if(sfilter==null) sfilter = new SearchFilter(this);
        return sfilter;
    }
}
