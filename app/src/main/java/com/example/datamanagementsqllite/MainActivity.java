package com.example.datamanagementsqllite;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager db;
    private EditText e_et, n_et, a_et;
    private RadioGroup rg;
    private Button u_b, d_b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(this);
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
        String email, name, address;
        int mstatus;
        email = e_et.getText().toString();
        name = n_et.getText().toString();
        address = a_et.getText().toString();
        mstatus = rg.getCheckedRadioButtonId();
        long resCode = db.addRecord(email, name, address, mstatus);
        if (resCode == -1) {
            Toast.makeText(this, "An error occured OR record already existed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "record id: " + resCode + " added", Toast.LENGTH_SHORT).show();
        }
    }

    public void search_b(View v){
        String email = e_et.getText().toString();
        Cursor cursor = db.searchRecord(email);
        if (cursor.moveToNext()) {
            n_et.setText(cursor.getString(1));
            a_et.setText(cursor.getString(2));
            int maritalStatus = cursor.getInt(3);
            rg.check(maritalStatus);
            u_b.setEnabled(true);
            d_b.setEnabled(true);
        } else {
            Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_b(View v){
        String email, name, address;
        int mstatus;
        email = e_et.getText().toString();
        name = n_et.getText().toString();
        address = a_et.getText().toString();
        mstatus = rg.getCheckedRadioButtonId();
        int resCode = db.updateRecord(email, name, address, mstatus);

        Toast.makeText(this, resCode + " records updated", Toast.LENGTH_SHORT).show();
    }

    public void delete_b(View v){
        String email = e_et.getText().toString();
        int resCode = db.deleteRecord(email);
        if(resCode > 0){
            resetV();
            Toast.makeText(this, resCode + " records deleted", Toast.LENGTH_SHORT).show();
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
        super.onDestroy();
        db.closeDatabase();
    }
}
