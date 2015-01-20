package com.ldcc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ElmatarSender {
	private final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args)  throws Exception {
		ElmatarSender sender = new ElmatarSender();
		 
 
		System.out.println("\nTesting 2 - Send Http POST request");
		sender.sendCurrent(45);
		sender.sendPost(12,"-483");

	}
	
	public ElmatarSender() {

	}

	public void sendPost(int ticks, String temp) throws Exception {
		 
		//String url = "http://192.168.1.64:8080/rest-sample/api/sample";
		String url = "http://ticket-ldcc.rhcloud.com/ticket/api/sample";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		Date date = new Date();
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String nowDate = dFormat.format(date);
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
		String urlParameters = "{\"numTicks\": \""+ticks+"\", \"temp\": \""+temp+"\", \"sampleDate\": \""+nowDate+"\"}";
 		//System.out.println("Temp is: "+temp + " Count is: "+ticks+ "Date is: "+nowDate);

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		if (responseCode != 200)
			throw new IOException("Response 10: "+responseCode);
 
	//	int responseCode = con.getResponseCode();
	//	System.out.println("\nSending 'POST' request to URL : " + url);
	//	System.out.println("Post parameters : " + urlParameters);
	//	System.out.println("Response Code : " + responseCode);
 
	//	BufferedReader in = new BufferedReader(
	//	        new InputStreamReader(con.getInputStream()));
	//	String inputLine;
	//	StringBuffer response = new StringBuffer();
 
	//	while ((inputLine = in.readLine()) != null) {
	//		response.append(inputLine);
	//	}
	//	in.close();
 
		//print result
		//System.out.println(response.toString());
	}
	public void sendCurrent(int currentSample) throws Exception {
		 
//		String url = "http://192.168.1.64:8080/rest-sample/api/current";
		String url = "http://ticket-ldcc.rhcloud.com/ticket/api/current";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
		String urlParameters = "{\"currentSample\": \""+currentSample+"\"}";
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		if (responseCode != 200)
			throw new IOException("Response: "+responseCode);
			
			
	//	System.out.println("\nSending 'POST' request to URL : " + url);
	//	System.out.println("Post parameters : " + urlParameters);
	//	System.out.println("Response Code : " + responseCode);
 
/*		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();*/
		//print result
		//System.out.println(response.toString());
	}
	
}
