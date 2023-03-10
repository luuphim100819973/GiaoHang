package com.example.giaohang.Driver;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.example.giaohang.onAppKilled;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.giaohang.Chat.ChatActivity;
import com.example.giaohang.FCMsend;
import com.example.giaohang.History.HistoryActivity2;
import com.example.giaohang.IncomeActivity;
import com.example.giaohang.R;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;

import com.example.giaohang.Login.LauncherActivity;
import com.example.giaohang.Login.LoginFragment;
import com.example.giaohang.Objects.DriverObject;
import com.example.giaohang.Objects.RideObject;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/**
 * M??n h??nh ch??nh khi driver ????ng nh???p
 */
public class DriverMapActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, DirectionCallback {
    int MAX_SEARCH_DISTANCE = 20;
    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SlideToActView mRideStatus;
    private Switch mWorkingSwitch;
    String database_name= "https://delivery-b9821-default-rtdb.asia-southeast1.firebasedatabase.app";
    private LinearLayout mCustomerInfo, mBringUpBottomLayout;
    private TextView mCustomerName;
    private TextView mLocationStop;
    private TextView mLocationStart;
    private TextView mMoney;
    private ImageView Image_Show_Driver_Map;
    private FloatingActionButton mMaps_start,mMaps_stop;
    DatabaseReference mUser;
    RideObject mCurrentRide;
    Marker pickupMarker, destinationMarker;
    DriverObject mDriver = new DriverObject();
    TextView mUsername, mLogout, mHistory_navi_fake_btn,mIncome_navi_fake_btn;
    private ValueEventListener driveHasEndedRefListener;

