//TODO Javadoc
//TODO random number of images
//TODO dynamically detect maximum difficulty
//TODO deleguete classes


package fr.upem.capcha.logic;
import java.io.File;

import java.util.concurrent.ThreadLocalRandom;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.net.URL;

import java.util.Collections;

import fr.upem.capcha.images.*;
import fr.upem.capcha.logic.CategoryManager;

/**
 * <b>MainLogic est la classe qui sert à la gestion logique du système.</b>
 * <p>
 * C'est à dire :
 * <ul>
 * <li>Savoir si le Captcha est correct.</li>
 * <li>Gérer la difficulté.</li>
 * <li>Stocker la liste des images actives.</li>
 * </ul>
 * </p>
 * <p>
 * 	Il commande le CategoryManager
 * </p>
 * 
 * @see CategoryManager
 * 
 */


public class MainLogic {
	
	private int difficulty;
	private int maxDifficulty = 3;
	private CategoryManager categoryManager = new CategoryManager(difficulty);
	
	private List<URL> images;
	
	public MainLogic() {
		difficulty = 0;
		images = poolImages(3);
	}
	

	/**
     * Increase difficulty of CAPTCHA.
     * 
     */


	public void increaseDifficulty() {
		if (difficulty < maxDifficulty) {
			difficulty++;
		}
		categoryManager.updateCategories(difficulty);
	}
	
	public Category getCurrentCategory() {
		return categoryManager.current();
	}
	
	public List<URL> getImages() {
		images = poolImages(2);
		return images;
	}
	
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
