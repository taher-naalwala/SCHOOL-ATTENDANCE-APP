package com.sabaq.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    RequestQueue mQueue;
    EditText editText;
    ArrayList<String> catList = new ArrayList<>();
    ArrayList<String> statusList = new ArrayList<>();
    ArrayList<String> pending_name_list = new ArrayList<>();
    ArrayList<String> pending_nisaab_list = new ArrayList<>();
    ArrayList<String> pending_parhawnar_name_list = new ArrayList<>();
    String its, sabaqID, name,parhawnar,s_name,date,time,nisaab,parhawnar_name,status,monitor;

   BackgroundTask backgroundTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        final SharedPreferences[] sharedPreferences = {getSharedPreferences("SaveData", Context.MODE_PRIVATE)};
        String value = sharedPreferences[0].getString("pending_its", "Data Not Found");
        if (value.equals("Data Not Found"))
        {

        }
        else
        {
            Intent intent=new Intent(Attendance.this,Pending.class);
            startActivity(intent);
        }
        Button buttonParse = findViewById(R.id.button);

        databaseHelper=new DatabaseHelper(this);




        mQueue = Volley.newRequestQueue(this);
        editText = findViewById(R.id.editText);


        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                android.net.NetworkInfo wifi = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                android.net.NetworkInfo datac = cm
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    //connection is avlilable
                    if (editText.getText().toString().isEmpty()) {
                        editText.setError("Please Enter ITS");
                    } else if (editText.getText().toString().length() == 8) {
                        String type="login";

                        backgroundTask = new BackgroundTask();
                        backgroundTask.execute(type,editText.getText().toString());

                    } else {

                        Toast.makeText(Attendance.this, "Invalid ITS", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No InternetConnection", Toast.LENGTH_SHORT).show();

                }

            }
        });


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
            loading = ProgressDialog.show(Attendance.this, null, "Please Wait", true, true);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            String check = s;
            if (check.equals("null")) {
                loading.dismiss();
                Toast.makeText(Attendance.this, "Network Error", Toast.LENGTH_SHORT).show();
            } else {
                loading.dismiss();
                final SharedPreferences[] sharedPreferences = {getSharedPreferences("SaveData", Context.MODE_PRIVATE)};
                if (check.contains("You are not in any sabaq")) {

                    Toast.makeText(Attendance.this, "You are not in any sabaq", Toast.LENGTH_SHORT).show();
                }
                else if(check.contains("PendingMembers"))
                {

                    try {

                        JSONObject jsonObject = new JSONObject(check);
                        JSONArray jsonArray = jsonObject.getJSONArray("PendingMembers");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);

                            its = employee.getString("its");
                            name = employee.getString("name");
                            nisaab = employee.getString("nisaab");
                            parhawnar_name=employee.getString("parhawnar_name");
                            status=employee.getString("status");


                                pending_name_list.add(name);
                                pending_nisaab_list.add(nisaab);
                                pending_parhawnar_name_list.add(parhawnar_name);
                                catList.add(name);
                                statusList.add(status);



                        }


                        Intent intent = new Intent(Attendance.this, Pending.class);
                        startActivity(intent);
                        sharedPreferences[0] = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences[0].edit();
                        editor.putString("pending_its", editText.getText().toString());
                        Gson gson = new Gson();
                        String namejson = gson.toJson(catList);
                        editor.putString("pending_name list", namejson);
                        String nisaabjson = gson.toJson(pending_nisaab_list);
                        editor.putString("pending_nisaab list", nisaabjson);
                        String parhawnar_name_json = gson.toJson(pending_parhawnar_name_list);
                        editor.putString("pending_parhawnar_name list", parhawnar_name_json);
                        String status_json = gson.toJson(statusList);
                        editor.putString("status list", status_json);
                        editor.apply();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                            monitor=employee.getString("monitor");
                         boolean result=   databaseHelper.insertData2(sabaqID,s_name,parhawnar,date,time,"",0,employee.getString("name"),employee.getString("count"),Integer.parseInt(employee.getString("admin")),monitor,employee.getString("participants"));




                            if (its.equals(editText.getText().toString())) {

                                catList.add(name);

                            }

                        }


                        Intent intent = new Intent(Attendance.this, Main2Activity.class);
                        startActivity(intent);
                        sharedPreferences[0] = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences[0].edit();
                        editor.putString("ITS", editText.getText().toString());
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

