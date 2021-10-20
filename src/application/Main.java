package application;


import controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.User;
import utils.DBConnector;

import java.io.IOException;
import java.sql.Connection;


public class Main extends Application {
	
	private static Stage stg;
	public User currentUser;
	private Connection con;
	public Controller c;
	@Override
	public void start(Stage primaryStage) throws Exception{
		stg = primaryStage;
		primaryStage.setResizable(false);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LogIn.fxml"));
		Parent root = loader.load();
		c = loader.getController();
		c.setMain(this);
		con = DBConnector.conDB();
		c.setConnection(con);
		c.fill();
		primaryStage.setTitle("Expiry Date tracking");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		
	}
	
	public void changeScene(String fxml) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
		Parent pane = loader.load();
		c = loader.getController();
		c.setMain(this);
		c.setConnection(con);
		c.fill();
		stg.getScene().setRoot(pane);
	}
	
	public void changeUser(User newUser){
		this.currentUser = newUser;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
