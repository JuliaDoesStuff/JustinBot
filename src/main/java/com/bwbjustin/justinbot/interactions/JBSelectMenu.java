package com.bwbjustin.justinbot.interactions;

import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import reactor.core.publisher.Mono;

public interface JBSelectMenu {
	public Mono<Void> handle(SelectMenuInteractionEvent event);
}
