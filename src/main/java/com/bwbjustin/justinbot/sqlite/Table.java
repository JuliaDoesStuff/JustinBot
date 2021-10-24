package com.bwbjustin.justinbot.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {
	private String name;
	private Connection connection;
	
	public static EntryList entryList(String... entries) {
		return new EntryList(entries);
	}
	
	public Table(String name, Connection connection) {
		this.name = name;
		this.connection = connection;
	}
	
	public boolean exists(EntryList conditions) throws SQLException {
		ArrayList<String> conditionList = new ArrayList<>();
		
		for (Entry entry : conditions.list)
			conditionList.add(entry.key + " = \"" + entry.value + "\"");
		
		String query = "EXISTS (SELECT * FROM " + name + " WHERE " + String.join(" AND ", conditionList) + ")";
		return connection.createStatement().executeQuery("SELECT " + query + ";").getInt(query) > 0;
	}
	
	public ResultSet select(EntryList conditions) throws SQLException {
		ArrayList<String> conditionList = new ArrayList<>();
		
		for (Entry entry : conditions.list)
			conditionList.add(entry.key + " = \"" + entry.value + "\"");
		
		return connection.createStatement().executeQuery("SELECT * FROM " + name + " WHERE " + String.join(" AND ", conditionList) + ";");
	}
	
	public ResultSet select(EntryList conditions, int offset, int limit) throws SQLException {
		ArrayList<String> conditionList = new ArrayList<>();
		
		for (Entry entry : conditions.list)
			conditionList.add(entry.key + " = \"" + entry.value + "\"");
		
		return connection.createStatement().executeQuery("SELECT * FROM " + name + " WHERE " + String.join(" AND ", conditionList) + " LIMIT " + offset + ", " + limit + ";");
	}
	
	public int insert(EntryList entries) throws SQLException {
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> values = new ArrayList<>();
		
		for (Entry entry : entries.list) {
			keys.add(entry.key);
			values.add(entry.value);
		}
		
		return connection.createStatement().executeUpdate("INSERT INTO " + name + " (" + String.join(", ", keys) + ") VALUES (\"" + String.join("\", \"", values) + "\");");
	}
	
	public int update(EntryList conditions, EntryList values) throws SQLException {
		ArrayList<String> conditionList = new ArrayList<>();
		ArrayList<String> valueList = new ArrayList<>();
		
		for (Entry entry : conditions.list)
			conditionList.add(entry.key + " = \"" + entry.value + "\"");
		
		for (Entry entry : values.list)
			valueList.add(entry.key + " = \"" + entry.value + "\"");
		
		return connection.createStatement().executeUpdate("UPDATE " + name + " SET " + String.join(", ", valueList) + " WHERE " + String.join(" AND ", conditionList) + ";");
	}
	
	public int delete(EntryList conditions) throws SQLException {
		ArrayList<String> conditionList = new ArrayList<>();
		
		for (Entry entry : conditions.list)
			conditionList.add(entry.key + " = \"" + entry.value + "\"");
		
		return connection.createStatement().executeUpdate("DELETE FROM " + name + " WHERE " + String.join(" AND ", conditionList) + ";");
	}
	
	private static class Entry {
		public String key;
		public String value;
		
		public Entry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
	
	public static class EntryList {
		public ArrayList<Entry> list;
		
		public EntryList(String... entries) {
			this.list = new ArrayList<>();
			
			for (int i = 0; i < entries.length; i += 2)
				list.add(new Entry(entries[i], entries[i + 1]));
		}
		
		public EntryList with(String... entries) {
			ArrayList<String> otherList = new ArrayList<>();
			
			for (Entry entry : list) {
				otherList.add(entry.key);
				otherList.add(entry.value);
			}
			
			otherList.addAll(Arrays.asList(entries));
			
			return new EntryList(otherList.toArray(new String[otherList.size()]));
		}
		
		public EntryList without(String... entryKeys) {
			ArrayList<String> otherList = new ArrayList<>();
			
			for (Entry entry : list) {
				otherList.add(entry.key);
				otherList.add(entry.value);
			}
			
			for (int i = 0; i < otherList.size(); i += 2) {
				for (int j = 0; j < entryKeys.length; ++j) {
					if (otherList.get(i).equals(entryKeys[j])) {
						otherList.remove(i);
						otherList.remove(i);
					}
				}
			}
			
			return new EntryList(otherList.toArray(new String[otherList.size()]));
		}
	}
}
