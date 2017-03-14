package edu.sjsu.yduan.resume;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.Picasso;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Filter;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{//,SearchView.OnQueryTextListener

    private static final String TAG =  MainActivity.class.getSimpleName();
    ProgressDialog mProgressDialog;
    ArrayList<ResumeItem> Resume;
    private Toolbar toolbar=null;
    private int Key=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!isNetworkConnected())
        {
            showAlertDialog("Warning!","No Network Connection");
            return;
        }
        //progress
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("downloading resume data");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIcon(0);
        mProgressDialog.show();
        startDownload();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setSubmitButtonEnabled(true);
        //searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(Resume!=null){
            int id = item.getItemId();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (id == R.id.menu_timeview){
                Key=R.string.Time_View;
                fragmentTransaction.replace(R.id.content_main,Fragment_ListView.newInstance(Key,Resume));
            } else if (id == R.id.menu_mapview){
                Key=R.string.Map_View;
                fragmentTransaction.replace(R.id.content_main,Fragment_MapView.newInstance(Key,Resume));
            }
            fragmentTransaction.commit();
            toolbar.setTitle(Key);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    private void startDownload() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://54.193.68.59:8123";
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "on okhttp.Call Failure:"+e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                final String result = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jd = new JSONObject(result);
                            JSONArray resume = jd.getJSONArray("exp");
                            showMenuFirstTime(jd.getJSONObject("info"));
                            Resume = new ArrayList<ResumeItem>();
                            try{
                                for (int i = 0; i < resume.length(); i++) {
                                    JSONObject jo = resume.getJSONObject(i);
                                    ResumeItem obj=new ResumeItem(jo);
                                    Resume.add(obj);
                                }
                                Collections.sort(Resume,new Comparator<ResumeItem>(){
                                    public int compare(ResumeItem a, ResumeItem b) {
                                        return a.beg.compareTo(b.beg);
                                    }
                                });
                            }catch (JSONException e) {
                                Log.d(TAG, "on JSON Failure:"+e.getMessage());
                            }
                            if (mProgressDialog != null) mProgressDialog.hide();
                        } catch (JSONException e) {
                            Log.d(TAG, "on JSON Failure:"+e.getMessage());
                        }
                    }
                });
            }
        });
    }
    private void showMenuFirstTime(JSONObject info){
        View headerView = findViewById(R.id.navheader);
        ImageView photo = (ImageView)headerView.findViewById(R.id.host_photo);
        TextView name = (TextView)headerView.findViewById(R.id.host_name);
        TextView phone = (TextView)headerView.findViewById(R.id.host_phone);
        TextView email = (TextView)headerView.findViewById(R.id.host_email);
        TextView address = (TextView)headerView.findViewById(R.id.host_address);
        try {
            Picasso.with(this).load(info.getString("image")).into(photo);
            name.setText(info.getString("name"));
            phone.setText(info.getString("phone"));
            email.setText(info.getString("email"));
            address.setText(info.getString("address"));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
        }catch (JSONException e) {
            Log.d(TAG, "on JSON Failure:"+e.getMessage());
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            intent.putExtra("MenuKey", this.Key);
            intent.putExtra("List",this.Resume);
        }
        super.startActivity(intent);
    }
}
