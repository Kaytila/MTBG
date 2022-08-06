package net.ck.game.test;
/*
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageTester extends Application implements Runnable
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

	Image image;
	String path;
	
	@Override
	public void run()
	{
		logger.error("launch");
		//Application.launch("");
	}

	@Override
	public void start(Stage stage2) throws Exception
	{
		logger.error("start");
		Stage stage = new Stage();
		// load the image
        Image image = new Image(path);

        // simple displays ImageView the image as is
        ImageView iv1 = new ImageView();
        iv1.setImage(image);


        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        HBox box = new HBox();
        box.getChildren().add(iv1);
        root.getChildren().add(box);

        stage.setTitle("ImageView of " + image.getUrl());
        stage.setWidth(415);
        stage.setHeight(200);
        stage.setScene(scene); 
        stage.sizeToScene(); 
        stage.show(); 
		
	}

	public ImageTester(String imgPath)
	{
		
		path = imgPath;
	}
}
*/
