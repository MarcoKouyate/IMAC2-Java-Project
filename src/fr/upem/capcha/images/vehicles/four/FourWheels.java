package fr.upem.capcha.images.vehicles.four;
import fr.upem.capcha.images.vehicles.Vehicle;

/**
 * <b>FourWheels class manages pictures including four wheels vehicles. 
 * It is a distant child of the Category class</b>
 */

public class FourWheels extends Vehicle {

	/**
	 * FourWheel constructor. 
	 * calls the parent constructor
	 */
	public FourWheels() {
		super();
	}
	
	/**
	 * String representation of the FourWheel Class
	 * @return a specific string to display
	 */
	@Override
	public String toString() {
		return "véhicules à quatres roues";
	}

}
