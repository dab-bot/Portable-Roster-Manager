package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Show {

	private int showID;
	
	private String showTitle;

    private String genre;

    private float length;
    
    private int movie;

    private int series;

    private int procoID;
    
    private int year;
    
    private String status;

	public Show(ResultSet resultSet) {
		try {
			this.showID = resultSet.getInt("showid");
			this.showTitle = resultSet.getString("show_title");
			this.genre = resultSet.getString("genre");
			this.length = resultSet.getFloat("length");
			this.movie = resultSet.getInt("movie");
			this.series = resultSet.getInt("series");
			this.procoID = resultSet.getInt("proco_id");
			this.year = resultSet.getInt("year");
			this.status = resultSet.getString("status");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getShowID() {
		return showID;
	}

	public void setShowID(int showID) {
		this.showID = showID;
	}

	public String getShowTitle() {
		return showTitle;
	}

	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public int getMovie() {
		return movie;
	}

	public void setMovie(int movie) {
		this.movie = movie;
	}

	public int getSeries() {
		return series;
	}

	public void setSeries(int series) {
		this.series = series;
	}

	public int getProcoID() {
		return procoID;
	}

	public void setProcoID(int procoID) {
		this.procoID = procoID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
