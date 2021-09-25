import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.lang.Thread;
import javafx.beans.binding.Bindings;
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
    time1_label,voltage2_label,current2_label,time2_label,voltage3_label,current3_label,time3_label, cycleInfoLabel, uiConsole, 
    realTimeVoltageSet,realTimeCurrentSet, rearRealTimeVoltageSet, rearRealTimeCurrentSet,
    voltageUi,currentUi,resistanceUi,temp1Ui,temp2Ui,temp3Ui,temp4Ui,temp5Ui,temp6Ui,temp7Ui,temp8Ui,temp9Ui,temp10Ui;

    @FXML private ToggleButton connectButton,simulationButton;

    @FXML private Slider voltageSlider,currentSlider, rearVoltageSlider, rearCurrentSlider;

    @FXML private static int voltage1,current1,time1,voltage2,current2,time2,voltage3,current3,time3;
    @FXML public boolean simulationMode;
    //@FXML private boolean cycleContinue; boolen power supply a taşındı

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
    @FXML private XYChart.Series constructorTime1 = new XYChart.Series();
    @FXML private XYChart.Series constructorTime2 = new XYChart.Series();
    @FXML private XYChart.Series constructorTime3 = new XYChart.Series();
    @FXML private XYChart.Series constructorTime4 = new XYChart.Series();

    @FXML private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    @FXML private ScheduledFuture<?> drawer;

    @FXML private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    @FXML private final int WINDOW_SIZE = 10;

    @FXML private Plc plc = new Plc(); // Plc Thread' i yaratıldı.
    @FXML private PowerSupply powerSupply = new PowerSupply(10); //Power Supply Task' i yaratıldı
   
    @FXML private ConnectToDatabase db= new ConnectToDatabase();

    @FXML // This method is called by the FXMLLoader when initialization is   complete
    void initialize() {

        bindParameters();
        
        doGraphSettings();

    }

    @FXML public void bindParameters(){
        voltage1_label.textProperty().bind(voltage1InputText.textProperty());
        current1_label.textProperty().bind(current1InputText.textProperty());
        time1_label.textProperty().bind(time1InputText.textProperty());

        voltage2_label.textProperty().bind(voltage2InputText.textProperty());
        current2_label.textProperty().bind(current2InputText.textProperty());
        time2_label.textProperty().bind(time2InputText.textProperty());

        voltage3_label.textProperty().bind(voltage3InputText.textProperty());
        current3_label.textProperty().bind(current3InputText.textProperty());
        time3_label.textProperty().bind(time3InputText.textProperty());

        realTimeVoltageSet.textProperty().bind(Bindings.format("%.0f",voltageSlider.valueProperty()));
        realTimeCurrentSet.textProperty().bind(Bindings.format("%.0f",currentSlider.valueProperty()));
        rearRealTimeVoltageSet.textProperty().bind(Bindings.format("%.0f",rearVoltageSlider.valueProperty()));
        rearRealTimeCurrentSet.textProperty().bind(Bindings.format("%.0f",rearCurrentSlider.valueProperty()));
        
        voltageUi.textProperty().bind(Bindings.format("%.0f",powerSupply.voltageMeasured));
        currentUi.textProperty().bind(Bindings.format("%.0f",powerSupply.currentMeasured));
        resistanceUi.textProperty().bind(Bindings.format("%.0f",powerSupply.resistanceMeasured));
        temp1Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(0)));
        temp2Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(1)));
        temp3Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(2)));
        temp4Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(3)));
        temp5Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(4)));
        temp6Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(5)));
        temp7Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(6)));
        temp8Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(7)));
        temp9Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(8)));
        temp10Ui.textProperty().bind(Bindings.format("%d",plc.getTemp(9)));
        
    }

    @FXML public void draw() throws InterruptedException{
        try{
            Thread.sleep(500);
        }
        catch(Exception e){
            System.out.println(e);
        }

        System.out.println("Power Supply cycleContinue : "+powerSupply.getCycleContinue());
        //Connect to database
        this.db.connect();
        this.db.createNewTable();

        // put dummy data onto graph per second
        this.drawer =  this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            if(this.powerSupply.getCycleContinue()){
                System.out.println("Çiziyorum");
                drawOnce();
            }
            else{
              //System.out.println("Not drawing because this.powerSupply.cycleContinue : " + this.powerSupply.getCycleContinue());
              //System.out.println("Is PowerSupply Run Cycle Thread alive ? ->" + this.powerSupplyThread.isAlive());
              //this.scheduledExecutorService.cancel(true);
              this.drawer.cancel(true);
              this.db.close();
              System.out.println("Çizmiyorum");
              System.out.println("DRAW END");
            }
            }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @FXML public void  drawOnce(){

        //Create plc and power supply data series
        XYChart.Series[] plcSeries = {temp1Series,temp2Series,temp3Series,temp4Series,temp5Series,
        temp6Series,temp7Series,temp8Series,temp9Series,temp10Series};

        XYChart.Series[] psSeries = {voltageSeries,currentSeries,resistanceSeries};

        int[] tempValues = new int [10];
        double[] psMeasurements= new double[3];
        double voltageMeasured = 0;
        double currentMeasured = 0;

        // setup a scheduled executor to periodically put data into the chart
        for (int i = 0; i < tempValues.length; i++){
                  tempValues[i] = this.plc.getTemp(i);
                }

                double[] powerSupplyReturn = new double[3];
                powerSupplyReturn = powerSupply.measure();
                //System.out.println("power_supply_values : "+ powerSupplyReturn[0] + powerSupplyReturn[1]+ powerSupplyReturn[2]);
                psMeasurements[0] = powerSupplyReturn[0];
                psMeasurements[1] = powerSupplyReturn[1];
                psMeasurements[2] = powerSupplyReturn[2];

                //writeToDatabase(tempValues);
                db.writeToDatabase(psMeasurements,tempValues);
                db.readFromDatabase();

                // Update the chart
                Platform.runLater(() -> {
                  // get current time
                  //Date now = new Date();
                  // put random number with current time

                  Calendar now = Calendar.getInstance();
                  
                  for (int i = 0; i < psSeries.length; i++){
                    psSeries[i].getData().add(new XYChart.Data<>(""+powerSupply.ellapsedTime, psMeasurements[i]));
                  }

                  for (int i = 0; i < plcSeries.length; i++){
                    plcSeries[i].getData().add(new XYChart.Data<>(""+powerSupply.ellapsedTime, tempValues[i]));
                  }
                  //System.out.println("Is PowerSupply Run Cycle Thread alive ? ->"+ this.powerSupplyThread.isAlive());
                  });

    }


    @FXML public void  doGraphSettings(){
        //Set graph styling and labels
        //categoryAxis.setLabel("Time/s");
        categoryAxis.setAnimated(false); // axis animations are removed
        numberAxis.setLabel("Value");
        numberAxis.setAnimated(false); // axis animations are removed
        cycleChart.setAnimated(false);
        cycleChart.setLegendVisible(true);

        /*
        this.constructorMelt1.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 365));
        */

        String[] plcLegendNames = {"TC1","TC2","TC3","TC4","TC5","TC6","TC7","TC8","TC9","TC10"};
        String[] psLegendNames = {"Voltage","Current","Resistance"};

        XYChart.Series[] consructorSeries = {constructorTime1,constructorTime2,constructorTime3,constructorTime4};

        //Create plc and power supply data series
        XYChart.Series[] plcSeries = {temp1Series,temp2Series,temp3Series,temp4Series,temp5Series,
        temp6Series,temp7Series,temp8Series,temp9Series,temp10Series};

        XYChart.Series[] psSeries = {voltageSeries,currentSeries,resistanceSeries};

        /*
        //add data containers to cycleChart
        for (int i = 0; i < consructorSeries.length; i++){

          cycleChart.getData().add(consructorSeries[i]);

        }
        */
        //add data containers to cycleChart
        for (int i = 0; i < plcSeries.length; i++){
          plcSeries[i].setName(plcLegendNames[i]);
          cycleChart.getData().add(plcSeries[i]);
        }

        for (int i = 0; i < psSeries.length; i++){
          psSeries[i].setName(psLegendNames[i]);
          cycleChart.getData().add(psSeries[i]);
        }
        /*
        Calendar now = Calendar.getInstance();
        constructorTime1.getData().add(new XYChart.Data<>(""+now,395));
        constructorTime2.getData().add(new XYChart.Data<>(""+now,335));
        constructorTime3.getData().add(new XYChart.Data<>(""+now,205));
        constructorTime4.getData().add(new XYChart.Data<>(""+now,265));

        now.add(Calendar.SECOND,1500);

        constructorTime1.getData().add(new XYChart.Data<>(""+now,395));
        constructorTime2.getData().add(new XYChart.Data<>(""+now,335));
        constructorTime3.getData().add(new XYChart.Data<>(""+now,205));
        constructorTime4.getData().add(new XYChart.Data<>(""+now,265));
        */
    }

    @FXML public void  clearGraph(){
        //Create plc and power supply data series
        XYChart.Series[] plcSeries = {temp1Series,temp2Series,temp3Series,temp4Series,temp5Series,
        temp6Series,temp7Series,temp8Series,temp9Series,temp10Series};

        XYChart.Series[] psSeries = {voltageSeries,currentSeries,resistanceSeries};
        //

        for (int i = 0; i < plcSeries.length; i++){
            plcSeries[i].getData().clear();
        }

        for (int i = 0; i < psSeries.length; i++){
            psSeries[i].getData().clear();
        }

        System.out.println("Graph cleared");
    }


    //CHECK and SET PARAMETERS FUNCTION
    @FXML public Hashtable<String, Integer> checkSetparameters() throws InterruptedException{
      try{
          Hashtable<String, Integer> parameters = new Hashtable<>();

          parameters.put("voltage1",Integer.parseInt(voltage1_label.getText()));
          parameters.put("current1",Integer.parseInt(current1_label.getText()));
          parameters.put("time1",Integer.parseInt(time1_label.getText()));

          parameters.put("voltage2",Integer.parseInt(voltage2_label.getText()));
          parameters.put("current2",Integer.parseInt(current2_label.getText()));
          parameters.put("time2",Integer.parseInt(time2_label.getText()));

          parameters.put("voltage3",Integer.parseInt(voltage3_label.getText()));
          parameters.put("current3",Integer.parseInt(current3_label.getText()));
          parameters.put("time3",Integer.parseInt(time3_label.getText()));

          //System.out.println(""+parameters);
          return parameters;
        }
      catch(Exception e){
          System.out.println("Cycle can't run. There is a proble at parameters: " + e);
          return null;
        }

    }

    //RUN CYCLE BUTTON FUNCTION
    @FXML protected int run_cycle(ActionEvent event) throws InterruptedException{
      Hashtable<String, Integer> parameters = new Hashtable<>();

      parameters = checkSetparameters();
      System.out.println("parameters"+parameters);

      if (parameters == null){
             System.out.println("Cycle cant start. There is problem at parameters");
             return 1;
        }
      else{
              powerSupply = new PowerSupply(10);  
              powerSupply.setCycleContinue(true);
              powerSupply.setParameters(parameters,simulationMode);
              Thread powerSupplyThread = new Thread(powerSupply);
              powerSupplyThread.start();
              clearGraph();
              System.out.println("Cycle başlamalı");
              draw();
              return 0;
      }
    }

    @FXML protected void connect(ActionEvent event) {
        try{
            plc.start();
            powerSupply.connect();
            connectButton.setStyle("-fx-background-color : green");
        }
        catch(Exception e){
            connectButton.setStyle("-fx-background-color : red");
            System.out.println("Couldn't connect to Power Supply"+e);
        }

    }

    //JUMP TO NEXT STEP ON GRAPH TAB
    @FXML protected void jumpToNextStep(ActionEvent event) throws InterruptedException{
        powerSupply.jumpToNextStep();
    }

     //JUMP TO NEXT STEP ON GRAPH TAB
    @FXML protected void applyManualChages(ActionEvent event) throws InterruptedException{
       int manualVoltage = Integer.parseInt(rearRealTimeVoltageSet.getText());
       int manualCurrent = Integer.parseInt(rearRealTimeCurrentSet.getText());
       powerSupply.manualChange(manualVoltage,manualCurrent);
    }

    //EMERGENCY STOP
    @FXML protected void emergency_stop(ActionEvent event) throws InterruptedException{
        System.out.println("Emergency stop button is pressed !");
        try{
            powerSupply.setCycleContinue(false);
            powerSupply.stopCycle();
            db.close();
            drawer.cancel(true);
        }
        catch(Exception e){
            System.out.println("Emergency button is pressed but there is an error : "+ e);
        }
    }

    //CALCULATE RESISTANCE FUNCTION
    @FXML protected void calculate_resistance(ActionEvent event) throws InterruptedException{
      System.out.println("Resistance calculation function is not completed !");
    }

    //CALCULATE RESISTANCE FUNCTION
    @FXML protected void changeSimulationMode(ActionEvent event) throws InterruptedException{
        if(simulationMode){
            simulationMode = false;
            simulationButton.setStyle("-fx-background-color : yellow");
        }
        else{
            simulationMode = true;
            simulationButton.setStyle("-fx-background-color : green");
        }
        System.out.println("###########################");
        System.out.println("## SIMULATION MODE:" + this.simulationMode +" ###");
        System.out.println("###########################");
    }
    
    @FXML protected void createCsv(ActionEvent event){
        
        System.out.println("Not implemented");
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
