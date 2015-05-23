package com.idatafox.learnwizard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    int[] images={R.drawable.ic_action_collapse,R.drawable.ic_action_share,R.drawable.ic_action_search,R.drawable.ic_action_search,R.drawable.ic_action_search};
    ListView list;
    String[] memeTitles;
    String[] memeDescriptions;
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


      //call LoginAction
        if(LoginActivity.landStatus.equals("-1")||LoginActivity.landStatus.equals("-2")) {
            Intent intentLoginAction = new Intent(this, LoginActivity.class);
            startActivity(intentLoginAction);
        }





       //add Toolbar
        toolbar=(Toolbar)findViewById(R.id.topbar);
        setSupportActionBar(toolbar);

      //pagelistView

        Resources res=getResources();
        memeTitles=res.getStringArray(R.array.titles);
        memeDescriptions=res.getStringArray(R.array.descriptions);

        list=(ListView)findViewById(R.id.viewOne);





        VivzAdapter adapter=new VivzAdapter(this,memeTitles,images,memeDescriptions);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Toast.makeText(getBaseContext(),memeTitles[position],Toast.LENGTH_LONG).show();
                Intent intentIns=new Intent(getBaseContext(),MainPageActivity.class);
                startActivity(intentIns);


            }
        });


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

        if (id == R.id.exit) {

                LoginActivity.landStatus="-1";

                Intent intentLoginAction = new Intent(this, LoginActivity.class);
                startActivity(intentLoginAction);


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




class MyViewHolder
{
    ImageView myImage;
    TextView myTitle;
    TextView myDescription;

    MyViewHolder(View v)
    {
        myImage=(ImageView)v.findViewById(R.id.imageView);
        myTitle=(TextView)v.findViewById(R.id.textView);
        myDescription=(TextView)v.findViewById(R.id.textView2);
    }
}



class VivzAdapter extends ArrayAdapter<String>{

    Context context;
    int[] images;
    String[] titleArray;
    String[] desArray;
    VivzAdapter(Context c,String[] titles,int imgs[],String[] des)
    {
        super(c,R.layout.row_data,R.id.textView,titles);
        context=c;
        this.images=imgs;
        this.titleArray=titles;
        this.desArray=des;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        MyViewHolder holder=null;

        if(row==null)
        {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.row_data,parent,false);
            holder=new MyViewHolder(row);
            row.setTag(holder);
            Log.d("Vivz","createing a new row");
        }
        else
        {
            holder=(MyViewHolder)row.getTag();
            Log.d("Vivz", "Recycling stuff.");
        }



        //change Font color




        TextView text = (TextView) row.findViewById(R.id.textView);
        text.setTextColor(Color.BLACK);

        text = (TextView) row.findViewById(R.id.textView2);
        text.setTextColor(Color.BLACK);





        holder.myImage.setImageResource(images[position]);
        holder.myTitle.setText(titleArray[position]);
        holder.myDescription.setText(desArray[position]);


        return row;
    }





}
