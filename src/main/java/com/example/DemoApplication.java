package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.URISyntaxException;
import java.net.URI;


@Controller
@SpringBootApplication
public class DemoApplication {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

	@RequestMapping("/employees")
	@ResponseBody
	String employees() {
		try {
			Connection connection  = getConnection();
			Statement stmt = connection.createStatement();
			String sql;
			sql = "SELECT id, first, last, age FROM Employees";
			ResultSet rs = stmt.executeQuery(sql);
			StringBuffer sb = new StringBuffer();
			// Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				int id  = rs.getInt("id");
				int age = rs.getInt("age");
				String first = rs.getString("first");
				String last = rs.getString("last");

				//Display values

				sb.append("ID: " + id + "<br/>");
				sb.append("Age: " + age + "<br/>");
				sb.append("First: " + first + "<br/>");
				sb.append("Last: " + last + "<br/>");
			}
			return sb.toString();
		}catch(Exception e) {
			return e.toString();
		}
	}

	@RequestMapping("/contacts")
	@ResponseBody
	String contacts() {
		try {
			Connection connection  = getConnection();
			Statement stmt = connection.createStatement();
			String sql;
			sql = "SELECT id, name FROM salesforce.contact";
			ResultSet rs = stmt.executeQuery(sql);
			StringBuffer sb = new StringBuffer();
			// Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				int id  = rs.getInt("id");
				String name = rs.getString("name");

				//sb.append("id: " + id + "<br/>");
				sb.append(" " + name + "<br/>");
			}
			return sb.toString();
		}catch(Exception e) {
			return e.toString();
		}
	}
	private static Connection getConnection() throws URISyntaxException, SQLException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

		return DriverManager.getConnection(dbUrl, username, password);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
