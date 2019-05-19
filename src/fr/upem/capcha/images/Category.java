package fr.upem.capcha.images;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.capcha.ui.MainUi;

public class Category implements Images{
	
	private List<URL> images = new ArrayList<URL>();
	
	public Category() {
		super();
		images = this.getPhotos();
	}

	@Override
	public List<URL> getPhotos() {
		images.clear(); //vide la liste des images
		
		
		List<URL> allImagesURL = new ArrayList<URL>();
		
		/* Pour obtenir le chemin de fichier vers la catégorie */
		String path = "src/"+this.getClass().getPackage().getName().replace('.', '/');
		String windowspath = "src\\"+this.getClass().getPackage().getName().replace('.', '\\');
		
		/* On initialise la liste de toutes les images */
		List<String> filelocations = null;

		System.out.println("package name :" + windowspath); //afficher le dossier en cours
		
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
			System.out.println("relative location : " + relativeLocation);
			allImagesURL.add(this.getClass().getResource(relativeLocation)); //on ajoute le chemin absolu dans la liste
		}
		
		
		return allImagesURL; //on retourne la liste
	}
	

	@Override
	public List<URL> getRandomPhotosURL(int value) {
		if (this.images.isEmpty()) { this.getPhotos(); } //s'assurer que la liste d'image est pleine
		List<URL> randomPhotosURL = images; 
		Collections.shuffle(randomPhotosURL); 
		return randomPhotosURL;
	}

	@Override
	public List<URL> getRandomPhotoURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPhotoCorrect(URL url) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
