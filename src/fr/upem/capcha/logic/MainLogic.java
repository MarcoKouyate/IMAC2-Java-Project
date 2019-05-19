package fr.upem.capcha.logic;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.upem.capcha.images.*;
import fr.upem.capcha.images.vehicles.*;

public class MainLogic {
	
	private int difficulty;
	private ArrayList<Category> categories;
	private Category theCategorie;
	
	public MainLogic() {
		difficulty = 0;
		categories = new ArrayList<Category>();
		
		this.getCategories();
	}
	
	public ArrayList<Category> getCategories() {
		
		List<String> categoryString = getCategoryNames(difficulty);
		do {
			for(String theCategoryString : categoryString) {
				System.out.println("\n Nom catégories : " + theCategoryString +"\n");
				try
			    {
					Class.forName("Vehicules").newInstance();
			    }
			    catch (ClassNotFoundException e)
			    {
			      // La classe n'existe pas
			    }
			    catch (InstantiationException e)
			    {
			      // La classe est abstract ou est une interface ou n'a pas de constructeur accessible sans paramètre
			    }
			    catch (IllegalAccessException e)
			    {
			      // La classe n'est pas accessible
			    }
			}
		}while(false);
		
		return categories;
	}
	
	
	public List<String> getCategoryNames(int level){
		
		String dirName = "src/fr/upem/capcha/images";
		
		List<String> categoryNames = new ArrayList<String>();
		List<File> files = this.getCategoryDirs(new File(dirName), level);
		for( File f : files) {
			if(f.isDirectory()) categoryNames.add(f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1));
		}
		
		return categoryNames;
	}
	
	
	public List<File> getCategoryDirs(File parent, int level){
		
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
	
	public void  browseImages() {
		
	}
	
	boolean checkCaptcha() {
		return true;
	}
}
