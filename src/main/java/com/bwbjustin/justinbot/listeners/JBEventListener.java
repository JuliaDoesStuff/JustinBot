package com.bwbjustin.justinbot.listeners;

import discord4j.core.event.domain.Event;

public interface JBEventListener<E extends Event> {
	public void handle(E event);
}
