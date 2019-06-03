package fr.upem.capcha.images.vehicles.bikes;
import fr.upem.capcha.images.vehicles.Vehicle;

/**
 * <b>Bike class manages pictures including bikes. 
 * It is a distant child of the Category class</b>
 */

public class Bike extends Vehicle {
	
	/**
	 * Bike constructor. 
	 * calls the parent constructor
	 */
	public Bike() {
		super();
	}
	
	/**
	 * String representation of the Bike Class
	 * @return a specific string to display
	 */
	@Override
	public String toString() {
		return "véhicules à deux roues";
	}

}
