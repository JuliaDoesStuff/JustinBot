package com.bwbjustin.justinbot.commands;

import java.time.Instant;

import com.bwbjustin.justinbot.JustinBot;
import com.bwbjustin.justinbot.interactions.JBCommand;
import com.bwbjustin.justinbot.sqlite.Table;
import com.bwbjustin.justinbot.sqlite.Table.EntryList;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Role;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public class AddColorCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		try {
			Table colors = JustinBot.DB.select("colors");
			Guild guild = event.getInteraction().getGuild().block();
			String name = event.getOption("name").get().getValue().get().asString();
			EntryList entries = Table.entryList("guild", guild.getId().asString(), "name", name);
			
			if (colors.exists(entries))
				return event.reply("A color role with this name already exists!");
			
			int rgb;
			try {
				rgb = Integer.parseInt(event.getOption("color").get().getValue().get().asString(), 16);
			} catch (NumberFormatException e) {
				return event.reply("The color could not be parsed!");
			}
			
			if (rgb < 0 || rgb > 16777216)
				return event.reply("The color is too high or too low!");
			
			Role role = guild.createRole().withName(name).withColor(Color.of(rgb)).block();
			role.changePosition(
				guild.getSelfMember().block()
				.getRoles().sort((a, b) -> a.getPosition().block() - b.getPosition().block()).blockLast()
				.getPosition().block() + 1
			);
			
			colors.insert(entries.with("role", role.getId().asString(), "rgb", Integer.toString(role.getColor().getRGB())));
	
			return event.reply(InteractionApplicationCommandCallbackSpec.create().withEmbeds(
				EmbedCreateSpec.create()
				.withTitle("Color role " + role.getName() + " successfully added")
				.withDescription("With color: #" + JustinBot.toHex(role.getColor().getRGB()))
				.withColor(role.getColor())
				.withTimestamp(Instant.now())
			));
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception trying to add the color role.");
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
