package com.example.parkingsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class map_test2 extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private float latitude;
    private float longitude;
    private custom_marker marker;
    private Marker selectedMarker;
    private MarkerOptions markerOptions;
    private View marker_view;
    private DisplayMetrics displayMetrics;
    private TextView marker_title;

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
                        marker = new custom_marker(0,0,"");
                        /*
                        주차장별 사용자리/전체자리 확인
                         */
//                        parking_check.check_remaining_spots(new parking_check.Callback_remaining() {
//                            @Override
//                            public void onCallback_remaining(int remaining_count, int all_count) {
////                                Log.d("testt", "가져온 자리   " + remaining_count + "  " + all_count);
//                                marker.setSpot_name((String) arraylist.get(final_i) + "\n" + remaining_count + " / " + all_count);
//                                parking_check.for_speech_spots.put((String) arraylist.get(final_i), remaining_count + " / " + all_count);
//                            }
//                        }, (String) arraylist.get(i));
                        /*
                        주차장별 위도, 경도 확인
                         */
//                        check_position_spots(new Callback_position() {
//                            @Override
//                            public void onCallback_position(String position) {
////                                Log.d("testt", "가져온 position: "+ position);
//                                if ( !position.isEmpty() ) {
//                                    latitude = Float.parseFloat(position.split(",")[0]);
//                                    longitude = Float.parseFloat(position.split(",")[1]);
//                                    marker.setLatitude(latitude);
//                                    marker.setLongitude(longitude);
//                                    add_parking_marker((String) arraylist.get(final_i), latitude, longitude);
//                                } else {
////                                    Log.d("testt", arraylist.get(final_i) + "는 position 값이 없어요.");
//                                }
//                            }
//                        }, (String) arraylist.get(i));
                        /*
                        주차장별 주차 자리, 위도/경도 동시에 확인
                         */
                        check_position_spots(new Callback_position() {
                            @Override
                            public void onCallback_position(int remaining_count, int all_count, String position) {
//                                Log.d("testt", "check_position_spots 값: "+remaining_count+ " "+all_count+" "+position);
                                marker.setSpot_name((String) arraylist.get(final_i) + "\n" + remaining_count + " / " + all_count);
                                latitude = Float.parseFloat(position.split(",")[0]);
                                longitude = Float.parseFloat(position.split(",")[1]);
                                marker.setLatitude(latitude);
                                marker.setLongitude(longitude);
                                addMarker(marker, false);
                                parking_check.for_speech_spots.put((String) arraylist.get(final_i), remaining_count + " / " + all_count);
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

//    private void add_parking_marker(String spot, float latitude, float longitude) {
//        LatLng location = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(location).title(spot));
//    }

    private Marker addMarker(custom_marker marker, boolean isSelected) {
        String title = marker.getSpot_name();
        LatLng location = new LatLng(marker.getLatitude(), marker.getLongitude());

        marker_title.setText(title);

        if (isSelected) {
            marker_title.setBackgroundResource(R.drawable.picked_marker);
            marker_title.setTextColor(Color.RED);
        } else {
            marker_title.setBackgroundResource(R.drawable.unpicked_marker);
            marker_title.setTextColor(Color.BLACK);
        }

        markerOptions = new MarkerOptions();
        markerOptions.title(title)
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(create_Drawable_From_View(this, marker_view)));

        return mMap.addMarker(markerOptions);
    }

    private Marker addMarker(Marker marker, boolean isSelected) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String title = marker.getTitle();
        custom_marker item = new custom_marker(lat, lon, title);
        return addMarker(item, isSelected);
    }

    private Bitmap create_Drawable_From_View(Context context, View view) {
        displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0,0,displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void setCustomMarker() {
        marker_view = LayoutInflater.from(this).inflate(R.layout.custom_marker, null);
        marker_title = (TextView) marker_view.findViewById(R.id.marker_title);
    }

//    private void check_position_spots(final Callback_position callback, String spot) {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("check").child(spot).orderByKey().equalTo("position").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String position = "";
//                try {
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                        Log.d("testt", "position: "+snapshot);
//                        position = (String) snapshot.getValue();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d("testt", "check_position_spots fail");
//                }
////                callback.onCallback_position(position);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private void check_position_spots(final Callback_position callback, String spot) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("check").child(spot).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int remaining_count = 0;
                int all_count = 0;
                String position = "";
                try {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equals("position")){
                            Data_for_check data = snapshot.getValue(Data_for_check.class);
                            if (data.isUse()) {
                                remaining_count ++;
                            }
                            all_count ++;
                        } else {
//                            Log.d("testt", "position: "+snapshot);
                            position = (String) snapshot.getValue();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("testt", "check_position_spots fail");
                }
                callback.onCallback_position(remaining_count, all_count, position);
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
    /*
    마커 클릭 이벤트
    해당 위치로 카메라 이동
    마커 배경 변경
    마커 주차장 액티비티 실행
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate move = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(move);
        changeSelectedMarker(marker);

//        Log.d("testt", "latitude: "+marker.getPosition().latitude);
//        Log.d("testt", "longitude: "+marker.getPosition().longitude);
        String temp = marker.getTitle();
        String spot_name = temp.split("\n")[0];
        String spare = temp.split("\n")[1];

        Intent intent = new Intent(getApplicationContext(), spot_check.class);
        intent.putExtra("spot", spot_name);
        intent.putExtra("space", spare);

        startActivity(intent);

//        Log.d("testt", "spot: "+spot_name);
//        Log.d("testt", "spare: " + spare);
        return true;
    }
    /*
    마커 클릭시 배경 변경 어시스트
     */
    private void changeSelectedMarker(Marker marker) {
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();
        }

        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            marker.remove();
        }
    }


    public interface Callback_position {
        void onCallback_position(int remaining_count, int all_count, String position);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setOnMarkerClickListener(this);
        setCustomMarker();

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
