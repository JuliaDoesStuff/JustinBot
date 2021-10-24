package com.bwbjustin.justinbot.buttons;

import com.bwbjustin.justinbot.commands.ListColorsCommand;
import com.bwbjustin.justinbot.interactions.JBButton;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

public class GoToPreviousButton implements JBButton {
	@Override
	public Mono<Void> handle(ButtonInteractionEvent event) {
		try {
			int page = Integer.parseInt(event.getMessage().get().getEmbeds().get(0).getFields().get(0).getValue()) - 2;
			EmbedCreateSpec embed = ListColorsCommand.createEmbed(event.getInteraction().getGuild().block(), page);
			
			return event.edit()
			.withEmbeds(embed)
			.withComponents(ActionRow.of(
				Button.primary("go-to-previous", "Previous").disabled(page == 0),
				Button.primary("go-to-next", "Next").disabled(embed.description().get().split("\n").length < 10),
				Button.danger("go-to-cancel", "Cancel")
			));
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception.");
		}
	}
}
