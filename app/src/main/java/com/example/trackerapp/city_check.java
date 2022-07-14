package com.example.trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class city_check extends AppCompatActivity {
    Spinner s,s1;
    ArrayAdapter<String> adp,adp1,adp2;
    SQLiteDatabase db;
    Button b1;
    EditText ti;
    TextView tv;
    String time;
    int hrr,minn,itime;
    ListView l;

    private void tshow()
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog t =new TimePickerDialog(city_check.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hrr=i;
                minn=i1;
                itime=(i*100)+i1;
                if(i1<10)
                {
                    ti.setText(i+":0"+i1);
                    time=i+":0"+i1;
                }
                else {
                    ti.setText(i + ":" + i1);
                    time = i + ":" + i1;
                }
            }
        }, hour, min,true);
        t.setTitle("Select Time");
        t.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_check);
        s = findViewById(R.id.ss);
        b1 = findViewById(R.id.bb);
        s1 = findViewById(R.id.ss1);
        ti = findViewById(R.id.time);
        tv=findViewById(R.id.avail);
        l=findViewById(R.id.list);
        ArrayList<String> str= new ArrayList<String>();
        str.add(" From Location");
        adp = new ArrayAdapter<String>(city_check.this, android.R.layout.simple_dropdown_item_1line,str);
        adp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s.setAdapter(adp);
        db = openOrCreateDatabase("bus_detail",MODE_PRIVATE,null);
        Cursor c = db.rawQuery("select distinct bfrom from bus ",null);
        while(c.moveToNext())
        {
            String city = c.getString(0);
            str.add(city);
            adp.notifyDataSetChanged();
        }
        ArrayList<String> str1= new ArrayList<String>();
        str1.add("To Location");
        adp1 = new ArrayAdapter<String>(city_check.this, android.R.layout.simple_dropdown_item_1line,str1);
        adp1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s1.setAdapter(adp1);
        Cursor c1 = db.rawQuery("select distinct bto from bus ",null);
        while(c1.moveToNext())
        {
            String city1 = c1.getString(0);
            str1.add(city1);
            adp1.notifyDataSetChanged();
        }
        //Availability of buses
        ArrayList<String> str3= new ArrayList<String>();
        adp2=new ArrayAdapter<String>(city_check.this, android.R.layout.simple_list_item_1,str3);
        l.setAdapter(adp2);

        ti.setClickable(true);
        ti.setLongClickable(false);
        ti.setInputType(InputType.TYPE_NULL);
        ti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tshow();
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = s.getSelectedItem().toString();
                String to = s1.getSelectedItem().toString();
                String time=ti.getText().toString();
                if(time.equals(""))
                {
                    Toast.makeText(city_check.this, "Time not entered", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Cursor c2 = db.rawQuery("select b.* from bus as b where bfrom=? and bto=? and btime =?" +
                            "",new String[]{from,to,String.valueOf(itime)});
                    if(c2.getCount()==0) {
                        Cursor c3 = db.rawQuery("select b.* from bus as b where bfrom=? and bto=? and btime >=? order by btime" +
                                "",new String[]{from,to,String.valueOf(itime)});
                        if(c3.getCount()==0)
                        {
                            adp2.clear();
                            adp2.notifyDataSetChanged();
                            tv.setText("No buses available");
                        }
                        else
                        {
                            String dis;
                            str3.clear();
                            while (c3.moveToNext()) {
                                int h,m;
                                tv.setText("Upcoming Buses:");
                                int tt=c3.getInt(4);
                                h=tt/100;
                                m=tt%100;
                                dis=h+":"+m;
                                String idbus = "ID: "+c3.getString(0)+" Name: "+c3.getString(1)+"                    Time: "+dis;
                                str3.add(idbus);
                                adp2.notifyDataSetChanged();
                            }
                        }
                    }
                    else
                    {
                        str3.clear();
                        int h,m;
                        String dis;
                        while (c2.moveToNext()) {
                            int tt=c2.getInt(4);
                            h=tt/100;
                            m=tt%100;
                            dis=h+":"+m;
                            tv.setText("Buses Available");
                            String idbus = "ID: "+c2.getString(0)+" Name: "+c2.getString(1)+"                    Time: "+dis;
                            str3.add(idbus);
                            adp2.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }
}