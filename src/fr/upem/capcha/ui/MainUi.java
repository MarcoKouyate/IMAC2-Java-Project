package fr.upem.capcha.ui;

import java.awt.Color;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.capcha.logic.CategoryManager;
import fr.upem.capcha.logic.MainLogic;

/**
 * <b>MainUi is the main class of the program.</b>
 * <p>
 * This class manages:
 * </p>
 * 
 * <ul>
 * <li>interface</li>
 * <li>user events</li>
 * <li>windows</li>
 * </ul>
 * 
 * <p>
 * This class delegates the logic part of the program to the MainLogic class and only display the state of the application
 * </p>
 * 
 */

public class MainUi {
	/**
	 * list of images url selected by user
	 */
	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	
	/**
	 * window instance 
	 */
	private static JFrame frame = new JFrame("Capcha");
	
	/**
	 * class running the logic part of the program 
	 */
	private static MainLogic logicEngine = new MainLogic();
	
	/**
	 * window instance displaying messages
	 */
	private static JFrame console = new JFrame("Capcha - result");
	
	/**
	 * message to display
	 */
	private static JTextArea message = new JTextArea("");

	/**
	 * Main function
	 * 
	 * <p>
	 * 	Set the settings and create a window 
	 * </p>
	 * @param args
	 * 		list of parameters specified during program execution
	 * 
	 * @throws IOException if image file can't be read
	 */
	public static void main(String[] args) throws IOException {
		
				
		 // Création de la fenêtre principale
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable
		
		console.setLayout(new GridLayout(1,1));
		console.setSize(512, 120); // définition de la taille
		console.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.
		console.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		createWindow();
		
		frame.setVisible(true);

	}
	
	/**
	 * Display list of images in the window
	 * 
	 * @param imageList
	 * 		list of images to display
	 * 
	 * @param window
	 * 		window displaying the images
	 * 
	 * @throws IOException if image file can't be read
	 */
	private static void createImages(List<URL> imageList, JFrame window) throws IOException {
		for (URL imageurl : imageList) {
			window.add(createLabelImage(imageurl));
		}
	}
	
	
	/**
	 * create a specific layout for the program window
	 * 
	 * @return grid layout (four lines, 3 rows)
	 */
	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}
	
	/**
	 * Create the validation button
	 * 
	 * @return button panel with specific action
	 */
	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Vérifier") { //ajouter l'action du bouton
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des évènements
					
					@Override
					public void run() { // c'est un runnable
						if (logicEngine.isCaptchaCorrect(selectedImages)) {
							createConsole("Félicitations vous avez réussi");
						} else {
							createWindow();
							createConsole("Vous avez fait une erreur. Réessayez");
						}
					}
				});
			}
		});
	}
	
	/**
	 * Create a alert window
	 * @param msg
	 * 		message to display in the alert window
	 */
	private static void createConsole(String msg) {
		console.remove(message);
		message = new JTextArea(msg);
		console.add(message);
		console.revalidate();
		console.setVisible(true);
	}
	
	/**
	 * Create / Refresh the application window with all components
	 */
	private static void createWindow () {
		selectedImages.clear();
		
		frame.getContentPane().removeAll();
		
		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes	
		frame.setLayout(layout);

		 
		try {
			createImages(logicEngine.getImages(), frame);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logicEngine.increaseDifficulty();
		
		frame.add(new JTextArea("Cliquez sur les images avec des " + logicEngine.getCurrentCategory()));
		
		JButton okButton = createOkButton();
		frame.add(okButton);
	
		 frame.revalidate();
		 frame.repaint();
	}
	
	/**
	 * Create Image component using the url to an image file
	 * <p>
	 * The component also includes mouse events
	 * </p>
	 * 
	 * @param
	 *  	url to the image file to display
	 * @return Image component including photo and mouse actions
	 * 
	 * @throws IOException if image file can't be read
	 */
	private static JLabel createLabelImage(URL url) throws IOException {
	
		BufferedImage img = ImageIO.read(url); //lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); //redimentionner l'image
		
		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre
		
		label.addMouseListener(new MouseListener() { //Ajouter le listener d'évenement de souris
			private boolean isSelected = false;
			
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
		
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) { //ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
				EventQueue.invokeLater(new Runnable() { 
					
					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							isSelected = true;
							selectedImages.add(url);
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(url);
						}
						
					}
				});
				
			}
		});
		
		return label;
	}
}
