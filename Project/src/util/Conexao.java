package util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Conexao {

    private static String url;
    private static String user;
    private static String password;

    static {
    	
        try {
        	
            Properties prop = new Properties();
            InputStream input = new FileInputStream("config.txt"); // arquivo na mesma pasta que src
            prop.load(input);

            url = prop.getProperty("db.url");
            user = prop.getProperty("db.user");
            password = prop.getProperty("db.password");

        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
    }

    public static Connection getConnection() throws Exception {
    	
        return DriverManager.getConnection(url, user, password);
        
    }
}