package com.bwbjustin.justinbot.interactions;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import reactor.core.publisher.Mono;

public interface JBButton {
	public Mono<Void> handle(ButtonInteractionEvent event);
}
