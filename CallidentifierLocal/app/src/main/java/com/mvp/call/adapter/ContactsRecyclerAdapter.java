package com.mvp.call.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mvp.call.R;
import com.mvp.call.database.ContactDetails;
import com.mvp.call.database.MyContactsDB;

import java.util.ArrayList;
import java.util.List;


public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<ContactDetails> cont;
    private ContactDetails list;
    private List<ContactDetails> arraylist;
    private Context mContext;
    SharedPreferences sharedpreferences;
    private MyContactsDB contactsDB;

    public ContactsRecyclerAdapter(LayoutInflater inflater, Context mcontext, List<ContactDetails> items) {
        this.layoutInflater = inflater;
        this.cont = items;
        this.arraylist = new ArrayList<ContactDetails>();
        this.arraylist.addAll(cont);
        mContext = mcontext;
        sharedpreferences = mContext.getSharedPreferences("MyPref", Activity.MODE_PRIVATE);
        Log.v("Call", "Total Size:::::::::::::::::::" + arraylist.size());
        MyContactsDB.getInstance(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.contactlist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        list = cont.get(position);
        String name = (list.getName());
        String phoneNo = list.getPhone();
        holder.title.setText(name);
        holder.phone.setText(phoneNo);

        int pos = sharedpreferences.getInt(Integer.toString(position), 0);
        Log.v("Call", "RecyclerView Position:" + position + "................Retried Pref Position:" + pos);

        //List<ContactDetails> contactDetails = MyContactsDB.getInstance(mContext).userDao().getContactsSelectedMobileNo(phoneNo);

        if (pos == 1) {
            holder.checkBox1.setChecked(true);
        } else {
            holder.checkBox1.setChecked(false);
        }

        holder.checkBox1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                saveCheckedPosition(position, 1, name, phoneNo);
                Log.v("Call", "onCheckedChanged saved:position:" + position);
            } else {
                Log.v("Call", "onCheckedChanged UNSAVED:position:" + position);
                saveCheckedPosition(position, 0, name, phoneNo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cont.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView phone;
        public CheckBox checkBox1;

        public ViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            title = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.no);
            checkBox1 = itemView.findViewById(R.id.checkBox);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void saveCheckedPosition(int key, int value, String name, String mobno) {

        sharedpreferences = mContext.getSharedPreferences("MyPref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(Integer.toString(key), value);
        if (value == 1)
            editor.putString(mobno, mobno + "-" + name); // same mobile no
        else
            editor.putString(mobno, String.valueOf(value)); //0
        editor.commit();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                MyContactsDB.getInstance(mContext).userDao().update1(String.valueOf(value), mobno);
            }
        });
    }

}