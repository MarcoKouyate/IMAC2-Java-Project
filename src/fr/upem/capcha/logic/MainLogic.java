//TODO Javadoc

package fr.upem.capcha.logic;

import java.util.List;
import java.util.stream.Collectors;
import java.net.URL;

import java.util.Collections;

import fr.upem.capcha.images.*;
import fr.upem.capcha.logic.CategoryManager;

/**
 * <b>MainLogic est la classe qui sert à la gestion logique du système.</b>
 * <p>
 * C'est à dire :
 * </p>
 * 
 * <ul>
 * <li>Savoir si le Captcha est correct.</li>
 * <li>Gérer la difficulté.</li>
 * <li>Stocker la liste des images actives.</li>
 * </ul>
 * 
 * <p>
 * 	Il commande le CategoryManager
 * </p>
 * 
 * @see CategoryManager
 * 
 */

//TODO ImageManager
//TODO Immutability
public class MainLogic {
	
    /**
     * Test difficulty 
     * 
     * @see MainLogic#increaseDifficulty()
     */
	private int difficulty;
	//TODO remove difficulty
	
	
    /**
     * Maximum difficulty
     * 
     * @see MainLogic#increaseDifficulty()
     */
	final static private int maxDifficulty = 3;
	
    /**
     * Dedicated class for managing categories
     * 
     * <p>
     * 	 That class provides the correct images depending on the current category
     * </p>
     * 
     * @see CategoryManager
     * @see MainLogic#increaseDifficulty()
     * @see MainLogic#poolImages(int)
     * @see MainLogic#isCaptchaCorrect(List)
     */
	private CategoryManager categoryManager = new CategoryManager(difficulty);
	
	/**
	 * List of active images to display on screen
	 * 
	 * @see MainLogic#getImages()
	 * @see MainLogic#poolImages(int)
	 * @see MainLogic#isCaptchaCorrect(List)
	 */
	private List<URL> images;
	
	
    /**
     * MainLogic Constructor.
     * <p>
     * Set difficulty to 0 by default and pool images
     * </p>
     * 
     * @see MainLogic#difficulty
     * @see MainLogic#images
     */
	public MainLogic() {
		difficulty = 0;
		images = poolImages(3);
	}
	//TODO make MainLogic singleton
	//TODO make MainLogic immutable

	/**
     * Increase difficulty of CAPTCHA (but never above maxDifficulty)
     * then asks category manager to update.
     */
	public void increaseDifficulty() {
		if (difficulty < maxDifficulty) {
			difficulty++;
		}
		categoryManager.updateCategories(difficulty);
	}
	//TODO avoid void functions for immutability
	
	/**
     * Getter of current category.
     * 
     * <p>
     * 	Useful for printing category
     * </p>
     * 
     * @return the current category
     */
	public Category getCurrentCategory() {
		return categoryManager.current();
	}
	//TODO find another way to print question
	
	public List<URL> getImages() {
		images = poolImages(2);
		return images;
	}
	
	
	/**
     * Get images to display
     *
     * <p>
     * 	 Select a number of correct images then fill the rest.
     * </p>
     * 
     * @param value
     * 		number of correct images to put into the list
     *
     * @return list of 9 URL representing images
     */
	private List<URL> poolImages (int value) {
		List<URL> validImages = categoryManager.current().getRandomPhotosURL(value);
		List<URL> allImages = categoryManager.first().getRandomPhotoURL();
		
		for (int i = value; validImages.size() <= 8; i++) {
			if (!validImages.contains(allImages.get(i))){
				validImages.add(allImages.get(i));
			}
		}
		
		Collections.shuffle(validImages);
		
		return validImages;
	}
	//TODO check if at least one correct image is present
	
	/**
     * check if images selected are correct. 
     *
     * @param selectedImages
     * 		list of images to check with valid images
     * 
     * @return true if all items match, false if at least one is missing or wrong
     */
	public boolean isCaptchaCorrect(List<URL> selectedImages) {
		boolean isTotalCorrect = true;
		
		List<URL> correctImages = images.stream()
			    .filter(i -> categoryManager.current().isPhotoCorrect(i)).collect(Collectors.toList());

		for (URL correcturl : correctImages) {
			isTotalCorrect = isTotalCorrect & selectedImages.contains(correcturl);
		}
		
		return isTotalCorrect & (selectedImages.size() == correctImages.size());
	}
}
