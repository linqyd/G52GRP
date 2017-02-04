import java.util.TimerTask;

/** 
 * This abstract class is used to be the base class for AutoMode, WinterMode, SummerMode, UserMode.
 * 
 * Also this class is also extends TimerTask.
 * 
 */
public abstract class AbstractMode extends TimerTask{
	/**
	 * initialState() is used to read the (mode)_config.csv file,
	 * and store the information into hashmap.
	 *
	 * @return void
	 */
	abstract public void initial();
	/**
	 * getAllConsumption() is used to get all consumption from database  
	 * 
	 */
	abstract public int getAllConsumption();
	/**
	 * checkHumidity(int) is used to check humidity if the current humidity is higher than the upper humidity,
	 * if higher, then close device 3 which is identified the humidifier.
	 * else open device 3
	 * 
	 * @return int
	 */
	public void checkHumidity(int currenctHumidity) {};
	/**
	 * checkTemperature(int) is used to check temperature if the current temperature is higher than the upper temperature,
	 * if higher, then close device 1 which is identified the heater.
	 * else then open device 1.
	 * 
	 * @return void
	 */
	public void checkTemperature(int currentTem) {};
	/**
	 * checkConsumption() is used to check consumption if the current consumption is higher than the upper consumption,
	 * if higher, then close the device in a priority queue which user inputs. 
	 * The device in priority queue is identified when the consumption is higher, which device the user want to close first. 
	 * 
	 * @return void
	 */
	public void checkConsumption() {};
	/**
	 * updateHistroy() is used to get a long value for all devices' consumption.
	 * The return value is used to be passed as a parameter into connect.insert(long) method
	 * 
	 * @return long
	 */
	abstract public long updateHistroy();
	/**
	 * updateMYSQLCurrentDevice(int) is used to update value for each device's consumption
	 * 
	 * @return void
	 */
	abstract public void updateMYSQLCurrentDevice(int counter);
}
