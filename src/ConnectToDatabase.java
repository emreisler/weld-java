
 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 

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
    String currentTableName;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd__HH_mm_ss");
    /*
    public ConnectToDatabase(){
        try {
            
            this.conn = DriverManager.getConnection(this.url);
            System.out.println("####################################################");
            System.out.println("Connection to SQLite has been established.");
            System.out.println("####################################################");
        } catch (SQLException e) {
            System.out.println("####################################################");
            System.out.println("Couldn' t connect to database because of : "+e);
            System.out.println("####################################################");
            System.out.println(e.getMessage());
        } 
    }
    */
    public void connect() {
        if (this.conn == null){
            try {
            
            this.conn = DriverManager.getConnection(this.url);
            System.out.println("####################################################");
            System.out.println("Connection to SQLite has been established.");
            System.out.println("####################################################");
            } catch (SQLException e) {
            System.out.println("####################################################");
            System.out.println("Couldn' t connect to database because of : "+e);
            System.out.println("####################################################");
            System.out.println(e.getMessage());
            } 
        
        }
        else{
            System.out.println("####################################################");
            System.out.println("Connection to SQLite has already been established.");
            System.out.println("####################################################");
        }    
        
    }
    public String createNewTable(){
        
        
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        
        //Date now = new Date();
        //System.out.println(""+dtf.format(now));
        String formattedTime = ""+ this.dtf.format(now);
        this.currentTableName = "YIL_AY_GUN__SAAT_DK_SN_"  + formattedTime;
        
        
        String sql = "CREATE TABLE IF NOT EXISTS " + this.currentTableName + "\n"
                + " (cycle_time text NOT NULL,\n"
                + " voltage double NOT NULL,\n"
                + " current double NOT NULL,\n"
                + " resistance double NOT NULL,\n"
                + " tc1 int NOT NULL,\n"
                + " tc2 int NOT NULL,\n"
                + " tc3 int NOT NULL,\n"
                + " tc4 int NOT NULL,\n"
                + " tc5 int NOT NULL,\n"
                + " tc6 int NOT NULL,\n"
                + " tc7 int NOT NULL,\n"
                + " tc8 int NOT NULL,\n"
                + " tc9 int NOT NULL,\n"
                + " tc10 int NOT NULL)";
              
             
        
        try {
            if (this.conn == null) {
                this.conn = DriverManager.getConnection(this.url);
                System.out.println("New conn created");
            }
            
            Statement stmt = this.conn.createStatement();
            
            stmt.execute(sql);
            /*
            System.out.println("####################################################");
            System.out.println("Table created succesfully" + this.currentTableName);
            System.out.println("####################################################");
            */
        } 
        catch (SQLException ex) {
            System.out.println("####################################################");
            System.out.println("Creation error : " + ex);
            System.out.println("####################################################");
            System.out.println(ex.getMessage());
        }
       
        return "SOIR_NUMBER"+now;
    }
    
    
    
    public void writeToDatabase(double[] psValues, int[] plcValues){
        try {
                if (this.conn == null) {
                    this.conn = DriverManager.getConnection(this.url);
                }
                
                LocalDateTime now = LocalDateTime.now();
                String time = this.dtf.format(now);
                //System.out.println(this.currentTableName);
                String sampleData = "INSERT INTO " + this.currentTableName +
                " (cycle_time,voltage,current,resistance,tc1,tc2,tc3,tc4,tc5,tc6,tc7,tc8,tc9,tc10) VALUES ('" + time + "',"+
                psValues[0]+","+
                psValues[1]+","+
                psValues[2]+","+
                plcValues[0]+","+
                plcValues[1]+","+
                plcValues[2]+","+
                plcValues[3]+","+
                plcValues[4]+","+
                plcValues[5]+","+
                plcValues[6]+","+
                plcValues[7]+","+
                plcValues[8]+","+
                plcValues[9]+")";
                
                //System.out.println("sampleData: " + sampleData);
                Statement stmt = this.conn.createStatement();
                stmt.execute(sampleData);
                
            } catch (SQLException ex) {
                System.out.println("####################################################");
                System.out.println("Writing error : " + ex);
                System.out.println("####################################################");
                System.out.println(ex.getMessage());
            }
    }
    
    public void readFromDatabase(){
        String sqlSorgu = "SELECT * FROM " + this.currentTableName;
        //System.out.println("Sorgu: " + sqlSorgu);
        try{
            
            Statement stmt = this.conn.createStatement();
        
            ResultSet rs  = stmt.executeQuery(sqlSorgu);
            /*
            while (rs.next()) {
                
                System.out.println("in");
                /*
                System.out.println("cycle_time : " + rs.getString("cycle_time") + "\n" +
                                   "Voltage : " + rs.getDouble("voltage")  +"\n" + 
                                   "Current : " + rs.getDouble("current")  +"\n" +
                                   "Resistance : " + rs.getDouble("resistance")  +"\n" +
                                   "TC1 : " + rs.getInt("tc1")  +"\n" +
                                   "TC2 : " + rs.getInt("tc2")  +"\n" +
                                   "TC3 : " + rs.getInt("tc3")  +"\n" +
                                   "TC4 : " + rs.getInt("tc4")  +"\n" +
                                   "TC5 : " + rs.getInt("tc5")  +"\n" +
                                   "TC6 : " + rs.getInt("tc6")  +"\n" +
                                   "TC7 : " + rs.getInt("tc7")  +"\n" +
                                   "TC8 : " + rs.getInt("tc8")  +"\n" +
                                   "TC9 : " + rs.getInt("tc9")  +"\n" +
                                   "TC10 : " + rs.getInt("tc10")  +"\n"
                                   );
                 
            }
            */
        }
        catch(Exception e){
            System.out.println("####################################################");
            System.out.println("Reading error : " + e);
            System.out.println("####################################################");
        }
        
        
    }
    
    public void close(){
        try {
                if (this.conn != null) {
                    this.conn.close();
                    this.conn = null;
                    System.out.println("####################################################");
                    System.out.println("Database connection has been closed");
                    System.out.println("####################################################");
                    System.out.println("Closed");
                }
            } catch (SQLException ex) {
                System.out.println("####################################################");
                System.out.println("Database connection couldn' t closed");
                System.out.println("####################################################");
                System.out.println(ex.getMessage());
                System.out.println("Closed");
                
            }
        
    }
    
    private void clearDatabase(){
         System.out.println("Not implemented");
    }
    public static void main(String[] args){
        System.out.println("SQL");
    }
    
    
}
   
