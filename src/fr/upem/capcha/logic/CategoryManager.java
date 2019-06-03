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

/**
 * <b>CategoryManager is a class managing categories and related pictures.</b>
 * 
 * <p>
 * What it does :
 * </p>
 * 
 * <ul>
 * <li>Get the relevant categories depending of the difficulty level</li>
 * <li>Pick one of them as the current category.</li>
 * </ul>
 * 
 * <p>
 * 	Directed by MainLogic and managing Category instances
 * </p>
 * 
 * @see MainLogic
 * @see Category
 * 
 */
public class CategoryManager {
	
    /**
     * list of categories of the current level
     * 
     * @see Category
     * @see CategoryManager#chooseCategory()
     * @see CategoryManager#updateCategories(int)
     */
	private ArrayList<Category> categories;
	//TODO see if we can remove categories
	
    /**
     * active category
     * 
     * @see CategoryManager#chooseCategory()
     * @see CategoryManager#updateCategories(int)
     * @see CategoryManager#current()
     */
	private Category currentCategory;
	
    /**
     * initial category 
     * 
     * @see CategoryManager#first()
     * 
     */
	private Category firstCategory;

    /**
     * CategoryManager Constructor.
     * <p>
     * set the current and the first category depending on the difficulty
     * </p>
     * 
     * @param difficulty
     * 		set the level of categories to choose from
     * 
     * @see CategoryManager#getCategories(int)
     * @see CategoryManager#chooseCategory()
     * @see CategoryManager#currentCategory
     * @see CategoryManager#firstCategory
     */
	public CategoryManager(int difficulty) {
		categories = this.getCategories(difficulty);
		currentCategory = chooseCategory();
		firstCategory = currentCategory;
	}
	
	
	/**
     * Search and create all categories with specified level of difficulty
     * 
     * <p>
     * 	Note : using introspection to create instances dynamically
     * </p>
     * 
     * @param difficulty
     * 		set the level of sub-categories to choose
     * 
     * @return list of all categories depending on the input difficulty
     * 
     * @see CategoryManager#getCategoryNames(int)
     */
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
			    	System.out.println("La classe n'existe pas");
			    	e.printStackTrace(); 
			    }
			    catch (InstantiationException e)
			    {
			    	System.out.println("// La classe est abstract ou est une interface ou n'a pas de constructeur accessible sans param�tre");
			    	e.printStackTrace(); 
			    }
			    catch (IllegalAccessException e)
			    {
			    	System.out.println("La classe n'est pas accessible");
			    	e.printStackTrace(); 
			    }
			}
		}	while	(false);
		
		return newCategories;
	}
	
	
	/**
     * update categories with specified level of difficulty
     * 
     * @param difficulty
     * 		set the new level of sub-categories to get
     * 
     * @see CategoryManager#categories
     * @see CategoryManager#currentCategory
     */
	public void updateCategories(int difficulty) {
		categories = getCategories(difficulty);
		currentCategory = chooseCategory();
	}
	
	
	/**
     * Get the name of all categories with specified level of difficulty
     * 
     * 
     * @param level
     * 		set the level of sub-categories to choose
     * 
     * @return list of all categories name depending on the input difficulty
     */
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
	
	/**
     * Get the list of all sub directories depending on the input level
     * 
     * <p>
     * 		Note: is a recursive function. calls itself until current directory has no child
     * </p>
     * 
     * @param level
     * 		level of depth when browsing the files
     * @param parent
     * 		current file to browse
     * 
     * @return list of all sub-directories depending on the input level
     */
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
	
	
	/**
     * Choose a random category from the list of categories of current level
     * 
     * @return a random category 
     */ 
	private Category chooseCategory () {
		ArrayList<Category> randomCategories = categories;
		Collections.shuffle(randomCategories);
		Category chosenCategory = randomCategories.get(0);
		return chosenCategory;
	}
	
	/**
     * Get the initial category
     * 
     * @return initial category 
     */ 
	public Category first() {
		return firstCategory;
	}
	
	/**
     * Get the current category
     * 
     * @return current category 
     */ 
	public Category current() {
		return currentCategory;
	}

}
