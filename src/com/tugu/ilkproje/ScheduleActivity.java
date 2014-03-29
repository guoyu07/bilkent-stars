package com.tugu.ilkproje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class ScheduleActivity extends Activity {
	
	private List<Lecture> lectures = new ArrayList<Lecture>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		new AsyncTaskClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/home.php");      
            try {
	            HttpResponse response = GlobalConstants.client.execute(httpget);
	            HttpEntity entity = response.getEntity();
	            InputStream is = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) // Read line by line
	                sb.append(line + "\n");

	            String res = sb.toString(); // Result is here
	            is.close(); // Close the stream
	            return res;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return "";
        }

        @Override
        protected void onPostExecute( String s) {
        	
        	Elements rows;        	
        	String result="";
        	
        	Document doc = Jsoup.parse(s);
        	Elements gradeDiv = doc.select(".DHTMLSuite_paneContentInner");
        	        	
        	Element header;
        	for ( Element el : gradeDiv)
        	{
        		header = el.select("tr").get(0);
        		header = header.select("td").get(0);
        		Lecture l = new Lecture("seperator", "", "", "", "");
        		lectures.add(l);
        		rows = el.select("tbody");
	    		for ( Element tr: rows)
	    		{
	    			Elements trs = tr.select("tr");
	    			for(Element tr2: trs)
	    			{
	    				Elements tds = tr2.select("td");
	    				l = new Lecture(tds.get(0).text(), tds.get(3).text(),"","","");
	    				lectures.add(l);
	    			}
	    		}
        	}
        	
        	
        	final String y = result;
        	Context context = getApplicationContext();
        	int duration = Toast.LENGTH_SHORT;

        	Toast toast = Toast.makeText(context, y, duration);
        	toast.show();
    		//expListView.setAdapter(listAdapter);
        }
	}


}
