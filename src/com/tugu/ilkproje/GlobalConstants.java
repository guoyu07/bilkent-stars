package com.tugu.ilkproje;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class GlobalConstants {
	public static HttpClient client = new DefaultHttpClient();
	public static boolean isLoggedIn = false;
	List<String> ar = new ArrayList<String>();
}
