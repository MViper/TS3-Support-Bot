/***********
 * Author: Florian Heinemann
 * Websites: https://BackToTheRules.net | Difficult-Knights.de
 * Release: 1.5
 * Copyright © 2020 by Florian Heinemann
 * License: Creative Common http://creativecommons.org/licenses/by-nc-nd/4.0/  Non Commercial - No Derivatives 4.0 International License
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */








package de.ts3tut.flo.main;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import de.ts3tut.flo.event.Events;

public class Main {
	
	public static final TS3Config config = new TS3Config();
	public static final TS3Query query = new TS3Query(config);
	public static final TS3Api api = query.getApi();
	
	public static ArrayList<Integer> onlineSups = new ArrayList<>();
	public static HashMap<Integer, ChannelHistory> clientChannelHistory = new HashMap<>();
	
	public static void  main(String[] args) {
		//Query Login Config
		config.setHost("173.249.53.250");
		config.setFloodRate(FloodRate.UNLIMITED);
		query.connect();
		
		api.login("Server-Security","CZ1sdON+");
		api.selectVirtualServerByPort(9987);
		api.setNickname("ServerSecurity");
	    System.out.println("Der Bot wurde erfolgreich gestartet");
		Events.loadEvents();
		start();
		updateChannelHistory();
	}
	//Channel Changer in specified group
	public static void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				onlineSups.clear();
				for(Client c : api.getClients()) {
					if(c.isInServerGroup(198)) {
						onlineSups.add(c.getId());
					}
				}
				Map<ChannelProperty, String> property = new HashMap<ChannelProperty, String>();
				if(onlineSups.size() == 0) {
					if(!api.getChannelInfo(288).getName().contains("╔-● Support 1 [Close]")) {
						property.put(ChannelProperty.CHANNEL_NAME, "╔-● Support 1 [Close]");
						property.put(ChannelProperty.CHANNEL_MAXCLIENTS, "0");
						property.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "0");
						Main.api.editChannel(288, property);
						property.clear();
					}
				}else {
					if(!api.getChannelInfo(288).getName().contains("╔-● Support 1 ["+onlineSups.size()+"]")) {
						property.put(ChannelProperty.CHANNEL_NAME, "╔-● Support 1 ["+onlineSups.size()+"]");
						property.put(ChannelProperty.CHANNEL_MAXCLIENTS, "1");
						property.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "1");	
						Main.api.editChannel(288, property);
						property.clear();
					}
				}
					
			}
		}, 1000, 5*1000);
	}
	
	public static void updateChannelHistory() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for(@SuppressWarnings("rawtypes") Map.Entry e : clientChannelHistory.entrySet()) {
					((ChannelHistory) e.getValue()).removeChannel();
				}
				System.out.println("ChannelHistories updated");
			}
		}, 60*1000, 60*1000);
	}

}
