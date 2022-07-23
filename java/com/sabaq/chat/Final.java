package com.sabaq.chat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class Final extends AppCompatActivity {
    private TextView name, its, exact;
    private static int SPLASH_SCREEN_TIME_OUT = 6000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_final);
        exact=findViewById(R.id.textView17);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel("MyNotification","MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle("Attendance Marked")
                .setSmallIcon(R.drawable.image)
                .setAutoCancel(true)
                .setContentText("Mubarak!!! You have achieved the happiness of Aqa Moula TUS");

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder.build());



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               Intent i = new Intent(Final.this,
                     Main2Activity.class);
                //Intent is used to switch from one activity to another.

                startActivity(i);

                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
        TextView textView = findViewById(R.id.textView2);

        its = findViewById(R.id.textView22);
        name=findViewById(R.id.textView);


        //ImageView imageView = findViewById(R.id.imageView2);


        Bundle bundle = getIntent().getExtras();

        its.setText(bundle.getString("its"));
        exact.setText("on "+bundle.getString("date")+" at "+bundle.getString("time"));
        name.setText(bundle.getString("name"));
    }





}
