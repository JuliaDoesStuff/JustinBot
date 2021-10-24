package com.bwbjustin.justinbot.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

import com.bwbjustin.justinbot.JustinBot;
import com.bwbjustin.justinbot.interactions.JBCommand;
import com.bwbjustin.justinbot.sqlite.Table;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.core.spec.EmbedCreateFields.Field;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class ListColorsCommand implements JBCommand {
	public static EmbedCreateSpec createEmbed(Guild guild, int page) throws SQLException {
		ArrayList<String> colorList = new ArrayList<>();
		ResultSet resultSet = JustinBot.DB.select("colors").select(Table.entryList("guild", guild.getId().asString()), page * 10, 10);
		
		while (resultSet.next())
			colorList.add(resultSet.getString("name") + " - #" + JustinBot.toHex(resultSet.getInt("rgb")));
		
		return EmbedCreateSpec.create()
		.withTitle("List of color roles")
		.withDescription(colorList.size() > 0 ? String.join("\n", colorList) : "None")
		.withColor(Color.of(new Random().nextInt(16777216)))
		.withFields(Field.of("Page", Integer.toString(page + 1), false))
		.withTimestamp(Instant.now());
	}

	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		try {
			EmbedCreateSpec embed = createEmbed(event.getInteraction().getGuild().block(), 0);
			
			return event.reply(
				InteractionApplicationCommandCallbackSpec.create()
				.withEmbeds(embed)
				.withComponents(ActionRow.of(
					Button.primary("go-to-previous", "Previous").disabled(),
					Button.primary("go-to-next", "Next").disabled(embed.description().get().split("\n").length < 10),
					Button.danger("go-to-cancel", "Cancel")
				))
			);
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception trying to list all color roles.");
		}
	}
}
