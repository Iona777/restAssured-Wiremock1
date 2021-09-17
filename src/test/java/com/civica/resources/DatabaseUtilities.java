package com.civica.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseUtilities
{

    //NOTE: Need to add dependency for the driver for your particular DB type to PM, e.g. mysql-connector-java
    public Connection getDBConnection(String URL, String username, String password) throws SQLException
    {
        //This is a try-with-resources statement and makes sure that your connection automatically gets closed
        // at the end of the block, without you having to close it manually.
        try (Connection conn = DriverManager.getConnection(URL, username, password))
        {

            // "0" means disabling the timeout, when doing isValid checks
            boolean isValid = conn.isValid(0);
            System.out.println("Do we have a valid db connection? = " + isValid);

            return  conn;
        }
    }

    public String readSQLFile(String fileName) throws FileNotFoundException
    {
        File myFileObj = new File(fileName);
        Scanner myReader = new Scanner(myFileObj);
        StringBuilder builder = new StringBuilder();

        while (myReader.hasNext())
        {
            builder.append(myReader.next());
        }
        myReader.close();

        return  builder.toString();
    }

}
