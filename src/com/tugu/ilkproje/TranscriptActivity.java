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
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TranscriptActivity extends Activity {

	private List<Transcript> transcript = new ArrayList<Transcript>();
	ListView expListView;
	ListAdapter listAdapter;
	JSONObject obj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transcript);
		
		expListView = (ListView) findViewById(R.id.main_list);
		listAdapter = new TranscriptListAdapter(this, 0, transcript);
		
		new AsyncTaskClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transcript, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/transcript.php");      
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
        	Elements printMode = doc.select(".printMod");
        	
        	//String result ="";
        	int skip=0;
        	
        	for ( Element el : printMode)
        	{
        		skip++;
        		// skip first 3 .printMode div
        		if(skip < 4) continue;
        		
        		Transcript a;
        		
        		String semester = el.select("h3").text();
        		a = new Transcript("semester", semester, "", "", "", "");
        		transcript.add(a);
        		try {
					obj.putOpt("semester", a);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		Elements row1;
        		row1 = el.select(".row1, .row2");
        		Elements tds1;
        		
    			for ( Element r1: row1)
	    		{
    				tds1 = r1.select("td");
    				if(tds1.size() > 2)
    				{
    					//result +=  "row1:: " + tds1.get(0).text() + "\n";
    					a = new Transcript(tds1.get(0).text(),tds1.get(1).text(),tds1.get(2).text(),tds1.get(3).text(),tds1.get(4).text(), tds1.get(5).text());
    					transcript.add(a);
    					try {
							obj.putOpt("ders", a);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
	    		}
    			
    			Elements gparows = el.select(".nobottom");
    			if(gparows.size() < 3) continue;
    			Element row = gparows.get(gparows.size()-3);
    			
    			String gpa = row.select("tr").get(0).select("td").get(1).text();
    			String cgpa = row.select("tr").get(1).select("td").get(1).text();
    			
    			String standing = "";
    			if(row.select("tr").size() > 2 && row.select("tr").get(2).select("td").size() > 1)
    				standing = row.select("tr").get(2).select("td").get(1).text();
    			
    			a = new Transcript("gpa", gpa, cgpa, standing, "", "");
    			transcript.add(a);
    			try {
					obj.putOpt("gpa", a);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
        	}
        	
        	/*for(int i =0; i<transcript.size(); i++)
        	{
        		result += transcript.get(i).code + transcript.get(i).name + transcript.get(i).grade + transcript.get(i).credit + transcript.get(i).gradepoint + transcript.get(i).repeating + "\n";
        	}*/
        	
        	String result = "";
        	for(int i = 0; i<obj.length(); i++)
        	{
        		result += obj.optString("ders");
        	}
        	final String y = result;
        	
        	/*TextView text = (TextView) findViewById(R.id.transcriptText);
        	text.setText(y);*/
        	
        	Context context = getApplicationContext();
        	int duration = Toast.LENGTH_LONG;

        	Toast toast = Toast.makeText(context, y, duration);
        	toast.show();
        	
        	expListView.setAdapter(listAdapter);
        }
	}


}
