import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to implement the User mode.
 * 
 */
public class ManualMode extends AbstractMode {

	private static int counterHour;
	Simulator simulator;
	List<String> rdid = new ArrayList<String>();
	ConnectionDB connect = ConnectionDB.getInstance();
	private static final String cpath = "./InputCommand/Create.csv";
	ModeObject modeobject = new ModeObject(simulator);
	
	public ManualMode(Simulator sim) {
		// TODO Auto-generated constructor stub
		simulator = sim;
		ChangeState(cpath);
	}
	
	@Override
	public void initial() {
		// TODO Auto-generated method stub
		counterHour = modeobject.getCounter();
	}
	/**
	 * this method is used to change the state of which user changes
	 * 
	 */
	public void ChangeState(String path) {
		BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String tempString = null;		
            while ((tempString = reader.readLine()) != null) {    	
            	String[] elements = tempString.split(",");
            	connect.updateState(elements[0]+"&"+elements[1], elements[2]); 
            	simulator.changeState(elements[0]+"&"+elements[1]+"&"+elements[2]);
            }   
        }catch(Exception e) {
        	e.printStackTrace();
        }    
	}	
	/**
	 * see AbstractMode,java
	 * 
	 */
	@Override
	public int getAllConsumption() {
		rdid = connect.getID();
		int sumConsumption = 0;
		for(int i=0; i<rdid.size(); i++) {
			if(connect.checkState(rdid.get(i)).equals("true")){
				sumConsumption += simulator.getPower(rdid.get(i));
			}
		}			
		System.out.println("sumConsumption" + sumConsumption);	
		return sumConsumption;
	}	
	/**
	 * this method is used to update the history table. 
	 * 
	 */
	@Override
	public long updateHistroy() {
		rdid = connect.getID();
		long updateHistroy = 0;
		for(int i=0; i<rdid.size(); i++) {
			if(connect.checkState(rdid.get(i)).equals("true")) {
				updateHistroy += Integer.parseInt(connect.getValue(rdid.get(i)));
			}
		}
		return updateHistroy;
	}
	
	/**
	 * this method is used to update the currentDevice table. 
	 * 
	 */
	@Override
	public void updateMYSQLCurrentDevice(int counter) {
		rdid = connect.getID();
		for(int i=0; i<rdid.size(); i++) {
			if(connect.checkState(rdid.get(i)).equals("true")) {
				if(counter<=12) {
					connect.updateHourValue(rdid.get(i), counter, simulator.getPower(rdid.get(i)));
				} else {
					connect.updateHourValue(rdid.get(i), counter%12, simulator.getPower(rdid.get(i)));
				} 
			}else {
				if(counter<=12) {
					connect.updateHourValue(rdid.get(i), counter, 0);
				} else {
					connect.updateHourValue(rdid.get(i), counter%12, 0);
				}
			}
			connect.updateCurrentDeviceDValue(rdid.get(i));
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("\n\n-------------------Start-----------------------");
		int counter = modeobject.getCounter();
		updateMYSQLCurrentDevice(counter);
		if(counter>20) {
			connect.insertHistroy(updateHistroy());
			 counter = 0;
		}
		System.out.println("counter: "+counter+"!!!!!!!!!");
		System.out.println("---------------------End-----------------------\n\n");
		modeobject.setCounter(++counter);
	}
}
