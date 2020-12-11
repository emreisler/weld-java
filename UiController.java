
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ProgressBar;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.ArrayList; // import the ArrayList class
import java.util.Hashtable; // import the Hashtable class
import javafx.concurrent.Task;
import javafx.application.Platform;
import java.util.Random;
import java.io.IOException;
import java.net.UnknownHostException;

public class UiController {

    @FXML private TextField voltage1InputText,current1InputText,time1InputText,voltage2InputText,
    current2InputText,time2InputText,voltage3InputText,current3InputText,time3InputText;

    @FXML private Label voltage1_label, current1_label,
    time1_label,voltage2_label,current2_label,time2_label,voltage3_label,current3_label,time3_label, cycleInfoLabel;
    @FXML private ToggleButton connectButton,simulationButton;
    
    @FXML private static int voltage1,current1,time1,voltage2,current2,time2,voltage3,current3,time3;
    @FXML public boolean simulationMode;
    
    @FXML private ProgressBar progressBar;

    @FXML private LineChart cycleChart;

    @FXML private CategoryAxis categoryAxis;

    @FXML private NumberAxis numberAxis, temp1Axis,temp2Axis,temp3Axis,temp4Axis,temp5Axis,temp6Axis,temp7Axis,temp8Axis,temp9Axis,temp10Axis,voltageAxis,currentAxis,resistanceAxis;

    @FXML private XYChart.Series voltageSeries = new XYChart.Series();
    @FXML private XYChart.Series currentSeries = new XYChart.Series();
    @FXML private XYChart.Series resistanceSeries = new XYChart.Series();
    @FXML private XYChart.Series temp1Series = new XYChart.Series();
    @FXML private XYChart.Series temp2Series = new XYChart.Series();
    @FXML private XYChart.Series temp3Series = new XYChart.Series();
    @FXML private XYChart.Series temp4Series = new XYChart.Series();
    @FXML private XYChart.Series temp5Series = new XYChart.Series();
    @FXML private XYChart.Series temp6Series = new XYChart.Series();
    @FXML private XYChart.Series temp7Series = new XYChart.Series();
    @FXML private XYChart.Series temp8Series = new XYChart.Series();
    @FXML private XYChart.Series temp9Series = new XYChart.Series();
    @FXML private XYChart.Series temp10Series = new XYChart.Series();
    

    @FXML private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    @FXML private ScheduledExecutorService scheduledExecutorService;

    @FXML private final int WINDOW_SIZE = 10;
    
    @FXML private Plc plc = new Plc(); // açınca donuyor
    @FXML private PowerSupply powerSupply = new PowerSupply(10);

