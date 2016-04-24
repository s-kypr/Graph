package user;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by sofia on 4/17/16.
 */
public class UserDB {
    private Connection connection;
    private String table;

    public UserDB(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public ArrayList<String> getDBusernames() throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select distinct username from "+table);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        ArrayList<String> usernames = new ArrayList<String>();

        int j=1;
        while (resultSet.next()) {
            int i = 1;
            String columnValue = resultSet.getString(i);
            System.out.print(j+") "+columnValue + " -> " + resultSetMetaData.getColumnName(i));
            j++;
            System.out.println("");
            usernames.add(columnValue);
        }

        return usernames;
    }

    public ArrayList<Integer> getUserItems (String username) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select distinct articleID from "+table+" where username = ? ");
        statement.setString(1,username);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

        ArrayList<Integer> items = new ArrayList<Integer>();

        int j=1;
        while (resultSet.next()) {
            int i = 1;
            String columnValue = resultSet.getString(i);
//            System.out.print(j+") "+columnValue + " -> " + resultSetMetaData.getColumnName(i));
            j++;
//            System.out.println("");
            items.add(Integer.parseInt(columnValue));
        }

        return items;
    }
}
