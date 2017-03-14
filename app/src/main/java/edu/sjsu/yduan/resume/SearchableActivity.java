package edu.sjsu.yduan.resume;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView=null;
    private RecycleViewAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView)findViewById(R.id.searchrecyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //int k = intent.getIntExtra("MenuKey",-1);
            ArrayList<ResumeItem> fl = (ArrayList<ResumeItem>)intent.getSerializableExtra("List");
            adapter = new RecycleViewAdapter(fl,this);
            adapter.getFilter().filter(query);
            mRecyclerView.swapAdapter(adapter,true);
        }
    }
}
