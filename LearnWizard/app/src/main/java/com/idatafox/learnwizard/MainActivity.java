package com.idatafox.learnwizard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] funcs;
    private ActionBarDrawerToggle drawerListener;


    private Toolbar toolbar;
    private Toolbar secondToolbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //add Toolbar
        toolbar=(Toolbar)findViewById(R.id.topbar);
        setSupportActionBar(toolbar);



       //add bottom Toolbar

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.aaa);
        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_settings:
                        // TODO
                        break;
                    // TODO: Other cases
                }
                return true;
            }
        });
        toolbarBottom.inflateMenu(R.menu.menu_bottom);



        //drawerlistener

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        drawerListener=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(MainActivity.this, "drawer openned", Toast.LENGTH_LONG).show();
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(MainActivity.this,"drawer closed",Toast.LENGTH_LONG).show();
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerListener);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerListener.setHomeAsUpIndicator(R.drawable.ic_drawer);




        //second Full ListView

        listView=(ListView)findViewById(R.id.funcList);
        funcs=getResources().getStringArray(R.array.funcationsNameList);

        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,funcs));
        listView.setOnItemClickListener(this);





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


        if(drawerListener.onOptionsItemSelected(item))
        {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("idatafox.com", "calling onItemClick...");
        Toast.makeText(this,funcs[position], Toast.LENGTH_LONG).show();
        selectItem(position);

    }

    public  void selectItem(int position) {

        listView.setItemChecked(position,true);
        setTitleA(funcs[position]);
        if(funcs[position].equals("Liferay在线课堂"))
        {
            Log.d("idatafox.com","calling Liferay classroom...");
           // Intent intent=new Intent(this,LiferayLearnActivity.class);
           // startActivity(intent);
        }
    }

    public void setTitleA(String title){

        getSupportActionBar().setTitle(title);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

}
