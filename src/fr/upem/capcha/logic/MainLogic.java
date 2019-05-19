package fr.upem.capcha.logic;
import java.util.ArrayList;
import fr.upem.capcha.images.*;
import fr.upem.capcha.images.vehicles.*;

public class MainLogic {
	
	private int difficulty;
	private ArrayList<Category> categories;
	private Category theCategorie;
	
	public MainLogic() {
		difficulty = 1;
		categories = new ArrayList<Category>();
		//categories = getCategories();
	}
	
	public void  browseImages() {
		
	}
	
	boolean checkCaptcha() {
		return true;
	}
}
