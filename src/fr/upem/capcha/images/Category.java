package fr.upem.capcha.images;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Category implements Images{
	
	List<URL> images = new ArrayList<URL>();
	
	public Category() {
		super();
		this.getPhotos();
		System.out.println("cat créée");
	}

	@Override
	public List<URL> getPhotos() {
		
		List<URL> allImagesURL = new ArrayList<URL>();
		String path = Category.class.getPackage().getName().replace('.', '/');
		Path pathObj = Paths.get(path);
		
		List<String> directories = null;
		
		
		
		System.out.println("package name :" + path);
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<URL> getRandomPhotosURL(int value) {
		// TODO Auto-generated method stub
		return null;
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
