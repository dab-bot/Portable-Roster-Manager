package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Review {
	private int reviewID;
	private int showID;
	private String userID;
	private int rating;
	private String review;
    private String date;


	public Review(ResultSet resultSet) {
		try {
			this.reviewID = resultSet.getInt("reviewId");
			this.showID = resultSet.getInt("show_id");
			this.userID = resultSet.getString("user_id");
			this.rating = resultSet.getInt("rating");
			this.review = resultSet.getString("review");
			this.date = resultSet.getString("date");
			

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public int getReviewID() {
		return reviewID;
	}


	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}


	public int getShowID() {
		return showID;
	}


	public void setShowID(int showID) {
		this.showID = showID;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public int getRating() {
		return rating;
	}


	public void setRating(int rating) {
		this.rating = rating;
	}


	public String getReview() {
		return review;
	}


	public void setReview(String review) {
		this.review = review;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}
	
}
