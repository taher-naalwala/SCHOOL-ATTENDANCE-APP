package com.sabaq.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class My_Pending  extends RecyclerView.Adapter<My_Pending.MyViewHolder> {

    String data1[],data2[],data4[]; Context context; int data3[];

    public My_Pending(Context ctx,String s1[],String s2[],int image[],String s3[]){
        context=ctx;
        data1=s1;
        data2=s2;
        data3=image;
        data4=s3;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.my_pending,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
        holder.imageView.setImageResource(data3[position]);
        holder.myText3.setText(data4[position]);
    }



    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView myText1,myText2,myText3;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1=itemView.findViewById(R.id.textView10);
            myText2=itemView.findViewById(R.id.textView11);
            imageView=itemView.findViewById(R.id.imageView6);
            myText3=itemView.findViewById(R.id.textView12);


        }
    }
}
