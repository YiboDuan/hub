package com.tiriam.hubble.persist;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CPanelConnector {
	
	final String SERVER_API_URL = "http://www.futureaccess.ca/~threep/api/index.php";
	
	boolean post(List<NameValuePair> params) {
		boolean result = false;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(SERVER_API_URL);
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
			    InputStream in = entity.getContent();
			    StringWriter writer = new StringWriter();
				IOUtils.copy(in, writer, "UTF-8");
				String str = writer.toString();
				if(str.equals("success")) {
					result = true;
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	
	JSONObject getUserJson(String user) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://www.futureaccess.ca/~threep/api/index.php?method=matchbyusername&field=" + user + "&format=json");
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
			    StringWriter writer = new StringWriter();
				IOUtils.copy(in, writer, "UTF-8");
				String str = writer.toString();
				JSONObject obj = new JSONObject(str);
			    return obj.getJSONObject("data");
			}
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	    	System.out.println("Unable to connect to server via http");
	    	throw new RuntimeException(e);
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return null;
	}
	
	JSONArray getHubs(double latitude, double longitude) throws JSONException {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("http://www.futureaccess.ca/~threep/api/index.php?method=gethubs&field=" + latitude + "," + longitude + "&format=json");
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
			    StringWriter writer = new StringWriter();
				IOUtils.copy(in, writer, "UTF-8");
				String str = writer.toString();
				JSONObject obj = new JSONObject(str);
			    return obj.getJSONArray("data");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return null;
	}
}
