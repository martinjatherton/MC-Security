package com.MCS.plugin.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.entity.Player;

import com.MCS.plugin.Main;

public class CheckBan {
	
	public CheckBan() {
		
	}
	
	private String api;
	private Player player;
	
	private String con = Main.connector;
	
	public void setApi(String apix) {
		api = apix;
	}
	
	public void setPlayer(Player playerx) {
		player = playerx;
	}
	
	public String send() {
		
		String rsp = null;
		
		String name = player.getName();
		
		try {
			
		    URL url = new URL(con + "check.php");
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);
		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		    writer.write("api=" + api + "&player=" + player.getUniqueId().toString() + "&name=" + name);
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
