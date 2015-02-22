package com.MCS.plugin.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class Lookup {
	
	public Lookup() {
		
	}
	
	private String player;
	
	public void setPlayer(String playerx) {
		player = playerx;
	}
	
	public String send() {
		
		String rsp = "";
		
		try {
			
		    URL url = new URL("http://mc-security.com/api/json/lookup.php");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		    writer.write("player=" + player);
		    writer.flush();
		    String line;
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    while ((line = reader.readLine()) != null) {
		      rsp = line;
		    }
		    writer.close();
		    reader.close();
			
		} catch (Exception e) {
			rsp = null;
			e.printStackTrace();
		}
		
		return rsp;

	  }

}
