package com.bwbjustin.justinbot.listeners;

import com.bwbjustin.justinbot.config.Config;

import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;

public class ReadyEventListener implements JBEventListener<ReadyEvent> {
	@Override
	public void handle(ReadyEvent event) {
		System.out.println("Now logged in as " + event.getSelf().getUsername() + "#" + event.getSelf().getDiscriminator());
		
		long guildCount = event.getClient().getGuilds().count().block();
		long userCount = event.getClient().getUsers().count().block();
		
		event.getClient().updatePresence(ClientPresence.of(
			Config.STATUS,
			ClientActivity.of(
				Config.ACTIVITY_TYPE,
				Config.ACTIVITY
				.replaceAll("\\{guilds\\}", Long.toString(guildCount))
				.replaceAll("guild\\(s\\)", "guild" + (guildCount != 1 ? "s" : ""))
				.replaceAll("\\{users\\}", Long.toString(userCount))
				.replaceAll("user\\(s\\)", "user" + (userCount != 1 ? "s" : "")),
				null
			)
		)).block();
	}
}
