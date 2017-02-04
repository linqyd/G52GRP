
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Simulator extends Observable {

	private static Simulator instance;
	
    synchronized public static Simulator getInstance() {
		if(instance == null){
			instance = new Simulator();
		}   	
    	return instance;
	}
	
	private ArrayList<Room> rooms;
    private String statecommand;
    
	public Simulator() {
		this.rooms = new ArrayList<Room>();
	}

	/**
	 * @param String
	 *            roomId ; eg 00 , 01, 02 This should be called as soon as the
	 *            web app decides to make a new room
	 */
	
    /* The front-end doesn't have a page to add specified room and it only has one page to add appliances directly,
     * so I change this method to "private" and it will only be called by "aaddDevice()";
     */
	
	private void addNewRoom(int roomId) {
		Room room = new Room(roomId);
		rooms.add(room);
		System.out.println("Success!");
	}
    
	/**
	 * @param String
	 *            applianceIdentifier ; eg rId&apId&state&value eg 01&02&01&00
	 *            state should only be 01 , or 00 value should stay as 00 for
	 *            now This should be called as soon as the web app decides to
	 *            add a new device
	 */

	public void addDevice(String applianceIdentifier) {

		String[] splitIdentifier = applianceIdentifier.split("&");
		int roomId = Integer.parseInt(splitIdentifier[0]);
		int applianceId = Integer.parseInt(splitIdentifier[1]);
        Boolean roomExists = false;
        System.out.println("Success!!");
        
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getRoomId() == roomId) {
				rooms.get(i).addNewAppliance(applianceId);
				int length = rooms.get(i).appliances.size();
				Appliance appliance = rooms.get(i).appliances.get(length-1);
				this.addObserver(appliance);    // When the appliance is created, it will be added as a observer.
				roomExists = true;
			}
		}
		if (roomExists == false) {              // If the room doesn't exists, create this room and then create the specified appliance
			addNewRoom(roomId);
			rooms.get(rooms.size()-1).addNewAppliance(applianceId);
			int length = rooms.get(rooms.size()-1).appliances.size();
			Appliance appliance = rooms.get(rooms.size()-1).appliances.get(length-1);
			this.addObserver(appliance);
		}
	}

	/**
	 * Should be called when the web app chooses to remove a device
	 * 
	 * @param removeIdentifier
	 *            ; eg 00&02 , rId&apId
	 */

	public void removeDevice(String removeIdentifier) {

		String[] splitIdentifier = removeIdentifier.split("&");
		int roomId = Integer.parseInt(splitIdentifier[0]);
		int applianceId = Integer.parseInt(splitIdentifier[1]);

		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getRoomId() == roomId) {
				for (int j = 0; j < rooms.get(i).appliances.size(); j++) {
					if (rooms.get(i).appliances.get(j).getApplianceId() == applianceId) {
						Appliance appliance = rooms.get(i).appliances.get(j);
						this.deleteObserver(appliance);       // When this appliance is removed, we also delete this observer.
						rooms.get(i).appliances.remove(j);	 
					}
				}

			}
		}
	}

	/**
	 * Should be called regularly (perhaps in a different thread) to constantly
	 * relay the current state of an appliance to the web app
	 * 
	 * @param dataRequest
	 *            ; rId&apId ; eg 00&01 - gets state of appliance 02 in room
	 *            00
	 * @return state of appliance, eg 00 (off) or 01 (on)
	 */

	public int getState(String dataRequest) {
//		System.out.println(dataRequest);
		int returnState = 0;
		String[] splitData = dataRequest.split("&");
		int roomId = Integer.parseInt(splitData[0]);
		int applianceId = Integer.parseInt(splitData[1]);

		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getRoomId() == roomId) {
				for (int j = 0; j < rooms.get(i).appliances.size(); j++) {
					if (rooms.get(i).appliances.get(j).getApplianceId() == applianceId) {
						returnState = rooms.get(i).appliances.get(j).getState();
//						System.out.println(returnState);
					}
				}
			}
		}

		return returnState;
	}
	
	/* Should be called to get the power for appliances
	 * @param dataRequest
	 *            ; rId&apId ; eg 00&01 - gets power of appliance 01 in room
	 *            00
	 * @return power of appliance
	 */
	public int getPower(String dataRequest) {
		System.out.println("input data " + dataRequest);
		int returnState = 0;
		String[] splitData = dataRequest.split("&");
		int roomId = Integer.parseInt(splitData[0]);
		int applianceId = Integer.parseInt(splitData[1]);
		
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getRoomId() == roomId) {
				for (int j = 0; j < rooms.get(i).appliances.size(); j++) {
					if (rooms.get(i).appliances.get(j).getApplianceId() == applianceId) {
						returnState = rooms.get(i).appliances.get(j).getPower();
					}
				}
			}
		}
		return returnState;
	}
	/**
	 * This should be called as soon as the web app turns on/off a device
	 * @param dataReceived ; rId&apId&state ;  eg 00&02&01 turns on the appliance 02 in room 00
	 */

	public void changeState(String dataReceived) {
		this.statecommand = dataReceived;
		this.setChanged();
		this.notifyObservers(statecommand); 
	}
	
	/* This should be called to provide real temperature.
	 */
	public int getTemperature() {
		int max=35;
        int min=0;
        
        Random random = new Random();
        int temperature = random.nextInt(max)%(max-min+1) + min;
        System.out.println("Real temperature: " + temperature);
        return temperature;
	}

	/* This should be called to provide real humidity.
	 */
	public int getHumidity() {
		int max=90;
        int min=20;
        
        Random random = new Random();
        int humidity = random.nextInt(max)%(max-min+1) + min;
        System.out.println("Real humidity: " + humidity);
        return humidity;
	}
}
