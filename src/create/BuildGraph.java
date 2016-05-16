package create;


import graph.Graph;
import item.ItemDB;
import session.SessionDB;
import user.UserDB;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by sofia on 4/13/16.
 */
public class BuildGraph {


    public static void main(String[] args) throws Exception {

        /******* BUILD GRAPH ***********/
        int timewindow = 30;

        /*** establish a connection ***/
        Database db = new Database("users_posted_papers");
        Connection connection = null;
        connection = Database.getConnection();

        System.out.println("Initializing graph..");

        /* init graph*/
        Graph graph = new Graph(timewindow, Database.getFistDate(connection, db.getTABLE()));
        System.out.println("timewindow: "+timewindow+" days");
        System.out.println("Creating nodes...");

        /** create userNodes -> without items **/
        UserDB userDB = new UserDB(connection, db.getTABLE());
        ArrayList<String> usernames = userDB.getDBusernames();
        graph.createUserNodes(usernames);

        /** create itemNodes -> without sessions, users**/
        ItemDB itemDB = new ItemDB(connection,db.getTABLE());
        ArrayList<Integer> items = itemDB.getDBitems();
        graph.createItemNodes(items, itemDB);


        /** create sessionNodes -> without items **/
        SessionDB sessionDB = new SessionDB(connection,db.getTABLE());
        graph.createSessionNodes(sessionDB);


        /*** MAKE CONNECTIONS***/

        System.out.println("Creating edges...");

        /* update user nodes with items*/
        graph.updateUserNodes(userDB);

        /* update session nodes with items*/
        graph.updateSessionNodes(sessionDB);

        /* update item nodes with users*/
        graph.updateItemNodes(itemDB);

//        System.out.println(graph.printSessionNodes());

//        System.out.println(graph.printItemNodes());


        System.out.println("\nNodes:");
        System.out.println("Users   : "+graph.getUserNodes().size());
        System.out.println("Items   : "+graph.getItemNodes().size());
        System.out.println("Sessions: "+graph.getSessionNodes().size());

        System.out.println("\nPairs:");
        System.out.println("user    <-> item : "+graph.getUserItemCon());
        System.out.println("session <-> item : "+graph.getItemSessionCon());

        System.out.println(graph.printItemNodes());

    }






}
