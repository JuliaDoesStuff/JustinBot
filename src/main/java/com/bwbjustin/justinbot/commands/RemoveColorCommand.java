package com.bwbjustin.justinbot.commands;

import com.bwbjustin.justinbot.JustinBot;
import com.bwbjustin.justinbot.interactions.JBCommand;
import com.bwbjustin.justinbot.sqlite.Table;
import com.bwbjustin.justinbot.sqlite.Table.EntryList;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public class RemoveColorCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		try {
			String name = event.getOption("name").get().getValue().get().asString();
			Table colors = JustinBot.DB.select("colors");
			Guild guild = event.getInteraction().getGuild().block();
			EntryList entries = Table.entryList("guild", guild.getId().asString(), "name", name);
			
			if (!colors.exists(entries))
				return event.reply("A color role with this name does not exist!");
			
			Role role = guild.getRoleById(Snowflake.of(colors.select(entries).getLong("role"))).block();
			if (role != null)
				role.delete().block();
			colors.delete(role != null ? entries.with("role", role.getId().asString()) : entries);
	
			return event.reply("Color role successfully removed.");
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception trying to remove the color role.");
		}
	}
	
	@Override
	public Permission botRequired() {
		return Permission.MANAGE_ROLES;
	}
	
	@Override
	public Permission memberRequired() {
		return Permission.MANAGE_ROLES;
	}
}
