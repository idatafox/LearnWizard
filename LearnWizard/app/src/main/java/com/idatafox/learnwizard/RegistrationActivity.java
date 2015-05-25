package com.idatafox.learnwizard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


public class RegistrationActivity extends ActionBarActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    EditText mobileNumber;
    EditText userPassword;
    EditText userPasswordC;
    EditText userCity;
    EditText userQQ;
    EditText userEmail;
    EditText vCode;

    Button button;

    String resValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mobileNumber = (EditText) findViewById(R.id.editText);
        userPassword = (EditText) findViewById(R.id.editText2);
        userPasswordC = (EditText) findViewById(R.id.editText3);
        userCity = (EditText) findViewById(R.id.editText5);
        userQQ = (EditText) findViewById(R.id.editText4);
        userEmail = (EditText) findViewById(R.id.editText6);
        vCode = (EditText) findViewById(R.id.editText7);

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _("started onClick");
                new MyTask().execute();
            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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


    class MyTask extends AsyncTask<Void, Void, Void> {
        public  final String TAG = MyTask.class.getSimpleName();
        @Override
        protected Void doInBackground(Void... params) {

            String urlPattern="http://www.idatafox.com/android/common/regNewUser";
            _a("stared doInBackground ...");

            getJson(urlPattern);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(resValue.indexOf("error")==-1)
            {
                Intent intent=new Intent(getBaseContext(),LoginActivity.class);
                startActivity(intent);
            }
            else
            {
                mobileNumber.setText("手机号码－您输入的手机号码可能有误");
                vCode.setText("微信验证-您输入的验证码可能有误");
            }
        }

        private void _a(String s){
            Log.d(TAG, s);
        }
    }


    private void getJson(String url){

        BufferedReader bufferedReader=null;
        StringBuffer stringBuffer=new StringBuffer("");


        HttpClient httpClient=new DefaultHttpClient();
        HttpPost post=new HttpPost(url);
        List<NameValuePair> postParameters=new ArrayList<NameValuePair>();




        postParameters.add(new BasicNameValuePair("mobileNumber",mobileNumber.getText().toString()));
        postParameters.add(new BasicNameValuePair("userPassword",userPassword.getText().toString()));
       // postParameters.add(new BasicNameValuePair("userPasswordC",userPasswordC.toString()));
        postParameters.add(new BasicNameValuePair("userCity",userCity.getText().toString()));
        postParameters.add(new BasicNameValuePair("userQQ",userQQ.getText().toString()));
        postParameters.add(new BasicNameValuePair("userEmail",userEmail.getText().toString()));
        postParameters.add(new BasicNameValuePair("vCode", vCode.getText().toString()));


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
            resValue=stringBuffer.toString();
            _("stringBuffer==="+stringBuffer.toString());

           // decodeResultIntoJson(stringBuffer.toString());


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

    private void decodeResultIntoJson(String response)
    {

        JSONObject jo= null;
        try {
            jo = new JSONObject(response);
            JSONArray jsonpost= jo.getJSONArray("user");
            _("jsonPost length:" + jsonpost.length());
            JSONObject fjo=jsonpost.getJSONObject(1);

            String name=fjo.getString("name");
            String password=fjo.getString("password");
            _("name=="+name+"password=="+password);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void _(String s) {
        Log.d(TAG, s);
    }


}
