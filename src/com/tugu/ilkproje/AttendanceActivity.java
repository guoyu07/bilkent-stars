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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;

public class AttendanceActivity extends Activity {
	
	private List<Attendance> attendances = new ArrayList<Attendance>();

	ListView expListView;
	ListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
		
		expListView = (ListView) findViewById(R.id.main_list);

		listAdapter = new AttendanceListAdapter(this, 0, attendances);
		
		new AsyncTaskClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendance, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/gradeAndAttend/attend.php");      
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
        	
        	Document doc = Jsoup.parse(s);
        	Elements attendDiv = doc.select(".attendDiv");
        	        	
        	Element header;
        	Elements rows;
        	
        	for ( Element el : attendDiv)
        	{
        		header = el.select("h4").get(0);
        		Attendance a = new Attendance("seperator", "", header.text());
        		attendances.add(a);
        		rows = el.select("tr");
	    		for ( Element tr: rows)
	    		{
	    			Elements tds = tr.select("td");
	    			if ( tds.size() > 1)
	    			{
	    				a = new Attendance(tds.get(0).text(), tds.get(1).text(),tds.get(2).text());
	    				attendances.add(a);
	    			}
	    		}
        	}

        	expListView.setAdapter(listAdapter);
        }
	}


}
