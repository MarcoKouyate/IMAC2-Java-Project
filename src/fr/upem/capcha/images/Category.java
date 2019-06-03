package fr.upem.capcha.images;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.capcha.logic.CategoryManager;
import fr.upem.capcha.logic.MainLogic;


/**
 * <b>Category is a class managing images of a specific category</b>
 * 
 * <p>
 * What it does :
 * </p>
 * 
 * <ul>
 * <li>Get all photos from this category</li>
 * <li>Check if a photo match this category.</li>
 * </ul>
 * 
 * <p>
 * 	Relies on heritage to create sub-categories.
 * 	Implements Images class
 * 	Is directed by CategoryManager and MainLogic
 * </p>
 * 
 * @see MainLogic
 * @see CategoryManager
 * @see Images
 * 
 */

//TODO full compatibility with windows (stock url file with same encoding)


public class Category implements Images{
	
    /**
     * list of images URL corresponding to the category
     * @see Category#getPhotosListSize()
     * @see Category#getPhotos()
     * @see Category#getRandomPhotoURL()
     * @see Category#isPhotoCorrect(URL)
     */
	private List<URL> images = new ArrayList<URL>();
	
    /**
     * current path of the category
     * @see Category#getPhotos()
     */
	private String path;
	
	 /**
     * current path of the category (for windows filesystem)
     * @see Category#getPhotos()
     */
	private String windowspath;
	
    /**
     * Category Constructor.
     * <p>
     * Get the path of the category and get all photos url from this category
     * </p>
     * 
     * @see Category#path
     * @see Category#windowspath
     * @see Category#images
     */
	public Category() {
		super();
		/* Pour obtenir le chemin de fichier vers la catégorie */
		path = "src/"+this.getClass().getPackage().getName().replace('.', '/');
		windowspath = "src\\"+this.getClass().getPackage().getName().replace('.', '\\');
		
		images = this.getPhotos();
	}
	
	
	 /**
     * Remove encoding format from url
     * 
     * @param url
     * 		encoded url to translate
     * @return new url with correct encoding format
     */
    private static String encodeurl(String url)  
    {  

        try {  
             String prevURL="";  
             String decodeURL=url;  
             while(!prevURL.equals(decodeURL))  
             {  
                  prevURL=decodeURL;  
                  decodeURL=URLDecoder.decode( decodeURL, "UTF-8" );  
             }  
             return decodeURL.replace('\\', '/');  
        } catch (UnsupportedEncodingException e) {  
             return "Issue while decoding" +e.getMessage();  
        }   
    }

    
	 /**
     * get all photos corresponding to the category
     * 
     * @return list of all image url contained in the current category
     * 
     * @see Category#images
     * @see Category#path
     */
	@Override
	public List<URL> getPhotos() {
		images.clear(); //vide la liste des images
		
		
		List<URL> allImagesURL = new ArrayList<URL>();
		
		/* On initialise la liste de toutes les images */
		List<String> filelocations = null;
		
		/*Nous allons retrouver les fichiers images présent dans le répertoire et tous ses sous-répertoires*/
		Path start = Paths.get(path); //détermine le point de départ 
		try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
		    filelocations = stream
		        .map(String::valueOf) //transforme les Path en string
		        .filter(filename -> filename.contains(".jpg") || filename.contains(".png")) //ne prend que les images jpg et png
		        .collect(Collectors.toList());
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/* Pour chaque fichier retrouvé, on essaie de retrouver son chemin absolu pour le stocker dans le allImagesURL */
		for (String filelocation : filelocations) {
			String relativeLocation = filelocation.replace(path+"/", ""); // Pour ne pas partir de src mais de la classe courante
			relativeLocation = relativeLocation.replace(windowspath+"\\", "");
			allImagesURL.add(this.getClass().getResource(relativeLocation)); //on ajoute le chemin absolu dans la liste
		}
		
		
		return allImagesURL; //on retourne la liste
	}
	
	
	 /**
     * get the number of photos corresponding to the category
     * 
     * @return size of images list
     * @see Category#images
     */
	public int getPhotosListSize() {
		return images.size();
	}

	 /**
     * select a random sublist from all photos
     * 
     * @return a sublist of getRandomPhoto()
     * @see Category#getRandomPhotoURL()
     */
	@Override
	public List<URL> getRandomPhotosURL(int value) {
		List<URL> randomPhotosURL = getRandomPhotoURL(); 
		return randomPhotosURL.subList(0, Math.min(randomPhotosURL.size(), value));
	}

	 /**
     * randomize order of the images list
     * 
     * @return a randomized list of all images url corresponding to the category
     * @see Category#images
     * @see Category#getRandomPhotosURL(int)
     */
	@Override
	public List<URL> getRandomPhotoURL() {
		if (this.images.isEmpty()) { this.getPhotos(); } //s'assurer que la liste d'image est pleine
		List<URL> randomPhotosURL = images; 
		Collections.shuffle(randomPhotosURL); 
		return randomPhotosURL;
	}

	 /**
     * checks if url match the category 
     * 
     * @param url
     * 		url of image to check
     * 
     * @return true if url matchs, false otherwise
     * 
     * @see Category#encodeurl(String)
     * @see Category#images
     */
	@Override
	public boolean isPhotoCorrect(URL url) {
		boolean isCorrect = false;
		String currentPhoto = encodeurl(url.toString());
		
		for (URL validImage : images) {
			isCorrect = isCorrect || (currentPhoto.equals(encodeurl(validImage.toString())));
		}
		
		return isCorrect;
	}
	
	 /**
     * Convert class into string.
     * @return String representation of the class.
     */
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
