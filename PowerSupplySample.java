import javafx.concurrent.Task;
import javafx.application.Platform;

class PowerSupplySample extends Task<Integer>{

  @Override
  protected Integer call() throws Exception {
    for (int i = 0; i < 10; i++){
      System.out.println(""+i+"new task");
      //updateProgress(i + 1,10);
      Thread.sleep(1000);
      if(isCancelled()){
        return i;
      }
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
