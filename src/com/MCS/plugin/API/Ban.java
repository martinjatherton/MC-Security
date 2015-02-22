package com.MCS.plugin.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.MCS.plugin.Main;

public class Ban {
	
	private String api = null;
	private String target = null;
	private String sender = null;
	private String reason = null;
	
	public Ban() {
		
	}
	
	public void setApi(String apix) {
		api = apix;
	}
	
	public void setTarget(String targetx) {
		target = targetx;
	}	
	
	public void setSender(String senderx) {
		sender = senderx;
	}
	
	
	public void setReason(String reasonx) {
		reason = reasonx;
	}
	
	public String toMsg() {
		return target + " was banned by " + sender + " for " + reason;
	}
	
	private String con = Main.connector;
	
	public String send() {
		
		String rsp = null;
		
		try {
			
		    URL url = new URL(con + "ban.php");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		    writer.write("api=" + api + "&target=" + target + "&sender=" + sender + "&reason=" + reason);
		    writer.flush();
		    String line;
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    while ((line = reader.readLine()) != null) {
		      rsp = line;
		    }
		    writer.close();
		    reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rsp;

	  }
}
