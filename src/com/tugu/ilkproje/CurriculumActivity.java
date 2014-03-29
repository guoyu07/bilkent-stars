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

public class CurriculumActivity extends Activity {

	private List<Curriculum> curriculum = new ArrayList<Curriculum>();
	ListView expListView;
	ListAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curriculum);
		
		expListView = (ListView) findViewById(R.id.main_list);
		listAdapter = new CurriculumListAdapter(this, 0, curriculum);
		
		new AsyncTaskClass().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.curriculum, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/curriculum.php");      
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
        	int year = 0;
        	
        	for ( Element el : printMode)
        	{
        		skip++;
        		// skip first 2 .printMode div
        		if(skip < 3) continue;
        		
        		Curriculum c;
        		
        		String semester = el.select("h3").text();
        		
        		if("".equals(semester))
        			break;
        		
        		if("Fall Semester".equals(semester))
        			year++;
        		c = new Curriculum("semester", "Year " + year + " " + semester, "", "", "", "", "");
        		curriculum.add(c);
        		Elements tds;
        		
        		Elements rows = el.select("tr");
	    		for ( Element tr: rows)
	    		{
	    			tds = tr.select("td");
					if(tds.size() > 2)
					{
						if("".equals(tds.get(2).text()))
							continue;
						c = new Curriculum(tds.get(0).text(),tds.get(1).text(),tds.get(2).text(),tds.get(3).text(),tds.get(4).text(), tds.get(5).text(), tds.get(6).text());
						curriculum.add(c);
					}
	    		}
        	}
        	
        	/*for(int i = 0; i<curriculum.size(); i++)
        	{
        		result += curriculum.get(i).code + " " + curriculum.get(i).name + " " + curriculum.get(i).status + " " + curriculum.get(i).grade + " "+ curriculum.get(i).credit + " " + curriculum.get(i).semester + " " + curriculum.get(i).insteadOf + "\n";
        	}
        	final String y = result;*/
        	
        	//TextView text = (TextView) findViewById(R.id.transcriptText);
        	//text.setText(y);
        	
        	/*Context context = getApplicationContext();
        	int duration = Toast.LENGTH_LONG;

        	Toast toast = Toast.makeText(context, y, duration);
        	toast.show();*/
        	
        	expListView.setAdapter(listAdapter);
        }
	}

	
}
