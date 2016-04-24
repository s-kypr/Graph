package create;

import java.sql.*;

/**
 * Created by sofia on 4/17/16.
 */
public class Database {

    private static String SERVER = "localhost:3306";
    private static String DATABASE = "CiteULike";
    private static String USER = "root";
    private static String PASSWORD = "1";
    private String TABLE;

    public Database(String TABLE) {
        this.TABLE = TABLE;
    }

    public String getTABLE() {
        return TABLE;
    }

    public static Connection getConnection() throws Exception {

        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + "?" + "user=" + USER + "&password=" + PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to get mysql driver: " + e);
        } catch (SQLException e) {
            System.err.println("Unable to connect to server: " + e);
        }

        return conn;
    }

    public static Timestamp getFistDate(Connection connection, String table) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select timestamp from "+ table +" order by timestamp asc limit 1");
        ResultSet resultSet = statement.executeQuery();

        Timestamp fistDate = null;
        while (resultSet.next()) {
            fistDate = resultSet.getTimestamp(1);
        }
        return fistDate;
    }

}

