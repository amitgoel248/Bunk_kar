package com.tutecentral.BUNK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLData;
import java.sql.SQLTransactionRollbackException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class schedule extends AppCompatActivity {

    MyDatabaseAdapter myhelper;
    private android.support.v7.widget.Toolbar toolbar;
    TableLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        toolbar =(android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        ll = (TableLayout)findViewById(R.id.tableLayoutList);

        myhelper = new MyDatabaseAdapter(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment =(NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        fetchdetails();
    }

    public void adddetails(View view) {
        ArrayList<String> cc = new ArrayList<String>();
        String dat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        cc.add(0, dat);
        int size = ll.getChildCount();
        for(int h=0;h<size;h++)
        {
            View v = ll.getChildAt(h);
            RadioGroup rgp  =(RadioGroup)v.findViewById(R.id.myRadio);
            if(rgp.getCheckedRadioButtonId()!=-1){
                int id= rgp.getCheckedRadioButtonId();
                View radioButton = rgp.findViewById(id);
                int radioId = rgp.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) rgp.getChildAt(radioId);
                String selection = (String) btn.getText();
                if(selection.equals("Attended"))
                {
                    cc.add(h+1,"1");
                }
                else if(selection.equals("Not Attended                                                                           "))
                {
                    cc.add(h+1,"-1");
                }
                else
                    cc.add(h+1,"0");
            }
        }

        long id = myhelper.insertData(cc);
        if(id<0) {
            Message.message(this,"Unsuccessful");
        }
        else
        {
            Message.message(this,"Successfully inserted a row");
        }

        Intent intent  = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void fetchdetails()
    {
        SharedPreferences prefA = getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        int size= prefA.getAll().size();
        String[] items = new String[size];
        int i=0;
        Map<String,?> allEntries = prefA.getAll();
        for (Map.Entry<String,?> entry : allEntries.entrySet()) {
            items[i] = entry.getValue().toString();
            i++;
        }


        for(int k=0;k<items.length;k++)
        {
            String[] arr = items[k].split(" ");
            TableRow mTableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.tablerow, null);
            TextView tt = (TextView)mTableRow.findViewById(R.id.heading);
            tt.setText(arr[0]);
            mTableRow.setTag(k + 1);
            ll.addView(mTableRow);
        }
    }

    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
