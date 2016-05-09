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

        /*** establish a connection ***/
        Database db = new Database("users_posted_papers");
        Connection connection = null;
        connection = db.getConnection();

        /* init graph*/
        Graph graph = new Graph(30, db.getFistDate(connection, db.getTABLE()));

        /** create userNodes -> without items **/
        UserDB userDB = new UserDB(connection, db.getTABLE());
        ArrayList<String> usernames = userDB.getDBusernames();
        graph.createUserNodes(usernames);

        /** create itemNodes -> without sessions, users**/
        ItemDB itemDB = new ItemDB(connection,db.getTABLE());
        ArrayList<Integer> items = itemDB.getDBitems();
        graph.createItemNodes(items);


        /** create sessionNodes -> without items **/
        SessionDB sessionDB = new SessionDB(connection,db.getTABLE());
        graph.createSessionNodes(sessionDB);


        /*** MAKE CONNECTIONS***/

        System.out.println("MAKING CONNECTIONS");

        /* update user nodes with items*/
        graph.updateUserNodes(userDB);

        /* update session nodes with items*/
        graph.updateSessionNodes(sessionDB);

        /* update item nodes with users*/
        graph.updateItemNodes(itemDB);



        System.out.println("Fist day: "+graph.getFirstDay().getTime());

//        System.out.println(graph.printSessionNodes());

        System.out.println(graph.printItemNodes());

//        itemDB.getItemSessions(42);





    }






}
