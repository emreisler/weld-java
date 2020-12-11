
import de.re.easymodbus.modbusclient.ModbusClient;
import java.util.concurrent.ThreadLocalRandom;
import java.io.*;
import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Write a description of class ModbusTrial here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Plc extends Thread{
    public ModbusClient modbusClient;
    /*
    public Plc(){
        try{
            this.modbusClient = new ModbusClient("192.168.1.30", 502);
            this.modbusClient.Connect();
        }
        catch(UnknownHostException e){
         
            System.out.println("////////////////////////////////////////////");
            System.out.println("NOT CONNECTED TO PLC : UnknownHostException");
            System.out.println("////////////////////////////////////////////");
            System.out.println(e.toString());
            
            
        }
        catch( IOException e){
            System.out.println("//////////////////////////////////");
            System.out.println("NOT CONNECTED TO PLC : IOException");
            System.out.println("//////////////////////////////////");
            System.out.println(e.toString());
        }
        
    }
    */
    
    public void run(){
        try{
            this.modbusClient = new ModbusClient("192.168.1.30", 502);
            this.modbusClient.Connect();
            System.out.println("//////////////////////////////////////////////////");
            System.out.println("//////       CONNECTED TO PLC       //////////////");
            System.out.println("//////////////////////////////////////////////////");
        }
        catch(UnknownHostException e){
         
            System.out.println("//////////////////////////////////////////////////");
            System.out.println("///NOT CONNECTED TO PLC : UnknownHostException///");
            System.out.println("//////////////////////////////////////////////////");
            System.out.println(e.toString());
            
            
        }
        catch(IOException e){
            System.out.println("////////////////////////////////////////");
            System.out.println("///NOT CONNECTED TO PLC : IOException///");
            System.out.println("////////////////////////////////////////");
            System.out.println(e.toString());
        }
    }
    public int read(int register, boolean simulationMode){
        
      if(simulationMode){
          return 0;
      }
      else{
        try{
          //Integer value = ThreadLocalRandom.current().nextInt(50);
          int[] tempArray = new int[1];
          tempArray = modbusClient.ReadHoldingRegisters(register, 2);
          int temp = tempArray[0] / 10 ;
          return temp;
        }
        catch(Exception e) {
          //  Block of code to handle errors
          System.out.println("Plc read error : "+e);
          
          return 0;
        }

      }
    }

}