    List<RideObject> requestList = new ArrayList<>();
    View mBottomSheet;
    /*View mBottomSheet_handPick;*/
    LinearLayout mCustomerInfo_handPicK;
    //BottomSheetBehavior<View> mBottomSheetBehavior;
    GeoQuery geoQuery;
    boolean started = false;
    boolean zoomUpdated = false;
    private String customerId="";
    private int status =0;
    private FloatingActionButton mCall;
    private FloatingActionButton mMessage;
    private Button mHistoryBtn,mReSetMap;
    private  ImageView mCancel;
    //don tu chon
    private TextView mLocationStop_handPick,mLocationStart_handPick,mMoney_handpick,mCustomerName_handpick;
    private FloatingActionButton mMaps_start_handPick,mMaps_stop_handPick;
    private Button mRideStatus_handpick;
    private LinearLayout mCustomerInfo_handpick;
    private String location_stop_request_again;
    private double lat1_again;
    private double long1_again;
    //????n r???t
    private TextView mLocationStop_handPickAfter,mLocationStart_handPickAfter,mMoney_handpickAfter,mCustomerName_handpickAfter;
    private FloatingActionButton mMaps_start_handPickAfter,mMaps_stop_handPickAfter;
    private Button mRideStatus_handpickAfter;
    private LinearLayout mCustomerInfo_handpickAfter;
    private FloatingActionButton mCallAfter,mMessageAfter;
    private String location_stop_request_again_after;
    private double lat1_again_after;
    private double long1_again_after;
    private Marker mDriverMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);

        Toolbar toolbar = findViewById(R.id.main_page_toolbar_55);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        startService(new Intent(DriverMapActivity.this,onAppKilled.class));
        mCustomerInfo_handPicK = findViewById(R.id.customerInfo_handPicK);
        polylines = new ArrayList<>();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mUser = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(FirebaseAuth.getInstance().getUid());
        mCustomerInfo = findViewById(R.id.customerInfo);
        mCustomerInfo_handpick = findViewById(R.id.customerInfo_handPicK);
        //after handpick
        mCustomerInfo_handpickAfter = findViewById(R.id.customerInfo_handPicK_after);
        mCustomerName_handpickAfter = findViewById(R.id.name_after);
        mLocationStop_handPickAfter = findViewById(R.id.location_stop_after);
        mLocationStart_handPickAfter = findViewById(R.id.location_start_after);
        mMaps_start_handPickAfter = findViewById(R.id.openMaps_start_after);
        mMaps_stop_handPickAfter = findViewById(R.id.openMaps_stop_after);
        mMoney_handpickAfter = findViewById(R.id.money_after);
        mRideStatus_handpickAfter = findViewById(R.id.rideStatus_after);
        mCallAfter = findViewById(R.id.phone_from_cust_after);
        mMessageAfter = findViewById(R.id.message_to_cust_after);
        //afterhandpick

        mHistoryBtn=findViewById(R.id.history_driver_btn);
        mReSetMap=findViewById(R.id.reset_map_btn);
       // mBringUpBottomLayout = findViewById(R.id.bringUpBottomLayout);
        mCustomerName = findViewById(R.id.name);
        mCustomerName_handpick = findViewById(R.id.name_handpick);
        mLocationStop = findViewById(R.id.location_stop);
        mLocationStart = findViewById(R.id.location_start);
        mMoney = findViewById(R.id.money);
        mLocationStop_handPick = findViewById(R.id.location_stop_handpick);
        mLocationStart_handPick = findViewById(R.id.location_start_handpick);
        mMoney_handpick = findViewById(R.id.money_handpick);
        mUsername = navigationView.getHeaderView(0).findViewById(R.id.usernameDrawer);
        mMaps_start = findViewById(R.id.openMaps_start);
        mMaps_stop = findViewById(R.id.openMaps_stop);
        mMaps_start_handPick = findViewById(R.id.openMaps_start_handpick);
        mMaps_stop_handPick = findViewById(R.id.openMaps_stop_handpick);
        mCall = findViewById(R.id.phone_from_cust);
        mMessage = findViewById(R.id.message_to_cus);
      //  FloatingActionButton mCall = findViewById(R.id.phone);
        mCancel = findViewById(R.id.cancel);
        mRideStatus = findViewById(R.id.rideStatus);
        mRideStatus_handpick = findViewById(R.id.rideStatus_handpick);
        mLogout = findViewById(R.id.logout);
        mHistory_navi_fake_btn = findViewById(R.id.history_navigation_fake_driver);
        mIncome_navi_fake_btn = findViewById(R.id.income_navigation_fake);
        mWorkingSwitch = findViewById(R.id.workingSwitch);
       mLogout.setOnClickListener(v -> {
           logOut();
       });
        //Xem l???ch s??? t??i  x???
        mHistory_navi_fake_btn.setOnClickListener(v -> {
            Intent intent= new Intent(DriverMapActivity.this, HistoryActivity2.class);
            intent.putExtra("customerOrDriver","Drivers");
            startActivity(intent);
        });
        //Xem thu nh???p t??i x???
        mIncome_navi_fake_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DriverMapActivity.this, IncomeActivity.class);
                startActivity(intent);
            }
        });

        mWorkingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            /*if (!mDriver.getActive()) {
                Toast.makeText(DriverMapActivity.this, R.string.not_approved, Toast.LENGTH_LONG).show();
                mWorkingSwitch.setChecked(false);
                return;
            }*/
            if (isChecked) {
                connectDriver();
            } else {
                disconnectDriver();
            }
        });
        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(DriverMapActivity.this, HistoryActivity2.class);
                intent.putExtra("customerOrDriver","Drivers");
                startActivity(intent);
            }
        });
        mReSetMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*checkLocationPermission();
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }*/
            }
        });

        mRideStatus.setOnSlideCompleteListener(v -> {
            switch (status) {
                case 1:

                    status=2;
                    mRideStatus.setText("????n ???? ho??n th??nh");
                    mRideStatus.resetSlider();
                    break;
                //Driver tren duong den Destinaton
                case 2:
                    /*if (mCurrentRide != null)
                        mCurrentRide.recordRide();*/
                    recordRide2();
                    recordIncome();
                    endRide2();
                    break;
            }
        });
        ImageView mDrawerButton = findViewById(R.id.drawerButton);
        mDrawerButton.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
        getUserData();

        getAssignedCustomer2();
    }
    private void openGoogleDirectionMaps(String destination1) {
        try {
            destination1 = destination1.replace(" ","+");
            System.out.println(destination1);
            String url2="https://www.google.com/maps/dir/?api=1&destination="+destination1+"&travelmode=BICYCLING";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
        }
    }
    private void recordRide2() {
        String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference driverRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(driverId).child("history");
       // String customer_uid = snapshot.getValue().toString();
        DatabaseReference customerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Customers").child(customerId).child("history");
        DatabaseReference historyRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("history");
        String requestId =historyRef.push().getKey();
        driverRef.child(requestId).setValue(true);
        customerRef.child(requestId).setValue(true);
        HashMap map= new HashMap();
        map.put("driver",driverId);
        map.put("customer",customerId);
        map.put("rating",0);
        map.put("timestamp",getTimestamp());
        historyRef.child(requestId).updateChildren(map);
    }
    private Long getTimestamp() {
        Long timestamp =System.currentTimeMillis()/1000;
        return timestamp;
    }
    private String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String datetime =formatter.format(date);
        return datetime;
    }
    //Thong tin phi ship sau ket thuc chuyen di gui vao
    private void recordIncome() {
        DatabaseReference customerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Customers").child(customerId);
        customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                if (snapshot2.exists() &&snapshot2.getChildrenCount()>0){
                    String shipMoney= snapshot2.child("shipFee").getValue().toString();
                    String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference driverIncomeRef= FirebaseDatabase.getInstance(database_name).getReference()
                            .child("Users").child("Drivers").child(driverId).child("income");
                    String Id =driverIncomeRef.push().getKey();
                    driverIncomeRef.child(String.valueOf(getDate())).child(Id).setValue(shipMoney);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getAssignedCustomer2() {
        String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        /*DatabaseReference assignedCustomerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(driverId);*/
        DatabaseReference assignedCustomerRef= FirebaseDatabase.getInstance(database_name).getReference()
                .child("Users").child("Drivers").child(driverId).child("customerRequest").child("customerRideId");
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // tai xe dang tren duong pickup customer
                    status=1;
                    customerId= snapshot.getValue().toString();
                    getAssignedCustomerPickupLocation2();
                    getAssignedCustomerInfo2();
                    mMessage.setOnClickListener(view -> {
                        Intent intent=new Intent(DriverMapActivity.this, ChatActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id_chat",customerId);
                        extras.putString("customerOrDriver","Drivers");
                        extras.putString("user_name","name_cust");
                        intent.putExtras(extras);
                        startActivity(intent);
                    });
                    /**t??? ch???i nh??n ????n chuy???n n?? v??? d???ng ????n r???t,th??ng b??o t???i custom t??i x??? ???? t??? ch???i, xin h??y ch???
                     * , x??a nh??nh customerRequest
                            t???o nh??nh customerRequest again*/
                    mCancel.setOnClickListener(v -> {
                        /**g???i th??ng b??o t??i x??? ???? t??? ch???i ????n*/
                        DatabaseReference tokenRef= FirebaseDatabase.getInstance(database_name).getReference()
                                .child("Users").child("Customers").child(customerId).child("token_id");
                        tokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String token_id=snapshot.getValue().toString();
                                    FCMsend.FirebaseMessagingService(DriverMapActivity.this,
                                            token_id
                                            , "Th??ng b??o ", "T??i x??? ???? t??? ch???i ????n, ?????ng lo t??i ??ang t??m t??i x??? kh??c cho b???n");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        DatabaseReference customerPickupAgain2Ref= FirebaseDatabase.getInstance(database_name).getReference()
                                .child("customerRequest").child(customerId).child("l");
                        customerPickupAgain2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()&& snapshot.getChildrenCount()>0){
                                    List<Object> map=(List<Object>) snapshot.getValue();
                                    double locationLat =0,locationLng=0;
                                    if(map.get(0)!=null){
                                        locationLat=Double.parseDouble(map.get(0).toString());
                                    }
                                    if(map.get(1)!=null){
                                        locationLng=Double.parseDouble(map.get(1).toString());
                                    }
                                    String hp=customerId;
                                    DatabaseReference ref= FirebaseDatabase.getInstance(database_name).getReference().child("customerRequestAgain");

                                    GeoFire geoFireWorking = new GeoFire(ref);
                                    geoFireWorking.setLocation(hp, new GeoLocation(locationLat, locationLng), (key, error) -> {
                                    });
                                    ref.child(customerId).child("state").setValue(0);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }

                        });
                        // end ride 1 n??a
                        mRideStatus.setText("Tr???ng th??i ????n ???? nh???n");
                        mRideStatus.resetSlider();
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference driverRef = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(userId).child("customerRequest");
                        driverRef.removeValue();
                        DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("customerRequest");
                        GeoFire geoFire = new GeoFire(ref);
                        geoFire.removeLocation(customerId, (key, error) -> {
                        });

                        if(mCustomMaker2!=null){
                            mCustomMaker2.remove();
                        }
                        /*mCustomerName.setText("");
                        mLocationStop.setText("");
                        mLocationStart.setText("");
                        mMoney.setText("");*/
                        mMap.clear();
                        zoomUpdated = false;
                        //hien customer info
                        mCustomerInfo.setVisibility(View.GONE);
                        mCustomerInfo.clearAnimation();

                    });
                    //
                } else {
                    endRide2();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void getAssignedCustomerInfo2() {
       // mCustomerInfo.setVisibility(View.VISIBLE);
        DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Customers").child(customerId);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() & dataSnapshot.getChildrenCount()>0) {
                //if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                    Map<String,Object> map =(Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("shipFee")!= null){
                        mMoney.setText(map.get("shipFee").toString());
                    }
                    if(map.get("locationStart")!= null){
                        mLocationStart.setText(map.get("locationStart").toString());
                    }
                    String mstart= mLocationStart.getText().toString();
                    mMaps_start.setOnClickListener(view -> {
                        openGoogleDirectionMaps(mstart);
                    });
                    if(map.get("locationStop")!= null){
                        mLocationStop.setText(map.get("locationStop").toString());
                    }
                    mCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String phone_start=map.get("phone_start").toString();
                            String phone_stop=map.get("phone_stop").toString();
                            CharSequence options[] = new CharSequence[]{"G???i ng?????i g???i", "G???i ng?????i nh???n"};
                            final AlertDialog.Builder builder = new AlertDialog.Builder(DriverMapActivity.this);
                            builder.setTitle("Ch???n");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(i == 0){
                                        if ((map.get("phone_start") == null)) {
                                            Snackbar.make(findViewById(R.id.drawer_layout), "kh??ch ch??a ?????t S??T cho ng?????i g???i k??a", Snackbar.LENGTH_LONG).show();
                                        }
                                        if(map.get("phone_start") != null){
                                            if (ContextCompat.checkSelfPermission(DriverMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_start));
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                    if(i == 1){
                                        if(map.get("phone_stop") == null){
                                            Snackbar.make(findViewById(R.id.drawer_layout), "Kh??ch ch??a ?????t S??T ng?????i nh???n", Snackbar.LENGTH_LONG).show();
                                        }
                                        if(map.get("phone_stop") != null){
                                            if (ContextCompat.checkSelfPermission(DriverMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_stop));
                                                startActivity(intent);}
                                        }
                                    }
                                }
                            });
                            builder.show();
                        }
                    });

                    String mstop= mLocationStop.getText().toString();
                    mMaps_stop.setOnClickListener(view -> {
                        openGoogleDirectionMaps(mstop);
                    });
                    if(map.get("name")!= null){
                        mCustomerName.setText(map.get("name").toString());
                    }
                    //String token_id = dataSnapshot.child("token_id").getValue().toString();
                    /*FCMsend.FirebaseMessagingService(DriverMapActivity.this,
                            token_id
                            , "New Order", "C?? t??i x??? ???? nh???n ????n");*/
                }
                /*if (mCurrentRide != null) {
                    mCurrentRide.getCustomer().parseData(dataSnapshot);
                    mCustomerName.setText(mCurrentRide.getCustomer().getName());
                }*/
                mCustomerInfo.setVisibility(View.VISIBLE);
                /*mBottomSheetBehavior.setHideable(false);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }
    ValueEventListener customerPickupAgainListener;
    private void makeRequestTurn2() {
        DatabaseReference customerPickupAgain2Ref= FirebaseDatabase.getInstance(database_name).getReference()
                .child("customerRequest").child(customerId).child("l");
        customerPickupAgainListener=customerPickupAgain2Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Object> map=(List<Object>) snapshot.getValue();
                    double locationLat =0;
                    double locationLng =0;
                    if(map.get(0)!=null){
                        locationLat=Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1)!=null){
                        locationLng=Double.parseDouble(map.get(1).toString());
                    }
                    DatabaseReference ref= FirebaseDatabase.getInstance(database_name).getReference().child("customerRequestAgain");
                  //  GeoFire geoFire=new GeoFire(ref);
                   // geoFire.setLocation(customerId, new GeoLocation(locationLat,locationLng));
                    GeoFire geoFireWorking = new GeoFire(ref);
                    geoFireWorking.setLocation(customerId, new GeoLocation(locationLat, locationLng), (key, error) -> {
                    });
                    /*LatLng driverLatLng= new LatLng(locationLat,locationLng);
                    if(mCustomMaker2 != null){
                        mCustomMaker2.remove();
                    }
                    // mCustomMaker2=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("pick up customer here"));
                    mCustomMaker2 = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin_loca_32))
                            .position(driverLatLng).title("New Request from"+customerId));
                    // mMap.addMarker(new MarkerOptions().position(driverLatLng).title("pickup locationn"));*/
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private Marker mCustomMaker2;
    DatabaseReference assignedCustomedPickupLocation;
    ValueEventListener assignedCustomedPickupLocationListener;
    // gan dinh vi len map
    private void getAssignedCustomerPickupLocation2() {
        assignedCustomedPickupLocation= FirebaseDatabase.getInstance(database_name).getReference()
                .child("customerRequest").child(customerId).child("l");
        assignedCustomedPickupLocationListener=assignedCustomedPickupLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Object> map=(List<Object>) snapshot.getValue();
                    double locationLat =0;
                    double locationLng =0;
                    if(map.get(0)!=null){
                        locationLat=Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1)!=null){
                        locationLng=Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLng= new LatLng(locationLat,locationLng);
                    if(mCustomMaker2 != null){
                        mCustomMaker2.remove();
                    }
                   // mCustomMaker2=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("pick up customer here"));
                    mCustomMaker2 = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.man))
                            .position(driverLatLng).title("New Request from"+customerId));
                   // mMap.addMarker(new MarkerOptions().position(driverLatLng).title("pickup locationn"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //getRequestAgianAround
    //phai la false neu muon no chi update 1 lan
    boolean getAllRequestCustomersAround = true;
    List<Marker> markerListRequestAgain = new ArrayList<Marker>();
    private Marker mDriverMaker;
    private void getAllRequestCustomersAround(){
        getAllRequestCustomersAround = true;
        DatabaseReference driversLocation = FirebaseDatabase.getInstance(database_name).getReference().child("customerRequestAgain");
        GeoFire geoFire = new GeoFire(driversLocation);
        //L???y ?????nh v??? c???a driver l??m g???c, t??m xung quanh b??n k??nh c???a driver 10000km
        GeoQuery geoQuery = geoFire
                .queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 10000);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            //lay tat ca data khi l???n ?????u ti??n ta g???i listener
            //g???i khi data trong firebase thay ?????i m??u xanh
            public void onKeyEntered(String key, GeoLocation location) {
                //v??ng for ki??m tra ch???c ch???n ta ko add c??c marker tr??ng nhau
                for (Marker markerIt : markerListRequestAgain) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key))
                        return;
                }
                //  checkDriverLastUpdated(key);
                //cho all key c???a c??c driver v??o markerlist
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
                mDriverMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin_loca_32))
                        .position(driverLocation).title(key));
                mDriverMarker.setTag(key);
                markerListRequestAgain.add(mDriverMarker);

            }
            //khi driver stop woking
            @Override
            public void onKeyExited(String key) {
                for (Marker markerIt : markerListRequestAgain) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    //x??a marker kh???i markerlist khi drivers ngo??i b??n k??nh qu??t
                    if (markerIt.getTag().equals(key)) {
                        markerIt.remove();
                        markerListRequestAgain.remove(markerIt);
                        return;
                    }
                }
            }
            //neu t??i xe ??ang di chuy???n, l???y key id c???a driver v?? thay ?????i v??? tr?? c??c marker li??n t???c
            //tr??n b???n ?????
            //g???i khi data trong firebase thay ?????i m??u v??ng
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker markerIt : markerListRequestAgain) {
                    if (markerIt.getTag() == null || key == null) {
                        continue;
                    }
                    if (markerIt.getTag().equals(key)) {
                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                        return;
                    }
                }
                // checkDriverLastUpdated(key);
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
                mDriverMarker = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin_loca_32)).position(driverLocation).title(key));
                mDriverMarker.setTag(key);
                markerListRequestAgain.add(mDriverMarker);
            }
            @Override
            public void onGeoQueryReady() {
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }

    /**
     * l???y user info
     * Check xem user c?? work hay ko tr?????c khi ????ng app
     * Set radio button "working"
     */
    private void getUserData() {
        String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(driverId);
        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mDriver.parseData(dataSnapshot);
                    mUsername.setText(mDriver.getName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance(database_name).getReference("driversWorking").child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    connectDriver();
                } else {
                    disconnectDriver();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * L???y c??c rider g???n nh???t v???i radius of MAX_SEARCH_DISTANCE v?? v??? tr?? driver hi???n t???i.
     * n???u 1 request ??c t??m th???y v?? rider ??ang ko th???c hi???n 1 request n??o kh??c thi g???i getRequestInfo(key)
     * key - id of the request.
     */
    private void getRequestsAround() {
        /*if (mLastLocation == null) {
            return;
        }*/
        DatabaseReference requestLocation = FirebaseDatabase.getInstance(database_name).getReference().child("customerRequest");
        GeoFire geoFire = new GeoFire(requestLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), MAX_SEARCH_DISTANCE);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mWorkingSwitch.isChecked()){
                    return;
                }
                if (mCurrentRide == null) {
                    for (RideObject mRideIt : requestList) {
                        if (mRideIt.getId().equals(key)) {
                            return;
                        }
                    }
                    //getRequestInfo(key);
                }else{
                    requestList.clear();
                }
            }
            @Override
            public void onKeyExited(String key) {
            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }
            @Override
            public void onGeoQueryReady() {
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }
    //Lo???i b??? c??c active listeners,
    // return to default state
    // X??a markers
    private void endRide() {}
    private void  endRide2() {
        mRideStatus.setText("Tr???ng th??i ????n ???? nh???n");
        mRideStatus.resetSlider();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cust_Ref = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Customers").child(customerId);
        cust_Ref.child("state_request").setValue(1);
        DatabaseReference driverRef = FirebaseDatabase.getInstance(database_name).getReference().child("Users").child("Drivers").child(userId).child("customerRequest");
        driverRef.removeValue();
        //Remove the request from the geofire child so that other drivers don't have to check this request in the future
        DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("customerRequest");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(customerId, (key, error) -> {
        });
        //customerId="";
        if(mCustomMaker2!=null){
            mCustomMaker2.remove();
        }
        if(assignedCustomedPickupLocationListener!=null){
            assignedCustomedPickupLocation.removeEventListener(assignedCustomedPickupLocationListener);
        }
        mCustomerName.setText("");
        mLocationStop.setText("");
        mLocationStart.setText("");
        mMoney.setText("");
        mMap.clear();
        zoomUpdated = false;
        mCustomerInfo.setVisibility(View.GONE);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);//interval with which the driver location will be updated
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                    .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                checkLocationPermission();
            }
        }
        /// lay thong tin cua d??n trn map
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String id_customer=marker.getTitle();
                DatabaseReference clickedCustomerRef = FirebaseDatabase.getInstance(database_name).getReference()
                        .child("Users").child("Customers").child(id_customer);
                clickedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Map<String,Object> map =(Map<String, Object>) snapshot.getValue();
                            if(map.get("shipFee")!= null){
                                mMoney_handpick.setText(map.get("shipFee").toString());
                            }
                            if(map.get("name")!= null){
                                mCustomerName_handpick.setText(map.get("name").toString());
                            }
                            if(map.get("locationStart")!= null){
                                mLocationStart_handPick.setText(map.get("locationStart").toString());
                            }
                            if(map.get("locationStop")!= null){
                                mLocationStop_handPick.setText(map.get("locationStop").toString());
                            }
                        }
                        mCustomerInfo_handPicK.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                mRideStatus_handpick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id_customer_handPick= id_customer;
                        DatabaseReference checkPickedOrNotRef= FirebaseDatabase.getInstance(database_name).getReference()
                                .child("customerRequestAgain").child(id_customer_handPick);
                        checkPickedOrNotRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.child("state").exists()&&snapshot.child("state").getValue().toString().equals("1")){
                                    /*Toast.makeText(DriverMapActivity.this,"????n ???? ???????c nh??n",Toast.LENGTH_LONG).show();*/
                                    Snackbar.make(findViewById(R.id.drawer_layout), "????n ???? ???????c nh???n b???i t??i x??? kh??c", Snackbar.LENGTH_LONG).show();
                                    mCustomerInfo_handPicK.setVisibility(View.GONE);
                                }
                                if(snapshot.child("state").exists()&&snapshot.child("state").getValue().toString().equals("0")){
                                    String this_driver_uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    checkPickedOrNotRef.child("state").setValue(1);
                                    checkPickedOrNotRef.child("from").setValue(this_driver_uid);
                                    mCustomerInfo_handPicK.setVisibility(View.GONE);
                                    mCustomerInfo_handPicK.clearAnimation();
                                    /**t???o l???i nh??nh customerRequest nh?? c??*/
                                    /*DatabaseReference customerPickupAgain3Ref= FirebaseDatabase.getInstance(database_name).getReference()
                                            .child("customerRequestAgain").child(id_customer_handPick).child("l");
                                    customerPickupAgain3Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()&& snapshot.getChildrenCount()>0){
                                                List<Object> map=(List<Object>) snapshot.getValue();
                                                double locationLat =0,locationLng=0;
                                                if(map.get(0)!=null){locationLat=Double.parseDouble(map.get(0).toString());}
                                                if(map.get(1)!=null){locationLng=Double.parseDouble(map.get(1).toString());}
                                                String hp=id_customer_handPick;
                                                DatabaseReference ref= FirebaseDatabase.getInstance(database_name).getReference().child("customerRequest");
                                                GeoFire geoFireWorking = new GeoFire(ref);
                                                geoFireWorking.setLocation(hp, new GeoLocation(locationLat, locationLng), (key, error) -> {});
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });*/
                                    /**T???o l???i nh??nh customerRequest trong Users/Driver/CustomerRequest*/

                                    String this_driver_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference diverRef= FirebaseDatabase.getInstance(database_name).getReference()
                                            .child("Users").child("Drivers").child(this_driver_id).child("customerRequest");
                                    HashMap map =new HashMap();
                                    map.put("customerRideId2",this_driver_id);
                                    String current_id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    DatabaseReference location_ref= FirebaseDatabase.getInstance(database_name).getReference()
                                            .child("Users").child("Customers").child(id_customer_handPick);
                                    location_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                location_stop_request_again = dataSnapshot.child("locationStop").getValue().toString();
                                                try {
                                                    Geocoder geocoder = new Geocoder(DriverMapActivity.this, Locale.getDefault());
                                                    Address address ;
                                                    List<Address> addressList = geocoder.getFromLocationName(location_stop_request_again, 1);
                                                    while (addressList.size()==0) {
                                                        addressList = geocoder.getFromLocationName(location_stop_request_again, 1);
                                                    }
                                                    if (addressList != null && addressList.size() > 0) {
                                                        address = addressList.get(0);
                                                        lat1_again = address.getLatitude();
                                                        long1_again = address.getLongitude();
                                                        map.put("destination",location_stop_request_again);
                                                        map.put("destinationLat",lat1_again);
                                                        map.put("destinationLng",long1_again);
                                                        diverRef.updateChildren(map);
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                    map.put("destination",location_stop_request_again);
                                    map.put("destinationLat",lat1_again);
                                    map.put("destinationLng",long1_again);
                                    diverRef.updateChildren(map);
                                    /**lay lai thong tin cua customer v?? ????a v??o customerinfo_after*/
                                    DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance(database_name).getReference()
                                            .child("Users").child("Customers").child(id_customer_handPick);
                                    mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists() & dataSnapshot.getChildrenCount()>0) {
                                                /**g???i th??ng b??o cho customer*/
                                                String token_id_cust=dataSnapshot.child("token_id").getValue().toString();
                                                FCMsend.FirebaseMessagingService(DriverMapActivity.this,
                                                        token_id_cust, "Th??ng b??o ", "C?? T??i x??? kh??c ???? nh???n ????n c???a b???n!!!");

                                                //if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0) {
                                                Map<String,Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                                if(map.get("shipFee")!= null){
                                                    mMoney_handpickAfter.setText(map.get("shipFee").toString());
                                                }
                                                if(map.get("locationStart")!= null){
                                                    mLocationStart_handPickAfter.setText(map.get("locationStart").toString());
                                                }
                                                String mstart= mLocationStart_handPickAfter.getText().toString();
                                                mMaps_start_handPickAfter.setOnClickListener(view -> {
                                                    openGoogleDirectionMaps(mstart);
                                                });
                                                if(map.get("locationStop")!= null){
                                                    mLocationStop_handPickAfter.setText(map.get("locationStop").toString());
                                                }
                                                mMessageAfter.setOnClickListener(view -> {
                                                    Intent intent=new Intent(DriverMapActivity.this, ChatActivity.class);
                                                    Bundle extras = new Bundle();
                                                    extras.putString("user_id_chat",id_customer_handPick);
                                                    extras.putString("customerOrDriver","Drivers");
                                                    extras.putString("user_name","name_cust");
                                                    intent.putExtras(extras);
                                                    startActivity(intent);
                                                });
                                                mCallAfter.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        String phone_start=map.get("phone_start").toString();
                                                        String phone_stop=map.get("phone_stop").toString();
                                                        CharSequence options[] = new CharSequence[]{"G???i ng?????i g???i", "G???i ng?????i nh???n"};
                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(DriverMapActivity.this);
                                                        builder.setTitle("Ch???n");
                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if(i == 0){
                                                                    if ((map.get("phone_start") == null)) {
                                                                        Snackbar.make(findViewById(R.id.drawer_layout), "kh??ch ch??a ?????t S??T cho ng?????i g???i k??a", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                    if(map.get("phone_start") != null){
                                                                        if (ContextCompat.checkSelfPermission(DriverMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_start));
                                                                            startActivity(intent);
                                                                        }
                                                                    }
                                                                }
                                                                if(i == 1){
                                                                    if(map.get("phone_stop") == null){
                                                                        Snackbar.make(findViewById(R.id.drawer_layout), "Kh??ch ch??a ?????t S??T ng?????i nh???n", Snackbar.LENGTH_LONG).show();
                                                                    }
                                                                    if(map.get("phone_stop") != null){
                                                                        if (ContextCompat.checkSelfPermission(DriverMapActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_stop));
                                                                            startActivity(intent);}
                                                                    }
                                                                }
                                                            }
                                                        });
                                                        builder.show();
                                                    }
                                                });
                                                // ????a state v??? 2 k???t th??c chuy???n ??i
                                                mRideStatus_handpickAfter.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        DatabaseReference endRef=FirebaseDatabase.getInstance(database_name).getReference().child("customerRequestAgain")
                                                                .child(id_customer_handPick).child("state");
                                                        endRef.setValue(2);
                                                        /// record Ride
                                                        String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                        DatabaseReference driverRef= FirebaseDatabase.getInstance(database_name).getReference()
                                                                .child("Users").child("Drivers").child(driverId).child("history");
                                                        // String customer_uid = snapshot.getValue().toString();
                                                        DatabaseReference customerRef= FirebaseDatabase.getInstance(database_name).getReference()
                                                                .child("Users").child("Customers").child(id_customer_handPick).child("history");
                                                        DatabaseReference historyRef= FirebaseDatabase.getInstance(database_name).getReference()
                                                                .child("history");
                                                        String requestId =historyRef.push().getKey();
                                                        driverRef.child(requestId).setValue(true);
                                                        customerRef.child(requestId).setValue(true);
                                                        HashMap map= new HashMap();
                                                        map.put("driver",driverId);
                                                        map.put("customer",customerId);
                                                        map.put("rating",0);
                                                        map.put("timestamp",getTimestamp());
                                                        historyRef.child(requestId).updateChildren(map);

                                                        //recordIncome
                                                        DatabaseReference customerRef_income= FirebaseDatabase.getInstance(database_name).getReference()
                                                                .child("Users").child("Customers").child(id_customer_handPick);
                                                        customerRef_income.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                                if (snapshot2.exists() &&snapshot2.getChildrenCount()>0){
                                                                    String shipMoney= snapshot2.child("shipFee").getValue().toString();
                                                                    String driverId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                    DatabaseReference driverIncomeRef= FirebaseDatabase.getInstance(database_name).getReference()
                                                                            .child("Users").child("Drivers").child(driverId).child("income");
                                                                    String Id =driverIncomeRef.push().getKey();
                                                                    driverIncomeRef.child(String.valueOf(getDate())).child(Id).setValue(shipMoney);
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                        DatabaseReference driverRef_again_request= FirebaseDatabase.getInstance(database_name).getReference()
                                                                    .child("Users").child("Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("customerRequest");
                                                        driverRef_again_request.removeValue();
                                                        //driverFoundId_cust=null;


                                                        mCustomerInfo_handpickAfter.setVisibility(view.GONE);

                                                        //endRide2();

                                                    }
                                                });

                                                String mstop= mLocationStop_handPickAfter.getText().toString();
                                                mMaps_stop_handPickAfter.setOnClickListener(view -> {
                                                    openGoogleDirectionMaps(mstop);
                                                });
                                                if(map.get("name")!= null){
                                                    mCustomerName_handpickAfter.setText(map.get("name").toString());
                                                }
                                                String token_id = dataSnapshot.child("token_id").getValue().toString();
                                            }
                                            mCustomerInfo.setVisibility(View.GONE);
                                            mCustomerInfo_handpick.setVisibility(View.GONE);
                                            mCustomerInfo_handpickAfter.setVisibility(View.VISIBLE);

                                            //thay doi 24/2
                                            /*mBottomSheetBehavior.setHideable(false);
                                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
                                        }
                                        @Override
                                        public void onCancelled(@NotNull DatabaseError databaseError) {
                                        }
                                    });
                                    /**X??a id ??? nh??nh CustomeRequestAgain*/
                                    /*DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("customerRequestAgain");
                                    GeoFire geoFire = new GeoFire(ref);
                                    geoFire.removeLocation(id_customer_handPick, (key, error) -> {
                                    });*/

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });

                /*Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                return false;
            }
        });
    }
    //Update v??? tr?? driver hi???n  t???i ????? customer c?? th??? l???y info drivers xung quanh
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference refWorking = FirebaseDatabase.getInstance(database_name).getReference("driversWorking");
                    GeoFire geoFireWorking = new GeoFire(refWorking);
                    if (!mWorkingSwitch.isChecked()) {
                        geoFireWorking.removeLocation(userId, (key, error) -> {
                        });
                        /*mCustomerInfo.setVisibility(View.GONE);
                        mBottomSheetBehavior.setHideable(true);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);*/
                        return;
                    }
                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
                    });
                    if (mCurrentRide != null && mLastLocation != null) {
                        mCurrentRide.setRideDistance(mCurrentRide.getRideDistance() + mLastLocation.distanceTo(location) / 1000);
                    }
                    mLastLocation = location;
                    if (!started) {
                        getRequestsAround();
                        started = true;
                    }
                    if (!getAllRequestCustomersAround) {}
                    // cap nhat lien tuc cac map quanh ban
                        getAllRequestCustomersAround();


                    //
                    Map<String, Object> newUserMap = new HashMap<>();
                    newUserMap.put("last_updated", ServerValue.TIMESTAMP);
                    mUser.updateChildren(newUserMap);
                    //thay doi
                    if (!zoomUpdated) {
                            float zoomLevel = 17.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                            zoomUpdated = true;
                        /*mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                        zoomUpdated = true;*/
                    }
                    /*String userId2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference refAvailable2 = FirebaseDatabase.getInstance(database_name).getReference("driversWorking");
                    DatabaseReference refWorking2 = FirebaseDatabase.getInstance(database_name).getReference("driversAvailable");
                    GeoFire geoFireAvailable2 = new GeoFire(refAvailable2);
                    GeoFire geoFireWorking2 = new GeoFire(refWorking2);
                    switch (customerId){
                        case "":
                            geoFireWorking2.removeLocation(userId2);
                            geoFireAvailable2.setLocation(userId2, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;

                        default:
                            geoFireAvailable2.removeLocation(userId2);
                            geoFireWorking2.setLocation(userId2, new GeoLocation(location.getLatitude(), location.getLongitude()));
                            break;*/
                }
            }
        }
    };
     // permissions
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(DriverMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1))
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(DriverMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void logOut() {
        disconnectDriver();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DriverMapActivity.this, LauncherActivity.class);
        startActivity(intent);
        finish();
        /*disconnectDriver();
      //
        //  FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(DriverMapActivity.this, LauncherActivity.class);
        startActivity(intent);
        finish();*/
    }
     // Connects driver
    private void connectDriver() {
        mWorkingSwitch.setChecked(true);
        checkLocationPermission();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }
     //Disconnects driver
    private void disconnectDriver() {
        mWorkingSwitch.setChecked(false);
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
        /*String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref1 = FirebaseDatabase.getInstance(database_name).getReference("driversAvailable").child(userId);
        DatabaseReference ref = FirebaseDatabase.getInstance(database_name).getReference("driversWorking").child(userId);
        ref.removeValue();
        ref1.removeValue();*/
        DatabaseReference refWorking = FirebaseDatabase.getInstance(database_name).getReference("driversWorking");
        GeoFire geoFireWorking = new GeoFire(refWorking);
        geoFireWorking.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
    private List<Polyline> polylines;
    // Remove route polylines
    private void erasePolylines(){}
    // Show map v???i ??i???m ??i v?? ??i???m ?????n
    private void setCameraWithCoordinationBounds(Route route) {}
    //Add rout to map
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {}
    @Override
    public void onDirectionFailure(Throwable t) {}
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_right_driver, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()== android.R.id.home) {
            Intent intent = new Intent(this, LoginFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.main_new_order){
            Intent order_intent = new Intent(this, LoginFragment.class);
            startActivity(order_intent);
            finish();
        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.history) {
            Intent intent= new Intent(DriverMapActivity.this, HistoryActivity2.class);
            intent.putExtra("customerOrDriver","Customers");
            startActivity(intent);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(DriverMapActivity.this, DriverSettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.payout) {
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    /*@Override
    protected void onStop() {
        disconnectDriver();
        super.onStop();
    }*/

}
