package com.example.bid;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public SharedPreferences prefs;
    private LocationListener listener;
    private LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       checkLocationPermission();
        prefs = this.getSharedPreferences(
                "com.example.bid", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

       GetlastLocation();
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)

                        .setPositiveButton("v", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void GetlastLocation() {

        final SharedPreferences.Editor editor = prefs.edit();
        Toast.makeText(getApplicationContext(),"Chose the location of your home",
                Toast.LENGTH_LONG).show();
        SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(com.example.bid.MapsActivity.this);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("p", "zap");
                /*editor.putFloat("latitude", (float) location.getLatitude());
                editor.putFloat("longitude", (float) location.getLongitude());*/
                // editor.apply();
                Intent i = new Intent("location_update");
                i.putExtra("latitude", location.getLatitude());
                i.putExtra("longitude", location.getLongitude());
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                Log.i("p", "z");
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, listener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(listener);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaahuh");
        final SharedPreferences.Editor editor = prefs.edit();
        final Intent intent = new Intent(this, Extendet_settings.class);

        final LatLng latlng = new LatLng(prefs.getFloat("latitude2",0), prefs.getFloat("longitude2",0));
        MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("Your home location");
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 3));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getApplicationContext(), "Chose the location of area that u want",
                        Toast.LENGTH_LONG).show();



                googleMap.clear();
                CircleOptions circleOptions=new CircleOptions()
                        .strokeColor(0x110000FF)
                        .fillColor(0x110000FF)
                        .center(new LatLng(point.latitude,point.longitude))
                        .radius(prefs.getFloat("spinn",0))
                        .strokeWidth(4);
                googleMap.addCircle(circleOptions);

                googleMap.addMarker(new MarkerOptions().position(point));
                editor.putString("latitude1", Double.toString( point.latitude));
                editor.putString("longitude1", Double.toString(point.longitude));
                editor.putFloat("longitude2",(float)point.longitude);
                editor.putFloat("latitude2",(float)point.latitude);
                editor.apply();
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Alert");
                builder.setMessage("Are u sure?");
                builder.setCancelable(false);
                builder.setNeutralButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(intent);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });
    }

}

