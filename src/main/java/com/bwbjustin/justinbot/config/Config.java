package com.bwbjustin.justinbot.config;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;

import discord4j.core.object.presence.Activity.Type;
import discord4j.core.object.presence.Status;

public class Config {
	public static String DB_NAME;
	public static String TOKEN;
	public static ArrayList<Long> COMMAND_GUILDS;
	public static String ACTIVITY;
	public static Type ACTIVITY_TYPE;
	public static Status STATUS;
	
	static {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(Path.of("justinbot.properties").toAbsolutePath().toString()));
			
			DB_NAME = props.getProperty("db_name");
			TOKEN = props.getProperty("token");
			COMMAND_GUILDS = new ArrayList<>();
			for (String guild : props.getProperty("command_guilds").split(","))
				COMMAND_GUILDS.add(Long.parseLong(guild));
			ACTIVITY = props.getProperty("activity");
			switch (props.getProperty("activity_type").toLowerCase()) {
				case "playing":
					ACTIVITY_TYPE = Type.of(0);
					break;
				case "streaming":
					ACTIVITY_TYPE = Type.of(1);
					break;
				case "listening":
					ACTIVITY_TYPE = Type.of(2);
					break;
				case "watching":
					ACTIVITY_TYPE = Type.of(3);
					break;
				case "custom":
					ACTIVITY_TYPE = Type.of(4);
					break;
				case "competing":
					ACTIVITY_TYPE = Type.of(5);
					break;
				default:
					ACTIVITY_TYPE = Type.of(-1);
					break;
			}
			STATUS = Status.of(props.getProperty("status").toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
}
