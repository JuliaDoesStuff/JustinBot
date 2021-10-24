package com.bwbjustin.justinbot.listeners;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.bwbjustin.justinbot.interactions.Interactions;
import com.bwbjustin.justinbot.interactions.JBCommand;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.MessageCreateFields.File;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;

public class ChatInputInteractionEventListener implements JBEventListener<ChatInputInteractionEvent> {
	@Override
	public void handle(ChatInputInteractionEvent event) {
		try {
			JBCommand command = Interactions.COMMANDS.get(event.getCommandName());
			Member member = event.getInteraction().getMember().get();
			PermissionSet botPermissions = event.getInteraction().getGuild().block().getSelfMember().block().getBasePermissions().block();
			PermissionSet memberPermissions = member.getBasePermissions().block();
			
			if (
				command.memberRequired() != null &&
				!event.getInteraction().getGuild().block().getOwner().block().getId().equals(member.getId()) &&
				!memberPermissions.contains(Permission.ADMINISTRATOR) &&
				!memberPermissions.contains(command.memberRequired())
			) {
				event.reply("You do not have the required permission: `" + command.memberRequired() + "`").withEphemeral(true).block();
				return;
			}
			
			if (
				command.botRequired() != null &&
				!botPermissions.contains(Permission.ADMINISTRATOR) &&
				!botPermissions.contains(command.botRequired())
			) {
				event.reply("I do not have the required permission: `" + command.botRequired() + "`").block();
				return;
			}
			
			command.handle(event).block();
		} catch (Exception e) {
			e.printStackTrace();
			
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			
			event.reply("There was an exception, and the request could not be completed.").then(
				event.editReply().withFiles(File.of("exception.txt", new ByteArrayInputStream(writer.toString().getBytes())))
			).subscribe();
		}
	}
}
