package org.lia;

import org.lia.entity.PointEntity;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBManager {

    String username;
    String password;
    String url = "jdbc:postgresql://localhost:5432/studs";
    Connection connection;

    public DBManager() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.cfg")) {
            Properties properties = new Properties();
            properties.load(input);
            this.username = properties.getProperty("jakarta.persistence.jdbc.user");
            this.password = properties.getProperty("jakarta.persistence.jdbc.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e);
        }

    }


    public int sendPoint(PointEntity point) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO Points(x, y, r, result, date)" + "VALUES (?, ?, ?, ?, ?) RETURNING id");
        statement.setDouble(1, point.getX());
        statement.setDouble(2, point.getY());
        statement.setDouble(3, point.getR());
        statement.setBoolean(4, point.getResult());
        statement.setTimestamp(5, point.getDate());
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("id");
    }

    public ArrayList<PointEntity> getPoints() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Points ORDER BY date DESC LIMIT 10;");
        ResultSet result = statement.executeQuery();

        ArrayList<PointEntity> pointList = new ArrayList<>();

        while (result.next()) {
            int id = result.getInt("id");
            double x = result.getDouble("x");
            double y = result.getDouble("y");
            double r = result.getDouble("r");
            Timestamp date = result.getTimestamp("date");
            boolean res = result.getBoolean("result");
            PointEntity point = new PointEntity();
            point.setX(x);
            point.setY(y);
            point.setR(r);
            point.setDate(date);
            point.setId(id);
            point.setResult(res);
            pointList.add(point);
        }

        return pointList;
    }

}
