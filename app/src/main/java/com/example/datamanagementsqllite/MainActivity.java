package com.example.datamanagementsqllite;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProfileDbHelper dbHelper;
    private EditText e_et, n_et, a_et;
    private RadioGroup rg;
    private Button u_b, d_b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new ProfileDbHelper(this);

        e_et = findViewById(R.id.emailET);
        n_et = findViewById(R.id.nameET);
        a_et = findViewById(R.id.addressET);
        rg = findViewById(R.id.maritalRG);
        u_b = findViewById(R.id.updateB);
        d_b = findViewById(R.id.delB);

        u_b.setEnabled(false);
        d_b.setEnabled(false);
    }

    public void add_b(View v){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProfileContract.Profile.COLUMN_NAME_EMAIL, e_et.getText().toString());
        values.put(ProfileContract.Profile.COLUMN_NAME_NAME, n_et.getText().toString());
        values.put(ProfileContract.Profile.COLUMN_NAME_ADDRESS, a_et.getText().toString());
        values.put(ProfileContract.Profile.COLUMN_NAME_MARITAL, rg.getCheckedRadioButtonId());
        // Insert the new row, returning the primary key value of the new row
        long resCode = db.insert(
                ProfileContract.Profile.TABLE_NAME,
                null,
                values
        );

        if (resCode == -1) {
            Toast.makeText(this, "An error occurred OR record already existed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "record id: " + resCode + " added", Toast.LENGTH_SHORT).show();
        }
    }

    public void search_b(View v){
        // Gets the data repository in read mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = {
                ProfileContract.Profile.COLUMN_NAME_EMAIL,
                ProfileContract.Profile.COLUMN_NAME_NAME,
                ProfileContract.Profile.COLUMN_NAME_ADDRESS,
                ProfileContract.Profile.COLUMN_NAME_MARITAL
        };
        // Filter results WHERE "email" = e_et.getText().toString()
        String selection = ProfileContract.Profile.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = {e_et.getText().toString()};

        // How you want the results sorted in the resulting Cursor (if search query returns more than one results)
        String sortOrder = ProfileContract.Profile.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                ProfileContract.Profile.TABLE_NAME, // The table to query
                projection,                         // The array of columns to return (pass null to get all)
                selection,                          // The columns for WHERE clause
                selectionArgs,                      // The values for the WHERE clause
                null,                       // don't group the rows
                null,                        // don't filter by row groups
                sortOrder
        );
        // Create a list to get all the rows
        List result = new ArrayList<>();
        ContentValues rowData = new ContentValues();
        // Loop the cursor
        while(cursor.moveToNext()){
            rowData.put(ProfileContract.Profile.COLUMN_NAME_EMAIL, cursor.getString(0));
            rowData.put(ProfileContract.Profile.COLUMN_NAME_NAME, cursor.getString(1));
            rowData.put(ProfileContract.Profile.COLUMN_NAME_ADDRESS, cursor.getString(2));
            rowData.put(ProfileContract.Profile.COLUMN_NAME_MARITAL, cursor.getInt(3));
            result.add(rowData);
        }

        if(!result.isEmpty()){
            for(Object data:result){
                n_et.setText(((ContentValues) data).get(ProfileContract.Profile.COLUMN_NAME_NAME).toString());
                a_et.setText(((ContentValues) data).get(ProfileContract.Profile.COLUMN_NAME_ADDRESS).toString());
                rg.check(((ContentValues) data).getAsInteger(ProfileContract.Profile.COLUMN_NAME_MARITAL));
                u_b.setEnabled(true);
                d_b.setEnabled(true);
            }
        }else {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_b(View v){
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProfileContract.Profile.COLUMN_NAME_NAME, n_et.getText().toString());
        values.put(ProfileContract.Profile.COLUMN_NAME_ADDRESS, a_et.getText().toString());
        values.put(ProfileContract.Profile.COLUMN_NAME_MARITAL, rg.getCheckedRadioButtonId());

        // Which row to update, based on the email
        String selection = ProfileContract.Profile.COLUMN_NAME_EMAIL + " LIKE ?";
        String[] selectionArgs = {e_et.getText().toString()};
        // update the row, returning number of rows affected
        long resCode = db.update(
                ProfileContract.Profile.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        if(resCode!=0){
           Toast.makeText(this, "record updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete_b(View v){
        String email = e_et.getText().toString();
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Define 'where' part of query
        String selection = ProfileContract.Profile.COLUMN_NAME_EMAIL + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {e_et.getText().toString()};
        // Issue SQL statement
        int deleteRows = db.delete(
                ProfileContract.Profile.TABLE_NAME,
                selection,
                selectionArgs
        );
        if(deleteRows > 0){
            resetV();
            Toast.makeText(this, deleteRows + " records deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No records deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset_b(View v){
        resetV();
    }

    public void resetV(){
        u_b.setEnabled(false);
        d_b.setEnabled(false);
        n_et.setText("");
        e_et.setText("");
        a_et.setText("");
        rg.clearCheck();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
