package com.tiriam.hubble.persist;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CPanelConnector {
	
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
	
	boolean addUser(String username, String password, String email) {
		boolean result = false;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.futureaccess.ca/~threep/api/index.php");
			
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("method", "createuser"));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("email", email));
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
	
	boolean createHub(String name, double latitude, double longitude, String creator, String privacy, float radius, int max, String password) {
		boolean result = false;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.futureaccess.ca/~threep/api/index.php");
			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("method", "createhub"));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
			params.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
			params.add(new BasicNameValuePair("creator", creator));
			params.add(new BasicNameValuePair("privacy", privacy));
			params.add(new BasicNameValuePair("radius", Float.toString(radius)));
			params.add(new BasicNameValuePair("max", Integer.toString(max)));
			params.add(new BasicNameValuePair("password", password));
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
