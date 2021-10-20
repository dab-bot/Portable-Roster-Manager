package controllers;

import application.Main;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.Show;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditAccountController extends Controller{

	@FXML private JFXTextField username, firstName, lastName, roleSelect;
	@FXML private JFXPasswordField password, re_password;
	@FXML private Label signUpError, previewLabel;
	@FXML private JFXColorPicker bgColourPicker, textColourPicker;
	
	
    private Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Main main;
    private Show show;
	
	 @FXML
		private void initialize() throws IOException {	
			
		}

	@Override
	public void setMain(Main main) {
		this.main = main;
	}
	
	public void setConnection(Connection c) {
		this.con = c;
	}

	@Override
	public void fill() {
	 	/*
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
        
		showTitle.setText(main.currentShow.getShowTitle());
		showLength.setText(String.valueOf(show.getLength()));
		if(show.getMovie() == 1) {
			movieSeriesSelect.setValue("Movie");
		}else {
			movieSeriesSelect.setValue("Series");
		}
		showYear.setText(String.valueOf(show.getYear()));
		
		sql = "SELECT * FROM production_company WHERE proco_id = ?";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, show.getProcoID());
            resultSet = preparedStatement.executeQuery();
            procoSelect.setValue(resultSet.getString("proco_name"));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

	 	 */
	}

	/*
	public void newShiftFormat(Boolean format){
		saveSingle.setVisible(!format);
		saveMultiple.setVisible(!format);
		cancelSingle.setVisible(!format);
		cancelMultiple.setVisible(!format);
		addShift.setVisible(format);
	}
	 */

	// On register page
	public void createAccount () throws IOException {

		String usrname = username.getText();
		String pswrd = password.getText();
		String rpswrd = re_password.getText();
		String fname = firstName.getText();
		String lname = lastName.getText();
		String role = roleSelect.getText();
		Color c = bgColourPicker.getValue();
		int r = (int)Math.round(c.getRed() * 255.0);
		int g = (int)Math.round(c.getGreen() * 255.0);
		int b = (int)Math.round(c.getBlue() * 255.0);
		String profileBG = String.format("#%02x%02x%02x" , r, g, b).toUpperCase();
		String profileText = textColourPicker.getValue().toString();


		if (fname.isEmpty() || lname.isEmpty() || usrname.isEmpty() || pswrd.isEmpty() || rpswrd.isEmpty() || role.isEmpty()) {
			signUpError.setText("Please enter all fields!");
		} else if (!rpswrd.toString().equals(pswrd.toString())) {
			signUpError.setText("Password not matching!");
		} else if (searchAccount(usrname)) {
			signUpError.setText("Username already exists");
		} else {
			//query
			String sql = "INSERT INTO accounts(username,password,first_name,last_name,role,profileBG,profileText) VALUES(?,?,?,?,?,?,?)";
			try {
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, usrname);
				preparedStatement.setString(2, pswrd);
				preparedStatement.setString(3, fname);
				preparedStatement.setString(4, lname);
				preparedStatement.setString(5, role);
				preparedStatement.setString(6, profileBG);
				preparedStatement.setString(7, profileText);
				preparedStatement.executeUpdate();
				JOptionPane.showMessageDialog(null,"Account successfully created");
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
		}

	}

	public boolean searchAccount(String userquery) {
		String usrname = userquery;
		if(usrname.isEmpty()) {
			return false;
		} else {
			//query
			String sql = "SELECT * FROM accounts Where username = ?";
			try {
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1, usrname);
				resultSet = preparedStatement.executeQuery();
				if (resultSet == null || !resultSet.next()) {
					return false;
				} else {
					return true;
				}
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
				return false;
			}
		}
	}

	public void backgroundChange(){
	 	Color c = bgColourPicker.getValue();
		int r = (int)Math.round(c.getRed() * 255.0);
		int g = (int)Math.round(c.getGreen() * 255.0);
		int b = (int)Math.round(c.getBlue() * 255.0);
		String hexValue = String.format("#%02x%02x%02x" , r, g, b).toUpperCase();
		previewLabel.setStyle("-fx-background-color: " + hexValue + ";");
	 }

	public void textChange(){
		previewLabel.setTextFill(Paint.valueOf(textColourPicker.getValue().toString()));
	}

	public void labelChange(){
		if(firstName.getText().length() > 0)
			previewLabel.setText(String.valueOf(firstName.getText().charAt(0)));
	}

	
}
