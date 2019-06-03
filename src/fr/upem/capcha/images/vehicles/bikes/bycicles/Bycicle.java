package fr.upem.capcha.images.vehicles.bikes.bycicles;
import fr.upem.capcha.images.vehicles.bikes.Bike;

/**
 * <b>Bicycle class manages pictures including bicycles. 
 * It is a distant child of the Category class</b>
 */

public class Bycicle extends Bike {

	/**
	 * Bicycle constructor. 
	 * calls the parent constructor
	 */
	public Bycicle() {
		super();
	}
	
	/**
	 * String representation of the Bicycle Class
	 * @return a specific string to display
	 */
	@Override
	public String toString() {
		return "byciclettes";
	}

}

