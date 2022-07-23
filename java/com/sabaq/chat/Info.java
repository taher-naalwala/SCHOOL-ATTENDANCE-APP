package com.sabaq.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Info extends AppCompatActivity {
    String its;
    ArrayList<String> NameList;
    TextView textView,textView3;
    ArrayList<String> p=new ArrayList<>();
    ArrayList<String> sa=new ArrayList<>();
    DatabaseHelper db;
    CardView cardView,cardView2,cardView4,cardView5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        textView = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView);
        cardView2=findViewById(R.id.cardView3);
        cardView4=findViewById(R.id.cardView4);
        cardView5=findViewById(R.id.cardView5);

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Info.this,PendingRaza.class);
                intent.putExtra("status","2");
                startActivity(intent);
            }
        });
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Info.this,PendingRaza.class);
                intent.putExtra("status","1");
                startActivity(intent);
            }
        });

        cardView=findViewById(R.id.cardView2);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Info.this,Ongoing.class);
                startActivity(intent);
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"thetoplisted34@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Chat With Us (Help)(App: Sabaq Attendance App)");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        SharedPreferences sharedPreferenc = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        Type type1 = new TypeToken<ArrayList<String>>() {
        }.getType();

        db = new DatabaseHelper(this);


        ImageButton logout = findViewById(R.id.button3);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Info.this, Attendance.class);
                SharedPreferences sharedPreferences1 = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.clear();
                editor.apply();
                db.deleteAllData();
                NameList.clear();
                db.deleteAllData2();


                startActivity(intent);

                finish();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_info);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_notifications:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_info:
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.online_sabaq:
                        startActivity(new Intent(getApplicationContext(), online.class));
                        overridePendingTransition(0, 0);

                        return true;
                }
                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        its = sharedPreferences.getString("ITS", "Data Not Found");
        Gson gson = new Gson();
        String namejson = sharedPreferences.getString("name list", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        NameList = gson.fromJson(namejson, type);

        Cursor res=db.getSabaq2();

        if (res.getCount()==0) {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        } else {
            textView.setText(its);
            textView3.setText(NameList.get(0));



        }

    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);


    }






}
