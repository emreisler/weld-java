/**
 * @license
 *
 * Copyright 2018-2020 Peter Froud
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.util.Hashtable; // import the Hashtable class
import jvisa.JVisaResourceManager;
import jvisa.JVisaInstrument;

/**
 * Example showing how to use JVisaResourceManager and JVisaInstrument.
 *
 * Here's what it does:<br>
 * (1) Opens the default resource manager<br>
 * (2) Searches for USB VISA instruments<br>
 * (3) If any are instruments are found, it does this for each instrtument:<br>
 * A. Opens the instrument<br>
 * B. Sends the *IDN? command and prints the response<br>
 * C. Closes the instrument<br>
 * (4) Closes the resource manager
 *
 * This is a low-level example. To see how you could use higher-level classes, look at HighLevelExample.java.
 *
 * @author Peter Froud
 */


public  class PowerSupply extends Thread {
         static long minPrime;
         static Hashtable<String, Integer> parameters = new Hashtable<>();
         static boolean simulationMode;
         static int totalTime;
         static double startTime;
         static double ellapsedTime;
         static double remainingTime;
         static int firstStep;
         static int secondStep;
         static int thirdStep;
         static boolean cycleContinue;
         static JVisaResourceManager rm;
         static PowerSupplyExample ps;
         static String emre;
         static boolean criticalSection;
         public double voltageMeasured;
         public double currentMeasured;
         public double resistanceMeasured;
         JVisaInstrument device;

         PowerSupply(long minPrime) {
             this.minPrime = minPrime;
             
         };
         
         public void setParameters(Hashtable<String, Integer> parameters,boolean simulationMode){
            this.parameters = parameters;
            this.simulationMode = simulationMode;
         }
         
         public void connect(){
              try{
                 this.rm = new JVisaResourceManager();
                 //this.ps = new PowerSupplyExample(this.rm, "TCPIP0::192.168.1.32::inst0::INSTR");
                 this.device = this.rm.openInstrument("TCPIP0::192.168.1.32::inst0::INSTR");
                 
                 System.out.println("//////////////////////////////////////////////////");
                 System.out.println("/////////   CONNECTED TO POWER SUPPLY  ///////////");
                 System.out.println("//////////////////////////////////////////////////");
               }
               catch(Exception e){
                 System.out.println("///////////////////////////////////////////////////////////////////////");
                 System.out.println("////////NOT CONNECTED TO POWER SUPPLY BECAUSE OF BELOW ERROR///////////");
                 System.out.println("///////////////////////////////////////////////////////////////////////");  
                 System.out.println("POWER SUPPLY CONNECTION ERROR : "+e);
               }  
         }


         public void run(){
             
            
             this.startTime = System.currentTimeMillis();
             this.firstStep = this.parameters.get("time1");
             this.secondStep = this.parameters.get("time1") + this.parameters.get("time2");
             this.totalTime = this.parameters.get("time1") + this.parameters.get("time2") + this.parameters.get("time3");
             this.cycleContinue = true;
             //PowerSupplyExample().
             if(this.simulationMode){
               runSimulationCycle();
             }
             else{
               try{
                 this.rm = new JVisaResourceManager();
                 this.ps = new PowerSupplyExample(this.rm, "TCPIP0::192.168.1.32::inst0::INSTR");
                 System.out.println("//////////////////////////////////////////////////");
                 System.out.println("/////////   CONNECTED TO POWER SUPPLY  ///////////");
                 System.out.println("//////////////////////////////////////////////////");
               }
               catch(Exception e){
                 System.out.println("///////////////////////////////////////////////////////////////////////");
                 System.out.println("////////NOT CONNECTED TO POWER SUPPLY BECAUSE OF BELOW ERROR///////////");
                 System.out.println("///////////////////////////////////////////////////////////////////////");  
                 System.out.println("POWER SUPPLY CONNECTION ERROR : "+e);
               }  
               runRealCycle();
             }
               //instrument.sendAndReceiveString(':SOURce:CURRent:LEVel:IMMediate:AMPLitude %G' % (self.current1))
          }

