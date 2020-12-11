public class SqlTrial{
    public static void main(String[] args){
    
    ConnectToDatabase db = new ConnectToDatabase();
    db.connect();
    db.createNewTable();
    
    }



}