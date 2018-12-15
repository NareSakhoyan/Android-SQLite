package am.orinak.student.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    final String LOG_TAG = "myLogs";

    private Button btnAdd, btnRead, btnClear;
    private EditText etName, etEmail;

    private TextView text;

    ConnectDB connectDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);

        text = (TextView)findViewById(R.id.textView);

        connectDB = new ConnectDB(this);
    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        SQLiteDatabase db = connectDB.getWritableDatabase();

        if(v.equals(btnAdd))
        {
            Log.d(LOG_TAG, "--- Insert in mytable: ---");

            cv.put("name", name);
            cv.put("email", email);

            long rowID = db.insert("mytable", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        }

        if(v.equals(btnRead))
        {
            Log.d(LOG_TAG, "--- Rows in mytable: ---");

            Cursor c = db.query("mytable", null, null, null, null, null, null);

            if (c.moveToFirst()) {

                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int emailColIndex = c.getColumnIndex("email");

                String str = "";

                do
                {
                    str+=("ID = " + c.getInt(idColIndex) +
                                    ", name = " + c.getString(nameColIndex) +
                                    ", email = " + c.getString(emailColIndex)+"\n");
                }
                while(c.moveToNext());

                text.setTextColor(Color.GREEN);
                text.setTextSize(16);
                text.setText(String.format("%s ", str));

            } else
                Log.d(LOG_TAG, "0 rows");

            c.close();
        }

        if(v.equals(btnClear))
        {
            Log.d(LOG_TAG, "--- Clear mytable: ---");

            int clearCount = db.delete("mytable", null, null);
            Log.d(LOG_TAG, "deleted rows count = " + clearCount);
        }

        db.close();
    }

    class ConnectDB extends SQLiteOpenHelper
    {

        public ConnectDB(Context context)
        {
            super(context,"db",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            db.execSQL("create table mytable ("
//                    + "id integer primary key autoincrement,"
//                    + "name text,"
//                    + "email text" + ");");

           db.execSQL("create table mytable(id integer primary key autoincrement,name text,email text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
