package fr.upem.capcha.images.vehicles.bikes.motorbikes;
import fr.upem.capcha.images.vehicles.bikes.Bike;

/**
 * <b>MotorBike class manages pictures including motorbikes. 
 * It is a distant child of the Category class</b>
 */

public class MotorBike extends Bike {

	/**
	 * MotorBike constructor. 
	 * calls the parent constructor
	 */
	public MotorBike() {
		super();
	}
	
	/**
	 * String representation of the MotorBike Class
	 * @return a specific string to display
	 */
	@Override
	public String toString() {
		return "Moto à deux roues";
	}

}
