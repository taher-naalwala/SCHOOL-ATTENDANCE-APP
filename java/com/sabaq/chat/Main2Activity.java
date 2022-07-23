    package com.sabaq.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Main2Activity extends AppCompatActivity {

BackgroundTask1 backgroundTask1;
    private BackgroundTask backgroundTask;
   public BTask bTask;
    Button btnregister;
    private static int SPLASH_SCREEN_TIME_OUT = 5000;
    private static int TIME_OUT = 3000;
    TextView exact,indicator;
    EditText editText;
DatabaseHelper db;
String sabaqID,s_name,parhawnar,date,time;
CardView monitorCard,instructionsCard,infoCard,statusCard;

Button btn_monitors;
    private static final String TAG = "Main2Activity";
    private static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    ImageView imageView;


    RequestQueue mQueue;

    public String its;

    String timel;
    String d_n;
    String n_v;

    ArrayList<String> NameList;
    TextView textView3;
    TextView textView,raza;
    ArrayList<Double> latList = new ArrayList<>();
    ArrayList<Double> longList = new ArrayList<>();
    Monitor_BackgroundTask monitor_backgroundTask;

    int n=0;
    String currentdat;
    BackgroundTask2 backgroundTask2;

    TextView alreadmarked,sabaqStatus,venue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        raza=findViewById(R.id.textView21);
        raza.setVisibility(View.GONE);
        venue=findViewById(R.id.textView22);
        venue.setVisibility(View.GONE);
        instructionsCard=findViewById(R.id.cardView2);
        btn_monitors=findViewById(R.id.button);
        infoCard=findViewById(R.id.cardView);
        statusCard=findViewById(R.id.cardView5);
        indicator=findViewById(R.id.status);

        db=new DatabaseHelper(this);

        monitorCard=findViewById(R.id.cardView4);

        btnregister = findViewById(R.id.button2);


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Fail";
                        }


                    }
                });

       
        BottomNavigationView bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_info:
                        startActivity(new Intent(getApplicationContext(),Info.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_home:
                        return true;
                    case R.id.online_sabaq:
                        startActivity(new Intent(getApplicationContext(),online.class));
                        overridePendingTransition(0,0);

                        return true;
                }
                return false;
            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());




        mQueue = Volley.newRequestQueue(this);
        imageView=findViewById(R.id.imageView5);
        imageView.setVisibility(View.GONE);
        alreadmarked=findViewById(R.id.textView15);
        alreadmarked.setVisibility(View.GONE);
        exact=findViewById(R.id.textView16);
        exact.setVisibility(View.GONE);
        sabaqStatus=findViewById(R.id.textView20);
        sabaqStatus.setVisibility(View.GONE);
        editText=findViewById(R.id.editText);

        SharedPreferences sharedPreferenc = getSharedPreferences("token", Context.MODE_PRIVATE);
        d_n = sharedPreferenc.getString("d_n", "Default");

        SharedPreferences sharedPreferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        its = sharedPreferences.getString("ITS", "Data Not Found");
        n_v=sharedPreferences.getString("n_v","Data Not Found");
        Gson gson = new Gson();
        String namejson = sharedPreferences.getString("name list", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        NameList = gson.fromJson(namejson, type);

        if (NameList == null) {
            NameList = new ArrayList<>();
        }


        if(its.equals("Data Not Found"))
        {
            Intent intent = new Intent(Main2Activity.this, Attendance.class);

            startActivity(intent);

        } else {
            textView.setText(its);
            textView3.setText(NameList.get(0));

         Cursor monitor=   db.getMonitor("1");
         if (monitor.getCount()==0)
         {

         }
         else
         {

             btnregister.setVisibility(View.GONE);
             instructionsCard.setVisibility(View.GONE);
             monitorCard.setVisibility(View.VISIBLE);
         }

               ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    String type1="login";

                    backgroundTask1 = new BackgroundTask1();
                    backgroundTask1.execute(type1,its);

                Timer timerAsync = new Timer();
                    TimerTask timerTaskAsync = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                              @Override public void run() {


                    Cursor res2=db.getSabaq2();
                    if (res2.getCount()>0) {

                        while (res2.moveToNext()) {
                            Cursor res3 = db.getLastMessage(res2.getString(0));
                            while (res3.moveToNext()) {
                                backgroundTask2 = new BackgroundTask2();
                                backgroundTask2.execute(res3.getString(0), res3.getString(2), res3.getString(3));
                             }
                        }
                    }
                                              }
                            });
                        }
                    };
                    timerAsync.schedule(timerTaskAsync, 0, 120000);


                    FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();
                                if(token.isEmpty())
                                {

                                }
                                else {
                                    bTask = new BTask();
                                    bTask.execute(token, its);
                                }


                            }
                        });
            } else {

            }



        }

