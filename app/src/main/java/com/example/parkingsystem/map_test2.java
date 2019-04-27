package com.example.parkingsystem;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class map_test2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private float mLatitude;
    private float mLongitude;
    private float latitude;
    private float longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test2);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);





        parking_check.check_parking_spots(new parking_check.Callback_spots() {
            @Override
            public void onCallback_spots(final ArrayList arraylist) {
//                Log.d("testt", "가져온 리스트   " + arraylist);
                parking_check.for_speech_spots = new HashMap<>();
                try {
                    for(int i = 0; i < arraylist.size(); i++ ) {
                        final int final_i = i;
                        /*
                        주차장별 사용자리/전체자리 확인
                         */
                        parking_check.check_remaining_spots(new parking_check.Callback_remaining() {
                            @Override
                            public void onCallback_remaining(int remaining_count, int all_count) {
//                                Log.d("testt", "가져온 자리   " + remaining_count + "  " + all_count);
                                parking_check.for_speech_spots.put((String) arraylist.get(final_i), remaining_count + " / " + all_count);
                            }
                        }, (String) arraylist.get(i));
                        /*
                        주차장별 위도, 경도 확인
                         */
                        check_position_spots(new Callback_position() {
                            @Override
                            public void onCallback_position(String position) {
//                                Log.d("testt", "가져온 position: "+ position);
                                if ( !position.isEmpty() ) {
                                    latitude = Float.parseFloat(position.split(",")[0]);
                                    longitude = Float.parseFloat(position.split(",")[1]);
                                    add_parking_marker((String) arraylist.get(final_i), latitude, longitude);
                                } else {
//                                    Log.d("testt", arraylist.get(final_i) + "는 position 값이 없어요.");
                                }

                            }
                        }, (String) arraylist.get(i));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "add_view fail");
                }
            }
        });
    }

//    private void setCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("testt", "지도 권한 없음");
//            return;
//        }
//
//        Task task = mFusedLocationClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener() {
//            @Override
//            public void onSuccess(Object location) {
//                try {
//                    for(Field field : location.getClass().getDeclaredFields()) {
//                        field.setAccessible(true);
//                        Object val = field.get(location);
//                        if ( field.getName().equals("mLatitude") ) {
//                            mLatitude = Float.parseFloat(val.toString());
//                        } else if ( field.getName().equals("mLongitude") ) {
//                            mLongitude = Float.parseFloat(val.toString());
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.d("testt", "위도: " + mLatitude);
//                Log.d("testt", "경도: " + mLongitude);
//                LatLng current_location = new LatLng(mLatitude, mLongitude);
//
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location, 16));
//            }
//        });
//    }

    private void add_parking_marker(String spot, float latitude, float longitude) {
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(spot));

    }

    private void check_position_spots(final Callback_position callback, String spot) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").child(spot).orderByKey().equalTo("position").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String position = "";
                try {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Log.d("testt", "position: "+snapshot);
                        position = (String) snapshot.getValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "check_position_spots fail");
                }
                callback.onCallback_position(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if ( locationList.size() > 0 ) {
                Location location = locationList.get(locationList.size() - 1);
//                Log.d("testt", "location: " + location.getLatitude() + "   " + location.getLongitude());
                LatLng current_location = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location, 15));


            }
        }
    };



    public interface Callback_position {
        void onCallback_position(String position);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(120000); // two minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("testt", "권한 없음, 확인 과정 필요해");

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
//            Log.d("testt", "권한 있음");
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
    }

}
