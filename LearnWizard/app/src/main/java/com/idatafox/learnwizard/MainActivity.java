package com.idatafox.learnwizard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener{

    int[] images={R.drawable.ic_action_collapse,R.drawable.ic_action_share,R.drawable.ic_action_search,R.drawable.ic_action_search,R.drawable.ic_action_search};
    ListView list;

    int imageOne=R.drawable.ic_action_share;//temp for test ,formal pattern is images object.

    String[] memeTitles;
    String[] memeDescriptions;
    String[] arc_authorA_array;
    String[] arc_timeA_array;
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


        /* Move these code into MyAsyncTask class !

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
        });*/

         //initialize list of ListView that display articles

             MyAsyncTask myAsyncTask=new MyAsyncTask();
             myAsyncTask.execute("androi-course-a");



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


    /**
     * define MyAsyncTask extends AsyncTask class
     * 1) retrieve data from server url using post method mode.
     * 2) parse returned data ,get three arrays of titles,authors,shorttxt
     * 3) call listView's instance list's setAdapter method initials it.
     */


    class MyAsyncTask extends AsyncTask<String,Void,Void>{


        @Override
        protected void onPreExecute() {

            list=(ListView)findViewById(R.id.viewOne);
            _a("initialize ListView's instance list");
            Resources res=getResources();
            memeTitles=res.getStringArray(R.array.titles);
            memeDescriptions=res.getStringArray(R.array.descriptions);


        }

        @Override
        protected Void doInBackground(String... params) {



            BufferedReader bufferedReader=null;
            StringBuffer stringBuffer=new StringBuffer("");

            //put data into memeTitles,images,memeDescription variables
            HttpClient httpClient=new DefaultHttpClient();
            String url="http://www.idatafox.com/android/common/listJSonArticle";
            HttpPost post=new HttpPost(url);
            List<NameValuePair> postParameters=new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("arc_type",params[0]));

            try {
                UrlEncodedFormEntity entity=new UrlEncodedFormEntity(postParameters);
                post.setEntity(entity);
                HttpResponse response=httpClient.execute(post);
                bufferedReader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line="";
                String lineSeparator=System.getProperty("line.separator");
                while((line=bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(line+lineSeparator);

                }
                bufferedReader.close();
                _a("stringBuffer===" + stringBuffer.toString());
                 parseResponse(stringBuffer.toString());


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            final VivzAdapter adapter=new VivzAdapter(getBaseContext(),memeTitles,images,memeDescriptions);

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Toast.makeText(getBaseContext(),memeTitles[position],Toast.LENGTH_LONG).show();
                    Intent intentIns=new Intent(getBaseContext(),ArticleSmallPageActivity.class);
                    SharedPreferences aSharedPre=getApplication().getSharedPreferences("smallPage",MODE_PRIVATE);
                    SharedPreferences.Editor editor=aSharedPre.edit();
                    editor.putString("arc_title",memeTitles[position]);
                    editor.putString("arc_author",arc_authorA_array[position]);
                    editor.putString("arc_time",arc_timeA_array[position]);
                    editor.commit();
                    startActivity(intentIns);


                }
            });
            _a("VivzAdapter initialized ,listView started displaying articles.");
        }

        private void _a(String s){
            Log.d(MyAsyncTask.class.getSimpleName(), s);
        }
    }


    private void _(String s){
        Log.d(MainActivity.class.getSimpleName(), s);
    }

    //parseResponse:get article detail.

    private void parseResponse(String response)
    {

        JSONObject jo= null;
        try {
            jo = new JSONObject(response);
            JSONArray jsonpost= jo.getJSONArray("article");
            _("jsonPost length:" + jsonpost.length());
            JSONObject fjo=jsonpost.getJSONObject(0);

            String arc_title="";
            String arc_author="";
            String arc_time="";
            String arc_shorttxt="";
            ArrayList<String> arc_title_array=new ArrayList<String>();
            ArrayList<String> arc_author_array=new ArrayList<String>();
            ArrayList<String> arc_time_array=new ArrayList<String>();
            ArrayList<String> arc_shorttxt_array=new ArrayList<String>();

            for(int i=0;i<=jsonpost.length()-1;i++)
            {
                fjo=jsonpost.getJSONObject(i);
                arc_title=fjo.getString("arc_tile");
                arc_author=fjo.getString("arc_author");
                arc_time=fjo.getString("arc_time");
                arc_shorttxt=fjo.getString("shorttxt");
                arc_title_array.add(arc_title);
                arc_author_array.add(arc_author);
                arc_time_array.add(arc_time);
                arc_shorttxt_array.add(arc_shorttxt);

                _("arc_title=="+arc_title+"arc_author=="+arc_author+"arc_time="+arc_time+"arc_shorttxt="+arc_shorttxt.substring(0,5));


            }


            memeTitles=(String[])arc_title_array.toArray(new String[arc_title_array.size()]);
            memeDescriptions=(String[])arc_shorttxt_array.toArray(new String[arc_shorttxt_array.size()]);
            arc_authorA_array=(String[])arc_author_array.toArray(new String[arc_author_array.size()]);
            arc_timeA_array=(String[])arc_time_array.toArray(new String[arc_time_array.size()]);



        } catch (JSONException e) {
            e.printStackTrace();
        }

          _("parseResponse finished");

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





        holder.myImage.setImageResource(R.drawable.ic_action_share);
        holder.myTitle.setText(titleArray[position]);
        holder.myDescription.setText(desArray[position]);


        return row;
    }





}
