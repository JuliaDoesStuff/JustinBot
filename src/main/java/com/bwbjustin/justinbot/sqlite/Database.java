package com.bwbjustin.justinbot.sqlite;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private Connection connection;
	
	public Database(String name) throws SQLException {
		this.connection = DriverManager.getConnection("jdbc:sqlite:" + Path.of(name + ".db").toAbsolutePath().toString());
		
		this.connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS colors (guild BIGINT, role BIGINT, name VARCHAR(255), rgb INT);");
	}
	
	public Table select(String name) {
		return new Table(name, connection);
	}
}
