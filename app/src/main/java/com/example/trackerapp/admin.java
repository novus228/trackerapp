package com.example.trackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admin extends AppCompatActivity {
    EditText us,pas;
    Button ch;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setContentView(R.layout.activity_admin);
        us =findViewById(R.id.username);
        pas=findViewById(R.id.password);
        ch = findViewById(R.id.admin);

        db = openOrCreateDatabase("admin_detail",MODE_PRIVATE,null);
        db.execSQL("create table if not exists admin (uname varchar(30) primary key,pass varchar(30))");
        ContentValues c1 = new ContentValues();
        c1.put("uname", "tanisha@08");
        c1.put("pass", "tan@123");
        long res = db.insert("admin", null, c1);
        ContentValues c2 = new ContentValues();
        c2.put("uname", "vishalbus");
        c2.put("pass", "88677567");
        long res1 = db.insert("admin", null, c2);
        ContentValues c3 = new ContentValues();
        c3.put("uname", "reshmabus");
        c3.put("pass", "bus@4321");
        long res2 = db.insert("admin", null, c3);

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(us.getText().toString().equals("") || pas.getText().toString().equals(""))
                {
                    Toast.makeText(admin.this, "Values not entered", Toast.LENGTH_SHORT).show();
                }
                else if(us.getText().toString().equals("") && pas.getText().toString().equals(""))
                {
                    Toast.makeText(admin.this, "Values not entered", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    String u = us.getText().toString().trim();
                    String p = pas.getText().toString();
                    Cursor c = db.rawQuery("Select * from admin where uname=?",new String[]{u});
                    if(c.getCount()>0)
                    {
                        c.moveToNext();
                        if(p.equals(c.getString(1)))
                        {
                            Toast.makeText(admin.this, "Access granted", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(admin.this,change_bus.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(admin.this, "Incorrect Password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(admin.this, "Incorrect Username.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}