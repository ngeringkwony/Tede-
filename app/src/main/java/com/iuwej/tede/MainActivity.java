package com.iuwej.tede;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    public static final String MESSAGE="IBM CyberSecurity training scheduled to run from today  to 24 July(Venue Hall 7 4th flr IBM lab) 5pm Avail yourself for further info. Acknowledge receipt";

    private ListView lst;
    private ArrayAdapter<String> adapter;
    private ProgressDialog dialog;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lst=(ListView)findViewById(R.id.lst_view);
        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        lst.setAdapter(adapter);

        Button btnTede=(Button)findViewById(R.id.btn_tede);

        btnTede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendSms(data,MESSAGE);
                Toast.makeText(MainActivity.this,"Tedeing them all",Toast.LENGTH_LONG).show();

            }
        });

         new FetchAsync().execute();
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

    private ArrayList<String> readContacts(){

        ArrayList<String> list= new ArrayList<>();

        try {
            InputStream inputStream=getAssets().open("contacts.tx");
            BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream));
            while(true){
                String current= reader.readLine();
                if(current==null  || current.isEmpty())
                    break;
                if(current.startsWith("7"))
                    current="0"+current;
                current.trim();
                list.add(current);
            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void sendSms(List<String> contacts, String message){

        SmsManager smsManager=SmsManager.getDefault();
        for(String contact: contacts){
            smsManager.sendTextMessage(contact,"",message,null,null);
        }
    }

    class FetchAsync extends AsyncTask<Void,Void,List<String>>{


        @Override
        protected List<String> doInBackground(Void... voids) {
            return readContacts();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=ProgressDialog.show(MainActivity.this,"TEDE!","Loading contacts ....");
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            dialog.dismiss();
            data=strings;
            adapter.addAll(strings);
            adapter.notifyDataSetChanged();

        }
    }

}
