package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Shift {
	private int shiftID;
	private String username;
    private LocalTime shiftStartTime;
	private LocalTime shiftEndTime;
	private LocalDate shiftStartDate;
	private LocalDate shiftEndDate;
	private int thirtyMinBreaks;
	private int tenMinBreaks;
	private boolean repeating;
	private int daysPerRepeat;

	public Shift(ResultSet resultSet) {
		try {
			this.shiftID = resultSet.getInt("shift_id");
			this.username = resultSet.getString("username");
			this.shiftStartTime = LocalTime.parse(resultSet.getString("shiftStartTime"));
			this.shiftEndTime = LocalTime.parse(resultSet.getString("shiftEndTime"));
			this.shiftStartDate = LocalDate.parse(resultSet.getString("shiftStartDate"));
			if(resultSet.getString("shiftEndDate") != null)
				this.shiftEndDate = LocalDate.parse(resultSet.getString("shiftEndDate"));
			this.thirtyMinBreaks = resultSet.getInt("thirtyMinBreaks");
			this.tenMinBreaks = resultSet.getInt("tenMinBreaks");
			this.repeating = resultSet.getInt("repeating") == 1;
			this.daysPerRepeat = resultSet.getInt("daysPerRepeat");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getShiftID() {
		return shiftID;
	}

	public void setShiftID(int shiftID) {
		this.shiftID = shiftID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalTime getShiftStartTime() {
		return shiftStartTime;
	}

	public void setShiftStartTime(LocalTime shiftStartTime) {
		this.shiftStartTime = shiftStartTime;
	}

	public LocalTime getShiftEndTime() {
		return shiftEndTime;
	}

	public void setShiftEndTime(LocalTime shiftEndTime) {
		this.shiftEndTime = shiftEndTime;
	}

	public LocalDate getShiftStartDate() {
		return shiftStartDate;
	}

	public void setShiftStartDate(LocalDate shiftStartDate) {
		this.shiftStartDate = shiftStartDate;
	}

	public LocalDate getShiftEndDate() {
		return shiftEndDate;
	}

	public void setShiftEndDate(LocalDate shiftEndDate) {
		this.shiftEndDate = shiftEndDate;
	}

	public int getThirtyMinBreaks() {
		return thirtyMinBreaks;
	}

	public void setThirtyMinBreaks(int thirtyMinBreaks) {
		this.thirtyMinBreaks = thirtyMinBreaks;
	}

	public int getTenMinBreaks() {
		return tenMinBreaks;
	}

	public void setTenMinBreaks(int tenMinBreaks) {
		this.tenMinBreaks = tenMinBreaks;
	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}

	public int getDaysPerRepeat() {
		return daysPerRepeat;
	}

	public void setDaysPerRepeat(int daysPerRepeat) {
		this.daysPerRepeat = daysPerRepeat;
	}
}
