import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class View  extends JFrame{

	//	DIMENSION CONTSTANTS
	private final int BIG_FONT_SIZE = 18;
	private final int TITLE_FONT_SIZE = 45;
	
	private final int WINDOW_HEIGHT = 576;
	private final int TOP_PANEL_HEIGHT = 90;
	private final int BOTTOM_PANEL_HEIGHT = 70;
	private final int LIST_PANEL_HEIGHT = WINDOW_HEIGHT - TOP_PANEL_HEIGHT - BOTTOM_PANEL_HEIGHT;
	
	private final int WINDOW_WIDTH = 1024;
	private final int LEFT_PANEL_WIDTH = 160;
	private final int RIGHT_PANEL_WIDTH = 180;
	private final int MID_PANEL_WIDTH = WINDOW_WIDTH - LEFT_PANEL_WIDTH - RIGHT_PANEL_WIDTH;
	
	private final int LABEL_WIDTH = 140;
	private final int LABEL_HEIGHT = 35;
	private final Dimension LABEL_DIMENSION = new Dimension(LABEL_WIDTH, LABEL_HEIGHT);
	
	//	VARIABLE FIELDS
	int activeTab;
	
	//	OBJECT FIELDS
	private Font standardFont;
	private Font titleFont;
	private ItemManager itemManager;
	
	private JLabel insertLabel;
	private JLabel topLabel;
	private JLabel tabLabel;
	
	private JPanel mainPanel;
	private JPanel leftPanel;
	private JPanel midPanel;
	private JPanel rightPanel;
	private JPanel topPanel;
	private JPanel listPanel;
	private JPanel bottomPanel;
	
	private JScrollPane searchPane;
	private JTable currentTable;
	private JTextField searchField;
	private JTextField idField;
	
	//	VECTOR FIELDS
	
	private Vector<JButton> tabButtons;
	private Vector<JButton> insertButtons;
	private Vector<JButton> bottomButtons;
	private Vector<JButton> getDataButtons;
	private Vector<JButton> updateButtons;
	
	private Vector<JPanel> insertPanels;
	
	private Vector<JLabel> titleLabels;
	private Vector<JLabel> actorLabels;
	private Vector<JLabel> writerLabels;
	private Vector<JLabel> directorLabels;
	
	private Vector<JScrollPane> scrollPanes;
	private Vector<JTextField> titleFields;
	private Vector<JTextField> actorFields;
	private Vector<JTextField> writerFields;
	private Vector<JTextField> directorFields;
	private Vector<JTextField> getIDFields;
	private Vector<String> tabStrings;
	
	//	COLOR CONSTANTS
	private final Color BACKGROUND_COLOR = new Color(0.1f,0.1f,0.1f);
	private final Color RED = new Color(0.8f,0.2f,0.2f);
	private final Color WHITE = new Color(0.9f,0.9f,0.9f);
	private final Color GREY = new Color(0.5f, 0.5f, 0.5f);
	
	public static void main(String[] args) {
		new View();
	}
	
	//	CONSTRUCTOR
	public View(){
		initView();
	}
	
	private void initView(){
		
		//	SET MAIN WINDOW
		setTitle("Netflix");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setFocusable(true);
		
		//	CREATE ITEM MANAGER
		itemManager = new ItemManager();
		
		//	CREATE FONTS
		standardFont = new Font(Font.MONOSPACED, Font.BOLD, BIG_FONT_SIZE);
		titleFont = new Font(Font.MONOSPACED, Font.BOLD, TITLE_FONT_SIZE);
		
		//	SET MAIN PANEL
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
		mainPanel.setBackground(BACKGROUND_COLOR);
		add(mainPanel);
		
		//	SET LEFT PANEL
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension(LEFT_PANEL_WIDTH, WINDOW_HEIGHT));
		leftPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.add(leftPanel);
		
		tabLabel = new JLabel("TABLES:");
		tabLabel.setFont(standardFont);
		tabLabel.setForeground(WHITE);
		leftPanel.add(tabLabel);
		leftPanel.add(new JLabel(" "));
		
		tabStrings = new Vector<String>(1,1);
		tabButtons = new Vector<JButton>(1,1);
		
		tabStrings.add("Titles");
		tabStrings.add("Genres");
		tabStrings.add("Actors");
		tabStrings.add("Writers");
		tabStrings.add("Directors");
		tabStrings.add("GenConns");
		tabStrings.add("ActRoles");
		tabStrings.add("WriRoles");
		tabStrings.add("DirRoles");
		
		for (int i = 0; i < tabStrings.size(); i++){
			tabButtons.add(new JButton(tabStrings.get(i)));
			tabButtons.get(i).setFont(standardFont);
			tabButtons.get(i).setBackground(BACKGROUND_COLOR);
			tabButtons.get(i).setForeground(GREY);
			tabButtons.get(i).addActionListener(new TabListener());
			leftPanel.add(tabButtons.get(i));
			leftPanel.add(new JLabel(" "));
		}
		
		//	SET MID PANEL
		midPanel = new JPanel();
		midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
		midPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.add(midPanel);
		
		//	SET TOP PANEL
		topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		topPanel.setPreferredSize(new Dimension(MID_PANEL_WIDTH, TOP_PANEL_HEIGHT));
		topPanel.setBackground(BACKGROUND_COLOR);
		midPanel.add(topPanel);
		
		topLabel = new JLabel("N E T F L I X");
		topLabel.setFont(titleFont);
		topLabel.setForeground(RED);
		topPanel.add(topLabel);
		
		//	SET LIST PANEL
		listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		listPanel.setBackground(BACKGROUND_COLOR);
		listPanel.setPreferredSize(new Dimension(MID_PANEL_WIDTH, LIST_PANEL_HEIGHT));
		midPanel.add(listPanel);
		
		//	SET LIST PANEL CONTENTS
		updateTables();
		
		//	SET BOTTOM PANEL
		bottomPanel = new JPanel();
		bottomPanel.setBackground(BACKGROUND_COLOR);
		
		searchField = new JTextField("search");
		searchField.setFont(standardFont);
		searchField.setPreferredSize(LABEL_DIMENSION);
		searchField.setHorizontalAlignment(SwingConstants.CENTER);
		bottomPanel.add(searchField);
		
		bottomButtons = new Vector<JButton>(1,1);
		bottomButtons.add(new JButton("SEARCH"));
		bottomButtons.add(new JButton("DELETE"));
		
		for (int i = 0; i < bottomButtons.size(); i++){
			bottomButtons.get(i).setFont(standardFont);
			bottomButtons.get(i).setPreferredSize(LABEL_DIMENSION);
			bottomButtons.get(i).setBackground(BACKGROUND_COLOR);
			bottomButtons.get(i).setForeground(RED);
			bottomButtons.get(i).addActionListener(new BottomListener());
		}
		bottomPanel.add(bottomButtons.get(0));
		
		idField = new JTextField("id");
		idField.setFont(standardFont);
		idField.setPreferredSize(LABEL_DIMENSION);
		idField.setHorizontalAlignment(SwingConstants.CENTER);
		
		bottomPanel.add(idField);
		bottomPanel.add(bottomButtons.get(1));

		midPanel.add(bottomPanel);
		
		//	SET RIGHT PANEL
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(RIGHT_PANEL_WIDTH, WINDOW_HEIGHT));
		rightPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.add(rightPanel);
		
		insertLabel = new JLabel("INSERT:");
		insertLabel.setFont(standardFont);
		insertLabel.setForeground(WHITE);
		rightPanel.add(insertLabel);
		rightPanel.add(new JLabel(" "));
		
		//	-> SET INSERT PANELS
		
		insertPanels = new Vector<JPanel>(1,1);
		for (int i = 0; i <tabStrings.size(); i++){
			insertPanels.add(new JPanel());
			insertPanels.get(i).setLayout(new BoxLayout(insertPanels.get(i), BoxLayout.Y_AXIS));
			insertPanels.get(i).setBackground(BACKGROUND_COLOR);
		}
		
		//	-> SET INSERT PANEL CONTENTS
		insertButtons = new Vector<JButton>(1,1);
		getDataButtons = new Vector<JButton>(1,1);
		updateButtons = new Vector<JButton>(1,1);
		getIDFields = new Vector<JTextField>(1,1);
		for (int i = 0; i < insertPanels.size(); i++){
			insertButtons.add(new JButton("INSERT"));
			insertButtons.get(i).setFont(standardFont);
			insertButtons.get(i).setPreferredSize(LABEL_DIMENSION);
			insertButtons.get(i).setBackground(BACKGROUND_COLOR);
			insertButtons.get(i).setForeground(RED);
			insertButtons.get(i).addActionListener(new RightPanelListener());
			
			getDataButtons.add(new JButton("GET DATA"));
			getDataButtons.get(i).setFont(standardFont);
			getDataButtons.get(i).setPreferredSize(LABEL_DIMENSION);
			getDataButtons.get(i).setBackground(BACKGROUND_COLOR);
			getDataButtons.get(i).setForeground(RED);
			getDataButtons.get(i).addActionListener(new RightPanelListener());
			
			updateButtons.add(new JButton("UPDATE"));
			updateButtons.get(i).setFont(standardFont);
			updateButtons.get(i).setPreferredSize(LABEL_DIMENSION);
			updateButtons.get(i).setBackground(BACKGROUND_COLOR);
			updateButtons.get(i).setForeground(RED);
			updateButtons.get(i).addActionListener(new RightPanelListener());
			
			getIDFields.add(new JTextField("id"));
			getIDFields.get(i).setHorizontalAlignment(SwingConstants.CENTER);
			getIDFields.get(i).setFont(standardFont);
		}
		
		//	-> SET INSERT-TITLE PANEL
		activeTab = 0;
		titleLabels = new Vector<JLabel>(1,1);
		titleLabels.add(new JLabel("Title:"));
		titleLabels.add(new JLabel("Type:"));
		titleLabels.add(new JLabel("Year:"));
		titleLabels.add(new JLabel("Minutes:"));
		titleLabels.add(new JLabel("Grade:"));
		titleLabels.add(new JLabel("Original:"));
		
		titleFields = new Vector<JTextField>(1,1);
		for (int i = 0; i < titleLabels.size(); i++){
			titleFields.add(new JTextField(""));
			titleFields.get(i).setFont(standardFont);
			titleLabels.get(i).setFont(standardFont);
			titleLabels.get(i).setForeground(WHITE);
			insertPanels.get(activeTab).add(titleLabels.get(i));
			insertPanels.get(activeTab).add(titleFields.get(i));
		}
		
		//	-> SET INSERT-ACTOR PANEL
		activeTab = 2;
		actorLabels = new Vector<JLabel>(1,1);
		actorLabels.add(new JLabel("First Name:"));
		actorLabels.add(new JLabel("Last Name:"));
		actorLabels.add(new JLabel("Sex:"));
		actorLabels.add(new JLabel("Year:"));
		
		actorFields = new Vector<JTextField>(1,1);
		for (int i = 0; i < actorLabels.size(); i++){
			actorFields.add(new JTextField(""));
			actorFields.get(i).setFont(standardFont);
			actorLabels.get(i).setFont(standardFont);
			actorLabels.get(i).setForeground(WHITE);
			insertPanels.get(activeTab).add(actorLabels.get(i));
			insertPanels.get(activeTab).add(actorFields.get(i));
		}
		
		//	-> SET INSERT-WRITER PANEL
		activeTab = 3;
		writerLabels = new Vector<JLabel>(1,1);
		writerLabels.add(new JLabel("First Name:"));
		writerLabels.add(new JLabel("Last Name:"));
		writerLabels.add(new JLabel("Year:"));
		
		writerFields = new Vector<JTextField>(1,1);
		
		for (int i = 0; i < writerLabels.size(); i++){
			writerFields.add(new JTextField(""));
			writerFields.get(i).setFont(standardFont);
			writerLabels.get(i).setFont(standardFont);
			writerLabels.get(i).setForeground(WHITE);
			insertPanels.get(activeTab).add(writerLabels.get(i));
			insertPanels.get(activeTab).add(writerFields.get(i));
		}
		
		//	-> SET INSERT-DIRECTOR PANEL
		activeTab = 4;
		directorLabels = new Vector<JLabel>(1,1);
		directorLabels.add(new JLabel("First Name:"));
		directorLabels.add(new JLabel("Last Name:"));
		directorLabels.add(new JLabel("Year:"));
		
		directorFields = new Vector<JTextField>(1,1);
		
		for (int i = 0; i < directorLabels.size(); i++){
			directorFields.add(new JTextField(""));
			directorFields.get(i).setFont(standardFont);
			directorLabels.get(i).setFont(standardFont);
			directorLabels.get(i).setForeground(WHITE);
			insertPanels.get(activeTab).add(directorLabels.get(i));
			insertPanels.get(activeTab).add(directorFields.get(i));
		}
		
		//	INSERT MORE CONTENTS ON EVERY PANEL
		for (int i = 0; i < insertPanels.size(); i++){
			insertPanels.get(i).add(new JLabel(" "));
			insertPanels.get(i).add(insertButtons.get(i));
			insertPanels.get(i).add(new JLabel(" "));
			insertPanels.get(i).add(getIDFields.get(i));
			insertPanels.get(i).add(getDataButtons.get(i));
			insertPanels.get(i).add(new JLabel(" "));
			insertPanels.get(i).add(updateButtons.get(i));
		}
		
		//	-> ADD ALL INSERT PANELS
		for (int i = 0; i < insertPanels.size(); i++){
			rightPanel.add(insertPanels.get(i));
			insertPanels.get(i).setVisible(false);
		}
		
		//	SET TITLE PANEL ACTIVE
		activeTab = 0;
		listPanel.add(scrollPanes.get(activeTab));
		insertPanels.get(activeTab).setVisible(true);
		tabButtons.get(activeTab).setForeground(WHITE);
		
		//	SET VISIBLE
		setVisible(true);
		music();
	}
	
	private void updateTables(){
		
		//	PREPARE TABLE COLUMNS
		String[] titleColumnNames = {"ID","Title", "Type", "Year", "Length", "Rating", "Netflix Original"};
		String[] genreColumnNames = {"ID","Genre"};
		String[] actorColumnNames = {"ID","First Name", "Last Name", "Sex", "Year"};
		String[] writerColumnNames = {"ID","First Name", "Last Name", "Year"};
		String[] directorColumnNames = {"ID","First Name", "Last Name", "Year"};
		String[] genreConnectionColumnNames = {"Title", "Genre"};
		String[] actorRoleColumnNames = {"First Name", "Last Name", "Role", "Title"};
		String[] writerRoleColumnNames = {"First Name", "Last Name", "Title"};
		String[] directorRoleColumnNames = {"First Name", "Last Name", "Title"};
		
		//	SET ALL SCROLL PANES
		scrollPanes = new Vector<JScrollPane>(1,1);
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("titles"), titleColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("genre"), genreColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("actors"), actorColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("writers"), writerColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("directors"), directorColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("genreconnections"), genreConnectionColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("actorroles"), actorRoleColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("writerroles"), writerRoleColumnNames)));
		scrollPanes.add(new JScrollPane(new JTable(itemManager.getTable("directorroles"), directorRoleColumnNames)));
		
		//	ADJUST TITLE COLUMN WITHS
		currentTable = (JTable)scrollPanes.get(0).getViewport().getView();
		currentTable.getColumnModel().getColumn(0).setMaxWidth(30);
		currentTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		
		currentTable = (JTable)scrollPanes.get(1).getViewport().getView();
		currentTable.getColumnModel().getColumn(0).setMaxWidth(30);
		
		currentTable = (JTable)scrollPanes.get(2).getViewport().getView();
		currentTable.getColumnModel().getColumn(0).setMaxWidth(30);
		
		currentTable = (JTable)scrollPanes.get(3).getViewport().getView();
		currentTable.getColumnModel().getColumn(0).setMaxWidth(30);
		
		currentTable = (JTable)scrollPanes.get(4).getViewport().getView();
		currentTable.getColumnModel().getColumn(0).setMaxWidth(30);
		
		currentTable = (JTable)scrollPanes.get(6).getViewport().getView();
		currentTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		for (int i = 0; i < scrollPanes.size(); i++){
			currentTable = (JTable)scrollPanes.get(i).getViewport().getView();
			currentTable.setAutoCreateRowSorter(true);
		}
		
		//	RESET LIST PANEL
		listPanel.removeAll();
		listPanel.add(scrollPanes.get(activeTab));
		listPanel.validate();
	}
	
	public void clearFields()
	{
		// Clear Fields
		for (int i = 0; i < titleFields.size(); i++){
			titleFields.get(i).setText("");
		}
		for (int i = 0; i < actorFields.size(); i++){
			actorFields.get(i).setText("");
		}
		for (int i = 0; i < writerFields.size(); i++){
			writerFields.get(i).setText("");
		}
		for (int i = 0; i < directorFields.size(); i++){
			directorFields.get(i).setText("");
		}
	}
	
	public static void music(){
		AudioPlayer player = AudioPlayer.player;
		AudioStream music;
		AudioData data;
		ContinuousAudioDataStream loop = null;
		
		try{
			music = new AudioStream(new FileInputStream("Fox.mp3"));
			data = music.getData();
			loop = new ContinuousAudioDataStream(data);
		}catch(IOException error){}
		
		player.start(loop);
	}
	
	class TabListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			//	CLEAR MID AND RIGHT PANELS
			listPanel.removeAll();
			
			// HIDE PANELS
			for (int i = 0; i < insertPanels.size(); i++){
				insertPanels.get(i).setVisible(false);
			}
			
			// LOOP THROUGH BUTTONS
			for (int i = 0; i < tabButtons.size(); i++)
			{
				tabButtons.get(i).setForeground(GREY);
				
				// GET BUTTON CLICKED
				if(tabButtons.get(i).equals((JButton)(e.getSource())))
				{
					tabButtons.get(i).setForeground(WHITE);
					
					// CHANGE TAB AND SET VISIBLE
					activeTab = i;
					
					clearFields();
					if(i < 5 && i != 1)
					{
						insertPanels.get(activeTab).setVisible(true);
					}
				}
			}
			updateTables();
			listPanel.validate();
			rightPanel.validate();
		}
	}
	
	class BottomListener implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			// Search and Destroy buttons
			switch (bottomButtons.indexOf((JButton)(e.getSource())))
			{
				// Search SQL with searchField text and create Table
				case 0:
					itemManager.searchTables(searchField.getText());
					searchPane = new JScrollPane(new JTable(itemManager.getTable("search"), new String[]{"","",""}));
					listPanel.removeAll();
					listPanel.add(searchPane);
					listPanel.validate();
					itemManager.clearTable("search");
					break;
				// Remove SQL Item From ID then Update Table
				case 1:
					try {
						itemManager.removeItem(itemManager.getItemComponent(Integer.parseInt(idField.getText()), activeTab));
						updateTables();
						
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					break;
				default:
					break;
			}
		}
	}
	
	class RightPanelListener implements ActionListener
	{
		public boolean notEmpty(int tab)
		{
			// Check if Fields are empty depending on Table
			switch (tab)
			{
				case 0:
					for (int i = 0; i < titleFields.size(); i++){
						if (titleFields.get(i).getText().equals("")){
							return false;
						}
					}
					return true;
				case 2:
					for (int i = 0; i < actorFields.size(); i++){
						if(actorFields.get(i).getText().equals("")){
							return false;
						}
					}
					return true;
				case 3:
					for (int i = 0; i < writerFields.size(); i++){
						if (writerFields.get(i).getText().equals("")){
							return false;
						}
					}
					return true;
				case 4:
					for (int i = 0; i < directorFields.size(); i++){
						if (directorFields.get(i).getText().equals("")){
							return false;
						}
					}
					return true;
				default:
					return false;
			}
		}
		
		public void actionPerformed(ActionEvent e)
		{
			// Variables
			ItemComponent insertItem = new ItemGroup("Insert", 0);
			ItemComponent item = new Item();
			Object[] newItem;
			int id = 0;
			boolean idOK = true;
			
			if(((JButton)(e.getSource())).getText() == "UPDATE" || ((JButton)(e.getSource())).getText() == "GET DATA" )
			{
				// Get ID and check ID
				try{
					id = Integer.parseInt(getIDFields.get(activeTab).getText());
					item = itemManager.getItemComponent(id, activeTab);
				}
				catch(Exception idE){
					idOK = false;
				}
			}
			
			// Insert Button Clicked
			if(((JButton)(e.getSource())).getText() == "INSERT")
			{
				// Check if Data in Fields
				if (notEmpty(activeTab))
				{
					// Insert Data in a new Item depending on Table
					switch (activeTab)
					{	
						case 0:
							newItem = new Object[titleFields.size()];
							for(int i = 0; i < titleFields.size(); i++)
							{
								newItem[i] = titleFields.get(i).getText();
							}
							insertItem.add(new Item(1, newItem));
							break;
						case 2:
							newItem = new Object[actorFields.size()];
							for(int i = 0; i < actorFields.size(); i++)
							{
								newItem[i] = actorFields.get(i).getText();
							}
							insertItem.add(new Item(2, newItem));
							break;
						case 3:
							newItem = new Object[writerFields.size()];
							for(int i = 0; i < writerFields.size(); i++)
							{
								newItem[i] = writerFields.get(i).getText();
							}
							insertItem.add(new Item(4, newItem));
							break;
						case 4:
							newItem = new Object[directorFields.size()];
							for(int i = 0; i < directorFields.size(); i++)
							{
								newItem[i] = directorFields.get(i).getText();
							}
							insertItem.add(new Item(3, newItem));
							break;
						default:
							break;
					}
					try {
						// Send Item To SQL and Update Table
						itemManager.insertItem(insertItem.getComponent(0));
						clearFields();
						updateTables();
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				insertItem.clear();
			}
			else if (idOK) // If ID OK;
			{
				// Get Data and Update Buttons
				switch (((JButton)(e.getSource())).getText())
				{
					// Start Data Buttons
					case "GET DATA":
						// Get Data From Item and set in Fields depending on Table
						switch (activeTab)
						{
							case 0:
								for (int i = 0; i < titleFields.size(); i++){
									titleFields.get(i).setText(item.getItem()[i + 1].toString());
								}
								break;
							case 2:
								for (int i = 0; i < actorFields.size(); i++){
									actorFields.get(i).setText(item.getItem()[i + 1].toString());
								}
								break;
							case 3:
								for (int i = 0; i < writerFields.size(); i++){
									writerFields.get(i).setText(item.getItem()[i + 1].toString());
								}
								break;
							case 4:
								for (int i = 0; i < directorFields.size(); i++){
									directorFields.get(i).setText(item.getItem()[i + 1].toString());
								}
								break;
							default:
								break;
						}
						// End Data Buttons
						break;
						// Start Update Buttons
					case "UPDATE":
						// Check if Data in Fields
						if (notEmpty(activeTab))
						{
							newItem = item.getItem();
							// Put New Data In Item depending on Table
							switch (activeTab)
							{
								case 0:
									for(int i = 0; i < titleFields.size(); i++)
									{
										newItem[i + 1] = titleFields.get(i).getText();
									}
									break;
								case 2:
									for(int i = 0; i < actorFields.size(); i++)
									{
										newItem[i + 1] = actorFields.get(i).getText();
									}
									break;
								case 3:
									for(int i = 0; i < writerFields.size(); i++)
									{
										newItem[i + 1] = writerFields.get(i).getText();
									}
									break;
								case 4:
									for(int i = 0; i < directorFields.size(); i++)
									{
										newItem[i + 1] = directorFields.get(i).getText();
									}
									break;
								default:
									break;
							}
							item.setItem(newItem);
							try {
								// Send changes to SQL and Update Table
								itemManager.updateItem(item);
								clearFields();
								updateTables();
								
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						break;
					default:
						break;
					//	End of GET DATA / UPDATE
				}
			}
		}
	}
}