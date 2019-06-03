package fr.upem.capcha.images.vehicles;
import fr.upem.capcha.images.Category;

/**
 * <b>Vehicle class manages pictures including vehicles. 
 * It is a distant child of the Category class</b>
 * @see Category
 */

public class Vehicle extends Category {
		
	/**
	 * Vehicle constructor. 
	 * calls the parent constructor
	 */
	public Vehicle() {
		super();
	}
	
	/**
	 * String representation of the Vehicle class
	 * @return a specific string to display
	 */
	@Override
	public String toString() {
		return "vehicules";
	}

}
