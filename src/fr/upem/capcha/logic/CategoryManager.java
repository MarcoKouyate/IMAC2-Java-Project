package fr.upem.capcha.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.capcha.images.Category;

public class CategoryManager {
	
	
	private ArrayList<Category> categories;
	private Category currentCategory;
	private Category firstCategory;

	public CategoryManager(int difficulty) {
		categories = this.getCategories(difficulty);
		currentCategory = chooseCategory();
		firstCategory = currentCategory;
	}
	
	
	public ArrayList<Category> getCategories(int difficulty) {
		ArrayList<Category> newCategories = new ArrayList<Category>();
		
		List<String> categoriesString = getCategoryNames(difficulty);
		do {
			for(String categoryString : categoriesString) {
				try
			    {	 
					Class cls = Class.forName(categoryString);
					Category cat = (Category) cls.newInstance();
					newCategories.add(cat);
			    }
			    catch (ClassNotFoundException e)
			    {
			      // La classe n'existe pas
			    	System.out.println("La classe n'existe pas");
			    	e.printStackTrace(); 
			    }
			    catch (InstantiationException e)
			    {
			      // La classe est abstract ou est une interface ou n'a pas de constructeur accessible sans param�tre
			    	System.out.println("// La classe est abstract ou est une interface ou n'a pas de constructeur accessible sans param�tre");
			    	e.printStackTrace(); 
			    }
			    catch (IllegalAccessException e)
			    {
			      // La classe n'est pas accessible
			    	System.out.println("La classe n'est pas accessible");
			    	e.printStackTrace(); 
			    }
			}
		}	while	(false);
		
		return newCategories;
	}
	
	public void updateCategories(int difficulty) {
		categories = getCategories(difficulty);
		currentCategory = chooseCategory();
	}
	
	private List<String> getCategoryNames(int level){
		
		String dirName = "src/fr/upem/capcha/images";
		
		List<String> categoryNames = new ArrayList<String>();
		
		
		List<File> files = this.getCategoryDirs(new File(dirName), level);
		
		for( File f : files) {
			
			/* On initialise la liste de toutes les images */
			List<String> filelocations = null;
			
			/*Nous allons retrouver les fichiers images présent dans le répertoire et tous ses sous-répertoires*/
			Path start = Paths.get(f.toString()); //détermine le point de départ 
			try (Stream<Path> stream = Files.walk(start, 1)) {
			    filelocations = stream
			        .map(String::valueOf) //transforme les Path en string
			        .filter(filename -> filename.contains(".java")) //ne prend que les images jpg et png
			        .collect(Collectors.toList());
			    
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			for (String filelocation : filelocations) {
				String relativeLocation = filelocation.replace('/','.').replace('\\','.').replace("src.", "").replace(".java", ""); // Pour ne pas partir de src mais de la classe courante
				categoryNames.add(relativeLocation); //on ajoute le chemin absolu dans la liste
			}
		}
		
		return categoryNames;
	}
	
	
	private List<File> getCategoryDirs(File parent, int level){
		
	    List<File> dirs = new ArrayList<File>();
	    File[] files = parent.listFiles();
	    
	    if (files == null) return dirs; // empty dir
	    for (File f : files){
	        if (f.isDirectory()) {
	             if (level == 0) dirs.add(f);
	             else if (level > 0) dirs.addAll(getCategoryDirs(f,level-1));
	        }
	    }
	    
	    return dirs;
	}
	
	
	/* Permet de choisir une catégorie au hasard parmi celles stockées */ 
	private Category chooseCategory () {
		ArrayList<Category> randomCategories = categories;
		Collections.shuffle(randomCategories);
		Category chosenCategory = randomCategories.get(0);
		return chosenCategory;
	}
	
	public Category first() {
		return firstCategory;
	}
	
	public Category current() {
		return currentCategory;
	}

}
