import javafx.concurrent.Task;
import javafx.application.Platform;
import java.util.Hashtable;

class UpdateUi extends Task<Integer>{
    
    static Hashtable<String, Integer> parameters = new Hashtable<>();
    static int totalTime; 
    static boolean cycleContinue;
    
    public UpdateUi(Hashtable<String, Integer> parameters){
    this.parameters = parameters;
    this.totalTime = this.parameters.get("time1") + this.parameters.get("time2") + this.parameters.get("time3");
    this.cycleContinue = true;
    }

  @Override
  protected Integer call() throws Exception {
    try{
        double startTime = System.currentTimeMillis();
        double ellapsedTimeFloat;
        int ellapsedTime;
        while (this.cycleContinue == true){
          Thread.sleep(1000);
          double currentTime = System.currentTimeMillis();
          ellapsedTimeFloat = currentTime - startTime;
          ellapsedTime = (int) ellapsedTimeFloat;
          updateProgress(ellapsedTime,this.totalTime);
          System.out.println("Start time progress : " +startTime);
           System.out.println("Current time progress : " +currentTime);
          System.out.println("Ellapsed time float progress : " +ellapsedTimeFloat);
          System.out.println("Ellapsed time progress : " +ellapsedTime);
          System.out.println("Total time progress : " +this.totalTime);
          updateMessage(""+ellapsedTimeFloat +"remaining");
          if(ellapsedTimeFloat > this.totalTime){
            this.cycleContinue = false;
            }
          if(isCancelled()){
            System.out.println("---------");
            System.out.println("Cancelled");
            System.out.println("---------");
          }
        }
    }
    catch(Exception e){
        System.out.println("Progressbar error : "+e);
    
    
    }
    return 10;
  }
  @Override
  public  boolean cancel(boolean mayInterruptIfRunning){
        updateMessage("Cancelled");
        return super.cancel(mayInterruptIfRunning);
  }

    @Override
    public  void updateProgress(double workDone, double max){
        updateMessage("progress" + workDone);
        super.updateProgress(workDone,max);
    }
}
