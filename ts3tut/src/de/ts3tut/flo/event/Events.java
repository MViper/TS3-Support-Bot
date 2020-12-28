package de.ts3tut.flo.event;

import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import de.ts3tut.flo.main.ChannelHistory;
import de.ts3tut.flo.main.Main;

public class Events {

	public static void loadEvents() {

		Main.api.registerAllEvents();
		Main.api.addTS3Listeners(new TS3Listener() {

			@Override
			public void onChannelCreate(ChannelCreateEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			@Override
			public void onChannelDeleted(ChannelDeletedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			@Override
			public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			@Override
			public void onChannelEdit(ChannelEditedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			@Override
			public void onChannelMoved(ChannelMovedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			@Override
			public void onChannelPasswordChanged(ChannelPasswordChangedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}
			//Client Join Message
			@Override
			public void onClientJoin(ClientJoinEvent event) {
				Client client = Main.api.getClientInfo(event.getClientId());
				Main.api.sendPrivateMessage(client.getId(),
						"Willkommen auf unserem Server " + client.getNickname() + "!");
				Main.clientChannelHistory.put(client.getId(), new ChannelHistory());

			}

			@Override
			public void onClientLeave(ClientLeaveEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			//Support Notification
			@Override
			public void onClientMoved(ClientMovedEvent event) {
				Client c = Main.api.getClientInfo(event.getClientId());
				ChannelHistory history = Main.clientChannelHistory.get(c.getId());
				history.addChannel();
				if(history.isChannelhopping() == 2) {
					Main.api.pokeClient(c.getId(), "Es scheint als würdest du Channelhoppen Bitte unterlasse das!");
				}else if(history.isChannelhopping() == 1) {
					Main.api.banClient(c.getId(), 30*60, "Autoban: ChannelHopping ist Verboten!");
				}
				if(event.getTargetChannelId() == 26) {
					int i = 0;
					for(Client client : Main.api.getClients()) {
						for(int i = 0; i > c.getServerGroups(); i++) {
							if(client.getServerGroups()[i] == 9) {
							i++;
							Main.api.sendPrivateMessage(client.getId(), "Ein User benötigt Support!");			
							}
						}
						Main.api.sendPrivateMessage(event.getClientId(), "[color=red]Es wurden [B]" + i + "[/B] Teammitglieder benachrichtigt!");
					}
				}

			}

			@Override
			public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}

			@Override
			public void onServerEdit(ServerEditedEvent arg0) {
				// TODO Automatisch generierter Methodenstub

			}
			//help Command 
			@Override
			public void onTextMessage(TextMessageEvent event) {
				Client client = Main.api.getClientInfo(event.getInvokerId());
				if(event.getMessage().equalsIgnoreCase("!nopoke")) {
					if(client.isInServerGroup(407)) {
						Main.api.removeClientFromServerGroup(407, client.getDatabaseId());
					}else {
						Main.api.addClientToServerGroup(407, client.getDatabaseId());
					}
				}
				if(event.getMessage().equalsIgnoreCase("!nomsg")) {
					if(client.isInServerGroup(408)) {
						Main.api.removeClientFromServerGroup(408, client.getDatabaseId());
					}else {
						Main.api.addClientToServerGroup(408, client.getDatabaseId());
					}
				}
				
				Main.api.sendPrivateMessage(client.getId(), "Danke für deine Antwort");
				String messageString = event.getMessage();
				
				String[] args = messageString.split(" ");
				
				if (args[0].equalsIgnoreCase("!help")) {
					Main.api.sendPrivateMessage(client.getId(), "Für weitere hilfe erstelle bitte ein Ticket unter https://support.dev-server-system.de/open.php oder nutze !online");
				} else if(args[0].equalsIgnoreCase("!online")) {
					int online = Main.api.getServerInfo().getClientsOnline();
					Main.api.sendPrivateMessage(client.getId(), "Zurzeit sind: " + online +   "Clients online.");
				} else {
					Main.api.sendPrivateMessage(client.getId(), "Unbekannter Command, nutze !help");
				}

			}

		});
	}
}
