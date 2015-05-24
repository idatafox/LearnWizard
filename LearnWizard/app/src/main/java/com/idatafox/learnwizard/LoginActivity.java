package com.idatafox.learnwizard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginActivity extends ActionBarActivity {

    public static String landStatus="-1";
    EditText userName;
    EditText userPassword;
    Button button;
    TextView tView;
    Context c;
    TextView tStart;
    TextView tContract;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        c=this;
        userName=(EditText)findViewById(R.id.userName);
        userPassword=(EditText)findViewById(R.id.userPassword);
        tView=(TextView)findViewById(R.id.textView3);
        tStart=(TextView)findViewById(R.id.textView3);


        tStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegAction = new Intent(getBaseContext(),RegistrationActivity.class);
                startActivity(intentRegAction);

            }
        });

        tContract=(TextView)findViewById(R.id.textView4);

        tContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"正在连接服务器，请稍后...",Toast.LENGTH_LONG).show();
                Intent intentReadAction = new Intent(getBaseContext(),ReadMeActivity.class);
                startActivity(intentReadAction);

            }
        });


        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int validDataFlag=0;

                if((userName.getText().toString().length()==0))
                {
                    Toast.makeText(getBaseContext(),"请输入你的用户名。",Toast.LENGTH_LONG).show();
                    validDataFlag=-1;
                }
                if((userPassword.getText().toString().length()==0))
                {
                    Toast.makeText(getBaseContext(),"请输入你的密码。",Toast.LENGTH_LONG).show();
                    validDataFlag=-1;
                }

                if(validDataFlag==-1)
                {
                    return ;
                }
                else
                {
                    Toast.makeText(getBaseContext(),userName.getText().toString()+":正在验证你的账户，请稍后...",Toast.LENGTH_LONG).show();

                    //if allow user to land system ...

                     new LoginAsyncTask().execute();




                }

            }
        });


    }


    //custom AsyncTask
    class LoginAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {


            String url="http://www.idatafox.com/android/common/login";

            _a("stared doInBackground ...");

            doValidateUserDetail(url);



            return null;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(landStatus.equals("-2")) {
                tView.setText("用户/密码错误，请重新登录");
            }
            else
            {
                tView.setText("登录状态:"+landStatus);
            }


            if(LoginActivity.landStatus.equals("0"))
            {
                SharedPreferences spre=getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor=spre.edit();
                editor.putString("username", userName.getText().toString());
                Intent intent=new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }
        }

        private void _a(String s){
            Log.d(LoginAsyncTask.class.getSimpleName(), s);
        }
    }


    //validate user's detail
    private void doValidateUserDetail(String url){

        BufferedReader bufferedReader=null;
        StringBuffer stringBuffer=new StringBuffer("");


        HttpClient httpClient=new DefaultHttpClient();
        HttpPost post=new HttpPost(url);
        List<NameValuePair> postParameters=new ArrayList<NameValuePair>();
        String aa=userName.getText().toString();
        Log.d(LoginActivity.class.getSimpleName(),"aa="+aa);
        postParameters.add(new BasicNameValuePair("userName",userName.getText().toString()));
        postParameters.add(new BasicNameValuePair("userName", aa));
        postParameters.add(new BasicNameValuePair("userPassword",userPassword.getText().toString()));


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
            _("stringBuffer==="+stringBuffer.toString());
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


    }

    //parseResponse
    private void parseResponse(String response)
    {

        JSONObject jo= null;
        try {
            jo = new JSONObject(response);
            JSONArray jsonpost= jo.getJSONArray("user");
            _("jsonPost length:" + jsonpost.length());
            JSONObject fjo=jsonpost.getJSONObject(0);

            String name=fjo.getString("userName");
            String landStatus=fjo.getString("landStatus");
            _("name=="+name+"landStatus=="+landStatus);

            if(landStatus.equals("0"))
            {
                 toast("starting loging....");
                 this.landStatus="0";
            }
            else
            {
               // Toast.makeText(getBaseContext(),"用户名或密码错误，请重新登录.",Toast.LENGTH_LONG).show();
                 this.landStatus="-2";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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


    private void _(String s){
        Log.d(LoginActivity.class.getSimpleName(), s);
    }



    public void toast(final String s)
    {
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(c,s,Toast.LENGTH_LONG).show();
                    }
                }
        );


    }


}
