package com.tugu.ilkproje;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;

public class MainActivity extends Activity {
	SharedPreferences.Editor editor;
	BootstrapEditText id, password;
	LinearLayout loading_layout;
	FontAwesomeText loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		editor = getSharedPreferences("user_info",MODE_PRIVATE).edit();
		SharedPreferences prefs = getSharedPreferences("user_info",MODE_PRIVATE);
		boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
		Log.i("TC", ""+isLoggedIn);
		Log.i("log", prefs.getString("stars_id", ""));
		Log.i("log", prefs.getString("stars_password", ""));
		
		
		if(isLoggedIn)
		{
  	      	Intent i = new Intent(getApplicationContext(), Dashboard.class);
  	      	startActivity(i);
  	      	finish();
		}
		else
		{
			setContentView(R.layout.activity_main);
			id = (BootstrapEditText) findViewById(R.id.stars_id);
			password = (BootstrapEditText ) findViewById(R.id.stars_password);
		    
			String student_id = prefs.getString("stars_id", null);
	    	String student_password = prefs.getString("stars_password", null);
	    	if (student_id != null) 
	    	{
	    		id.setText(student_id);
	    		password.setText(student_password);
	    	}
	    	
			final BootstrapButton login = (BootstrapButton) findViewById(R.id.login_button);
	        login.setOnClickListener(new View.OnClickListener(){
	            public void onClick(View v) {
	            	String stars_id;
	            	String stars_password;
	        		BootstrapEditText  id = (BootstrapEditText) findViewById(R.id.stars_id);
	            	stars_id = id.getText().toString();
	            	BootstrapEditText  password = (BootstrapEditText ) findViewById(R.id.stars_password);
	            	stars_password = password.getText().toString();
	            	
	            	editor.putString("stars_id", stars_id);
	            	editor.putString("stars_password", stars_password);
	            	editor.commit();
	            	
	            	
	      	      	loading = (FontAwesomeText) findViewById(R.id.loading_gif);
	      	      	loading.startRotate(v.getContext(), true, FontAwesomeText.AnimationSpeed.SLOW);
	      	      	loading_layout = (LinearLayout) findViewById(R.id.loading_layout);
	      	      	loading_layout.setVisibility(View.VISIBLE);
	      	      	
	      	      	new LoginTask(true).execute();
	            }
	        });
	        
	        final FontAwesomeText info = (FontAwesomeText) findViewById(R.id.info_button);
	        info.setOnClickListener(new View.OnClickListener(){
	            public void onClick(View v) {
	      	      	Intent i = new Intent(v.getContext(), InfoActivity.class);
	      	      	startActivity(i);
	            }
	        });

	    }		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class LoginTask extends AsyncTask<String, String, String> {
		private boolean loadDashboard;
		
		public LoginTask(boolean loadDashboard) {
			this.loadDashboard = loadDashboard;
		}
		
        @Override
        protected void onPreExecute() {
            //uzun islem oncesi yapilacaklar
        }
        
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

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("ID", id.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("PWD", password.getText().toString()));
            
            try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            HttpResponse response = GlobalConstants.client.execute(post);
	            String res = readResponse(response);
	            Log.i("log", res);
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
        protected void onPostExecute(String s) {
        	runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	loading_layout.setVisibility(View.INVISIBLE);
            	}
        	});
        	
        	if(s.contains("HOME"))
        	{
        		editor.putBoolean("isLoggedIn", true );
        		editor.commit();
        		GlobalConstants.isLoggedIn = true;
        		if(loadDashboard)
		        	runOnUiThread(new Runnable() {
		                @Override
		                public void run() {
		            		Intent i = new Intent(MainActivity.this.getApplicationContext(), Dashboard.class);
		          	      	startActivity(i);
		            	}
		        	});
        	}
            else {
	        	runOnUiThread(new Runnable() {
	                @SuppressWarnings("deprecation")
					@Override
	                public void run() {
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		
		        		// Setting Dialog Title
		        		alertDialog.setTitle("Error");
		
		        		// Setting Dialog Message
		        		alertDialog.setMessage("Your Login Credentials Are Invalid.");
				
		        		// Setting OK Button
		        		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        				public void onClick(DialogInterface dialog, int which) {}
		        		});
		
		        		// Showing Alert Message
		        		alertDialog.show();
                	}
                });
            }
        }
    }
}
