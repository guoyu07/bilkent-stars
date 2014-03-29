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

public class GradesActivity extends Activity {
	private List<Grade> grades = new ArrayList<Grade>();
	
	ListView expListView;
	ListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grades);
		
		expListView = (ListView) findViewById(R.id.main_list);

		listAdapter = new GradeListAdapter(this, 0, grades);

		// setting list adapter
		new AsyncTaskClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grades, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/gradeAndAttend/grade.php");      
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
        	Elements gradeDiv = doc.select(".gradeDiv");
        	        	
        	Element header;
        	Elements rows;
        	for ( Element el : gradeDiv)
        	{
        		header = el.select("h4").get(0);
        		Grade g = new Grade("seperator", "", header.text());
        		grades.add(g);
        		rows = el.select("tr");
	    		for ( Element tr: rows)
	    		{
	    			Elements tds = tr.select("td");
	    			if ( tds.size() > 1)
	    			{
	    				g = new Grade(tds.get(0).text(), tds.get(3).text(),"");
	    				grades.add(g);
	    			}
	    		}
        	}
    		expListView.setAdapter(listAdapter);
        }
	}
}
