import org.junit.Assert;
import org.junit.Test;

public class JunitTest {
	private static final String PATH = "/Users/zhangjinhang1/Documents/workspace/GRP/";
	private static final String AUTO = "Auto";
	private static final String WINTER = "Winter";
	private static final String SUMMER = "Summer";
	private static final String CONFIG = "_config.csv";	
	private static final String CREATE = "Create.csv";
	
	Simulator simulator = Simulator.getInstance();
	/**
	 * Testing the Mode->choosemode() 
	 * 
	 */
	@Test
	public void testChooseAutoMode() 
	{	
		Manager manager = new Manager(simulator);
		manager.SwitchCsvObject(CREATE);		
		Mode mode = new Mode(simulator, AUTO);
		
		String expected = mode.choosemode();
		String actual = PATH+AUTO+CONFIG;
		
		System.out.println(expected+"&"+actual);	
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testChooseWinterMode() 
	{	
		Manager manager = new Manager(simulator);
		manager.SwitchCsvObject(CREATE);

		Mode mode = new Mode(simulator, WINTER);	
		
		String expected = mode.choosemode();
		String actual = PATH+WINTER+CONFIG;
		
		System.out.println(expected+"&"+actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testChooseSummerMode() 
	{	
		Manager manager = new Manager(simulator);
		manager.SwitchCsvObject(CREATE);

		Mode mode = new Mode(simulator, SUMMER);
		
		String expected = mode.choosemode();
		String actual = PATH+SUMMER+CONFIG;
		
		System.out.println(expected+"&"+actual);	
		Assert.assertEquals(expected, actual);
	}
	/** 
	 * Current devices in database(grp)->table(currentDevice) include 1&1(300) 1&2(300) 2&1(300) 2&2(300) 3&1(300) 3&2(300) 3&3(300)
	 * therefore, the consumption should be 10800
	 */
	@Test
	public void testGetConsumption() 
	{
		Manager manager = new Manager(simulator);
		manager.SwitchCsvObject(CREATE);
		
		Mode mode = new Mode(simulator, AUTO);
		int expected = 2300;
		int actual = mode.getAllConsumption();
		
		Assert.assertEquals(expected, actual);
	}
}

