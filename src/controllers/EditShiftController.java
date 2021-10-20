package controllers;

import application.Main;
import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Shift;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class EditShiftController extends Controller {


    @FXML
    private Button saveSingle, saveMultiple, cancelSingle, cancelMultiple, addShift;
    @FXML
    private Label addShiftError,repeatLabel;
    @FXML
    private JFXComboBox employeeSelect, repeatUnit;
    @FXML
    private JFXDatePicker startDate;
    @FXML
    private JFXTextField repeatValue, tenMinBreaks, thirtyMinBreaks;
    @FXML
    private JFXTimePicker startTime, endTime;
    @FXML
    private JFXToggleButton repeatingShiftToggle;


    private Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Main main;
    private Shift shift;
    private MainMenuController parent;
    private LocalDate currentShiftDate;
    private Stage currentShiftCard;

    @Override
    public void setMain(Main main) { this.main = main; }

    public void setParent(MainMenuController m) {
        this.parent = m;
    }

    public void setConnection(Connection c) {
        this.con = c;
    }

    public void setDate(LocalDate d) { this.currentShiftDate = d; }

    public void setShift(Shift s) { this.shift = s; }

    public void setController(Stage s) {this.currentShiftCard = s;}

    public String getUserName(String employeeName) {
        String sql = "Select * from accounts where accounts.first_name || ' ' || accounts.last_name = ?";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, employeeName);
            resultSet = preparedStatement.executeQuery();
            return resultSet.getString("username");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return "";
    }

    public void newShiftFormat(Boolean format) {
        saveSingle.setVisible(!format);
        saveMultiple.setVisible(!format&&shift.isRepeating());
        cancelSingle.setVisible(!format);
        cancelMultiple.setVisible(!format&&shift.isRepeating());
        addShift.setVisible(format);
        if (!format) {
            employeeSelect.setValue(getFullname(shift.getUsername()));
            startDate.setValue(currentShiftDate);
            startTime.setValue(shift.getShiftStartTime());
            endTime.setValue(shift.getShiftEndTime());
            thirtyMinBreaks.setText(String.valueOf(shift.getThirtyMinBreaks()));
            tenMinBreaks.setText(String.valueOf(shift.getTenMinBreaks()));
            repeatingShiftToggle.setSelected(shift.isRepeating());
            if(shift.isRepeating()){
                showRepeatOptions();
                repeatValue.setText(String.valueOf(shift.getDaysPerRepeat()));
                repeatUnit.setValue("Days");
            }
        }
    }

    public void showRepeatOptions(){
        if(repeatingShiftToggle.isSelected()){
            repeatValue.setVisible(true);
            repeatUnit.setVisible(true);
            repeatLabel.setVisible(true);
        }else{
            repeatValue.setVisible(false);
            repeatUnit.setVisible(false);
            repeatLabel.setVisible(false);
        }
    }

    public String getFullname(String username) {
        String fullname = "";
        String sql = "SELECT * FROM accounts Where username = ?";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            fullname = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return fullname;
    }

    @Override
    public void fill() {

        String sql = "SELECT * FROM accounts";
        try {
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                employeeSelect.getItems().add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        repeatUnit.getItems().add("Weeks");
        repeatUnit.getItems().add("Days");

    }

    public boolean validateInput(){
        if (employeeSelect.getValue() == null || startDate.getValue() == null || startTime.getValue() == null || endTime.getValue() == null)
            return false;
        String employeeName = employeeSelect.getValue().toString();
        String sDate = startDate.getValue().toString();
        String sTime = startTime.getValue().toString();
        String eTime = endTime.getValue().toString();
        if (employeeName.isEmpty() || sDate.isEmpty() || sTime.isEmpty() || eTime.isEmpty())
            return false;
        return true;
    }

    public void addShift() {
        String employeeName = employeeSelect.getValue().toString();
        String usrname = getUserName(employeeName);
        String sDate = startDate.getValue().toString();
        String sTime = startTime.getValue().toString();
        String eTime = endTime.getValue().toString();
        int repeat = (repeatingShiftToggle.isSelected()) ? 1 : 0;
        int thirtyMin = 0;
        int tenMin = 0;
        int daysPerRepeat = 1;
        if(repeatingShiftToggle.isSelected()){
            try {
                if (!thirtyMinBreaks.getText().equals("")) {
                    thirtyMin = Integer.parseInt(thirtyMinBreaks.getText());
                }
                if (!tenMinBreaks.getText().equals("")) {
                    tenMin = Integer.parseInt(tenMinBreaks.getText());
                }
                int multiplier = ((repeatUnit.getValue().toString().equals("Weeks")) ? 7 : 1);
                daysPerRepeat = Integer.parseInt(repeatValue.getText()) * multiplier;
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        String sql = "INSERT INTO shifts(username,shiftStartTime,shiftEndTime,shiftStartDate,thirtyMinBreaks,tenMinBreaks,repeating,daysPerRepeat) VALUES(?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, usrname);
            preparedStatement.setString(2, sTime);
            preparedStatement.setString(3, eTime);
            preparedStatement.setString(4, sDate);
            preparedStatement.setInt(5, thirtyMin);
            preparedStatement.setInt(6, tenMin);
            preparedStatement.setInt(7, repeat);
            preparedStatement.setInt(8, daysPerRepeat);
            preparedStatement.executeUpdate();
            parent.updatePage();
            JOptionPane.showMessageDialog(null, "Shift successfully created");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void endOriginal(){
        String sql = "";
        //End original shift 1 day early
        try {
            if(currentShiftDate.minusDays(1).isAfter(shift.getShiftStartDate())) {
                sql = "UPDATE shifts SET shiftEndDate = ? WHERE shift_id = ?";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, currentShiftDate.minusDays(1).toString());
                preparedStatement.setInt(2, shift.getShiftID());
            }else {
                sql = "DELETE FROM shifts WHERE shift_id = ?";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, shift.getShiftID());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void resumeOnNextCycle(){
        //Resume Original shift starting from next cycle
        String sql = "";
        if(shift.getShiftEndDate() == null || shift.getShiftEndDate().isAfter(currentShiftDate)){
            if(shift.getShiftEndDate() == null){
                String eDate = "";
                sql = "INSERT INTO shifts(username,shiftStartTime,shiftEndTime,shiftStartDate,thirtyMinBreaks,tenMinBreaks,repeating,daysPerRepeat) VALUES(?,?,?,?,?,?,?,?)";
            }else{
                sql = "INSERT INTO shifts(username,shiftStartTime,shiftEndTime,shiftStartDate,shiftEndDate,thirtyMinBreaks,tenMinBreaks,repeating,daysPerRepeat) VALUES(?,?,?,?,?,?,?,?,?)";
            }
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, shift.getUsername());
                preparedStatement.setString(2, shift.getShiftStartTime().toString());
                preparedStatement.setString(3, shift.getShiftEndTime().toString());
                preparedStatement.setString(4, currentShiftDate.plusDays(shift.getDaysPerRepeat()).toString());
                if(shift.getShiftEndDate() == null) {
                    preparedStatement.setInt(5, shift.getThirtyMinBreaks());
                    preparedStatement.setInt(6, shift.getTenMinBreaks());
                    preparedStatement.setInt(7, shift.isRepeating() ? 1 : 0);
                    preparedStatement.setInt(8, shift.getDaysPerRepeat());
                }else{
                    preparedStatement.setString(5, shift.getShiftEndDate().toString());
                    preparedStatement.setInt(6, shift.getThirtyMinBreaks());
                    preparedStatement.setInt(7, shift.getTenMinBreaks());
                    preparedStatement.setInt(8, shift.isRepeating() ? 1 : 0);
                    preparedStatement.setInt(9, shift.getDaysPerRepeat());
                }
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public void singleSave(){
        String sql = "";
        endOriginal();
        resumeOnNextCycle();
        //Create Single new shift with changes added
        String employeeName = employeeSelect.getValue().toString();
        String usrname = getUserName(employeeName);
        String sDate = startDate.getValue().toString();
        String sTime = startTime.getValue().toString();
        String eTime = endTime.getValue().toString();
        int repeat = (repeatingShiftToggle.isSelected()) ? 1 : 0;
        int thirtyMin = 0;
        int tenMin = 0;
        int daysPerRepeat = 1;
        try {
            if (!thirtyMinBreaks.getText().equals("")) {
                thirtyMin = Integer.parseInt(thirtyMinBreaks.getText());
            }
            if (!tenMinBreaks.getText().equals("")) {
                tenMin = Integer.parseInt(tenMinBreaks.getText());
            }
            int multiplier = ((repeatUnit.getValue().toString().equals("Weeks")) ? 7 : 1);
            daysPerRepeat = Integer.parseInt(repeatValue.getText()) * multiplier;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        sql = "INSERT INTO shifts(username,shiftStartTime,shiftEndTime,shiftStartDate,shiftEndDate,thirtyMinBreaks,tenMinBreaks,repeating,daysPerRepeat) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, usrname);
            preparedStatement.setString(2, sTime);
            preparedStatement.setString(3, eTime);
            preparedStatement.setString(4, sDate);
            preparedStatement.setString(5, sDate);
            preparedStatement.setInt(6, thirtyMin);
            preparedStatement.setInt(7, tenMin);
            preparedStatement.setInt(8, repeat);
            preparedStatement.setInt(9, daysPerRepeat);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Shift successfully updated");
            parent.updatePage();
            currentShiftCard.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void multiSave(){
        endOriginal();
        //Create new shift with changes added
        String employeeName = employeeSelect.getValue().toString();
        String usrname = getUserName(employeeName);
        String sDate = startDate.getValue().toString();
        String sTime = startTime.getValue().toString();
        String eTime = endTime.getValue().toString();
        int repeat = (repeatingShiftToggle.isSelected()) ? 1 : 0;
        int thirtyMin = 0;
        int tenMin = 0;
        int daysPerRepeat = 1;
        try {
            if (!thirtyMinBreaks.getText().equals("")) {
                thirtyMin = Integer.parseInt(thirtyMinBreaks.getText());
            }
            if (!tenMinBreaks.getText().equals("")) {
                tenMin = Integer.parseInt(tenMinBreaks.getText());
            }
            int multiplier = ((repeatUnit.getValue().toString().equals("Weeks")) ? 7 : 1);
            daysPerRepeat = Integer.parseInt(repeatValue.getText()) * multiplier;
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        String sql = "INSERT INTO shifts(username,shiftStartTime,shiftEndTime,shiftStartDate,thirtyMinBreaks,tenMinBreaks,repeating,daysPerRepeat) VALUES(?,?,?,?,?,?,?,?)";
        //TODO: add some way of noting what the previous endDate was when making a new shift, as this will ignore what previous enddate was
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, usrname);
            preparedStatement.setString(2, sTime);
            preparedStatement.setString(3, eTime);
            preparedStatement.setString(4, sDate);
            preparedStatement.setInt(5, thirtyMin);
            preparedStatement.setInt(6, tenMin);
            preparedStatement.setInt(7, repeat);
            preparedStatement.setInt(8, daysPerRepeat);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Shift successfully updated");
            parent.updatePage();
            currentShiftCard.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void singleCancel() throws IOException {
        endOriginal();
        if(shift.isRepeating()){
            resumeOnNextCycle();
        }
        JOptionPane.showMessageDialog(null, "Shift successfully cancelled");
        parent.updatePage();
        currentShiftCard.close();
    }

    public void multiCancel() throws IOException {
        endOriginal();
        JOptionPane.showMessageDialog(null, "Shifts successfully cancelled");
        parent.updatePage();
        currentShiftCard.close();
    }

}
