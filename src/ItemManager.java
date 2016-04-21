import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ItemManager 
{
	private static final String USERNAME = "netflix";
	private static final String PASSWORD = "netflix";
	private static final String CONN_STRING = "jdbc:mysql://localhost/netflix";
	
	private Object[][] colums = {{"id", "name", "type", "year", "minutes", "grade", "original" },
			{"id", "firstName", "lastName", "sex", "year"}, {"id", "firstName", "lastName", "year"}, 
			{"id", "firstName", "lastName", "year"}, {"id", "name"}, {"id", "titleID", "genreID" },
			{"id", "name", "titleID", "actorID"}, {"id", "titleID", "directorID"}, {"id", "titleID", "writerID"}};
	private String[] itemType = {"titles", "actors", "directors", "writers", "genre", 
			"genreconnections", "actorroles", "directorroles", "writerroles", "search"};

	private ItemComponent everyTable = new ItemGroup("Every Table", 0);
	private ItemComponent titles = new ItemGroup("titles", 1);
	private ItemComponent actors = new ItemGroup("actors", 2);
	private ItemComponent directors = new ItemGroup("directors", 3);
	private ItemComponent writers = new ItemGroup("writers", 4);
	private ItemComponent genre = new ItemGroup("genre", 5);
	private ItemComponent genreConnections = new ItemGroup("genreconnections", 6);
	private ItemComponent actorRole = new ItemGroup("actorroles", 7);
	private ItemComponent directorRole = new ItemGroup("directorroles", 8);
	private ItemComponent writerRole = new ItemGroup("writerroles", 9);
	private ItemComponent searchTable = new ItemGroup("search", 0);
	
	public ItemManager()
	{
		everyTable.add(titles);
		everyTable.add(actors);
		everyTable.add(directors);
		everyTable.add(writers);
		everyTable.add(genre);
		everyTable.add(genreConnections);
		everyTable.add(actorRole);
		everyTable.add(directorRole);
		everyTable.add(writerRole);
		everyTable.add(searchTable);
		
		updateTable("titles");
		updateTable("actors");
		updateTable("directors");
		updateTable("writers");
		updateTable("genre");
		updateTable("genreconnections");
		updateTable("actorroles");
		updateTable("directorroles");
		updateTable("writerroles");
	}
	
	/**
	 * Get table
	 * @param table
	 * @return
	 */
	public Object[][] getTable(String table)
	{
		for(int i = 0; i < itemType.length; i++)
		{
			if(everyTable.getComponent(i).getGroupName() == table)
				return everyTable.getComponent(i).getTable();
		}
		return null;
	}
	
	/**
	 * Get Item from Table
	 * @param id
	 * @param tableIndex
	 * @return
	 */
	public ItemComponent getItemComponent(int id, int tableIndex)
	{
		switch (tableIndex)
		{
			case 0:
				return titles.getByIdComponent(id);
			case 1:
				return genre.getByIdComponent(id);
			case 2:
				return actors.getByIdComponent(id);
			case 3:
				return writers.getByIdComponent(id);
			case 4:
				return directors.getByIdComponent(id);
			default:
				return null;
		}
	}
	
	/**
	 * Get table from SQL
	 * @param table
	 */
	public void updateTable(String table)
	{
		// Change SQL Query depending on Table
		String sql = "SELECT * FROM " + table;
		
		if(table == itemType[6])
		{
			sql = "SELECT a.firstName, a.lastName, r.name, t.name AS title " +
					"FROM actorRoles r JOIN actors a ON r.actorID = a.id " +
					"JOIN titles t ON r.titleID = t.id";
		}
		else if(table == itemType[7])
		{
			sql = "SELECT d.firstName, d.lastName, t.name AS title " +
					"FROM directorRoles r JOIN directors d ON r.directorID = d.id " +
					"JOIN titles t ON r.titleID = t.id";
		}
		else if(table == itemType[8])
		{
			sql = "SELECT w.firstName, w.lastName, t.name AS title FROM writerRoles r " +
					"JOIN writers w ON r.writerID = w.id JOIN titles t ON r.titleID = t.id";
		}
		else if(table == itemType[5])
		{
			sql = "SELECT t.name AS title, g.name FROM genreconnections c " +
					"JOIN genre g ON c.genreID = g.id JOIN titles t ON c.titleID = t.id";
		}
		
		// Clear table and try SQL query
		clearTable(table);
		try (
				Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(sql);
			) 
		{	
			// Loop Through SQL items and add components depending on table
			while(rs.next())
			{
				if(titles.getGroupName() == table)
				{
					titles.add(new Item(1, rs.getInt("id"), rs.getString("name"), rs.getString("type"),
							rs.getInt("year"), rs.getInt("minutes"), rs.getFloat("grade"), rs.getInt("original")));
				}
				if(actors.getGroupName() == table)
				{ 
					actors.add(new Item(2, rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getString("sex"), rs.getInt("year")));
				}
				if(directors.getGroupName() == table)
				{
					directors.add(new Item(3, rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getInt("year")));
				}
				if(writers.getGroupName() == table)
				{
					writers.add(new Item(4, rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
							rs.getInt("year")));
				}
				if(genre.getGroupName() == table)
				{
					genre.add(new Item(5, rs.getInt("id"), rs.getString("name")));
				}
				if(genreConnections.getGroupName() == table)
				{
					genreConnections.add(new Item(6, rs.getString("title"), rs.getString("name")));
				}
				if(actorRole.getGroupName() == table)
				{
					actorRole.add(new Item(7, rs.getString("firstName"), rs.getString("lastName"),
							rs.getString("name"), rs.getString("title")));
				}
				if(directorRole.getGroupName() == table)
				{
					directorRole.add(new Item(8, rs.getString("firstName"), rs.getString("lastName"),
							rs.getString("title")));
				}
				if(writerRole.getGroupName() == table)
				{
					writerRole.add(new Item(9, rs.getString("firstName"), rs.getString("lastName"),
							rs.getString("title")));
				}
			}
		}
		catch (SQLException e) 
		{
			System.err.println("Error message: " + e.getMessage());
			System.err.println("Error code: " + e.getErrorCode());
			System.err.println("SQL state: " + e.getSQLState());
		}
	}
	
	/**
	 * Get Table and clear it.
	 * @param table
	 */
	public void clearTable(String table)
	{
		for(int i = 0; i < itemType.length; i++)
		{
			if(everyTable.getComponent(i).getGroupName() == table)
				everyTable.getComponent(i).clear();
		}
	}
	
	/**
	 * Insert Item into SQL.
	 * @param item
	 * @return boolean
	 * @throws Exception
	 */
	public boolean insertItem(ItemComponent item) throws Exception
	{
		// Create SQL Query depending on Table
		String colum = "";
		String values = "NULL";
		for(int i = 0; i < colums[item.getItemType() - 1].length; i++)
		{
			if(i != 0)
				values += "?";
				
			colum += colums[item.getItemType() - 1][i];
			
			if(i != colums[item.getItemType() - 1].length - 1)
			{
				colum += ", ";
				values += ", ";
			}
		}
		String sql = "INSERT INTO " + itemType[item.getItemType() - 1] + "(" + colum  + ") " +
				"VALUES (" + values + ")";
		
				ResultSet keys = null;
		try (
				Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			) 
		{	
			// Insert info depending on Table
			for(int i = 1; i < item.getItem().length; i++)
			{
				if(item.getItem()[i].getClass().equals(Integer.class))
				{
					stmt.setInt(i, (int) item.getItem()[i]);
				}
				else if(item.getItem()[i].getClass().equals(String.class))
				{
					stmt.setString(i, (String) item.getItem()[i]);
				}
				else if(item.getItem()[i].getClass().equals(Float.class))
				{
					stmt.setFloat(i, (Float) item.getItem()[i]);
				}
				else if(item.getItem()[i].getClass().equals(Boolean.class))
				{
					stmt.setBoolean(i, (Boolean) item.getItem()[i]);
				}
			}
			
			int affected = stmt.executeUpdate();
			
			// Update Table when done
			if (affected == 1){
				updateTable(itemType[item.getItemType() - 1]);
			} else {
				return false;
			}
		}
		catch (SQLException e) 
		{
			System.err.println(e);
			return false;
		} finally
		{
			if (keys != null) keys.close();
		}
		
		return true;
	}
	
	/**
	 * Update SQL row
	 * @param item
	 * @return boolean
	 * @throws Exception
	 */
	public boolean updateItem(ItemComponent item) throws Exception 
	{
		// Create SQL Query depending on Table
		String colum = "";
		for(int i = 1; i < colums[item.getItemType() - 1].length; i++)
		{
			colum += colums[item.getItemType() - 1][i];
			
			if(i != colums[item.getItemType() - 1].length - 1)
			{
				colum += " = ?, ";
			}
		}
		colum += " = ? WHERE id = ?";
		
		String sql = "UPDATE " + itemType[item.getItemType() - 1] + " SET " + colum;
		
		try (
				Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
			)
		{
			// Update info depending on Table
			for(int i = 1; i < item.getItem().length; i++)
			{
				if(item.getItem()[i].getClass().equals(Integer.class))
				{
					stmt.setInt(i, (int) item.getItem()[i]);
				}
				else if(item.getItem()[i].getClass().equals(String.class))
				{
					stmt.setString(i, (String) item.getItem()[i]);
				}
				else if(item.getItem()[i].getClass().equals(Float.class))
				{
					stmt.setFloat(i, (Float) item.getItem()[i]);
				}
				else if(item.getItem()[i].getClass().equals(Boolean.class))
				{
					stmt.setBoolean(i, (Boolean) item.getItem()[i]);
				}
			}
			stmt.setInt(item.getItem().length, item.getID());
			
			int affected = stmt.executeUpdate();
			
			// Update Table when done
			if (affected == 1) {
				updateTable(itemType[item.getItemType() - 1]);
				return true;
			} else {
				return false;
			}
			
		}
		catch(SQLException e) {
			System.err.println(e);
			return false;
		}

	}
	
	/**
	 * Remove SQL row
	 * @param item
	 * @return boolean
	 * @throws Exception
	 */
	public boolean removeItem(ItemComponent item) throws Exception {
		
		// Create SQL query depending on Table
		String sql = "DELETE FROM " + itemType[item.getItemType() - 1] + " WHERE " +
						itemType[item.getItemType() - 1] + ".id = ?";
		
		try (
				Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(sql);
			)
		{
			// Get row from ID and delete
			stmt.setInt(1, item.getID());
			
			int affected = stmt.executeUpdate();
			
			// Update Table when done
			if (affected == 1) {
				updateTable(itemType[item.getItemType() - 1]);
				return true;
			} else {
				return false;
			}
			
		}
		catch(SQLException e) {
			System.err.println(e);
			return false;
		}

	}
	
	/**
	 * Get search word and search SQL
	 * @param sWord
	 */
	public void searchTables(String sWord)
	{
		// SQL Query Strings
		String sql[] = {"SELECT titles.name, titles.type, titles.year FROM titles WHERE titles.name  LIKE '%"
					+ sWord + "%' OR titles.type LIKE '%" + sWord + "%' OR titles.year LIKE '%" + sWord + "%'",
					
					"SELECT actors.firstName, actors.lastName FROM actors WHERE actors.firstName" +
					" LIKE '%" + sWord + "%' OR actors.lastName LIKE '%" + sWord + "%'",
					
					"SELECT d.firstName, d.lastName FROM directorroles r JOIN directors d ON r.directorID = d.id" +
					" JOIN titles ON r.titleID = titles.id WHERE d.firstname LIKE '%" + sWord + "%' OR titles.name "
							+ "LIKE '%" + sWord + "%' OR d.lastname LIKE '%" + sWord + "%'",
							
					"SELECT w.firstName, w.lastName FROM writerroles r JOIN writers w ON r.writerID = w.id" +
					" JOIN titles ON r.titleID = titles.id WHERE w.firstname LIKE '%" + sWord + "%' OR w.lastname "
							+ "LIKE '%" + sWord + "%' OR titles.name LIKE '%" + sWord + "%'",
					
					"SELECT genre.name, titles.name AS tname, titles.type FROM genreConnections g JOIN genre ON g.genreID" 
					+ " = genre.id JOIN titles ON g.titleID = titles.id WHERE genre.name LIKE '%" + sWord + "%'",
					
					"SELECT a.name, titles.name AS tname, actors.id FROM actorroles a JOIN actors ON a.actorID = actors.id" +
					" JOIN titles ON a.titleID = titles.id WHERE a.name LIKE '%" + sWord + "%' OR titles.name LIKE '%" + sWord + "%'"
					};
		
		// Clear Old Table
		clearTable("search");
		// VARIBLES
		String thirdString = "";
		
		// Loop trough Searches
		for(int i = 0; i < sql.length; i++)
		{
			try (
					Connection conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
					Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					ResultSet rs = stmt.executeQuery(sql[i]);
				) 
			{	
				// Get and save rows from search
				while(rs.next())
				{
					thirdString = itemType[i].substring(0, 1).toUpperCase() + itemType[i].substring(1, itemType[i].length() - 1);;
					
					if(i == 0 || i == 4)
						thirdString = rs.getObject(3).toString();
					else if(i == 5)
						thirdString = actors.getByIdComponent(rs.getInt("id")).getFirstName() + " " +
								actors.getByIdComponent(rs.getInt("id")).getLastName();
					
					searchTable.add(new Item(0, rs.getObject(1).toString(), rs.getObject(2).toString(), 
							thirdString, i));
				}
			}
			catch (SQLException e) 
			{
				System.err.println("Error message: " + e.getMessage());
				System.err.println("Error code: " + e.getErrorCode());
				System.err.println("SQL state: " + e.getSQLState());
			}
		}
		if(searchTable.getTable() == null)
		{
			searchTable.add(new Item(0, "No Result Found", "", 
					"", 0));
		}
	}
}
