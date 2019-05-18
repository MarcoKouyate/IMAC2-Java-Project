package fr.upem.capcha.images;

import java.net.URL;
import java.util.List;

public interface Images {

	public List<URL> getPhotos();
	
	public List<URL> getRandomPhotosURL(int value);

	public List<URL> getRandomPhotoURL();
	
	public boolean isPhotoCorrect(URL url);

}