          public int runRealCycle(){
            boolean step1End = false;
            boolean step2End = false;
            boolean step3End = false;
            boolean stepChange = false;
            int voltage  = this.parameters.get("voltage1");
            int current = this.parameters.get("current1");
            System.out.println("1st step time : " +this.firstStep);
            System.out.println("2nd step time : " +this.secondStep);
            System.out.println("3th step time : " +this.totalTime);
            while(this.cycleContinue){
              this.ellapsedTime = (System.currentTimeMillis() - this.startTime) / 1000;
              int progressTime = (int) this.ellapsedTime;
              
              
              
              if(this.ellapsedTime > this.firstStep && !(step1End)){
                voltage = this.parameters.get("voltage2");
                current = this.parameters.get("current2");
                step1End = true;
                stepChange = true;
                System.out.println("-----------------");
                System.out.println("Step1 completed");
                System.out.println("-----------------");
              }
              else if(this.ellapsedTime > this.secondStep && !(step2End)){
                voltage = this.parameters.get("voltage3");
                current = this.parameters.get("current3");
                step2End = true;
                stepChange = true;
                System.out.println("-----------------");
                System.out.println("Step2 completed");
                System.out.println("-----------------");
              }
              else if(this.ellapsedTime > this.totalTime && !(step3End)){
                voltage = 0;
                current = 0;
                step3End = true;
                stepChange = true;
                this.cycleContinue = false;
                System.out.println("-----------------");
                System.out.println("Step3 completed");
                System.out.println("-----------------");
              }
              if(stepChange){
                this.criticalSection = true;  
                try{
                  this.device.write(":SOURce:VOLTage:LEVel:IMMediate:AMPLitude " + voltage);
                  this.device.write(":SOURce:CURRent:LEVel:IMMediate:AMPLitude " + current);  
                  //this.ps.setVoltage(voltage);
                  //this.ps.setCurrent(current);
                  stepChange = false;
                  this.criticalSection = false; 
                }
                catch(Exception e){
                 System.out.println("///////////////////////////////////////////////////////////////////////");
                 System.out.println("////////POWER SUPPLY CAN'T SET BACUSE OF BELOW ERROR///////////////////");
                 System.out.println("///////////////////////////////////////////////////////////////////////");  
                 System.out.println("POWER SUPPLY SET ERROR: "+e);
                 stepChange = false;
                 this.criticalSection = false; 
                 continue;
                }
                
              }
              try{
                Thread.sleep(500);
              }
              catch(Exception e){
                System.out.println(""+e);
              }
            }
            return 1;
          }

          public void runSimulationCycle(){
            int totalTime = this.parameters.get("time1") + this.parameters.get("time2") + this.parameters.get("time3");
            System.out.println("Total time : "+totalTime);
            System.out.println(""+this.parameters);
            for (int i = 0; i < 20; i++){
              try{
                  ellapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                  System.out.println("Cycle running"+i);
                  Thread.sleep(1000);
                  System.out.println("Cycle time : "+  ellapsedTime);
              }
              catch(Exception e){
                System.out.println(""+e);
              }
            }
          }
          
          public void stopCycle(){
              
              try{
                this.cycleContinue = false;
                this.ps.setVoltage(0);
                this.ps.setCurrent(0);
              }
              catch(Exception e){
                this.cycleContinue = false;  
                System.out.println("Stop Cycle Exception : "+ e);
              }
              
          }
          
          public double[] measure(){
             if(!(this.criticalSection)){
                  try{
                    //this.voltageMeasured = this.ps.measureVoltage();  
                    //this.currentMeasured = this.ps.measureCurrent();
                    //String currentString = this.device.sendAndReceiveString("measure:current?");
                    //String voltageString = this.device.sendAndReceiveString("measure:voltage?");
                    this.currentMeasured = Double.parseDouble(this.device.sendAndReceiveString("measure:current?"));
                    this.voltageMeasured = Double.parseDouble(this.device.sendAndReceiveString("measure:voltage?"));
                    this.resistanceMeasured = (this.currentMeasured == 0) ? 0 : (this.voltageMeasured / this.currentMeasured);
                    double[] measured = {this.voltageMeasured,this.currentMeasured,this.resistanceMeasured};
                    return measured;
                  }
                  catch(Exception e){
                    System.out.println("Power supply Measurement Exception : " + e);
                    double[] measured = {10,150,100};
                    return measured;
                  }
             }
             else{
                System.out.println("Power supply Measurement couldn' t be performed because device is working in its criticial section.. It is changing cycle parameters"); 
                System.out.println("Critical section : " + this.criticalSection);
                double[] measured = {0,0,0};
                return measured;
                }
          }
          
          
          
        }
          
          
