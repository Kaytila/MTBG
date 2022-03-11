package net.ck.game.test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
 
public class ImageViewer extends Application 
{

	private final Logger logger = (Logger) LogManager.getLogger(getRealClass());

	public Class<?> getRealClass()
	{
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
		{
			return enclosingClass;
		} else
		{
			return getClass();
		}
	}
	

	    public static void main(String[] args) 
	    {
	        Application.launch(args);
	    }
	     
	    @Override
	    public void start(Stage stage) 
	    {
	        // Load an image in the background
	        String imageUrl = "https://docs.oracle.com/javafx/javafx/images/javafx-documentation.png";
	        Image image = new Image(imageUrl);
	         
	        // Create three views of different sizes of the same image
	        ImageView view1 = getImageView(image, 100, 50, false);
	        ImageView view2 = getImageView(image, 100, 50, true);
	        ImageView view3 = getImageView(image, 100, 100, true);
	         
	        // Create the HBox      
	        HBox root = new HBox(10);
	        // Add Children to the HBox
	        root.getChildren().addAll(view1, view2, view3);
	         
	        // Set the padding of the HBox
	        root.setStyle("-fx-padding: 10;");
	        // Set the border-style of the HBox
	        root.setStyle("-fx-border-style: solid inside;");
	        // Set the border-width of the HBox
	        root.setStyle("-fx-border-width: 2;");
	        // Set the border-insets of the HBox
	        root.setStyle("-fx-border-insets: 5;");
	        // Set the border-radius of the HBox
	        root.setStyle("-fx-border-radius: 5;");
	        // Set the border-color of the HBox
	        root.setStyle("-fx-border-color: blue;");       
	        // Set the size of the HBox
	        root.setPrefSize(350, 200);
	         
	        // Create the Scene
	        Scene scene = new Scene(root);
	        // Add the scene to the Stage
	        stage.setScene(scene);
	        // Set the title of the Stage
	        stage.setTitle("Multiple Views of an Image");
	        // Display the Stage
	        stage.show();       
	    }
	     
	    private ImageView getImageView(Image image, double fitWidth,
	            double fitHeight, boolean preserveRation) 
	    {
	            // Create the ImageView
	            ImageView view = new ImageView(image);
	            // Set Properties Width, Height, Smooth and PreserveRatio
	            view.setFitWidth(fitWidth);
	            view.setFitHeight(fitHeight);
	            view.setPreserveRatio(preserveRation);
	            view.setSmooth(true);
	            return view;
	    }   
	}	
