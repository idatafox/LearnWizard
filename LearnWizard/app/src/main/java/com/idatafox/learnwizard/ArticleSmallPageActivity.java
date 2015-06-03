package com.idatafox.learnwizard;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ArticleSmallPageActivity extends ActionBarActivity {

    String arc_stxt;
    String arc_title;
    String arc_content;
    String arc_time;
    String arc_author;


    TextView tvTitle;
    TextView tvAuthor;
    TextView tvTime;
    TextView tvContent;
    TextView tvShortText;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_small_page);


        toolbar=(Toolbar)findViewById(R.id.topbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        //setup arc_title
        SharedPreferences mSharedPre=getSharedPreferences("smallPage",MODE_PRIVATE);
        arc_title=mSharedPre.getString("arc_title","NotFind");
        arc_author=mSharedPre.getString("arc_author","NotFind");
        arc_time=mSharedPre.getString("arc_time","NotFind");

        toolbar.setTitle(arc_title);

        _("returned arc_title:" + arc_title);

        //get TextViews

        tvTitle=(TextView)findViewById(R.id.textView6);
        tvAuthor=(TextView)findViewById(R.id.textView7);
        tvTime=(TextView)findViewById(R.id.textView9);
        tvContent=(TextView)findViewById(R.id.textView10);

        tvTitle.setText(arc_title);
        tvAuthor.setText(arc_author);
        tvTime.setText(arc_time);
        tvContent.setText("请稍后更在读取...");


       ;

        new MyAsyncTask().execute(tvTitle.getText().toString());//CALLING SERVLET GET DATA FROM SERVER.



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_small_page, menu);
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



    class MyAsyncTask extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

            BufferedReader bufferedReader=null;
            StringBuffer stringBuffer=new StringBuffer("");

            //put data into memeTitles,images,memeDescription variables
            HttpClient httpClient=new DefaultHttpClient();
            String url="http://www.idatafox.com/android/common/displayArticle";
            HttpPost post=new HttpPost(url);
            List<NameValuePair> postParameters=new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("arc_title",params[0]));

            try {
                UrlEncodedFormEntity entity=new UrlEncodedFormEntity(postParameters, HTTP.UTF_8);
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
                arc_content=stringBuffer.toString();



              //  String bb="{article:[{\"arc_tile\":\"什么是liferay\",\"arc_author\":\"admin\",\"arc_time\":\"2014-6-17 21:30:56\",\"arc_boby\":\"dd\"}]}";



                 //   parseResponse(stringBuffer.toString());


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

             //set TextView values
            tvContent.setText(Html.fromHtml(arc_content));


        }

        private void _a(String s){
             Log.d(ArticleSmallPageActivity.MyAsyncTask.class.getSimpleName(), s);
        }
    }




    private void _(String s){
        Log.d(MainActivity.class.getSimpleName(), s);
    }




    private void parseResponse(String response)
    {

        JSONObject jo= null;
        try {
            jo = new JSONObject(response);
            JSONArray jsonpost= jo.getJSONArray("article");
            _("jsonPost length:" + jsonpost.length());
            JSONObject fjo=jsonpost.getJSONObject(0);





                arc_title=fjo.getString("arc_tile");
                arc_author=fjo.getString("arc_author");
                arc_time=fjo.getString("arc_time");
                arc_stxt=fjo.getString("shorttxt");


                _("arc_title=="+arc_title+"arc_author=="+arc_author+"arc_time="+arc_time+"arc_shorttxt="+arc_stxt.substring(0,5));






        } catch (JSONException e) {
            e.printStackTrace();
        }}



}
