package com.bwbjustin.justinbot.listeners;

import com.bwbjustin.justinbot.interactions.Interactions;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;

public class SelectMenuInteractionEventListener implements JBEventListener<SelectMenuInteractionEvent> {
	@Override
	public void handle(SelectMenuInteractionEvent event) {
		Interactions.SELECT_MENUS.get(event.getCustomId()).handle(event).block();
	}
}
