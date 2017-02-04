import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * this class is used to delete object when user want to delete some devices.
 * 
 */
public class DeleteObject extends Functionality{
	 private static Simulator simulator; 
	 private static DeleteObject instance;
	 ConnectionDB connect = ConnectionDB.getInstance();
	  
	 public DeleteObject(Simulator simulator){
		 this.simulator = simulator;
	 }
	 synchronized public static DeleteObject getInstance() {
		if(instance == null){
			instance = new DeleteObject(simulator);
		}   	
	    	return instance;
	 }
	   
	   public void sendCommand(){
		   BufferedReader reader = null;
	       try {
	           reader = new BufferedReader(new FileReader("./InputCommand/Delete.csv"));
	           String tempString = null;
	           while ((tempString = reader.readLine()) != null) {
	        	   String[] commands = tempString.split(",");
	        	   simulator.removeDevice(commands[0]+"&"+commands[1]);
	        	   connect.delete(tempString);
	           }
	           reader.close();
	       } catch (IOException e) {
	           e.printStackTrace();
	       } 
	   }
}
