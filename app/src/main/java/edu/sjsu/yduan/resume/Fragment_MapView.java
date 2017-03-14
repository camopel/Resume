package edu.sjsu.yduan.resume;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.squareup.picasso.Picasso;

import android.location.Address;

public class Fragment_MapView extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {
    private final String TAG = this.getClass().getSimpleName();
    //private static final String ARG_PARAM1 = "mv_param1";
    private static final String ARG_PARAM2 = "mv_param2";
    MapView mapView;
    GoogleMap googleMap;
    private HashMap<Marker, ResumeItem> MRDict;
    private ArrayList<ResumeItem> Resume;
    private LatLngBounds.Builder builder;
    private final int MY_PERMISSION_LOCATION = 1;

    //private int Key;
    //private OnFragmentInteractionListener mListener;
    public Fragment_MapView() {
    }

    public static Fragment_MapView newInstance(int param1, ArrayList<ResumeItem> param2) {
        Fragment_MapView fragment = new Fragment_MapView();
        Bundle args = new Bundle();
        //args.putInt(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Key = getArguments().getInt(ARG_PARAM1);
            Resume = (ArrayList<ResumeItem>) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        //getActivity().setTitle("Map View");
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        LatLng p1 = null;
        try {
            String[] add = strAddress.split(",");
            address = coder.getFromLocationName(add[0] + "+" + add[add.length - 1], 1);
            if (address != null && address.size() > 0) {
                Address location = address.get(0);
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                p1 = new LatLng(lat, lon);
            } else p1 = new LatLng(0, 0);
        } catch (IOException ex) {
            Log.d(TAG, ex.getStackTrace().toString());
        }
        return p1;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        ResumeItem mData = MRDict.get(marker);
        Context context = getContext();
        Intent intent = new Intent(context, ListItemDetailActivity.class);
        String skey = context.getString(R.string.Time_View);
        intent.putExtra(skey, mData);
        context.startActivity(intent);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        /*
        this.googleMap = gMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        this.googleMap = gMap;
        googleMap.setOnMarkerClickListener(this);
        builder = new LatLngBounds.Builder();
        MRDict = new HashMap<>();
        for (ResumeItem ri : Resume) {
            LatLng ll = getLocationFromAddress(ri.address);
            builder.include(ll);
            Marker m = googleMap.addMarker(new MarkerOptions()
                    .position(ll)
                    .title(ri.org)
                    .draggable(false)
                    .snippet(ri.title));
            MRDict.put(m, ri);
            POITarget pt = new POITarget(m);
            Picasso.with(this.getContext()).load(ri.logoUrl).resize(100,100).into(pt);
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        showMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSION_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showMap();
        }
    }
    private void showMap(){
        int c1 = ActivityCompat.checkSelfPermission(this.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        int c2 = ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int c3 = PackageManager.PERMISSION_GRANTED;
        if (c1 != c3 || c2 != c3) {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_LOCATION);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this.getActivity());
        LatLngBounds bounds = builder.build();
        int padding = 100;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cameraUpdate);
    }
    public void showAlertDialog(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getContext());
        builder.setTitle(title);
        builder.setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
