package com.MCS.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.MCS.plugin.API.Ban;
import com.MCS.plugin.API.CheckBan;
import com.MCS.plugin.API.CheckServer;
import com.MCS.plugin.API.Lookup;
import com.MCS.plugin.API.UUIDFetcher;
import com.MCS.plugin.API.Unban;

public class Main extends JavaPlugin implements Listener {
	
	private String noperm = "You don't have permission for that.";
	private String api = getConfig().getString("api-key");
	private String handle = ChatColor.DARK_RED + getConfig().getString("handle");
	
	public static String connector = "http://178.62.103.141/api/json/";
	
	private boolean updatemsg = false;
	
	Logger lg = Bukkit.getLogger();
	
	public void onEnable() {
		
		saveDefaultConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		//String text;
		
	    try {
	    	File file = new File("plugins/MC-Security/backup.json");
	        BufferedWriter output = new BufferedWriter(new FileWriter(file));
	        output.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    CheckServer cs = new CheckServer();
	    
	    cs.setVersion(this.getDescription().getVersion());
	    cs.setApi(api);
	    
	    String rsp = cs.send();
	    
	    Object j = JSONValue.parse(rsp);
	    JSONObject json = (JSONObject) j;
	    
	    boolean version = new Boolean((Boolean) json.get("updated"));
	    boolean apicorrect = new Boolean((Boolean) json.get("api-correct"));
	    
	    if (apicorrect != true) {
	    	lg.severe("[MC-Security] API not entered correctly in config.yml!");
	    	this.setEnabled(false);
	    }
	    
	    if (version != true) {
	    	updatemsg = false;
	    	lg.info("[MC-Security] There is an update for MC-Security!");
	    }
		
	}
	
	public void onDisable() {
		
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender s, org.bukkit.command.Command cmd, String label, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("ban")) {
			
			if (s.hasPermission("mcs.ban")) {
				
				if (args.length > 1) {
					
					String uuid;
					
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
					String nom = p.getName();
					
					try {
						uuid = UUIDFetcher.getUUIDOf(nom).toString();
					} catch (Exception e) {
						s.sendMessage(ChatColor.RED + "Are you sure that player exists?");
						uuid = null;
					}
					
					if (uuid != null) {
						
						StringBuilder sb = new StringBuilder();
						
						boolean count = true;
						
						for (String sx : args) {
							if (count == false) {
								sb.append(sx + " ");
							} else {
								count = false;
							}
						}
						
						String reason = sb.toString();
						
						String sender;
						
						if (s instanceof Player) {
							Player oop = (Player) s;
							sender = oop.getUniqueId().toString();
						} else {
							sender = "console";
						}
						
						Ban b = new Ban();
						
						b.setApi(api);
						b.setTarget(uuid);
						b.setSender(sender);
						b.setReason(reason);
						
						String rsp = b.send();
						
						Object j = JSONValue.parse(rsp);
						JSONObject json = (JSONObject) j;

						boolean success = new Boolean((Boolean) json.get("success"));
						String msg = new String((String) json.get("response"));
						
						OfflinePlayer p1 = Bukkit.getOfflinePlayer(args[0]);
						
						if (success == true) {
							Bukkit.broadcastMessage(handle + ChatColor.WHITE + p1.getName() + " was banned!");
							
							for (Player op : Bukkit.getOnlinePlayers()) {
								if (op.getName() == nom) {
									op.kickPlayer(reason);
								}
							}
							
						} else {
							s.sendMessage(handle + ChatColor.WHITE + "Error, ban won't be stored on MC-Security.com: " + msg);
							if (p1.isBanned()) {
								s.sendMessage(handle + ChatColor.WHITE + "Player already banned locally, not storing it there!");
							} else {
								s.sendMessage(handle + ChatColor.WHITE + "Banned player locally!");
								for (Player op : Bukkit.getOnlinePlayers()) {
									if (op.getName() == nom) {
										op.kickPlayer(reason);
									}
								}
								p1.setBanned(true);
							}
							
						}
						
					} else {
						s.sendMessage(ChatColor.RED + "Unable to complete task!");
					}
					
				} else {
					s.sendMessage(ChatColor.RED + "Usage: /ban <name> <reason>");
				}
				
			} else {
				s.sendMessage(ChatColor.RED + noperm);
			}
			
		}
		
		if (cmd.getName().equalsIgnoreCase("lookup") || cmd.getName().equalsIgnoreCase("lup")) {
			if (s.hasPermission("mcs.lookup")) {
				
				if (args.length == 1) {
					
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
					
					String uuid;
					
					try {
						uuid = UUIDFetcher.getUUIDOf(p.getName()).toString();
					} catch (Exception e) {
						e.printStackTrace();
						uuid = null;
					}
					
					if (uuid != null) {
						
						Lookup l = new Lookup();
						l.setPlayer(uuid);
						String rsp = l.send();
						
						Object j = JSONValue.parse(rsp);
						JSONObject json = (JSONObject) j;
						
						JSONArray arrayx = (JSONArray) json.get("bans");
						
						Object[] array = arrayx.toArray();
						
						int amt = array.length;
						
						boolean perm = new Boolean((Boolean) json.get("perm"));
						
						if (perm) {
							s.sendMessage(handle + ChatColor.BOLD + ChatColor.DARK_AQUA + "This player is currently globally banned.");
						}
						
						s.sendMessage(handle + ChatColor.GREEN + p.getName() + " has " + amt + " bans!");
						
						for (Object sx : array) {
							s.sendMessage(" - " + ChatColor.GRAY + ChatColor.ITALIC + sx);
						}
						
					} else {
						s.sendMessage(ChatColor.RED + "Are you sure that player exists?");
					}
					
					
				} else {
					s.sendMessage(ChatColor.RED + "Usage: /lookup <name>");
				}
				
			} else {
				s.sendMessage(ChatColor.RED + noperm);
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("unban")) {
			if (s.hasPermission("mcs.unban")) {
				if (args.length == 1) {
					
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
					
					if (p.isBanned()) {
						p.setBanned(false);
						s.sendMessage(handle + ChatColor.WHITE + "Removed ban from local system.");
					}
					
					String uuid;
					
					try {
						uuid = UUIDFetcher.getUUIDOf(p.getName()).toString();
					} catch (Exception e) {
						uuid = null;
						s.sendMessage(ChatColor.RED + "Are you sure that player exists?");
					}
					
					if (uuid != null) {
						
						Unban u = new Unban();
						u.setApi(api);
						u.setPlayer(uuid);
						String rsp = u.send();
						Object j = JSONValue.parse(rsp);
						JSONObject json = (JSONObject) j;
						
						boolean success = new Boolean((Boolean) json.get("success"));
						String msg = new String((String) json.get("msg"));
						
						if (success == true) {
							s.sendMessage(handle + ChatColor.WHITE + p.getName() + " was unbanned!");
						} else {
							s.sendMessage(handle + ChatColor.WHITE + "Error: " + msg);
						}
						
					}
					
				} else {
					s.sendMessage(ChatColor.RED + "Usage: /unban <name>");
				}
			} else {
				s.sendMessage(ChatColor.RED + noperm);
			}
		}
		
		return true;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		
		if (p.hasPermission("mcs.login.update")) {
			if (updatemsg) {
				p.sendMessage(handle + ChatColor.RED + "You plugin needs updating!");
			}
		}
	}
	
	@EventHandler
	public void login(PlayerLoginEvent e) {

		Player p = e.getPlayer();

		CheckBan cb = new CheckBan();

		cb.setApi(api);
		cb.setPlayer(p);

		String rsp = cb.send();

		Object j = JSONValue.parse(rsp);
		JSONObject json = (JSONObject) j;

		boolean isBanned = new Boolean((Boolean) json.get("banned"));
		boolean isGlobal = new Boolean((Boolean) json.get("globalbanned"));
		String reason = new String((String) json.get("reason"));
		
		if (getConfig().getBoolean("use-global-bans")) {
			if (isGlobal) {
				e.setResult(Result.KICK_BANNED);
				e.setKickMessage("Globally banned: Visit www.MC-Security.com");
			}
		} else {
			if (isBanned) {
				e.setResult(Result.KICK_BANNED);
				e.setKickMessage(reason);
			}
		} 
		
	}

}
