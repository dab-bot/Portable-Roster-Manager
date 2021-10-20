package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Show;
import models.RoleRequest;

public class AccountController extends Controller {
	
	
	@FXML private TextField fName, lName, email, country, gender;
	@FXML private Label errorLabel, errorLabelRole;
	@FXML private TextField current_pass, new_pass, re_new_pass;
	
	@FXML private ChoiceBox roleType;
	@FXML private TextField orgName, orgContact;
	
    private Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Main main = null;
	
	ObservableList<String> Type = FXCollections.observableArrayList("Series", "Show");
	
	
	@FXML private void initialize() {
	}
	
	

	
	// update Account info
	public void updateDetails () {
        if (fName.getText().equals("") || lName.getText().equals("") || email.getText().equals("")) {
        	errorLabel.setText("Some fields have been entered incorrectly, please check your input");
		} else {
			String sql = "UPDATE accounts SET first_name = ?, last_name = ?, email = ?, country = ?, gender = ? WHERE username = ?";
	        try {
	            preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, fName.getText());
                preparedStatement.setString(2, lName.getText());
                preparedStatement.setString(3, email.getText());
                preparedStatement.setString(4, country.getText());
                preparedStatement.setString(5, gender.getText());
                preparedStatement.setString(6, main.currentUser.getUsername());
	            preparedStatement.executeUpdate();
	            JOptionPane.showMessageDialog(null,"Account details have been updated");
	            } catch (SQLException ex) {
	                System.err.println(ex.getMessage());
	            }
	        main.currentUser.setFirst_name(fName.getText());
	        main.currentUser.setLast_name(lName.getText());
        }
	}
	
	public void changePassword() {
		
	}
	
	public void requestMovie() throws IOException {
		main.changeScene("/views/AddMovie.fxml");
	}
	
	public void requestRole() {
        if (orgName.getText().equals("") || orgContact.getText().equals("")) {
        	errorLabelRole.setText("Some fields have been entered incorrectly, please check your input");
		} else {
            //query
            String sql = "INSERT INTO roleRequests(username,currentRole,requestedRole,orgName,orgContact) VALUES(?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, main.currentUser.getUsername());
                preparedStatement.setString(2, main.currentUser.getRole());
                preparedStatement.setString(3, String.valueOf(roleType.getValue()));
                preparedStatement.setString(4, orgName.getText());
                preparedStatement.setString(5, orgContact.getText());
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null,"Request has been sent to the administration team");
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
	}
	
	

	public void setMain(Main main) {
		this.main = main;
		
	}

	@Override
	public void setConnection(Connection c) {
		this.con = c;
	}

	@Override
	public void fill() {
		roleType.getItems().add("Production Company owner");
		roleType.getItems().add("Critic");

        
		fName.setText(main.currentUser.getFirst_name());
		lName.setText(main.currentUser.getLast_name());
	}
	
	public void logOut() throws IOException {
		main.changeUser(null);
		main.changeScene("/views/LogIn.fxml");
	}
	
	public void goBack() throws IOException {
		main.changeScene("/views/MainMenu.fxml");
	}

	
	
	
}
