package com.bwbjustin.justinbot.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.bwbjustin.justinbot.config.Config;
import com.bwbjustin.justinbot.interactions.Interactions;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;

public class Commands {
	public static void register(RestClient client) throws IOException {
		long applicationId = client.getApplicationId().block();
		
		for (long guild : Config.COMMAND_GUILDS) {
			Map<String, ApplicationCommandData> discordCommands = client.getApplicationService()
				.getGuildApplicationCommands(applicationId, guild)
				.collectMap(ApplicationCommandData::name)
				.block();
	
			HashMap<String, ApplicationCommandRequest> localCommands = new HashMap<>();
			for (String json : getCommands()) {
				ApplicationCommandRequest localCommand = JacksonResources.create().getObjectMapper().readValue(json, ApplicationCommandRequest.class);
	
				localCommands.put(localCommand.name(), localCommand);
	
				if (!discordCommands.containsKey(localCommand.name())) {
					client.getApplicationService().createGuildApplicationCommand(applicationId, guild, localCommand).block();
					System.out.println("Created command " + localCommand.name());
				}
			}
	
			for (ApplicationCommandData discordCommand : discordCommands.values()) {
				ApplicationCommandRequest localCommand = localCommands.get(discordCommand.name());
	
				if (localCommand == null) {
					client.getApplicationService().deleteGuildApplicationCommand(applicationId, guild, Long.parseLong(discordCommand.id())).block();
					System.out.println("Deleted command " + discordCommand.name());
					
					continue;
				}
	
				if (!discordCommand.description().equals(localCommand.description().get()) || !discordCommand.options().equals(localCommand.options())) {
					client.getApplicationService().modifyGuildApplicationCommand(applicationId, guild, Long.parseLong(discordCommand.id()), localCommand).block();
					System.out.println("Updated command " + localCommand.name());
				}
			}
		}
	}

	private static ArrayList<String> getCommands() throws IOException {
		ArrayList<String> list = new ArrayList<>();
		
		for (String file : Interactions.COMMANDS.entrySet().stream().map(x -> x.getKey() + ".json").collect(Collectors.toList())) {
			InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream("commands/" + file);
			if (resourceStream == null) {
				list.add(null);
				continue;
			}
				
			list.add(new BufferedReader(new InputStreamReader(resourceStream)).lines().collect(Collectors.joining(System.lineSeparator())));
		}
		
		return list;
	}
}
