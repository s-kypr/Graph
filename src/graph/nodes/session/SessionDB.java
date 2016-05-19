package graph.nodes.session;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by sofia on 4/19/16.
 */
public class SessionDB {
    private Connection connection;
    private String table;

    public SessionDB(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    public ArrayList<Timestamp> getUserTimestamps(String username) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select DISTINCT timestamp from "+ table +" where username = ? order by timestamp asc;");
        statement.setString(1,username);
        ResultSet resultSet = statement.executeQuery();

        ArrayList<Timestamp> timestamps = new ArrayList<Timestamp>();
        while (resultSet.next()) {
            timestamps.add(resultSet.getTimestamp(1));
        }

        return timestamps;
    }

    public ArrayList<Integer> getSessionItems (String username, Timestamp firstDay, Timestamp lastDay) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select distinct articleID from "+table+" where username = ? and timestamp BETWEEN ? and ?");
        statement.setString(1,username);
        statement.setTimestamp(2,firstDay);
        statement.setTimestamp(3,lastDay);
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
