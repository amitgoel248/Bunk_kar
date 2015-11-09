package com.tutecentral.BUNK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;


public class MainActivity extends ActionBarActivity {

    DrawerLayout mylayout;
    MyDatabaseAdapter myhelper;
    private android.support.v7.widget.Toolbar toolbar;
    TextView cmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mylayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mylayout.closeDrawers();
        myhelper = new MyDatabaseAdapter(this);
        cmp = (TextView)findViewById(R.id.component);
        toolbar =(android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment =(NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);

        SharedPreferences prefA = getSharedPreferences("SUBJECTS", Context.MODE_PRIVATE);
        int size= prefA.getAll().size();
        String[] items = new String[size];
        int i=0;
        Map<String,?> allEntries = prefA.getAll();
        for (Map.Entry<String,?> entry : allEntries.entrySet()) {
            items[i] = entry.getValue().toString();
            i++;
        }
        ListAdapter myAdapter = new CustomAdaptor(this,items);
        ListView listView = (ListView) findViewById(R.id.mylist);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
                        String items = String.valueOf(parent.getItemAtPosition(position));
                        String[] arr = items.split(" ");
                        String data = myhelper.getSubjectData(arr[0]);
                        Intent intent = new Intent(MainActivity.this,main_components.class);
                        intent.putExtra("value",data);
                        startActivity(intent);

                    }
                }

        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
