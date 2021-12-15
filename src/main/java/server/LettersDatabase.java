package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * ORM for MailDatabase.sqlite3. 
 * 
 * @author lbrito2
 */
public class LettersDatabase {
	private Connection conn = null;

	/**
	 * Default constructor for letters database
	 * 
	 * @param urlToDB Path to the .sqlite3 database ,
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public LettersDatabase(String urlToDB) throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String username;
		String password;
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
	}

	/**
	 * Perform the provided query on the addresses relation. 
	 * 
	 * @param query the SQL query to perform as a String
	 * @return an ArrayList of Addresses, the query result
	 * @throws SQLException
	 */
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

	/**
	 * Perform query on Address relation for rows with given Address field 
	 * 
	 * @param address String 
	 * @return an ArrayList of Addresses, the query result
	 * @throws SQLException
	 */
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

	/**
	 * Perform query on Address relation for rows with given TrueKey field 
	 * 
	 * @param trueKey String 
	 * @return an ArrayList of Addresses, the query result
	 * @throws SQLException
	 */
	public ArrayList<Address> getAddressByTrueKey(String trueKey) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
				"SELECT Address, TrueKey FROM Addresses WHERE TrueKey=?");
		prep.setString(1, trueKey);
		ResultSet rs = prep.executeQuery();
		ArrayList<Address> resultList = new ArrayList<Address>(); 
		while (rs.next()) {
			Address currentAddress = new Address(rs.getString(1), rs.getInt(2));
			resultList.add(currentAddress);
		}
		return resultList;
	}

	/**
	 * Update the Address row with the given TrueKey to have address field newAddress
	 * @param trueKey
	 * @param newAddress
	 * @throws SQLException
	 */
	public void changeAddress(String trueKey, String newAddress) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"UPDATE Addresses SET Address = ? WHERE TrueKey=?");
		prep.setInt(2, Integer.parseInt(trueKey));
		prep.setString(1, newAddress);
		prep.executeUpdate();
	}

	/**
	 * Perform query on Parcel relation for rows with given Id
	 * 
	 * @param id String
	 * @return an ArrayList of Parcels, the query result
	 * @throws SQLException
	 */
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
	
	/**
	 * Perform query on Parcel relation for rows with given Recipient
	 * 
	 * @param recipient String
	 * @return an ArrayList of Parcels, the query result
	 * @throws SQLException
	 */
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

	/**
	 * Perform query on Parcel relation for rows with given Sender
	 * 
	 * @param sender String
	 * @return an ArrayList of Parcels, the query result
	 * @throws SQLException
	 */
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

	/**
	 * Perform query on Parcel relation for rows with Downloaded field true
	 * 
	 * @return ArrayList of Parcels, the query result
	 * @throws SQLException
	 */
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

	/**
	 * Perform query on Parcel relation for rows with Downloaded field false 
	 * 
	 * @return ArrayList of Parcels, the query result
	 * @throws SQLException
	 */
	public ArrayList<Parcel> getUndownloadedParcels() throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT ID, Recipient, Sender, Downloaded, Parcel FROM Parcels WHERE Downloaded=false");
		ResultSet rs = prep.executeQuery();
		ArrayList<Parcel> resultList = new ArrayList<Parcel>(); 
		while (rs.next()) {
			Parcel currentParcel = new Parcel(rs.getInt(1), rs.getInt(2),
					rs.getInt(3), rs.getString(4), rs.getString(5));
			resultList.add(currentParcel);
		}
		return resultList;
	}

	public ArrayList<Parcel> getUndownloadedParcelsByRecipient(int recipient) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"SELECT ID, Recipient, Sender, Downloaded, Parcel FROM Parcels WHERE Downloaded=false AND recipient=?");
		prep.setInt(1, recipient);
		ResultSet rs = prep.executeQuery();
		ArrayList<Parcel> resultList = new ArrayList<Parcel>(); 
		while (rs.next()) {
			Parcel currentParcel = new Parcel(rs.getInt(1), rs.getInt(2),
					rs.getInt(3), rs.getString(4), rs.getString(5));
			resultList.add(currentParcel);
		}
		return resultList;
	}

	/**
	 * Update the Parcel relation row with the given id to have the given 
	 * downloaded value. 
	 * 
	 * @param id Int
	 * @param downloaded String, one of `true` or `false`
	 * @throws SQLException
	 */
	public void setParcelDownloaded(int id, String downloaded) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"UPDATE Parcels SET Downloaded = ? WHERE Id=?");
		prep.setInt(2, id);
		prep.setString(1, downloaded);
		prep.executeUpdate();
	}

	/**
	 * Insert a new Parcel row into the Parcel relation 
	 * 
	 * @param parcel Parcel object to insert
	 * @throws SQLException
	 */
	public void insertParcel(String recipient, String sender, String parcelString)
		 throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"INSERT INTO Parcels (Recipient, Sender, Downloaded, Parcel)"
			+ "VALUES (?, ?, ?, ?)"
		);
		prep.setString(2, recipient);
		prep.setString(3, sender);
		// New parcel has default downloaded value "false"; assume new parcel 
		// has not been downloaded before endpoint send is done handling
		// request. 
		prep.setString(4, "false");
		prep.setString(5, parcelString);

		prep.executeUpdate();
	}

	/**
	 * Delete the Parcel relation row with the given Id. 
	 * 
	 * @param id integer, id of parcel to delete 
	 * @throws SQLException
	 */
	public void deleteParcel(int id) throws SQLException {
		
		PreparedStatement prep = conn.prepareStatement(
			"DELETE FROM Parcels WHERE Id=?"
		);
		prep.setInt(1, id);

		prep.executeUpdate(); 
	}

	/**
	 * Inserts an address into the Addresses table. The TrueKey is set by the table.
	 * @param address - The new address.
	 * @throws SQLException
	 */
	public void insertAddress(String address) throws SQLException {
		PreparedStatement prep = conn.prepareStatement(
			"INSERT INTO Addresses(Address)"
			+ "VALUES (?)"
		);
		prep.setString(1, address);

		prep.executeUpdate();
	}
}
