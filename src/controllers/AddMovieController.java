package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.DBConnector;

public class AddMovieController extends Controller{

	
	@FXML private Button addMovieButton, logOutButton;
	@FXML private CheckBox actionCheck, adventureCheck, crimeCheck, docoCheck, dramaCheck, horrorCheck, romanceCheck, scifiCheck, thrillerCheck;
	@FXML private ChoiceBox<String> movieSeriesSelect, procoSelect;
	@FXML private TextField showLength, showTitle, showYear;
	@FXML private Label errorLabel;
	
	
    private Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Main main;
	
	 @FXML
		private void initialize() throws IOException {
			con = DBConnector.conDB();
			movieSeriesSelect.getItems().add("Movie");
			movieSeriesSelect.getItems().add("Series");
			String sql = "SELECT * FROM production_company";
	        try {
	            preparedStatement = con.prepareStatement(sql);
	            resultSet = preparedStatement.executeQuery();
	            while (resultSet.next())
	            	procoSelect.getItems().add(resultSet.getString("proco_name"));
	        } catch (SQLException ex) {
	            System.err.println(ex.getMessage());
	        }
			
		}

	@Override
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setConnection(Connection c) {
		this.con = c;
	}
	public void addMovie () throws IOException {
		
		String show_title = showTitle.getText();
		Float length = Float.parseFloat(showLength.getText());
        int movie = (movieSeriesSelect.getValue().equals("Movie") ? 1 : 0 );
        int series = (movieSeriesSelect.getValue().equals("Series") ? 1 : 0 );
        int proco_id = getProcoId((String) procoSelect.getValue());
        int year = Integer.parseInt(showYear.getText());
        String genre = "";
        if (actionCheck.isSelected())
        	genre += "Action,";
        if (adventureCheck.isSelected())
        	genre += "Adventure,";
        if (crimeCheck.isSelected())
        	genre += "Crime,";
        if (docoCheck.isSelected())
        	genre += "Documentary,";
        if (dramaCheck.isSelected())
        	genre += "Drama,";
        if (horrorCheck.isSelected())
        	genre += "Horror,";
        if (romanceCheck.isSelected())
        	genre += "Romance,";
        if (scifiCheck.isSelected())
        	genre += "Sci-Fi,";
        if (thrillerCheck.isSelected())
        	genre += "Thriller,";
        
        

        
        
        if (show_title.equals("") || length <=0 || length == null || (movie == 0 && series == 0) || proco_id < 0 || year>9999 || year < 1000) {
        	errorLabel.setText("Some fields have been entered incorrectly, please check your input");
		} else {
            //query
            String sql = "INSERT INTO shows(show_title,genre,length,movie,series,proco_id,year) VALUES(?,?,?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, show_title);
                preparedStatement.setString(2, genre);
                preparedStatement.setFloat(3, length);
                preparedStatement.setInt(4, movie);
                preparedStatement.setInt(5, series);
                preparedStatement.setInt(6, proco_id);
                preparedStatement.setInt(7, year);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null,"Movie has been added to database");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
		
	}
	
	public int getProcoId(String proco_name) {
        String sql = "SELECT * FROM production_company Where proco_name = ?";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, proco_name);
            resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("proco_id");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return -1;
	}
	
	public void logOut() throws IOException {
		main.changeUser(null);
		main.changeScene("/views/LogIn.fxml");
	}
	
	public void goBack() throws IOException {
		if(main.currentUser != null && main.currentUser.getRole().equals("ADMIN")) {
			main.changeScene("/views/AddMoviePCo.fxml");
		} else {
			main.changeScene("/views/AccountControlPanel.fxml");
		}
		
	}

	@Override
	public void fill() {
	}

	
}
