package com.bwbjustin.justinbot.listeners;

import com.bwbjustin.justinbot.interactions.Interactions;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;

public class ButtonInteractionEventListener implements JBEventListener<ButtonInteractionEvent> {
	@Override
	public void handle(ButtonInteractionEvent event) {
		Interactions.BUTTONS.get(event.getCustomId()).handle(event).block();
	}
}
