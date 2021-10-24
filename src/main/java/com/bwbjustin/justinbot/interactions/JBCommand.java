package com.bwbjustin.justinbot.interactions;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public interface JBCommand {
	public Mono<Void> handle(ChatInputInteractionEvent event);
	
	public default Permission botRequired() {
		return null;
	}
	
	public default Permission memberRequired() {
		return null;
	}
}
