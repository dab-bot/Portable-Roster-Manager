package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRequest {

	private int reqID;
	
	private String username;

    private String currentRole;
    
    private String requestedRole;

    private String orgName;
    
    private String orgContact;
    
    private String reqStatus;

	public RoleRequest(ResultSet resultSet) {
		try {
			this.reqID = resultSet.getInt("reqID");
			if(resultSet.getString("username")!=null)
				this.username = resultSet.getString("username");
			if(resultSet.getString("currentRole")!=null)
				this.currentRole = resultSet.getString("currentRole");
			if(resultSet.getString("requestedRole")!=null)
				this.requestedRole = resultSet.getString("requestedRole");
			if(resultSet.getString("orgName")!=null)
				this.orgName = resultSet.getString("orgName");
			if(resultSet.getString("orgContact")!=null)
				this.orgContact = resultSet.getString("orgContact");
			if(resultSet.getString("status")!=null)
				this.reqStatus = resultSet.getString("status");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getReqID() {
		return reqID;
	}

	public void setReqID(int reqID) {
		this.reqID = reqID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}

	public String getRequestedRole() {
		return requestedRole;
	}

	public void setRequestedRole(String requestedRole) {
		this.requestedRole = requestedRole;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgContact() {
		return orgContact;
	}

	public void setOrgContact(String orgContact) {
		this.orgContact = orgContact;
	}

	public String getReqStatus() {
		return reqStatus;
	}

	public void setReqStatus(String status) {
		this.reqStatus = status;
	}
}
