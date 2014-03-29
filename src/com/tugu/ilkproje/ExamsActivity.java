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
import android.widget.TextView;

public class ExamsActivity extends Activity {

	private List<Exam> exams = new ArrayList<Exam>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exams);
		
		new AsyncTaskClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exams, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/exam/index.php");      
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
        	Elements div = doc.select(".corner");
        	       	
        	//String result = div.toString();
        	        	
        	Element header;
        	Element table;
        	Elements rows;
        	for ( Element el : div)
        	{
        		header = el.select("h1").get(0);
        		Exam e;
        		Elements divs = el.select("div");
        		table = divs.get(3);
        		
        		rows = table.select(".bold");
        		e = new Exam(header.text(), rows.get(0).text(), rows.get(1).text(),rows.get(3).text());
				exams.add(e);
        		
        	}
        	String result="";
        	for(int i = 0; i<exams.size(); i++)
        	{
        		result += exams.get(i).course +"  "+ exams.get(i).date +"  "+ exams.get(i).time +"  "+ exams.get(i).classroom+"\n";
        	}
        	final String y = result;
        	
        	TextView text = (TextView) findViewById(R.id.examText);
        	text.setText(y);
        }
	}
}
