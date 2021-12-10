package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// TODO documentation 

/**
 * ORM for MailDatabase.sqlite3. 
 * 
 * @author lbrito2
 */
public class LettersDatabase {

	private Connection conn = null;

	public LettersDatabase(String urlToDB) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		// String urlToDB = "" + pathToFile;
		String username = "";
		String password = "";
		if (urlToDB.equals("jdbc:postgresql://localhost:5432/addresses")) {
			username = "postgres";
			password = "password";
		} else {
			username = System.getenv("JDBC_DATABASE_USERNAME");
			password = System.getenv("JDBC_DATABASE_PASSWORD");
		}
		try {
			conn = DriverManager.getConnection(urlToDB, username, password);
		} catch (Exception e) {
			System.out.println("ERROR: Couldn't connect" + e.getMessage());
		}
		//Statement stat = conn.createStatement();
		//stat.executeUpdate("PRAGMA foreign_keys=ON;");
	}

	public ArrayList<Address> rawAddressQuery(String query) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(query); 
		ResultSet rs = prep.executeQuery(); 
		ArrayList<Address> resultList = new ArrayList<Address>(); 
		while (rs.next()) {
			Address currentAddress = new Address(rs.getString(1), rs.getInt(2));
			resultList.add(currentAddress);
		}
		return resultList;
	}

	public ArrayList<Address> getAddressByAddress(String address) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT Address, TrueKey FROM Addresses WHERE Address=?");
		prep.setString(1, address);
		ResultSet rs = prep.executeQuery();
		ArrayList<Address> resultList = new ArrayList<Address>();
		while (rs.next()) {
			Address currentAddress = new Address(rs.getString(1), rs.getInt(2));
			resultList.add(currentAddress);
		}
		return resultList;
	}

	public ArrayList<Address> getAddressByTrueKey(String address) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
				"SELECT Address, TrueKey FROM Addresses WHERE TrueKey=?");
		prep.setString(1, address);
		ResultSet rs = prep.executeQuery();
		ArrayList<Address> resultList = new ArrayList<Address>(); 
		while (rs.next()) {
			Address currentAddress = new Address(rs.getString(1), rs.getInt(2));
			resultList.add(currentAddress);
		}
		return resultList;
	}
	// - Change address, active, downloaded
	
	public ArrayList<Parcel> getParcelById(int id) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT ID, Recipient, Sender, Downloaded, Parcel FROM Parcels WHERE ID=?");
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		ArrayList<Parcel> resultList = new ArrayList<Parcel>(); 
		while (rs.next()) {

			Parcel currentParcel = new Parcel(rs.getInt(1), rs.getInt(2),
					rs.getInt(3), rs.getString(4), rs.getString(5));
			resultList.add(currentParcel);
		}
		return resultList;
	}
	
	public ArrayList<Parcel> getParcelByRecipient(String recipient) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT ID, Recipient, Sender, Downloaded, Parcel FROM Parcels WHERE Recipient=?");
		prep.setString(1, recipient);
		ResultSet rs = prep.executeQuery();
		ArrayList<Parcel> resultList = new ArrayList<Parcel>(); 
		while (rs.next()) {
			Parcel currentParcel = new Parcel(rs.getInt(1), rs.getInt(2),
					rs.getInt(3), rs.getString(4), rs.getString(5));
			resultList.add(currentParcel);
		}
		return resultList;
	}

	public ArrayList<Parcel> getParcelBySender(String sender) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT ID, Recipient, Sender, Downloaded, Parcel FROM Parcels WHERE Sender=?");
		prep.setString(1, sender);
		ResultSet rs = prep.executeQuery();
		ArrayList<Parcel> resultList = new ArrayList<Parcel>(); 
		while (rs.next()) {
			Parcel currentParcel = new Parcel(rs.getInt(1), rs.getInt(2),
					rs.getInt(3), rs.getString(4), rs.getString(5));
			resultList.add(currentParcel);
		}
		return resultList;
	}

	public ArrayList<Parcel> getDownloadedParcels() throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT ID, Recipient, Sender, Downloaded, Parcel FROM Parcels WHERE Downloaded=true");
		ResultSet rs = prep.executeQuery();
		ArrayList<Parcel> resultList = new ArrayList<Parcel>(); 
		while (rs.next()) {
			Parcel currentParcel = new Parcel(rs.getInt(1), rs.getInt(2),
					rs.getInt(3), rs.getString(4), rs.getString(5));
			resultList.add(currentParcel);
		}
		return resultList;
	}

	public void setParcelDownloaded(int id, String downloaded) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"UPDATE Parcels SET Downloaded = ? WHERE Id=?");
		prep.setInt(2, id);
		prep.setString(1, downloaded);
		prep.executeUpdate();
	}

	// Active attribute no longer needed
	/*
	public void setAddressActive(String trueKey, boolean active) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"UPDATE Addresses"
			+ "SET Active = ?"
			+ "WHERE TrueKey=?"
		);
		prep.setBoolean(1, active);
		prep.setString(2, trueKey);
		prep.executeUpdate();
	}
	 */

	public void insertParcel(Parcel parcel) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"INSERT INTO Parcels (ID, Recipient, Sender, Downloaded, Parcel)"
			+ "VALUES (?, ?, ?, ?, ?)"
		);
		prep.setInt(1, parcel.getId());
		prep.setInt(2, parcel.getRecipient());
		prep.setInt(3, parcel.getSender());
		prep.setString(4, String.valueOf(parcel.getDownloaded()));
		prep.setString(5, parcel.getParcel());

		prep.executeUpdate();
	}

	public void insertAddress(Address address) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"INSERT INTO Addresses (Address, TrueKey)"
			+ "VALUES (?, ?)"
		);
		prep.setString(1, address.getAddress());
		prep.setInt(2, address.getTrueKey());

		prep.executeUpdate();
	}
}
