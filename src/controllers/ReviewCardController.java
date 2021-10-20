package controllers;

import java.sql.Connection;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Review;

public class ReviewCardController extends Controller {

	private Main main;
	private Connection con;
	private Review review;
	@FXML private Label reviewUser, reviewRating, reviewText;
	
	@Override
	public void setMain(Main main) {
		this.main = main;	
	}

	@Override
	public void setConnection(Connection c) {
		this.con = c;
		
	}

	@Override
	public void fill() {
		
		
	}
	
	public void setReview(Review r) {
		this.review = r;
		reviewUser.setText(review.getUserID());
		reviewRating.setText(String.valueOf(review.getRating()));
		reviewText.setText(review.getReview());
	}

}
