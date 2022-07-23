package com.sabaq.chat;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import java.util.Iterator;

public class History extends AppCompatActivity {
    private ShimmerFrameLayout mShimmerViewContainer;

    int images[]= new  int[10];

    BackTask1 backTask1;
    Spinner spinner;
    String its,id,day,state,p,d;

    BackTask backTask;
    String selectedItem;
    RequestQueue mQueue;
    RecyclerView recyclerView;
    ArrayList<String> sabaqList=new ArrayList<>();
    ArrayList<String> sabaqList1=new ArrayList<>();
    ArrayList<String> day1=new ArrayList<>();
    ArrayList<String> state1=new ArrayList<>();
    ArrayList<String> date1=new ArrayList<>();
    CardView cardView;

    ImageView imageView;
    DatabaseHelper myDb;
    BackgroundTask1 backgroundTask1;


    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        myDb = new DatabaseHelper(this);

        Cursor res = myDb.getSabaq2();
        cardView = findViewById(R.id.cardView4);
        cardView.setVisibility(View.GONE);


        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.rv);
        spinner = findViewById(R.id.spinner);
        SharedPreferences sharedPreferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        its = sharedPreferences.getString("ITS", "Data Not Found");
        imageView = findViewById(R.id.imageView10);

        if (res.getCount() == 0) {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();

        } else {

            while (res.moveToNext()) {
                sabaqList1.add(res.getString(1) + " (" + res.getString(2) + ")" + " (" + res.getString(0) + ")");

            }
            spinner.setAdapter(new ArrayAdapter<String>(History.this,
                    android.R.layout.simple_spinner_dropdown_item, sabaqList1));
            Cursor res1 = myDb.getSabaq();
            if (res1.getCount() == 0) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int f = spinner.getSelectedItem().toString().lastIndexOf("(");
                            int l = spinner.getSelectedItem().toString().lastIndexOf(")");
                            selectedItem = spinner.getSelectedItem().toString();

                            backTask = new BackTask();
                            backTask.execute(its, spinner.getSelectedItem().toString().substring(f + 1, l));


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    imageView.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            selectedItem = spinner.getSelectedItem().toString();
                            Cursor res1 = myDb.getAllData(selectedItem);
                            day1.clear();
                            state1.clear();
                            date1.clear();
                            while (res1.moveToNext()) {
                                day1.add(res1.getString(1));
                                state1.add(res1.getString(2));
                                date1.add(res1.getString(3));


                            }

                            String s1[] = new String[day1.size()];
                            String s2[] = new String[day1.size()];
                            String s3[] = new String[day1.size()];
                            for (int i = 0; i < day1.size(); i++) {
                                if (state1.get(i).equals("PRESENT")) {
                                    s1[i] = day1.get(i);
                                    s2[i] = state1.get(i);
                                    s3[i] = date1.get(i);
                                    images[i] = R.drawable.ic_tick_;
                                } else {
                                    s1[i] = day1.get(i);
                                    s2[i] = state1.get(i);
                                    s3[i] = date1.get(i);
                                    images[i] = R.drawable.wrong;
                                }
                                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), s1, s2, images, s3);
                                recyclerView.setAdapter(myAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);


                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                    String type = "login";

                    backgroundTask1 = new BackgroundTask1();
                    backgroundTask1.execute(type, its);


                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    while (res.moveToNext()) {
                        sabaqList1.add(res.getString(0));

                    }
                    spinner.setAdapter(new ArrayAdapter<String>(History.this,
                            android.R.layout.simple_spinner_dropdown_item, sabaqList1));

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                            selectedItem = spinner.getSelectedItem().toString();
                            Cursor res1 = myDb.getAllData(selectedItem);
                            day1.clear();
                            state1.clear();
                            date1.clear();
                            while (res1.moveToNext()) {
                                day1.add(res1.getString(1));
                                state1.add(res1.getString(2));
                                date1.add(res1.getString(3));


                            }

                            String s1[] = new String[day1.size()];
                            String s2[] = new String[day1.size()];
                            String s3[] = new String[day1.size()];
                            for (int i = 0; i < day1.size(); i++) {
                                if (state1.get(i).equals("PRESENT")) {
                                    s1[i] = day1.get(i);
                                    s2[i] = state1.get(i);
                                    s3[i] = date1.get(i);
                                    images[i] = R.drawable.ic_tick_;
                                } else {
                                    s1[i] = day1.get(i);
                                    s2[i] = state1.get(i);
                                    s3[i] = date1.get(i);
                                    images[i] = R.drawable.wrong;
                                }
                                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), s1, s2, images, s3);
                                recyclerView.setAdapter(myAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                mShimmerViewContainer.stopShimmerAnimation();
                                mShimmerViewContainer.setVisibility(View.GONE);

                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }


            }
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_notifications:

                        return true;
                    case R.id.navigation_info:
                        startActivity(new Intent(getApplicationContext(), Info.class));
                        overridePendingTransition(0, 0);
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

    }



    class BackgroundTask1 extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];

            String loginURL = "https://indoreestates.com/sabaq/h_list.php";
            if (type.equals("login")) {
                String its = strings[1];


                try {
                    URL url = new URL(loginURL);
                    try {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        HttpURLConnection.setFollowRedirects(true);
                        httpURLConnection.setInstanceFollowRedirects(true);

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
            cardView.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String s) {

            String check=s;
            if (check.equals("null"))
            {
                cardView.setVisibility(View.GONE);
                Toast.makeText(History.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
            else {
                final SharedPreferences[] sharedPreferences = {getSharedPreferences("SaveData", Context.MODE_PRIVATE)};


                try {
                    JSONObject jsonObject = new JSONObject(check);


                    //  Toast.makeText(History.this, key, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = jsonObject.getJSONArray("Server response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        name = employee.getString("name");
                        id = employee.getString("id");
                        p = employee.getString("p_name");
                        sabaqList.add(name + " (" + p + ")" + " (" + id + ")");


                    }
                    spinner.setAdapter(new ArrayAdapter<String>(History.this,
                            android.R.layout.simple_spinner_dropdown_item, sabaqList));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int f = spinner.getSelectedItem().toString().lastIndexOf("(");
                        int l = spinner.getSelectedItem().toString().lastIndexOf(")");
                        cardView.setVisibility(View.VISIBLE);
                        selectedItem = spinner.getSelectedItem().toString();
                        Cursor res1 = myDb.getAllData(selectedItem);
                        day1.clear();
                        state1.clear();
                        date1.clear();
                        while (res1.moveToNext()) {
                            day1.add(res1.getString(1));
                            state1.add(res1.getString(2));
                            date1.add(res1.getString(3));


                        }

                        String s1[] = new String[day1.size()];
                        String s2[] = new String[day1.size()];
                        String s3[] = new String[day1.size()];
                        for (int i = 0; i < day1.size(); i++) {
                            if (state1.get(i).equals("PRESENT")) {
                                s1[i] = day1.get(i);
                                s2[i] = state1.get(i);
                                s3[i] = date1.get(i);
                                images[i] = R.drawable.ic_tick_;
                            } else {
                                s1[i] = day1.get(i);
                                s2[i] = state1.get(i);
                                s3[i] = date1.get(i);
                                images[i] = R.drawable.wrong;
                            }
                            MyAdapter myAdapter = new MyAdapter(getApplicationContext(), s1, s2, images, s3);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                        }

                        backTask1 = new BackTask1();
                        backTask1.execute(its, spinner.getSelectedItem().toString().substring(f + 1, l));


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // super.onPostExecute(s);
      /*  String check=s;
        if (check.equals("Attendance Marked")) {
            context.startActivity(new Intent(context, Final.class));

        }
        else {
            Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
        } */
            }


        }
    }


    class BackTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            String loginURL = "https://indoreestates.com/sabaq/i_history.php";

            String its = strings[0];
            String sabaqid=strings[1];


            try {
                URL url = new URL(loginURL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    String login_data = URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8")+ "&" + URLEncoder.encode("sabaqid", "UTF-8") + "=" + URLEncoder.encode(sabaqid, "UTF-8");
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
            recyclerView.setVisibility(View.GONE);
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
        }

        @Override
        protected void onPostExecute(String s) {


            String check=s;

if (check.equals("null"))
{
    mShimmerViewContainer.setVisibility(View.GONE);
    mShimmerViewContainer.stopShimmerAnimation();
    Toast.makeText(History.this, "Network Error", Toast.LENGTH_SHORT).show();
}
else {
    try {
        JSONObject jsonObject = new JSONObject(check);

        //  Toast.makeText(History.this, key, Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = jsonObject.getJSONArray("Server response");
        String s1[] = new String[jsonArray.length()];
        String s2[] = new String[jsonArray.length()];
        String s3[] = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject employee = jsonArray.getJSONObject(i);

            day = employee.getString("day");
            state = employee.getString("state");
            d = employee.getString("d");
            boolean isInserted = myDb.insertData(selectedItem, day, state, d);


            if (state.equals("PRESENT")) {
                s1[i] = day;
                s2[i] = state;
                s3[i] = d;
                images[i] = R.drawable.ic_tick_;
            } else {
                s1[i] = day;
                s2[i] = state;
                s3[i] = d;
                images[i] = R.drawable.wrong;
            }


            MyAdapter myAdapter = new MyAdapter(getApplicationContext(), s1, s2, images, s3);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        }
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


    } catch (JSONException e) {
        e.printStackTrace();
    }


    // super.onPostExecute(s);
      /*  String check=s;
        if (check.equals("Attendance Marked")) {
            context.startActivity(new Intent(context, Final.class));

        }
        else {
            Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
        } */

}

        }
    }


    class BackTask1 extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            String loginURL = "https://indoreestates.com/sabaq/i_history.php";

            String its = strings[0];
            String sabaqid=strings[1];


            try {
                URL url = new URL(loginURL);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    String login_data = URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8")+ "&" + URLEncoder.encode("sabaqid", "UTF-8") + "=" + URLEncoder.encode(sabaqid, "UTF-8");
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
            mShimmerViewContainer.startShimmerAnimation();

        }

        @Override
        protected void onPostExecute(String s) {


            String check=s;
            if (check.equals("null"))
            {
                cardView.setVisibility(View.GONE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                Toast.makeText(History.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
            else {


                try {
                    JSONObject jsonObject = new JSONObject(check);
                    myDb.deleteData(selectedItem);

                    //  Toast.makeText(History.this, key, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = jsonObject.getJSONArray("Server response");
                    String s1[] = new String[jsonArray.length()];
                    String s2[] = new String[jsonArray.length()];
                    String s3[] = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject employee = jsonArray.getJSONObject(i);

                        day = employee.getString("day");
                        state = employee.getString("state");
                        d = employee.getString("d");

                        boolean isInserted = myDb.insertData(selectedItem, day, state, d);


                        if (state.equals("PRESENT")) {
                            s1[i] = day;
                            s2[i] = state;
                            s3[i] = d;
                            images[i] = R.drawable.ic_tick_;
                        } else {
                            s1[i] = day;
                            s2[i] = state;
                            s3[i] = d;
                            images[i] = R.drawable.wrong;
                        }


                        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), s1, s2, images, s3);
                        recyclerView.setAdapter(myAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


                    }
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

                    cardView.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // super.onPostExecute(s);
      /*  String check=s;
        if (check.equals("Attendance Marked")) {
            context.startActivity(new Intent(context, Final.class));

        }
        else {
            Toast.makeText(context, check, Toast.LENGTH_SHORT).show();
        } */

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





}
