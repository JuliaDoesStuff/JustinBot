package com.bwbjustin.justinbot.commands;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.bwbjustin.justinbot.JustinBot;
import com.bwbjustin.justinbot.interactions.JBCommand;
import com.bwbjustin.justinbot.selectmenus.SelectColorRoleSelectMenu;
import com.bwbjustin.justinbot.sqlite.Table;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.SelectMenu;
import discord4j.core.object.component.SelectMenu.Option;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public class SelectColorsCommand implements JBCommand {
	@Override
	public Mono<Void> handle(ChatInputInteractionEvent event) {
		try {
			ArrayList<Option> colorList = new ArrayList<>();
			Snowflake guild = event.getInteraction().getGuild().block().getId();
			ResultSet resultSet = JustinBot.DB.select("colors").select(Table.entryList("guild", guild.asString()));
			
			while (resultSet.next())
				colorList.add(Option.of(resultSet.getString("name"), resultSet.getString("role")));
			
			if (colorList.size() == 0)
				return event.reply("There are no color roles to select!");
			
			SelectColorRoleSelectMenu.CONTENTS.put(guild.asLong(), colorList.stream().map(x -> Long.parseLong(x.getValue())).collect(Collectors.toList()));
			
			return event.reply("Select a color role to give yourself.").withComponents(ActionRow.of(SelectMenu.of("select-color-role", colorList).withPlaceholder("Select a color role")));
		} catch (Exception e) {
			e.printStackTrace();
			return event.reply("There was an exception trying to generate a color role select menu.");
		}
	}
	
	@Override
	public Permission botRequired() {
		return Permission.MANAGE_GUILD;
	}
	
	@Override
	public Permission memberRequired() {
		return Permission.MANAGE_GUILD;
	}
}
