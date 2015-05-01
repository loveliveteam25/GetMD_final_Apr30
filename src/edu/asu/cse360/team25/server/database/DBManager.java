package edu.asu.cse360.team25.server.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class DBManager {

	protected Connection conn;
	
	protected Statement stmt;
	
	public DBManager() {
		
		
		
	}
	
	public boolean connect() {
		
		
		
		
		return false;
	}
	
	public boolean disconnect() {
		
		return false;
	}
	
	public boolean reconnect() {
		
		return false;
	}
	
	public List<Object []> queryTable(String table, String[] columns, String condition, String[] type) {
		
		String[] types = {"Integer", "String"};
		
		Object [] result = {new Integer(109), new String("eric")};
		
		return null;
	}
	
	public boolean instertTable(String table, String[] columns, String[] type, String[] data) {
		
		
		return false;
	}
	
	public boolean updateTable(String table, String[] columns, String condition, String[] type, String[] data) {
		
		
		return false;
	}
	
	
	
	
	
	protected ResultSet executeSQL(String sql) {
		
		
		return null;
	}
	
	
	
	
}
