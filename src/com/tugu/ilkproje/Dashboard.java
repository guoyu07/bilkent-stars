package com.tugu.ilkproje;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class Dashboard extends Activity {
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		prefs = getSharedPreferences("user_info",MODE_PRIVATE);
		
		
		
		if(!GlobalConstants.isLoggedIn)
		{
			new LoginTask().execute();
		} else {
			new AsyncTaskClass().execute();
		}
		
		
		// Grades
        final BootstrapButton grades = (BootstrapButton) findViewById(R.id.grades_button);
        grades.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), GradesActivity.class);
      	      	startActivity(i);
            	
            }
        });
        
        final BootstrapButton exams = (BootstrapButton) findViewById(R.id.exams_button);
        exams.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), ExamsActivity.class);
      	      	startActivity(i);
            }
        });
        
        final BootstrapButton attendance = (BootstrapButton) findViewById(R.id.attendance_button);
        attendance.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), AttendanceActivity.class);
      	      	startActivity(i);
            }
        });
        
        final BootstrapButton schedule = (BootstrapButton) findViewById(R.id.schedule_button);
        schedule.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), ScheduleActivity.class);
      	      	startActivity(i);
            }
        });
        
        final BootstrapButton information = (BootstrapButton) findViewById(R.id.information_button);
        information.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), InformationActivity.class);
      	      	startActivity(i);
            }
        });
        
        final BootstrapButton transcript = (BootstrapButton) findViewById(R.id.transcript_button);
        transcript.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), TranscriptActivity.class);
      	      	startActivity(i);
            }
        });
        
        final BootstrapButton curriculum = (BootstrapButton) findViewById(R.id.curriculum_button);
        curriculum.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	Intent i = new Intent(v.getContext(), CurriculumActivity.class);
      	      	startActivity(i);
            }
        });
        
        final BootstrapButton logout = (BootstrapButton) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	
            	GlobalConstants.isLoggedIn = false;
            	Editor editor = prefs.edit();
            	editor.putBoolean("isLoggedIn", false);
            	editor.commit();
            	Intent i = new Intent(v.getContext(), MainActivity.class);
      	      	startActivity(i);
      	      	finish();
            }
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
	
	class AsyncTaskClass extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }

        @Override
        protected String doInBackground(String... strings) {
        	HttpGet httpget = new HttpGet("https://stars.bilkent.edu.tr/srs/ajax/userInfo.php");      
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
        	String result = "";
        	
        	/*editor.putString("name", "abcdefg");
        	editor.putString("surname", "");
        	editor.putString("department", "");
        	editor.putString("id", "");
        	editor.putString("cumgpa", "");
        	editor.putString("standing", "");
        	editor.putString("class", "");
        	editor.commit();
        	
        	if(prefs.getString("name", null) != null)
        	{
        		result += prefs.getString("name", null) + "\n";
        		result += prefs.getString("surname", null) + "\n";
        		result += prefs.getString("department", null) + "\n";
        		result += prefs.getString("id", null) + "\n";
        		result += prefs.getString("cumgpa", null) + "\n";
        		result += prefs.getString("standing", null) + "\n";
        		result += prefs.getString("class", null) + "\n";
        	}
        	else
        	{
        		
	        	Document doc = Jsoup.parse(s);
	        	Element fieldset = doc.select("fieldset").get(0);
	        	 
	        	Element tr0 = fieldset.select("tr").get(0);
	        	Element td1 = tr0.select("td").get(1);
	        	Elements info = td1.select("tr");
	        	int i = 0;
	        	for(Element tr: info)
	        	{
	        		if( i == 2 || i == 4) continue;
	        		Elements td = tr.select("td");
	        		ar.add(td.get(0).text());
	        		i++;
	        	}
	        	
	        	Element cgpatable = doc.select(".standard").get(0);
	        	Elements td21 = cgpatable.select("tr");
	        	
	        	for(Element tr: td21)
	        	{
	        		Element td = tr.select("td").get(1);
	        		ar.add(td.text());
	        	}
	        	
	        	/*editor.putString("name", ar.get(0));
	        	editor.putString("surname", ar.get(1));
	        	editor.putString("department", ar.get(2));
	        	editor.putString("id", ar.get(3));
	        	editor.putString("cumgpa", ar.get(4));
	        	editor.putString("standing", ar.get(5));
	        	editor.putString("class", ar.get(6));
	        	editor.commit();
	        	
	        	for(int j =0; j<ar.size(); j++)
	        	{
	        		result += ar.get(i) + "\n";
	        	}
        	}*/
	        	
        	TextView studentinfo = (TextView)findViewById(R.id.studentinfo);
        	studentinfo.setText(result);
        }
	}
	
	
	public class LoginTask extends AsyncTask<String, String, String> {
		
        private String readResponse(HttpResponse response) throws IOException {
		        InputStream in = response.getEntity().getContent();
		        BufferedInputStream bis = new BufferedInputStream(in);
		        ByteArrayOutputStream buf = new ByteArrayOutputStream();
		        int result = bis.read();
		        while (result != -1) {
		                byte b = (byte) result;
		                buf.write(b);
		                result = bis.read();
		        }
		        return buf.toString();
		}

        @Override
        protected String doInBackground(String... strings) {
        	HttpPost post = new HttpPost("https://stars.bilkent.edu.tr/srs/ajax/login.php");

        	String student_id = prefs.getString("stars_id", null);
	    	String student_password = prefs.getString("stars_password", null);
	    	Log.i("log", student_id + " " + student_password);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("ID", student_id));
            nameValuePairs.add(new BasicNameValuePair("PWD", student_password));
            
            try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = GlobalConstants.client.execute(post);
	            String res = readResponse(response);
	            Log.i("log", res);
	            return res;
			} catch (Exception e) {
				e.printStackTrace();
			}
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
        	if(s.contains("HOME")) {
        		GlobalConstants.isLoggedIn = true;
        		new AsyncTaskClass().execute();
        	}
        }
    }
}
