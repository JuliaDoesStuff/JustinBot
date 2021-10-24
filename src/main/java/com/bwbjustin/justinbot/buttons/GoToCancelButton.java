package com.bwbjustin.justinbot.buttons;

import com.bwbjustin.justinbot.interactions.JBButton;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import reactor.core.publisher.Mono;

public class GoToCancelButton implements JBButton {
	@Override
	public Mono<Void> handle(ButtonInteractionEvent event) {
		return event.edit().withComponents();
	}
}
