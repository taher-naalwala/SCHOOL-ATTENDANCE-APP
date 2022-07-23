package com.sabaq.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class PendingRaza extends AppCompatActivity {
    BackgroundTask backgroundTask;
    String its;
    ArrayList<String> statusList=new ArrayList<>();
    ArrayList<String> pending_nisaab_list=new ArrayList<>();
    ArrayList<String> pending_parhawnar_name_list=new ArrayList<>();
    RecyclerView recyclerView;
    TextView emptymsg,title_tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_raza);
        recyclerView=findViewById(R.id.recycler);
        emptymsg=findViewById(R.id.textView31);
        title_tool=findViewById(R.id.textView9);
        SharedPreferences sharedPreferences = getSharedPreferences("SaveData", Context.MODE_PRIVATE);
        its = sharedPreferences.getString("ITS", "Data Not Found");
        Bundle bundle = getIntent().getExtras();
       String status= bundle.getString("status");
       if (status.equals("1"))
       {
           emptymsg.setText("No Pending Raza");
           title_tool.setText("Pending Raza");
       }
       else
       {
           emptymsg.setText("No Denied Raza");
           title_tool.setText("Denied Raza");
       }
        String type = "login";

        backgroundTask = new BackgroundTask();
        backgroundTask.execute(type, its,status);
    }

    class BackgroundTask extends AsyncTask<String, String, String> {
        ProgressDialog loading;


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String loginURL = "https://indoreestates.com/sabaq/pending.php";
            if (type.equals("login")) {
                String its = strings[1];
                String status=strings[2];


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
                        String login_data = URLEncoder.encode("its", "UTF-8") + "=" + URLEncoder.encode(its, "UTF-8")+ "&" + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");
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
            loading = ProgressDialog.show(PendingRaza.this, null, "Please Wait", true, true);
            loading.setCancelable(false);
            loading.setCanceledOnTouchOutside(false);
        }

        @Override
        protected void onPostExecute(String s) {
            String check = s;
            if (check.equals("null")) {
                loading.dismiss();
                Toast.makeText(PendingRaza.this, "Network Error", Toast.LENGTH_SHORT).show();
            } else {
                loading.dismiss();
                final SharedPreferences[] sharedPreferences = {getSharedPreferences("SaveData", Context.MODE_PRIVATE)};
                if (check.contains("You are not in any sabaq")) {

                    Toast.makeText(PendingRaza.this, "You are not in any sabaq", Toast.LENGTH_SHORT).show();
                } else if(check.contains("No Result"))
                {
                    recyclerView.setVisibility(View.GONE);
                    emptymsg.setVisibility(View.VISIBLE);
                }

                else   {

                    try {

                        JSONObject jsonObject = new JSONObject(check);
                        JSONArray jsonArray = jsonObject.getJSONArray("PendingMembers");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject employee = jsonArray.getJSONObject(i);

                            String nisaab = employee.getString("nisaab");
                          String  parhawnar_name = employee.getString("parhawnar_name");
                          String  status = employee.getString("status");
                            
                          


    pending_nisaab_list.add(nisaab);
    pending_parhawnar_name_list.add(parhawnar_name);
    statusList.add(status);



                        }
                        String s1[]=new String[pending_nisaab_list.size()];String s2[]=new String[pending_nisaab_list.size()];String s3[]=new String[pending_nisaab_list.size()];
                        int[] image=new int[pending_nisaab_list.size()];
                        for(int i=0;i<pending_nisaab_list.size();i++) {


                            if (statusList.get(i).equals("1")) {

                                s1[i] = pending_nisaab_list.get(i);
                                s2[i] = pending_parhawnar_name_list.get(i);
                                image[i] = R.drawable.ic_history_48px_512;
                                s3[i] = "Pending";
                            }
                            else
                            {
                                s1[i] = pending_nisaab_list.get(i);
                                s2[i] = pending_parhawnar_name_list.get(i);
                                image[i] = R.drawable.wrong;
                                s3[i] = "Pending";
                            }
                            My_Pending myAdapter = new My_Pending(getApplicationContext(), s1, s2, image, s3);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }


                       


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } 
            }


        }

    }
}
