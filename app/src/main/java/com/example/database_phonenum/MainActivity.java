package com.example.database_phonenum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contacts ( _id INTEGER PRIMARY KEY" +
                " AUTOINCREMENT, name TEXT, tel TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}

public class MainActivity extends AppCompatActivity {
    DBHelper helper;
    SQLiteDatabase db;
    EditText editText_name, editText_tel;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DBHelper(this);
        try{
            db = helper.getWritableDatabase();
        } catch (SQLException exception) {
            db = helper.getReadableDatabase();
        }
        editText_name = findViewById(R.id.editText_name);
        editText_tel = findViewById(R.id.editText_tel);
        result = findViewById(R.id.result);
    }

    public void insert(View v) {
        String name = editText_name.getText().toString();
        String tel = editText_tel.getText().toString();

        db.execSQL("INSERT INTO contacts VALUES (null, '" + name + "','" + tel + "');");
        Toast.makeText(getApplicationContext(), "추가 성공!", Toast.LENGTH_SHORT).show();
        editText_name.setText(null);
        editText_tel.setText(null);
    }

    public void search(View v) {
        String name = editText_name.getText().toString();
        Cursor cursor;
        cursor = db.rawQuery("SELECT name, tel FROM contacts WHERE name='"
                + name +"';",null);
        Toast.makeText(getApplicationContext(), ""+cursor.getCount(), Toast.LENGTH_SHORT).show();

        while (cursor.moveToNext()) {
            String tel = cursor.getString(1);
            editText_tel.setText(tel);
        }
    }

    public void select_all(View v) {
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM contacts",null);

        String str = "Id          Name          Tel \r\n";
        while (cursor.moveToNext()){
            str += cursor.getString(0) + "          ";
            str += cursor.getString(1) + "          ";
            str += cursor.getString(2) + "          \r\n";
        }
        result.setText(str);


    }

}