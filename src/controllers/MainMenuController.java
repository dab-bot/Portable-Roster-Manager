package controllers;


import application.Main;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Shift;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;


public class MainMenuController extends Controller {

    @FXML
    private Label welcomeLabel, userLabel;
    @FXML
    private JFXDatePicker datePkr;
    @FXML
    private VBox monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox;
    @FXML
    private HBox weekdayBox;

    private Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    public void setConnection(Connection c) {
        this.con = c;
    }

    public void fill() {
        welcomeLabel.setText("Welcome back, " + main.currentUser.getFirst_name() + "!");
        userLabel.setText(String.valueOf(main.currentUser.getFirst_name().charAt(0)));
        userLabel.setStyle("-fx-background-color: " + main.currentUser.getBgColour() + ";");
        userLabel.setTextFill(Paint.valueOf(main.currentUser.getTextColour()));
        datePkr.setValue(LocalDate.now());
        updatePage();
    }

    public void updateDay(LocalDate date, VBox shiftContainer, int dayOfWeek, ArrayList<Shift> allShifts) {
        shiftContainer.getChildren().removeAll(shiftContainer.getChildren());
        long weekDay = date.getDayOfWeek().getValue();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RosterDayCard.fxml"));
        VBox rosterDayCard = null;
        try {
            rosterDayCard = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RosterDayCardController rdc = loader.getController();
        rdc.setMain(main);
        rdc.setConnection(con);
        rdc.setDate(date.minusDays(weekDay - dayOfWeek));
        rdc.setParent(this);
        rdc.fill();
        if (rdc.getDate() == date) {
            rdc.select();
        }
        HBox.setHgrow(rosterDayCard, Priority.ALWAYS);
        weekdayBox.getChildren().add(rosterDayCard);


        for (Shift s : allShifts) {
            boolean repeatShiftDay = (s.isRepeating() && DAYS.between(s.getShiftStartDate(), date.minusDays(weekDay - dayOfWeek)) % s.getDaysPerRepeat() == 0 && DAYS.between(s.getShiftStartDate(), date.minusDays(weekDay - dayOfWeek)) >= 0);
            boolean equalDay = s.getShiftStartDate().equals(date.minusDays(weekDay - dayOfWeek));
            boolean pastEnd = s.getShiftEndDate() != null && s.getShiftEndDate().isBefore(date.minusDays(weekDay - dayOfWeek));
            if ((equalDay || repeatShiftDay) && !pastEnd) {
                loader = new FXMLLoader(getClass().getResource("/views/ShiftCard.fxml"));
                AnchorPane shiftCard = null;
                try {
                    shiftCard = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ShiftCardController sc = loader.getController();
                sc.setMain(main);
                sc.setConnection(con);
                sc.setShift(s);
                sc.setDate(date.minusDays(weekDay - dayOfWeek));
                sc.setParent(this);
                sc.fill();
                shiftContainer.getChildren().add(shiftCard);
            }
        }
    }

    public void updatePage() {
        ArrayList<Shift> allShifts = new ArrayList<>();
        String sql = "SELECT * FROM shifts JOIN accounts a on a.username = shifts.username ORDER BY shiftStartTime, a.first_name";
        try {
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allShifts.add(new Shift(resultSet));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        if (datePkr.getValue() == null)
            datePkr.setValue(LocalDate.now());

        weekdayBox.getChildren().removeAll(weekdayBox.getChildren());
        updateDay(datePkr.getValue(), monBox, 1, allShifts);
        updateDay(datePkr.getValue(), tueBox, 2, allShifts);
        updateDay(datePkr.getValue(), wedBox, 3, allShifts);
        updateDay(datePkr.getValue(), thuBox, 4, allShifts);
        updateDay(datePkr.getValue(), friBox, 5, allShifts);
        updateDay(datePkr.getValue(), satBox, 6, allShifts);
        updateDay(datePkr.getValue(), sunBox, 7, allShifts);
    }

    // On main page, go back to login page when user logout
    public void userLogout() throws IOException {
        main.changeScene("/views/LogIn.fxml");
    }

    public void addNewShift() throws IOException {
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
        c.newShiftFormat(true);
        c.setParent(this);
        shiftEditStage.setTitle("Add a new Shift");
        shiftEditStage.setScene(new Scene(root));
        shiftEditStage.showAndWait();
    }

    public void addNewAccount() throws IOException {
        Stage shiftEditStage = new Stage();
        EditAccountController c;
        shiftEditStage.setResizable(false);
        shiftEditStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AccountEdit.fxml"));
        Parent root = loader.load();
        c = loader.getController();
        c.setMain(main);
        c.setConnection(con);
        c.fill();
        shiftEditStage.setTitle("Add a new Account");
        shiftEditStage.setScene(new Scene(root));
        shiftEditStage.showAndWait();
    }

    public void weekForward() {
        setDatePkr(datePkr.getValue().plusWeeks(1));
    }

    public void weekBackward() {
        setDatePkr(datePkr.getValue().minusWeeks(1));
    }

    public void setDatePkr(LocalDate date) {
        datePkr.setValue(date);
        updatePage();
    }


}
