package com.bwbjustin.justinbot.commands;

import java.time.Instant;

import com.bwbjustin.justinbot.JustinBot;
import com.bwbjustin.justinbot.interactions.JBCommand;
import com.bwbjustin.justinbot.sqlite.Table;
import com.bwbjustin.justinbot.sqlite.Table.EntryList;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.RoleEditMono;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public class UpdateColorCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		try {
			Table colors = JustinBot.DB.select("colors");
			Guild guild = event.getInteraction().getGuild().block();
			String name = event.getOption("name").get().getValue().get().asString();
			EntryList entries = Table.entryList("guild", guild.getId().asString(), "name", name);
			
			if (!colors.exists(entries))
				return event.reply("A color role with this name does not exist!");
			
			RoleEditMono edit = guild.getRoleById(Snowflake.of(colors.select(entries).getLong("role"))).block().edit();
			
			if (event.getOption("new-color").isPresent()) {
				int newColor;
				try {
					newColor = Integer.parseInt(event.getOption("new-color").get().getValue().get().asString(), 16);
				} catch (NumberFormatException e) {
					return event.reply("The color could not be parsed!");
				}
			
				if (newColor < 0 || newColor > 16777216)
					return event.reply("The color is too high or too low!");
				
				edit = edit.withColor(Color.of(newColor));
				colors.update(entries, Table.entryList("rgb", Integer.toString(newColor)));
			}
			
			if (event.getOption("new-name").isPresent()) {
				String newName = event.getOption("new-name").get().getValue().get().asString();
				
				edit = edit.withName(newName);
				colors.update(entries, Table.entryList("name", newName));
			}
			
			Role role = edit.block();
	
			return event.reply(InteractionApplicationCommandCallbackSpec.create().withEmbeds(
				EmbedCreateSpec.create()
				.withTitle("Color role " + role.getName() + " successfully updated")
				.withDescription("With color: #" + JustinBot.toHex(role.getColor().getRGB()))
				.withColor(role.getColor())
				.withTimestamp(Instant.now())
			));
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception trying to update the color role.");
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
