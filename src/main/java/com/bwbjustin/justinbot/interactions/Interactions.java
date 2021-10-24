package com.bwbjustin.justinbot.interactions;

import java.util.HashMap;

import com.bwbjustin.justinbot.buttons.GoToCancelButton;
import com.bwbjustin.justinbot.buttons.GoToNextButton;
import com.bwbjustin.justinbot.buttons.GoToPreviousButton;
import com.bwbjustin.justinbot.commands.AddColorCommand;
import com.bwbjustin.justinbot.commands.GetColorCommand;
import com.bwbjustin.justinbot.commands.InviteCommand;
import com.bwbjustin.justinbot.commands.ListColorsCommand;
import com.bwbjustin.justinbot.commands.PingCommand;
import com.bwbjustin.justinbot.commands.RemoveColorCommand;
import com.bwbjustin.justinbot.commands.SelectColorsCommand;
import com.bwbjustin.justinbot.commands.UpdateColorCommand;
import com.bwbjustin.justinbot.selectmenus.SelectColorRoleSelectMenu;

public class Interactions {
	public static final HashMap<String, JBCommand> COMMANDS = new HashMap<>();
	public static final HashMap<String, JBButton> BUTTONS = new HashMap<>();
	public static final HashMap<String, JBSelectMenu> SELECT_MENUS = new HashMap<>();
	
	static {
		COMMANDS.put("add-color", new AddColorCommand());
		COMMANDS.put("get-color", new GetColorCommand());
		COMMANDS.put("invite", new InviteCommand());
		COMMANDS.put("list-colors", new ListColorsCommand());
		COMMANDS.put("ping", new PingCommand());
		COMMANDS.put("remove-color", new RemoveColorCommand());
		COMMANDS.put("select-colors", new SelectColorsCommand());
		COMMANDS.put("update-color", new UpdateColorCommand());
		
		BUTTONS.put("go-to-cancel", new GoToCancelButton());
		BUTTONS.put("go-to-next", new GoToNextButton());
		BUTTONS.put("go-to-previous", new GoToPreviousButton());
		
		SELECT_MENUS.put("select-color-role", new SelectColorRoleSelectMenu());
	}
}
