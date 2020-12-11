 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 *
 * @author sqlitetutorial.net
 */
public class ConnectToDatabase {
     /**
     * Connect to a sample database
     */
    Connection conn = null;
    String url = "jdbc:sqlite:C:/Users/Administrator/Desktop/java/FX/database.db";
    public void connect() {
        
        try {
            // db parameters
            //this.url = "jdbc:sqlite:C:/Users/Administrator/Desktop/java/FX/database.db";
            // create a connection to the database
            this.conn = DriverManager.getConnection(this.url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    public String createNewTable(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
        Date now = new Date();
        String sql = "CREATE TABLE IF NOT EXISTS " + "SOIR_NUMBER4"  + "\n"
                + "	(cycle_time text NOT NULL,\n"
                /*
                + "	voltage double NOT NULL,\n"
                + "	current double NOT NULL,\n"
                + "	resistance double NOT NULL,\n"
                
                + "	tc1 int NOT NULL,\n"
                + "	tc2 int NOT NULL,\n"
                + "	tc3 int NOT NULL,\n"
                + "	tc4 int NOT NULL,\n"
                + "	tc5 int NOT NULL,\n"
                + "	tc6 int NOT NULL,\n"
                + "	tc7 int NOT NULL,\n"
                + "	tc8 int NOT NULL,\n"
                + "	tc9 int NOT NULL,\n"
                */
                + "	tc10 int NOT NULL)";
               
        String sampleData = "INSERT INTO " + "SOIR_NUMBER4"  +" (cycle_time,tc10) VALUES ('time',10)" ;        
        String sqlSorgu = "SELECT * FROM SOIR_NUMBER4" ;
        try {
            if (this.conn == null) {
                this.conn = DriverManager.getConnection(this.url);
            }
            Statement stmt = this.conn.createStatement();
            
            stmt.execute(sql);
            stmt.execute(sampleData);
            ResultSet rs  = stmt.executeQuery(sqlSorgu);
            //System.out.println("" + rs);
            
            
            while (rs.next()) {
                System.out.println("in");
                System.out.println("" + rs.getInt("tc10")  +"\t" 
                                    + rs.getDouble("cycle_time"));
                                                 
            }
        } 
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return "SOIR_NUMBER"+now;
    }
    
    public void close(){
        try {
                if (this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
    }
    
    public void writeToDatabase(){
        try {
                if (this.conn != null) {
                    this.conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
    }
    
    
    public static void main(String[] args){
        System.out.println("SQL");
    }
}
   
