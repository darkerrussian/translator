package com.cage.translator.connection;

import netscape.javascript.JSObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.Date;
import java.util.StringTokenizer;

public class SQLiteDB {

    Connection connection;
    Statement statement;
    PreparedStatement ps;

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:base.db");
            statement = connection.createStatement();
            checkCreateMainTable();
        } catch (SQLException e) {
            System.out.println("Cant Connect");
            e.printStackTrace();
        }

    }
    private void checkCreateMainTable() throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS input_info (" +
                "    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "    time_of_request CHAR (30) NOT NULL, " +
                "    params CHAR (255) NOT NULL," +
                        "    from_to CHAR (255) NOT NULL,"+
                "    ip CHAR(30) NOT NULL );");
    }

    public void addDataToTable(String text, String from, String to) {

        Date date = new Date();
        try {
            connection.setAutoCommit(false);

            ps = connection.prepareStatement("INSERT INTO input_info(time_of_request, params, from_to, ip) VALUES (?, ?,?, ?)");
            ps.setString(1, date.toString());
            ps.setString(2, text);
            ps.setString(3,from + "-" + to);
            ps.setString(4, getUserIp());
            ps.addBatch();
            ps.executeBatch();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }



    private String getUserIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String xForwarded = request.getHeader("X-Forwarded-For");
        if(xForwarded == null)
            return request.getRemoteAddr();
        else {
            return new StringTokenizer(xForwarded, ",").nextToken().trim();
        }



    }



}
