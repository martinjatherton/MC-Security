package com.MCS.plugin.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.MCS.plugin.Main;

public class CheckServer {
	
	public CheckServer() {
		
	}
	
	private String con = Main.connector;
	
	private String version;
	private String api;
	
	public void setVersion(String versionx) {
		version = versionx;
	}
	
	public void setApi(String apix) {
		api = apix;
	}
	
	public String send() {
		
		String rsp = null;
		
		try {
			
		    URL url = new URL(con + "updater.php");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		    writer.write("api=" + api + "&version=" + version);
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
