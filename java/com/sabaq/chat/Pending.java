package com.sabaq.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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

public class Pending extends AppCompatActivity {
    String its;
    ArrayList<String> NameList=new ArrayList<>();
    ArrayList<String> statusList=new ArrayList<>();
    ArrayList<String> nisaabList=new ArrayList<>();
    ArrayList<String> p_name=new ArrayList<>();
    TextView textView,textView3;
    RecyclerView recyclerView;
    BackgroundTask backgroundTask;

    DatabaseHelper databaseHelper;

    RequestQueue mQueue;
    ArrayList<String> catList = new ArrayList<>();

    String  sabaqID, name,parhawnar,s_name,date,time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        textView = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.r_info);
        ImageButton logout = findViewById(R.id.button3);
        databaseHelper=new DatabaseHelper(this);

        mQueue = Volley.newRequestQueue(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pending.this, Attendance.class);
                SharedPreferences sharedPreferences1 = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.clear();
                editor.apply();
                NameList.clear();
                nisaabList.clear();
                statusList.clear();
                p_name.clear();


                startActivity(intent);

                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        its = sharedPreferences.getString("pending_its", "Data Not Found");
        Gson gson = new Gson();
        String namejson = sharedPreferences.getString("pending_name list", null);
        String nisaabjson = sharedPreferences.getString("pending_nisaab list", null);
        String pjson = sharedPreferences.getString("pending_parhawnar_name list", null);
        String sjson = sharedPreferences.getString("status list", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        NameList = gson.fromJson(namejson, type);
        nisaabList = gson.fromJson(nisaabjson, type);
        statusList=gson.fromJson(sjson,type);
       p_name = gson.fromJson(pjson, type);
        if (NameList == null) {
            NameList = new ArrayList<>();
        }

        if (its.equals("Data Not Found"))
        {
            Intent intent = new Intent(Pending.this, Pending.class);
            startActivity(intent);
        }
        else
        {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            android.net.NetworkInfo wifi = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo datac = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((wifi != null & datac != null)
                    && (wifi.isConnected() | datac.isConnected())) {
                String type1 = "login";

                backgroundTask = new BackgroundTask();
                backgroundTask.execute(type1, its);
            }
            else
            {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            textView.setText(its);
            textView3.setText(NameList.get(0));
           String s1[]=new String[nisaabList.size()];String s2[]=new String[nisaabList.size()];String s3[]=new String[nisaabList.size()];
            int[] image=new int[nisaabList.size()];
            for(int i=0;i<nisaabList.size();i++)
            {


                if (statusList.get(i).equals("1")) {
                    s1[i] = nisaabList.get(i);
                    s2[i] = p_name.get(i);
                    image[i] = R.drawable.ic_history_48px_512;
                    s3[i]="Pending";
                }
                else
                {
                    s1[i] = nisaabList.get(i);
                    s2[i] = p_name.get(i);
                    image[i]=R.drawable.wrong;
                    s3[i]="Denied";
                }
                My_Pending myAdapter = new My_Pending(getApplicationContext(), s1,s2,image,s3);
                recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }
    }

    class BackgroundTask extends AsyncTask<String, String, String> {
        ProgressDialog loading;


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
            loading = ProgressDialog.show(Pending.this, null, "Refreshing", true, true);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            String check = s;
            if (check.equals("null")) {
                loading.dismiss();
                Toast.makeText(Pending.this, "Network Error", Toast.LENGTH_SHORT).show();
            } else {
                loading.dismiss();
                final SharedPreferences[] sharedPreferences = {getSharedPreferences("SaveData", Context.MODE_PRIVATE)};
                if (check.contains("You are not in any sabaq")) {

                    Toast.makeText(Pending.this, "You are not in any sabaq", Toast.LENGTH_SHORT).show();
                }
                else if(check.contains("PendingMembers"))
                {

                }

                else {

                    try {

                        JSONObject jsonObject = new JSONObject(check);
                        JSONArray jsonArray = jsonObject.getJSONArray("Members");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);

                            its = employee.getString("its");
                            sabaqID = employee.getString("sabaqid");
                            name = employee.getString("name");
                            parhawnar=employee.getString("parhawnar");
                            s_name=employee.getString("s_name");
                            date=employee.getString("date");
                            time=employee.getString("time");
                            boolean result=   databaseHelper.insertData2(sabaqID,s_name,parhawnar,date,time,"",0,employee.getString("name"),employee.getString("count"),Integer.parseInt(employee.getString("admin")),employee.getString("monitor"),employee.getString("participants"));





                                catList.add(name);



                        }


                        Intent intent = new Intent(Pending.this, Main2Activity.class);
                        startActivity(intent);
                        sharedPreferences[0] = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences[0].edit();
                        editor.putString("ITS", its);
                        editor.putString("n_v","1");
                        Gson gson = new Gson();
                        String namejson = gson.toJson(catList);
                        editor.putString("name list", namejson);
                        editor.apply();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }


        }

    }

    protected void onDestroy(){
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (backgroundTask != null && backgroundTask.getStatus() != AsyncTask.Status.FINISHED)
            backgroundTask.cancel(true);
        super.onDestroy();
    }
    public  void onBackPressed()
    {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

}


