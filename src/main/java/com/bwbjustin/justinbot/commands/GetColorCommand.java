package com.bwbjustin.justinbot.commands;

import java.time.Instant;

import com.bwbjustin.justinbot.JustinBot;
import com.bwbjustin.justinbot.interactions.JBCommand;
import com.bwbjustin.justinbot.sqlite.Table;
import com.bwbjustin.justinbot.sqlite.Table.EntryList;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class GetColorCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		try {
			String name = event.getOption("name").get().getValue().get().asString();
			Table colors = JustinBot.DB.select("colors");
			Guild guild = event.getInteraction().getGuild().block();
			EntryList entries = Table.entryList("guild", guild.getId().asString(), "name", name);
			
			if (!colors.exists(entries))
				return event.reply("A color role with this name does not exist!");
			
			int rgb = colors.select(entries).getInt("rgb");
			
			return event.reply(InteractionApplicationCommandCallbackSpec.create().withEmbeds(
				EmbedCreateSpec.create()
				.withTitle("Color role: " + name)
				.withDescription("With color: #" + JustinBot.toHex(rgb))
				.withColor(Color.of(rgb))
				.withTimestamp(Instant.now())
			));
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception trying to get info on a color role.");
		}
	}
}
