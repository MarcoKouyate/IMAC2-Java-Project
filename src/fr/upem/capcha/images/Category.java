package fr.upem.capcha.images;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.capcha.ui.MainUi;

//TODO full compatibility with windows (stock url file with same encoding)

public class Category implements Images{
	
	private List<URL> images = new ArrayList<URL>();
	private String path;
	private String windowspath;
	
	public Category() {
		super();
		/* Pour obtenir le chemin de fichier vers la catégorie */
		path = "src/"+this.getClass().getPackage().getName().replace('.', '/');
		windowspath = "src\\"+this.getClass().getPackage().getName().replace('.', '\\');
		
		images = this.getPhotos();
	}
	
    public static String encodeurl(String url)  
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
	
	public int getPhotosListSize() {
		return images.size();
	}

	@Override
	public List<URL> getRandomPhotosURL(int value) {
		List<URL> randomPhotosURL = getRandomPhotoURL(); 
		return randomPhotosURL.subList(0, Math.min(randomPhotosURL.size(), value));
	}

	@Override
	public List<URL> getRandomPhotoURL() {
		if (this.images.isEmpty()) { this.getPhotos(); } //s'assurer que la liste d'image est pleine
		List<URL> randomPhotosURL = images; 
		Collections.shuffle(randomPhotosURL); 
		return randomPhotosURL;
	}

	@Override
	public boolean isPhotoCorrect(URL url) {
		boolean isCorrect = false;
		String currentPhoto = encodeurl(url.toString());
		
		for (URL validImage : images) {
			isCorrect = isCorrect || (currentPhoto.equals(encodeurl(validImage.toString())));
		}
		
		return isCorrect;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
