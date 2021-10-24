package com.bwbjustin.justinbot.selectmenus;

import java.util.HashMap;
import java.util.List;

import com.bwbjustin.justinbot.interactions.JBSelectMenu;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.SelectMenuInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import reactor.core.publisher.Mono;

public class SelectColorRoleSelectMenu implements JBSelectMenu {
	public static final HashMap<Long, List<Long>> CONTENTS = new HashMap<>();
	
	@Override
	public Mono<Void> handle(SelectMenuInteractionEvent event) {
		try {
			Member member = event.getInteraction().getMember().get();
			
			member.addRole(Snowflake.of(event.getValues().get(0))).block();
			
			for (Role role : member.getRoles().filter(x -> CONTENTS.get(event.getInteraction().getGuild().block().getId().asLong()).contains(x.getId().asLong())).toIterable())
				member.removeRole(role.getId()).block();
			
			return event.reply("Color role successfully selected!").withEphemeral(true);
		} catch (Exception e) {
			return event.reply("There was an exception trying to select a color role.").withEphemeral(true);
		}
	}
}
