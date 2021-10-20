package controllers;

import application.Main;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Shift;
import models.Show;
import utils.DBConnector;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ShiftCardController extends Controller{

	@FXML private AnchorPane backgroundPane;
	@FXML private Label employeeIcon,employeeName,employeeRole,startTime,endTime,startAMPM,endAMPM;

	private Main main;
	private Shift shift;
	private Connection con;
	private LocalDate shiftCardDate;
	private MainMenuController parent;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	public ShiftCardController() {
	}

	public void editShift() throws IOException {
		Stage shiftEditStage = new Stage();
		EditShiftController c;
		shiftEditStage.setResizable(false);
		shiftEditStage.initModality(Modality.APPLICATION_MODAL);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ShiftEdit.fxml"));
		Parent root = loader.load();
		c = loader.getController();
		c.setMain(main);
		c.setConnection(con);
		c.fill();
		c.setShift(shift);
		c.setParent(parent);
		c.setDate(shiftCardDate);
		c.newShiftFormat(false);
		c.setController(shiftEditStage);
		shiftEditStage.setTitle("Edit Shift");
		shiftEditStage.setScene(new Scene(root));
		shiftEditStage.showAndWait();
	}

	@Override
	public void setMain(Main newMain) {
		this.main = newMain;
	}
	
	public void setConnection(Connection c) {
		this.con = c;
	}

	public void setShift(Shift newShift) {
		this.shift = newShift;
	}

	public void setDate(LocalDate d){
		this.shiftCardDate = d;
	}

	public void setParent(MainMenuController parent){
		this.parent = parent;
	}

	@Override
	public void fill() {
		String sql = "SELECT * FROM accounts Where username = ?";
		try {
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, shift.getUsername());
			resultSet = preparedStatement.executeQuery();
			employeeName.setText(resultSet.getString("first_name") + ". " + resultSet.getString("last_name").charAt(0));
			employeeRole.setText(resultSet.getString("role"));
			employeeIcon.setText(String.valueOf(resultSet.getString("first_name").charAt(0)));
			employeeIcon.setStyle("-fx-background-color: " + resultSet.getString("profileBG") + ";");
			employeeIcon.setTextFill(Paint.valueOf(resultSet.getString("profileText")));
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
		startTime.setText(shift.getShiftStartTime().format(formatter));
		endTime.setText(shift.getShiftEndTime().format(formatter));
		formatter = DateTimeFormatter.ofPattern("a");
		startAMPM.setText(shift.getShiftStartTime().format(formatter));
		endAMPM.setText(shift.getShiftEndTime().format(formatter));
	}

	public void hoverOn(){
		backgroundPane.setStyle("-fx-background-color: #dee9ff; -fx-background-radius: 5;");
	}

	public void hoverOff(){
		backgroundPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 5;");
	}
}
