package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.net.URISyntaxException;
import java.net.URI;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@SpringBootApplication
public class HerokuConnectApplication {

    @RequestMapping("/")
    public String home(Model model) {
        return "home";
    }

    @RequestMapping("/createcontactform")
    public String createUserForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "createcontact";
    }

	@RequestMapping("/contacts")
    public String contacts(Model model) {
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String sql;
            sql = "SELECT id, sfid,  firstname, lastname, name, email FROM salesforce.contact";
            ResultSet rs = stmt.executeQuery(sql);
            StringBuffer sb = new StringBuffer();
            List contacts = new ArrayList<>();
            // Extract data from result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String sfid = rs.getString("sfid");
                String name = rs.getString("name");
                String first = rs.getString("firstname");
                String last = rs.getString("lastname");
                String email = rs.getString("email");
                contacts.add(new Contact(id, sfid, first, last, email));
            }
            model.addAttribute("contacts", contacts);
            return "contact";
        } catch (Exception e) {
            return e.toString();
        }
    }

    @RequestMapping(value="/createuser", method= RequestMethod.POST)
    public String createUser(@ModelAttribute Contact contact, Model model) {
        model.addAttribute("user", contact);
        int id = contact.getId();
        String first = contact.getFirst();
        String last = contact.getLast();
        String email = contact.getEmail();
        //String city = contact.getCity();
        //String company = contact.getCompany();
        try {
            Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            String sql;
            //SELECT id, sfid,  firstname, lastname, name, email FROM salesforce.contact
            sql = "insert into salesforce.contact(firstname, lastname, name, company, city) values " +
                    "('" + first  + "', '" + last + " ',' " + email +  "');";
            ResultSet rs = stmt.executeQuery(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "result";
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath()
                + "?sslmode=require";

		return DriverManager.getConnection(dbUrl, username, password);
	}

	public static void main(String[] args) {
		SpringApplication.run(HerokuConnectApplication.class, args);
	}
}
