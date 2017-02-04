
import java.util.ArrayList;

public class Room {
    final static int d1Value = 300;
    final static int d2Value = 300;
    final static int d3Value = 500;
    final static int d4Value = 1200;
    final static int d5Value = 150;
    final static int defaultValue = 100;       // This values are used to set power for specified appliances.
    
	private int roomId;
	public ArrayList<Appliance> appliances;
	
	public Room(int roomId) {
		this.setRoomId(roomId);
		this.setAppliances(new ArrayList<Appliance>());
//		System.out.println("room " + roomId + " created");
	}
	
	public void addNewAppliance(int applianceId){
		Appliance appliance = new Appliance(applianceId,roomId);
		new Thread(appliance).start();
		appliances.add(appliance);
		
		switch (applianceId) {
		 case 01 :
			 appliance.setPower(d1Value);
			 break;
		 case 02 :
			 appliance.setPower(d2Value);
			 break;
		 case 03 :
			 appliance.setPower(d3Value);
			 break;
		 case 04 :
			 appliance.setPower(d4Value);
			 break;
		 case 05 :
			 appliance.setPower(d5Value);
			 break;
		 default:
			 appliance.setPower(defaultValue);
			 break;
		}
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public ArrayList<Appliance> getAppliances() {
		return appliances;
	}

	public void setAppliances(ArrayList<Appliance> appliances) {
		this.appliances = appliances;
	}

}