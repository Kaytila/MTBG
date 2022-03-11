package net.ck.game.test;

import java.util.ArrayList;
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

public class ImageTest extends Application implements Runnable
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
	/**
	 * only used for testing
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		Application.launch(args);
	}
	Image image;;


	ImageView iv1 = new ImageView();

	@SuppressWarnings("exports")
	public Image getImage()
	{
		return image;
	}

	public void loadImages()
	{
		logger.error(Thread.currentThread().getName() + ", executing run() method!");

		Image standardImage = new Image("file:graphics/image1.png");

		logger.error("image height image1: " + standardImage.getHeight());
		logger.error("image width image1:" + standardImage.getWidth());

		Image movingImage = new Image("file:graphics/image2.png");

		logger.error("image height image2: " + standardImage.getHeight());
		logger.error("image width image2:" + standardImage.getWidth());

		ArrayList<Image> images = new ArrayList<Image>();
		images.add(movingImage);
		images.add(standardImage);
	}

	@Override
	public void run()
	{
		logger.error("running run for imageTest class");
		Application.launch(ImageTest.class);
	}

	@SuppressWarnings("exports")
	public void setImage(Image image)
	{
		this.image = image;
	}

	@Override
	public void start(@SuppressWarnings("exports") Stage stage)
	{

		logger.error(Thread.currentThread().getName() + ", executing start() method!");

		Image standardImage = new Image("file:graphics/image1.png");

		logger.error("image height image1: " + standardImage.getHeight());
		logger.error("image width image1:" + standardImage.getWidth());

		Image movingImage = new Image("file:graphics/image2.png");
		ArrayList<Image> images = new ArrayList<Image>();
		images.add(movingImage);
		images.add(standardImage);
		ImageView iv1 = new ImageView();
		iv1.setImage(standardImage);
		ImageView iv2 = new ImageView();
		iv2.setImage(movingImage);
		Group root = new Group();
		Scene scene = new Scene(root);
		scene.setFill(Color.BLACK);
		HBox box = new HBox();
		box.getChildren().add(iv1);
		box.getChildren().add(iv2);
		root.getChildren().add(box);

		stage.setTitle("ImageView");
		stage.setWidth(415);
		stage.setHeight(200);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
	}

	@SuppressWarnings("exports")
	public void updateImage(Image img)
	{
		if (iv1 == null)
		{
			logger.error("iv1 is null");
		}
		if (img == null)
		{
			logger.error("image is null");
		}
		iv1.setImage(img);
	}
}