    @FXML public void draw() throws InterruptedException{

        //setInitials(); // will be implemented
        /*
        Date setTime = new Date();
        series.getData().add(new XYChart.Data(simpleDateFormat.format(setTime), 23));
        series.getData().add(new XYChart.Data(simpleDateFormat.format(setTime)+30, 28));
        */
        categoryAxis.setLabel("Time/s");
        categoryAxis.setAnimated(false); // axis animations are removed
        numberAxis.setLabel("Value");
        numberAxis.setAnimated(false); // axis animations are removed
        cycleChart.setAnimated(false);
        

        XYChart.Series[] plcSeries = {temp1Series,temp2Series,temp3Series,temp4Series,temp5Series,
        temp6Series,temp7Series,temp8Series,temp9Series,temp10Series};
        
        XYChart.Series[] psSeries = {voltageSeries,currentSeries,resistanceSeries};
        

        for (int i = 0; i < plcSeries.length; i++){
          cycleChart.getData().add(plcSeries[i]);
        }
        
        for (int i = 0; i < psSeries.length; i++){
          cycleChart.getData().add(psSeries[i]);
        }

        int[] tempValues = new int [10];
        double[] psMeasurements= new double[3];
        double voltageMeasured = 0;
        double currentMeasured = 0;
        System.out.println("Drawing starts");
        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        
        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            
            for (int i = 0; i < tempValues.length; i++){
              tempValues[i] = this.plc.read(i+40,this.simulationMode); // this.simulationMode will be implemented as arguement
              //System.out.println("temp : "+tempValues[0]);
              //tempValues[i] = new Random().nextInt(100);
            }
            
            
            double[] powerSupplyReturn = new double[3];
            powerSupplyReturn = this.powerSupply.measure();
            System.out.println("power_supply_values : "+ powerSupplyReturn[0] + powerSupplyReturn[1]+ powerSupplyReturn[2]);
            psMeasurements[0] = powerSupplyReturn[0];
            psMeasurements[1] = powerSupplyReturn[1];
            psMeasurements[2] = powerSupplyReturn[2];
            
            //writeToDatabase(tempValues);
            
            // Update the chart
            System.out.println("temp : "+tempValues[0]);
            Platform.runLater(() -> {
              // get current time
              Date now = new Date();
              // put random number with current time
               
              
              for (int i = 0; i < psSeries.length; i++){
                  
                psSeries[i].getData().add(new XYChart.Data<>(simpleDateFormat.format(now), psMeasurements[i]));
              }
              
              for (int i = 0; i < plcSeries.length; i++){
                plcSeries[i].getData().add(new XYChart.Data<>(simpleDateFormat.format(now), tempValues[i]));
              }

              });
            }, 0, 500, TimeUnit.MILLISECONDS);
            System.out.println("DRAW END");
            
        }

    //SET PARAMETERS BUTTON FUNCTION
    @FXML public void set_parameters(ActionEvent event) throws InterruptedException{


        TextField[] parameter_inputs = {voltage1InputText,current1InputText,time1InputText,voltage2InputText,current2InputText,
                                        time2InputText,voltage3InputText,current3InputText,time3InputText
                                      };

        Label[] parameter_labels = {voltage1_label,current1_label,time1_label,voltage2_label,current2_label,
                                    time2_label,voltage3_label,current3_label,time3_label
                                  };

        int[] cycle_parameters = {this.voltage1,this.current1,this.time1,this.voltage2,this.current2,
                                  this.time3,this.voltage3,this.current2,this.time3
                                };

        System.out.println("Setting");

        for (int i = 0; i < parameter_inputs.length; i++) {
            if(parameter_inputs[i].getText() == "" || !(isInteger(parameter_inputs[i].getText()))){
              System.out.println("Eksik veya hatalı tipte giriş yapıldı");
              for (int j = 0; j < 9; j++){
                parameter_labels[j].setText("0");
                cycle_parameters[j] = 0;
              }
              break;
            }
            else{
              try {
                //  Block of code to try
                parameter_labels[i].setText(""+parameter_inputs[i].getText());
                cycle_parameters[i] = Integer.parseInt(parameter_inputs[i].getText());
                System.out.println("setted");
              }
              catch(Exception e) {
                //  Block of code to handle errors
                System.out.println(""+e);
              }
            }
        }
    }

    //RUN CYCLE BUTTON FUNCTION
    @FXML protected void run_cycle(ActionEvent event) throws InterruptedException{

      this.voltage1 = Integer.parseInt(voltage1_label.getText());
      this.current1 = Integer.parseInt(current1_label.getText());
      this.time1 = Integer.parseInt(time1_label.getText());
      this.voltage2 = Integer.parseInt(voltage2_label.getText());
      this.current2 = Integer.parseInt(current2_label.getText());
      this.time2 = Integer.parseInt(time2_label.getText());
      this.voltage3 = Integer.parseInt(voltage3_label.getText());
      this.current3 = Integer.parseInt(current3_label.getText());
      this.time3 = Integer.parseInt(time3_label.getText());


      Hashtable<String, Integer> parameters = new Hashtable<>();

      parameters.put("voltage1",this.voltage1);
      parameters.put("current1",this.current1);
      parameters.put("time1",this.time1);
      parameters.put("voltage2",this.voltage2);
      parameters.put("current2",this.current2);
      parameters.put("time2",this.time2);
      parameters.put("voltage3",this.voltage3);
      parameters.put("current3",this.current3);
      parameters.put("time3",this.time3);

      UpdateUi updateUi = new UpdateUi(parameters);
      this.cycleInfoLabel.textProperty().bind(updateUi.messageProperty());
      this.progressBar.progressProperty().bind(updateUi.progressProperty());
      Thread updatingUi = new Thread(updateUi);
      updatingUi.start();
        
      //PowerSupply powerSupply = new PowerSupply(1,parameters,false);// this.simulationMode will be implemented as arguement
      powerSupply.setParameters(parameters,false);
      Thread powerSupplyThread = new Thread(powerSupply);
      
      powerSupplyThread.start();
     
      draw();
    }
    
    @FXML protected void connect(ActionEvent event) {
        try{
            
            this.plc.start();
            this.powerSupply.connect();
            connectButton.setStyle("-fx-background-color : green");
            //System.out.println("Connected to Power Supply");
        }
        catch(Exception e){
           //this.plc.join();
            connectButton.setStyle("-fx-background-color : red");
            System.out.println("Couldn't connect to Power Supply"+e);
        }
        
        
        
    }


    //EMERGENCY STOP
    @FXML protected void emergency_stop(ActionEvent event) throws InterruptedException{
      System.out.println("Emergency stop function is not completed !");

    }
    //CALCULATE RESISTANCE FUNCTION
    @FXML protected void calculate_resistance(ActionEvent event) throws InterruptedException{
      System.out.println("Resistance calculation function is not completed !");

    }
    //CALCULATE RESISTANCE FUNCTION
    @FXML protected void writeToDatabase(int[] tempValues) throws InterruptedException{
      System.out.println("Database writing function is not implemented ");
      
    }
    
    //CALCULATE RESISTANCE FUNCTION
    @FXML protected void changeSimulationMode(ActionEvent event) throws InterruptedException{
        if(this.simulationMode){
            this.simulationMode = false;
            this.simulationButton.setStyle("-fx-background-color : yellow");
        }
        else{
            this.simulationMode = true;
            this.simulationButton.setStyle("-fx-background-color : green");
        }
        System.out.println("###########################");
        System.out.println("## SIMULATION MODE:" + this.simulationMode +" ###");
        System.out.println("###########################");
    }
    
    
    

    //HELPER FUNCTIONS
    public boolean isInteger(String s){
        try
        {
        Integer.parseInt(s);
        return true;
        } catch (NumberFormatException ex)
        {
        return false;
        }
    }
    
    
 }
