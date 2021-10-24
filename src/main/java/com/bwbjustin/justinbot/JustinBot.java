package com.bwbjustin.justinbot;

import java.io.IOException;
import java.sql.SQLException;

import com.bwbjustin.justinbot.commands.Commands;
import com.bwbjustin.justinbot.config.Config;
import com.bwbjustin.justinbot.listeners.ButtonInteractionEventListener;
import com.bwbjustin.justinbot.listeners.ChatInputInteractionEventListener;
import com.bwbjustin.justinbot.listeners.ReadyEventListener;
import com.bwbjustin.justinbot.listeners.SelectMenuInteractionEventListener;
import com.bwbjustin.justinbot.sqlite.Database;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

public class JustinBot {
	public static Database DB;
	static {
		try {
			DB = new Database(Config.DB_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		GatewayDiscordClient client = DiscordClient.create(Config.TOKEN).login().block();
		
		try {
			Commands.register(client.getRestClient());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		client.on(ReadyEvent.class).subscribe(new ReadyEventListener()::handle);
		client.on(ButtonInteractionEvent.class).subscribe(new ButtonInteractionEventListener()::handle);
		client.on(ChatInputInteractionEvent.class).subscribe(new ChatInputInteractionEventListener()::handle);
		client.on(SelectMenuInteractionEvent.class).subscribe(new SelectMenuInteractionEventListener()::handle);
		
		client.onDisconnect().block();
	}
	
	public static String toHex(int rgb) {
		String hex = Integer.toString(rgb, 16).toUpperCase();
		return "0".repeat(6 - hex.length()) + hex;
	}
}
