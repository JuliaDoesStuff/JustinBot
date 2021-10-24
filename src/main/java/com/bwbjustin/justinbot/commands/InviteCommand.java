package com.bwbjustin.justinbot.commands;

import com.bwbjustin.justinbot.interactions.JBCommand;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import reactor.core.publisher.Mono;

public class InviteCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		return event.reply(
			InteractionApplicationCommandCallbackSpec.create()
			.withContent("Here are the links!")
			.withComponents(ActionRow.of(
				Button.link("https://discord.com/api/oauth2/authorize?client_id=842832651324227594&permissions=8&scope=bot%20applications.commands", "Invite me!"),
				Button.link("https://discord.gg/KjX93dcYEM", "Join the Discord server!")
			))
		);
	}
}