btn_monitors.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (editText.getText().toString().isEmpty())
        {
            editText.setFocusable(true);
            editText.setError("Please Enter ITS");
        }
        else if (editText.getText().toString().length()==8)
        {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            android.net.NetworkInfo wifi = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo datac = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((wifi != null & datac != null)
                    && (wifi.isConnected() | datac.isConnected())) {
                //connection is avlilable
                mark();
                infoCard.setVisibility(View.INVISIBLE);
                statusCard.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }


        }
        else
        {
            editText.setError("Invalid ITS");
        }

    }
});




        btnregister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                n=0;
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    //connection is avlilable
                    getLastLocation();


                } else {
                    Toast.makeText(getApplicationContext(), "No InternetConnection", Toast.LENGTH_SHORT).show();

                }

            }
        });
        ImageButton logout = findViewById(R.id.button3);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Attendance.class);
                SharedPreferences sharedPreferences1 = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.clear();
                editor.apply();
                SharedPreferences sharedPreferences2 = getSharedPreferences("token", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                editor1.clear();
                editor1.apply();
                db.deleteAllData();
                db.deleteAllData2();
                NameList.clear();
                latList.clear();
                longList.clear();
                startActivity(intent);

                finish();
            }
        });








    }
    public void mark() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        timel = format.format(calendar.getTime());
        Double hr = Double.parseDouble(timel.substring(0, 2));
        Double min = Double.parseDouble(timel.substring(3));
        Double testTime = (hr + (min / 60));
        String testT = testTime.toString();
        String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()).substring(0, 2);
        //  String dayCompare = currentdate.substring(0, 2);3

        String group;
        String type = "login";
        //   for (int l=0;l<sabaqList.size();l++) {
        //    group=sabaqList.get(l);

        monitor_backgroundTask = new Monitor_BackgroundTask();
        monitor_backgroundTask.execute(type, testT, currentdate, editText.getText().toString(), timel);

        //  }

    }



    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {

                                    requestNewLocationData();

                                } else {
                                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                    android.net.NetworkInfo wifi = cm
                                            .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                    android.net.NetworkInfo datac = cm
                                            .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                    if ((wifi != null & datac != null)
                                            && (wifi.isConnected() | datac.isConnected())) {
                                    String macAddress=getMacAddr();
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                    timel = format.format(calendar.getTime());
                                    Double hr = Double.parseDouble(timel.substring(0, 2));
                                    Double min = Double.parseDouble(timel.substring(3));
                                    Double testTime = (hr + (min / 60));
                                    String testT=testTime.toString();
                                    String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()).substring(0, 2);
                                    //  String dayCompare = currentdate.substring(0, 2);3
                                    currentdat=DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                                    String group;
                                    String type = "login";
                                    String lat= String.valueOf(location.getLatitude());
                                    String lon=String.valueOf(location.getLongitude());

                                    backgroundTask = new BackgroundTask();
                                    backgroundTask.execute(type, testT, currentdate, its, NameList.get(0), timel,macAddress,lat,lon);
                                    }

                                    else {
                                        Toast.makeText(getApplicationContext(), "No InternetConnection", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null & datac != null)
                && (wifi.isConnected() | datac.isConnected())) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
        }

        else {
            Toast.makeText(getApplicationContext(), "No InternetConnection", Toast.LENGTH_SHORT).show();

        }

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {


                Location mLastLocation = locationResult.getLastLocation();
            String macAddress=getMacAddr();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            timel = format.format(calendar.getTime());
            Double hr = Double.parseDouble(timel.substring(0, 2));
            Double min = Double.parseDouble(timel.substring(3));
            Double testTime = (hr + (min / 60));
            String testT=testTime.toString();
            String currentdate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime()).substring(0, 2);
            //  String dayCompare = currentdate.substring(0, 2);3
            currentdat=DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

            String group;
            String type = "login";
            String lat= String.valueOf(mLastLocation.getLatitude());
            String lon=String.valueOf(mLastLocation.getLongitude());

                backgroundTask = new BackgroundTask();
                backgroundTask.execute(type, testT, currentdate, its, NameList.get(0), timel, macAddress, lat, lon);


        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }




    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);


    }








    class BackgroundTask extends AsyncTask<String, String, String> {
        ProgressDialog loading;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];

            String loginURL = "https://indoreestates.com/sabaq/daytime3.php";
            if (type.equals("login")) {
                String time = strings[1];
                String day = strings[2];
                String its = strings[3];
                String name = strings[4];
                String t=strings[5];
                String mac=strings[6];
                String latitude1=strings[7];
                String longitude1=strings[8];


                try {
                    URL url = new URL(loginURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        HttpURLConnection.setFollowRedirects(true);
                        httpURLConnection.setInstanceFollowRedirects(true);

                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        String login_data =URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&" + URLEncoder.encode("day", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8") + "&" + URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8") + "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8")+ "&" + URLEncoder.encode("t", "UTF-8") + "=" + URLEncoder.encode(t, "UTF-8")+ "&" + URLEncoder.encode("macAddress", "UTF-8") + "=" + URLEncoder.encode(mac, "UTF-8")+ "&" + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude1, "UTF-8")+ "&" + URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude1, "UTF-8");
                        bufferedWriter.write(login_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String result = "";
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");

                        }
                        result = stringBuilder.toString();
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                        String result="null";
                        return result;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    String result="null";
                    return result;
                }

            }

            return null;



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Main2Activity.this, null, "Please Wait", true, true);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            String check = s;
            if (check.equals("null")) {
                loading.dismiss();
                Toast.makeText(Main2Activity.this, "Network Error", Toast.LENGTH_SHORT).show();
            } else {

                loading.dismiss();
                if (check.contains("Attendance Marked")) {
                    n = 1;
                    alreadmarked.setVisibility(View.GONE);
                    sabaqStatus.setVisibility(View.GONE);
                    raza.setVisibility(View.GONE);
                    venue.setVisibility(View.GONE);

                    Intent intent = new Intent(Main2Activity.this, Final.class);
                    intent.putExtra("its", its);
                    intent.putExtra("time", timel);
                    intent.putExtra("date", currentdat);
                    intent.putExtra("name", NameList.get(0));
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Attendance Marked", Toast.LENGTH_SHORT).show();

                } else if (check.contains("Attendance Already Marked")) {
                    n = 1;
                    Toast.makeText(getApplicationContext(), "Attendance Already Marked", Toast.LENGTH_SHORT).show();
                    imageView.setVisibility(View.VISIBLE);
                    alreadmarked.setVisibility(View.VISIBLE);
                    exact.setText("on " + currentdat + " at " + timel);
                    exact.setVisibility(View.VISIBLE);
                    btnregister.setVisibility(View.GONE);
                    venue.setVisibility(View.GONE);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setVisibility(View.GONE);
                            alreadmarked.setVisibility(View.GONE);
                            exact.setVisibility(View.GONE);
                            btnregister.setVisibility(View.VISIBLE);


                        }
                    }, SPLASH_SCREEN_TIME_OUT);


                } else if (check.contains("Sabaq has not started")) {
                    if (n == 0) {
                        exact.setVisibility(View.GONE);
                        alreadmarked.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        sabaqStatus.setVisibility(View.VISIBLE);

                        raza.setVisibility(View.GONE);
                        venue.setVisibility(View.GONE);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sabaqStatus.setVisibility(View.GONE);


                            }
                        }, TIME_OUT);
                    } else {

                    }
                } else if (check.contains("TAKE RAZA")) {
                    if (n == 0) {
                        exact.setVisibility(View.GONE);
                        alreadmarked.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        sabaqStatus.setVisibility(View.GONE);
                        raza.setVisibility(View.VISIBLE);
                        venue.setVisibility(View.GONE);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                raza.setVisibility(View.GONE);


                            }
                        }, TIME_OUT);
                    } else {

                    }

                } else if (check.contains("Not Near Venue")) {
                    if (n == 0) {
                        exact.setVisibility(View.GONE);
                        alreadmarked.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        sabaqStatus.setVisibility(View.GONE);
                        raza.setVisibility(View.GONE);
                        venue.setVisibility(View.VISIBLE);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                venue.setVisibility(View.GONE);


                            }
                        }, TIME_OUT);
                    } else {

                    }

                } else {

                    Toast.makeText(Main2Activity.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                }


                // super.onPostExecute(s);
      /*  String check=s;
        if (check.contains("Attendance Marked")) {
            context.startActivity(new Intent(context, Final.class));

        }
        else {
            Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
        } */


            }
        }
    }
    protected void onDestroy(){
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (backgroundTask != null && backgroundTask.getStatus() != AsyncTask.Status.FINISHED)
            backgroundTask.cancel(true);
        super.onDestroy();
    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    class BTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

               String token=strings[0];
               String its=strings[1];
               String loginURL="https://indoreestates.com/sabaq/reg_id.php";


                try {
                    URL url = new URL(loginURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        HttpURLConnection.setFollowRedirects(true);
                        httpURLConnection.setInstanceFollowRedirects(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        String login_data = URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8")+ "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
                        bufferedWriter.write(login_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String result = "";
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");

                        }
                        result = stringBuilder.toString();
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }



            return null;



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              }

        @Override
        protected void onPostExecute(String s) {
            String check=s;




            // super.onPostExecute(s);
      /*  String check=s;
        if (check.contains("Attendance Marked")) {
            context.startActivity(new Intent(context, Final.class));

        }
        else {
            Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
        } */



        }
    }


    class BackgroundTask1 extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String loginURL = "https://indoreestates.com/sabaq/login2.php";
            if (type.equals("login")) {
                String its = strings[1];



                try {
                    URL url = new URL(loginURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        HttpURLConnection.setFollowRedirects(true);
                        httpURLConnection.setInstanceFollowRedirects(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        String login_data = URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8");
                        bufferedWriter.write(login_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String result = "";
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");

                        }
                        result = stringBuilder.toString();
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                        String result="null";
                        return result;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    String result="null";
                    return result;
                }

            }
            return null;



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String check = s;
            if (check.equals("null")) {

            } else {

                 if (check.contains("You are not in any sabaq")) {

                    Intent intent=new Intent(Main2Activity.this,Attendance.class);
                    SharedPreferences sharedPreferences1 = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.clear();
                    editor.apply();
                    SharedPreferences sharedPreferences2 = getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                    editor1.clear();
                    editor1.apply();
                    db.deleteAllData();
                    db.deleteAllData2();
                    db.deleteAllData3();
                    NameList.clear();
                    latList.clear();
                    longList.clear();
                    startActivity(intent);

                    finish();
                } else {
                     final SharedPreferences[] sharedPreferences = {getSharedPreferences("SaveData", Context.MODE_PRIVATE)};

                    db.deleteAllData2();
                    try {

                        JSONObject jsonObject = new JSONObject(check);
                        JSONArray jsonArray = jsonObject.getJSONArray("Members");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);


                            sabaqID = employee.getString("sabaqid");
                            parhawnar=employee.getString("parhawnar");
                            s_name=employee.getString("s_name");
                            date=employee.getString("date");
                            time=employee.getString("time");
                            boolean result=   db.refreshData(sabaqID,s_name,parhawnar,date,time,"",0,employee.getString("name"),employee.getString("count"),Integer.parseInt(employee.getString("admin")),employee.getString("monitor"),employee.getString("participants"));




                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }


        }

    }

    class BackgroundTask2 extends AsyncTask<String, String, String> {



        @Override
        protected String doInBackground(String... strings) {

            String loginURL = "https://indoreestates.com/sabaq/chat_msg.php";

            String sabaqid = strings[0];
            String date=strings[1];
            String time=strings[2];


            try {
                URL url = new URL(loginURL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    HttpURLConnection.setFollowRedirects(true);
                    httpURLConnection.setInstanceFollowRedirects(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    String login_data = URLEncoder.encode("sabaqid", "UTF-8") + "=" + URLEncoder.encode(sabaqid, "UTF-8") + "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
                    bufferedWriter.write(login_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String result = "";
                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");

                    }
                    result = stringBuilder.toString();
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    String result = "null";
                    return result;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                String result = "null";
                return result;
            }



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            String check = s;
            if (check.equals("null")) {


            } else {

                if (check.contains("No New Message")) {

                } else {

                    try {

                        JSONObject jsonObject = new JSONObject(check);
                        JSONArray jsonArray = jsonObject.getJSONArray("Server response");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);
                            if (employee.getString("name").equals(its))
                            {
                                boolean l = db.insertData3(employee.getString("sabaqid"), employee.getString("msg"), employee.getString("date"), employee.getString("time"), 1, "You");

                            }
                            else {
                                boolean l = db.insertData3(employee.getString("sabaqid"), employee.getString("msg"), employee.getString("date"), employee.getString("time"), 1, employee.getString("name"));
                            }
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }


        }

    }

    class Monitor_BackgroundTask extends AsyncTask<String, String, String> {
        ProgressDialog loading;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String regURL = "https://indoreestates.com/sabaq/register.php";
            String loginURL =  "https://indoreestates.com/sabaq/testmonitors1.php";
            if (type.equals("login")) {
                String time = strings[1];
                String day = strings[2];
                String its = strings[3];
                String t=strings[4];
                try {
                    URL url = new URL(loginURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        HttpURLConnection.setFollowRedirects(true);
                        httpURLConnection.setInstanceFollowRedirects(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        String login_data =  URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8") + "&" + URLEncoder.encode("day", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8") + "&" + URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8") + "&" + URLEncoder.encode("t", "UTF-8") + "=" + URLEncoder.encode(t, "UTF-8") ;
                        bufferedWriter.write(login_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "ISO-8859-1");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String result = "";
                        String line = "";
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");

                        }
                        result = stringBuilder.toString();
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();

                        return result;
                    } catch (IOException e) {
                        e.printStackTrace();
                        String result = "null";
                        return result;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    String result = "null";
                    return result;
                }

            }
            return null;



        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(Main2Activity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            loading.dismiss();
            String check=s;
            if (check.equals("null"))
            {
                Toast.makeText(Main2Activity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
            else {
                if (check.contains("Attendance Marked")) {
                    indicator.setText("Attendance Marked");
                    indicator.setTextColor(Color.parseColor("#013220"));

                    editText.setText("");
                } else if (check.contains("Attendance Already Marked")) {
                    indicator.setText("Attendance Already Marked");
                    indicator.setTextColor(Color.parseColor("#013220"));
                    Toast.makeText(getApplicationContext(), "Attendance Already Marked", Toast.LENGTH_SHORT).show();

                    editText.setText("");
                } else if (check.contains("TAKE RAZA")) {
                    indicator.setText("TAKE RAZA");
                    indicator.setTextColor(Color.parseColor("#013220"));

                    editText.setText("");
                } else {
                    indicator.setText("");

                    editText.setText("");
                }

                // super.onPostExecute(s);
      /*  String check=s;
        if (check.contains("Attendance Marked")) {
            context.startActivity(new Intent(context, Final.class));

        }
        else {
            Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
        } */

            }

        }
    }


}
