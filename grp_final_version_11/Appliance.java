import java.util.Observable;
import java.util.Observer;

public class Appliance implements Observer,Runnable {
	
	private int applianceId;
	private int roomId;
	private int state;
    private int power;

	public Appliance(int applianceId, int roomId) {
		this.setApplianceId(applianceId);	
		this.roomId = roomId;
		this.state = 0;	 
		System.out.println("Room " + roomId + "  Appliance " + applianceId + " created");
	}
 
	public int getApplianceId() {
		return applianceId;
	}

	public void setApplianceId(int applianceId) {
		this.applianceId = applianceId;
	}
	 
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public int getState(){
		System.out.println("appliance " + applianceId + " state = " + state);
		return this.state;
	}
	
	public void setState(int newState){
		this.state = newState;
		System.out.println("Room " + roomId + "  Appliance " + applianceId + " new state = " + state);
	}

	public void notifySensor(int state) {
		System.out.println("!!!!");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		String[] splitData = arg.toString().split("&");
		int roomID = Integer.parseInt(splitData[0]);
		int applianceID = Integer.parseInt(splitData[1]);
		int state = Integer.parseInt(splitData[2]);
		if (this.roomId == roomID && this.applianceId == applianceID) {
			setState(state);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int preState;
		while(true){
			preState = this.state;
			while(preState==this.state)
				;
			preState = this.state;
			notifySensor(this.state);
		}
	}
}
