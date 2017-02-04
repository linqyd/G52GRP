import static org.junit.Assert.*;

import org.junit.Test;
/*
 * This is a JUnit class that tests the main funtions in the simulator
 * - addDevice() and removeDevice()
 * 
 * */

public class SimulatorTests {

	private static final String DATA_Request = "00&01&0&00";
	private static final String DATA_Received = "00&01";
	
	@Test
	public void testAddDevice() {
		int expected = 00;
		int result;
		
		Simulator simulator = new Simulator();
		simulator.addDevice(DATA_Request);
		result = simulator.getState("00&01");
		assertEquals(expected, result);
	}
	
	@Test
	public void testRemoveDevice() {
		int expected = 0;
		int result;
		
		Simulator simulator = new Simulator();
		simulator.removeDevice(DATA_Received);
		result = simulator.getState("00&01");
		assertEquals(expected, result);
	}
	

}
