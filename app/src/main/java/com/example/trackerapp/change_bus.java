package com.example.trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class change_bus extends AppCompatActivity {
    EditText id,bn,fr,to,ti;
    Button upd,ins,del;
    SQLiteDatabase db;
    String time;
    int hrr,minn,itime;
    private void tshow()
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog t =new TimePickerDialog(change_bus.this, new TimePickerDialog.OnTimeSetListener() {
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
        setContentView(R.layout.activity_change_bus);
        id = findViewById(R.id.busid);
        bn = findViewById(R.id.busname);
        fr = findViewById(R.id.from);
        to = findViewById(R.id.to);
        ti = findViewById(R.id.time);
        upd = findViewById(R.id.update);
        ins = findViewById(R.id.insert);
        del = findViewById(R.id.delete);
        db = openOrCreateDatabase("bus_detail",MODE_PRIVATE,null);
        db.execSQL("create table if not exists bus (bid varchar(30) primary key,bname varchar(30),bfrom varchar(30),bto varchar(30),btime int)");
        ti.setClickable(true);
        ti.setLongClickable(false);
        ti.setInputType(InputType.TYPE_NULL);
        ti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tshow();
            }
        });

        ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = id.getText().toString();
                String n = bn.getText().toString();
                String f = fr.getText().toString();
                String t = to.getText().toString();
                if(i.equals("")||n.equals("")||f.equals("")||t.equals("")||time.equals(""))
                {
                    Toast.makeText(change_bus.this, "Value not entered.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Cursor c = db.rawQuery("select * from bus where bid=?",new String[]{i});
                    if(c.getCount()==0)
                    {
                        db.execSQL("insert into bus values('"+ i +"','"+ n +"','"+ f +"','"+ t +"',"+ itime +")");
                        Toast.makeText(change_bus.this, "Values entered.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(change_bus.this, "Already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = id.getText().toString();
                String n = bn.getText().toString();
                String f = fr.getText().toString();
                String t = to.getText().toString();
                if(i.equals("")||n.equals("")||f.equals("")||t.equals("")||time.equals(""))
                {
                    Toast.makeText(change_bus.this, "Value not entered.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(i.equals(""))
                    {
                        Toast.makeText(change_bus.this, "ID not entered.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        db.execSQL("update bus set bname='"+ n +"',bfrom='"+ f +"',bto='"+ t +"',btime="+ itime +" where bid='"+ i +"'");
                        Toast.makeText(change_bus.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String i = id.getText().toString();
                if(i.equals(""))
                {
                    Toast.makeText(change_bus.this, "ID not entered!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db.execSQL("delete from bus where bid='" + i +"'");
                    Toast.makeText(change_bus.this, "Deletion Complete.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}