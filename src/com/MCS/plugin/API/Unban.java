package com.MCS.plugin.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.MCS.plugin.Main;

public class Unban {
	
	private String player;
	private String api;
	
	private String con = Main.connector;
	
	public Unban() {
		
	}
	
	public void setPlayer(String uuid) {
		player = uuid;
	}
	
	public void setApi(String newapi) {
		api = newapi;
	}
	
	public String send() {
		
		String rsp = null;
		
		try {
			
			URL url = new URL(con + "unban.php");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		    writer.write("api=" + api + "&target=" + player);
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
