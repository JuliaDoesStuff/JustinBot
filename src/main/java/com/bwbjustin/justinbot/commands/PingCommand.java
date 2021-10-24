package com.bwbjustin.justinbot.commands;

import com.bwbjustin.justinbot.interactions.JBCommand;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.gateway.GatewayClient;
import reactor.core.publisher.Mono;

public class PingCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		return event.reply(
			event.getClient()
			.getGatewayClient(event.getShardInfo().getIndex())
			.map(GatewayClient::getResponseTime)
			.get().toMillis() + "ms"
		);
	}
}
