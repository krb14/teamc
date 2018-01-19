package application;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import application.Simulation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import com.itextpdf.text.DocumentException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import surfaces.Impervious;
import surfaces.Pervious;
import surfaces.Saturated;
import surfaces.Surface;
/**
 * Date: 12/10/17
 * @author C. Warren Hammock, Derek Owens
 *
 */

public class MainFXMLController implements Initializable {
	
	Simulation simulation = new Simulation();
    ArrayList<Surface> masterList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ObservableList<String> observableList;
    ObservableList<String> observableTempList;
    Surface editedSurface = null;
    boolean editFlag = false;
    boolean pauseState = false;
    
    @FXML
    private Tooltip hourtooltip;
    
    @FXML
    private Tooltip minutetooltip;
    
    @FXML
    private Tooltip lengthtooltip;
    
    @FXML
    private Tooltip widthtooltip;

    @FXML
    private Tooltip surftemptooltip;
    
    @FXML
    private Tooltip rateraintooltip;
    
    @FXML
    private Tooltip raintemptooltip;
    
    @FXML
    private Tooltip evaptooltip;
    
    @FXML
    private Tooltip waterdepthtooltip;
    
    @FXML
    private MenuItem importSimulation;

    @FXML
    private MenuItem exportSimulation;

    @FXML
    private Label numberPervObjects;

    @FXML
    private Label numberSatObjects;

    @FXML
    private Label numberImpervObjects;

    @FXML
    private Label labelnumberPerv;

    @FXML
    private Label labelNumSat;

    @FXML
    private Label labelImperItems;
    
	@FXML
	private ComboBox<String> surfaceTypeDropdown;

	@FXML
    private ComboBox<String> listToBeCalculatedDropdown;
	
	@FXML
    private Tooltip speedToolTip;

    @FXML
    private Tooltip surfaceListToolTip;
    
    @FXML
    private Tooltip addSurfaceToolTip;
    
    @FXML
    private Tooltip globalVarToolTip;
    
    @FXML
    private Tooltip satGraphToolTip;
    
    
    @FXML
    private Tooltip imperGraphToolTip;
    
    
    @FXML
    private MenuItem loadSimulation;

    @FXML
    private MenuItem aboutSWS;

    @FXML
    private Spinner<Double> gRateOfRainfallVariable;

    @FXML
    private Spinner<Integer> gDurationHourVariable;

    @FXML
    private Spinner<Integer> gDurationMInuteVariable;

    @FXML
    private Spinner<Double> gRainTemperatureVariable;

    @FXML
    private Spinner<Double> gEvaporationRatesVariable;

    @FXML
    private Spinner<Double> gWaterTableDepthVariable;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button exportPDFButton;

    @FXML
    private Button addToListButton;

    @FXML
    private Spinner<Integer> surfaceLengthVariable;

    @FXML
    private Spinner<Integer> surfaceWidthVariable;

    @FXML
    private Spinner<Double> surfaceTemperatureVariable;

    @FXML
    private Spinner<Double> infiltrationRateVaraible;

    @FXML
    private Label tempLabel1;

    @FXML
    private ToggleButton farenheitSelect;

    @FXML
    private ToggleGroup Group1;

    @FXML
    private ToggleButton celsiusSelect;

    @FXML
    private Label tempLabel2;

    @FXML
    private Button editSurfaceSelectionButton;

    @FXML
    private LineChart<Number, Number> saturatedGraph;

    @FXML
    private LineChart<Number, Number> permeableGraph;

    @FXML
    private LineChart<Number, Number> imperviousGraph;

    @FXML
    private Label totalSaturatedPerMinute;

    @FXML
    private Label totalPermeablePerMinute;

    @FXML
    private Label totalImperviousPerMinute;

    @FXML
    private Slider simulationSpeed;

    @FXML
    private Label totalTimeExpired;

    @FXML
    private ListView<String> finalSurfaceList;

    @FXML
    private LineChart<Number, Number> TotalSurfaceRunoffGraph;

    @FXML
    private Label allTotalRunOff;

    @FXML
    private Label allTotalRain;

    @FXML
    private Label finalRainTemperature;

    @FXML
    private Label tempLabel3;

    @FXML
    private Label rainLostToEvaporation;

    @FXML
    private Label isRunningLabel;
    
    @FXML
    private MenuItem exitSimulation;

    @FXML
	private CategoryAxis permXAxis;

	@FXML
	private NumberAxis permYAxis;
	
	@FXML
	private CategoryAxis impervXAxis;

	@FXML
	private NumberAxis impervYAxis;
	
	@FXML
	private CategoryAxis satXAxis;

	@FXML
	private NumberAxis totYAxis;
	
	@FXML
	private CategoryAxis totXAxis;

	@FXML
	private NumberAxis satYAxis;
	
    @FXML
    private Label isPaused;
    /**
     * Initializes buttons and values for simulation.
     */
	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		isRunningLabel.setVisible(true);
    	surfaceTypeDropdown.getItems().removeAll(surfaceTypeDropdown.getItems());
        surfaceTypeDropdown.getItems().addAll("Saturated", "Permeable", "Impervious-Roof-Aluminum",
        					"Impervious-Roof-Asphalt", "Impervious-Ground-Concrete", "Impervious-Ground-Asphalt" );
        pauseButton.setOnAction(this::PauseSimulation);
        resetButton.setOnAction(this::ResetSimulation);
        editSurfaceSelectionButton.setOnAction(this::editSelection);
        startButton.setOnAction(this::RunMasterSimulation);
        addToListButton.setOnAction(this::AddToList);
        celsiusSelect.setOnAction(this::ToggleToCelsius);
        farenheitSelect.setOnAction(this::ToggleToFarenheit);
        SetEvapRate();
        exportPDFButton.setOnAction(this::ExportToPDF);
        isRunningLabel.setText("Ready For Input...");
        isPaused.setVisible(false);
        aboutSWS.setOnAction(this::aboutAlert);
    }

	/** 
	 * Allows user to choose the file they would like to import and creates the masterList and subsequent lists for the chosen import in the GUI
	 */
	@FXML
	private void ImportSimulation() {
		System.out.println("import simulation pressed");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose .txt File To Import");
		File file = fileChooser.showOpenDialog(addSurfaceToolTip.getScene().getWindow());
		System.out.println(file.getAbsolutePath());
		masterList = new ArrayList<>(importCSV(file.getAbsolutePath()));
		//nameList = 
		observableList = FXCollections.observableArrayList(nameList);
        listToBeCalculatedDropdown.setItems(observableList);
	}
	/**
	 * Reads in delimited data from a .txt file and populates arrayList with surfaces.
	 * @param fname
	 * @return arrayList of surfaces
	 */
	private ArrayList<Surface> importCSV(String fname) {

		String C_DEL = ",";

		ArrayList<Surface> surfaces = new ArrayList<>();

		BufferedReader reader = null;
		FileReader fr = null;

		try {
			fr = new FileReader(fname);
			reader = new BufferedReader(fr);

			String line = "";

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				String[] token = line.split(C_DEL);
				for(String item: token) {
					System.out.println(item);
				}

				if (token.length > 0) {
					if (token[1].equals("1")) {
						Pervious pervious = new Pervious(token[0],Double.parseDouble(token[2]),Double.parseDouble(token[3]),Integer.parseInt(token[4]),Integer.parseInt(token[5]),Double.parseDouble(token[6]),Integer.parseInt(token[7]),Double.parseDouble(token[8]),Double.parseDouble(token[9]),Double.parseDouble(token[10]));
						surfaces.add(pervious);
						System.out.println(token[4]);
					}
					else if (token[1].equals("2")) {
						Impervious impervious = new Impervious(token[0],Double.parseDouble(token[2]),Integer.parseInt(token[3]),Integer.parseInt(token[4]),Double.parseDouble(token[5]),Integer.parseInt(token[6]),Double.parseDouble(token[7]),Double.parseDouble(token[8]),Integer.parseInt(token[9]));
						surfaces.add(impervious);
					}
					else if (token[1].equals("3")) {
						Saturated saturated = new Saturated(token[0],Double.parseDouble(token[2]),Double.parseDouble(token[3]),Integer.parseInt(token[4]),Integer.parseInt(token[5]),Double.parseDouble(token[6]),Integer.parseInt(token[7]),Double.parseDouble(token[8]),Double.parseDouble(token[9]),Double.parseDouble(token[10]));
						surfaces.add(saturated);
					}
				}
			}

			} catch (IOException e) {
					e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(surfaces.get(0).getName());
			return surfaces;

		}
	
	/**
	 * Creates a file and exports simulation data to the file.  Only saves 1 simulation per
	 * file. 
	 */
	@FXML
	private void ExportSimulation() {
		LocalDateTime dateTime = LocalDateTime.now();
		String fileName = new String((dateTime.getMonth()) + "." + 
									Integer.toString(dateTime.getDayOfMonth()) + "." + 
									Integer.toString(dateTime.getYear()) + "." +
									Integer.toString(dateTime.getHour()) + "." +
									Integer.toString(dateTime.getMinute()) + "." +
									Integer.toString(dateTime.getSecond()) + ".txt"
									);
		System.out.println(fileName);
		File file = new File(fileName);
		//create file
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//write surfaces to file
		for(Surface surface: masterList) {
			if (surface.getType() == 1) {
				Pervious pervious = (Pervious)surface;
				pervious.genCCSV(file.getAbsolutePath());
			}
			if (surface.getType() == 2) {
				Impervious impervious = (Impervious)surface;
				impervious.genCCSV(file.getAbsolutePath());
			}
			if (surface.getType() == 3) {
				Saturated saturated = (Saturated)surface;
				saturated.genCCSV(file.getAbsolutePath());
			}
		}

	}
	/**
	 * Creates an instance of the Export class with the masterList data. 
	 * @param action
	 */
	@FXML
	private void ExportToPDF(ActionEvent action) {
		try {
			Export export = new Export(masterList, simulation);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Changes state of simulation and creates a sleep for the duration of that state.
	 * @param action
	 */
	@FXML
	private void PauseSimulation(ActionEvent action) {
		if(pauseState == false) {
			pauseState = true;
		}else {
			pauseState = false;
		}

	}
	
	/**
	 * Clears all values and resets values
	 * @param action
	 */
	@FXML
	private void ResetSimulation(ActionEvent action) {
		permeableGraph.getData().clear();
		saturatedGraph.getData().clear();
		imperviousGraph.getData().clear();
		TotalSurfaceRunoffGraph.getData().clear();
		masterList.clear();
		//Added this when added for editing
		nameList.clear();
		simulation.ClearArrays();
		//Change
		observableList = FXCollections.observableArrayList(nameList);
        listToBeCalculatedDropdown.setItems(observableList);
        ArrayList<String> clearTempList = new ArrayList<>();
        observableTempList = FXCollections.observableArrayList(clearTempList);
        finalSurfaceList.setItems(observableTempList);;
        totalTimeExpired.setText("0");
		gRateOfRainfallVariable.getValueFactory().setValue(0.0);
		gDurationHourVariable.getValueFactory().setValue(0);
		gDurationMInuteVariable.getValueFactory().setValue(0);
		gRainTemperatureVariable.getValueFactory().setValue(0.0);
		gEvaporationRatesVariable.getValueFactory().setValue(0.0);
		gWaterTableDepthVariable.getValueFactory().setValue(35.0);
		finalRainTemperature.setText("0.0");
		totalSaturatedPerMinute.setText("0.0");
		totalImperviousPerMinute.setText("0.0");
		totalPermeablePerMinute.setText("0.0");
		allTotalRain.setText("0.0");
		allTotalRunOff.setText("0.0");
		rainLostToEvaporation.setText("0.0");
		finalRainTemperature.setText("0.0");
		SetEvapRate();
		isRunningLabel.setText("Ready For Input...");
		numberPervObjects.setVisible(false);
		labelnumberPerv.setVisible(false);
		numberImpervObjects.setVisible(false);
		labelImperItems.setVisible(false);
		numberSatObjects.setVisible(false);
		labelNumSat.setVisible(false);
		
		
		
	/**
	 * Is activated on clicking Add to List Button. Checks if it is an edit and either creates a new surface and list item or edits an existing one.
	 * @param event
	 */	
	}
	@FXML
	private void AddToList(ActionEvent event) {
		int index = 0;
        
        //Flag to check whether a new surface needs created or an edit
        if (!editFlag) {
        	
            masterList.add(CreateSurface());
            nameList.add((Integer.toString(nameList.size())  + surfaceTypeDropdown.getValue()));
        } else {
            //searches for the surface from observable list in masterList. When found, updates
            for (String surfaceName : nameList) {
            	
                //Something is wrong here;
                if(surfaceName.equals(editedSurface.getName())) {    	
                    masterList.set(index, CreateSurface());
                    
                    //nameList.set(index, surfaceName);
                }
                
                index++;
            }
            
            editFlag = false;
        }
        
        //observableList = FXCollections.observableArrayList(masterList);
        observableList = FXCollections.observableList(nameList);
        
        listToBeCalculatedDropdown.setItems(observableList);
        
        //This function resets the inputs after add button is hit and data is recorded by initEditListView()
        clearSurfaceInputs();
    }
	
	/**
	 * Activated upon clicking Edit Selection button. Finds the surface with name matching that of the selected dropdown and repopulates fields with its current
	 * attributes. Sets edit flag true for addToList function
	 * @param event
	 */
	private void editSelection(ActionEvent event) {
		//Retrieve surface from list
        String editedName = listToBeCalculatedDropdown.getValue();
        
        for(Surface surface : masterList) {     	
        	
        	if(surface.getName().equals(editedName)) {
        		editedSurface = surface;
        	}
        }
        
        if(editedSurface.getType() == 1) {
        	surfaceTypeDropdown.setValue("Permeable");
        } else if(editedSurface.getType() == 2) {
        	
        	if(editedSurface.getSType() == 10) {
   
        	} else if(editedSurface.getSType() == 11) {
        		
        	} else if(editedSurface.getSType() == 20) {
        		
        	} else if(editedSurface.getSType() == 21) {
        		
        	} else {
        		System.out.println("A valid SType could not be determined.");
        	}
        	
        	System.out.println("Impervious SType not yet implemented");
        } else if(editedSurface.getType() == 3) {
        	surfaceTypeDropdown.setValue("Saturated");
        } else {
        	System.out.println("getType() returned an error value.");
        }
        
        
        surfaceLengthVariable.getValueFactory().setValue(editedSurface.getLngth());
        surfaceWidthVariable.getValueFactory().setValue(editedSurface.getWdth());
        surfaceTemperatureVariable.getValueFactory().setValue(editedSurface.getSrfcTemp());
        infiltrationRateVaraible.getValueFactory().setValue(editedSurface.getIflRate());

        editFlag = true;
    }
	
    /**
     * Clears all arrays for reset
     */
    private void clearSurfaceInputs() {
        surfaceTypeDropdown.setValue("Surface Type");
        surfaceLengthVariable.getValueFactory().setValue(1);
        surfaceWidthVariable.getValueFactory().setValue(1);
        surfaceTemperatureVariable.getValueFactory().setValue(0.0);
        infiltrationRateVaraible.getValueFactory().setValue(0.0);
    }
	/**
	 * Starts simulation
	 * @param event
	 */
	@FXML
	private void RunMasterSimulation(ActionEvent event) {
		//starts graph output simulation
		MakeSurfaceLists(); //changed to separate surface class interaction

		StartPermSimulation();
		StartImperviousSimulation();
		StartSaturatedSimulation();
		StartTotalSimulation();
	}
	/**
	 * Sets total rain label
	 */
	@FXML
	private void SetTotalRain() {
		double totalRain = simulation.GetTotalRain();
        allTotalRain.setText(Double.toString(totalRain));

	}
	
	/**
	 * Sets final rain temperature label
	 */
	private void setFinalRainTemp() {
		double totalSum = 0.0;
		//for some reason this isn't working
		for(Surface surface : masterList) {
			totalSum += surface.finalTemp();
		}
		
		finalRainTemperature.setText(Double.toString(totalSum / masterList.size()));
	}
	
	/**
	 * Populates the label for evaporation lost to rain
	 */
	@FXML
	public void PopulateEvaporationLostLabel() {
		double rainLost = simulation.RainLostToEvaporation(masterList);
		rainLostToEvaporation.setText(Double.toString(rainLost));
	}
	/**
	 * Uses array created in CreateTempList to populate the observable list for 
	 * temp surfaces.
	 */
	@FXML
	private void ViewTempList() {
		ArrayList<String> templist = simulation.CreateTempList();
        observableTempList = FXCollections.observableArrayList(templist);
        finalSurfaceList.setItems(observableTempList);
	}
	
	/**
	 * This class creates a new surface object with the specified inputs whenever addToList is called.
	 * @return new surface object
	 */
	@FXML
	private Surface CreateSurface() {

	        String surfaceType = surfaceTypeDropdown.getValue();
	        String surfaceName = "";
	        
	        //Need to make name more meaningful
	        if (!editFlag) {
	        	surfaceName = (Integer.toString(masterList.size())  + surfaceType);
	        } else {
	        	surfaceName = editedSurface.getName();
	        }

	        
	        int length = surfaceLengthVariable.getValue();
	        int width = surfaceWidthVariable.getValue();
	        double evpRate = gEvaporationRatesVariable.getValue();
	        double waterDepth = gWaterTableDepthVariable.getValue();
	        double rainRate = gRateOfRainfallVariable.getValue();
	        int stormHr = gDurationHourVariable.getValue();
	        int stormMin = gDurationMInuteVariable.getValue();
	        int stormDur = (stormHr * 60) + stormMin;
	        double surfTemp = surfaceTemperatureVariable.getValue();
	        double rainTemp = gRainTemperatureVariable.getValue();
	        double infRate = infiltrationRateVaraible.getValue();

	        int sType = 0;
	        
	        Surface surface1 = null; 
	        
	        if (surfaceType.equalsIgnoreCase("Permeable")) {
				surface1 = new Pervious(surfaceName, evpRate,waterDepth,length, width,rainRate,stormDur,surfTemp,rainTemp,infRate);
	        } 
	        else if (surfaceType.equalsIgnoreCase("Saturated")) {
	            surface1 = new Saturated(surfaceName,evpRate,waterDepth,length,width,rainRate,stormDur,surfTemp,rainTemp,infRate);
	        } else if (surfaceType.toLowerCase().contains("impervious")) {	            
	            //Checks if type contains the subtype identifiers. Sets sType to appropriate value
	            if (surfaceType.toLowerCase().contains("roof-aluminum")){
	                sType = 10;
	            } else if (surfaceType.toLowerCase().contains("roof-asphalt")) {
	                sType = 11;
	            } else if(surfaceType.toLowerCase().contains("ground-concrete")) {
	                sType = 20;
	            } else if(surfaceType.toLowerCase().contains("ground-asphalt")) {
	                sType = 21;
	            } else {
	                System.out.println("Error on setting impervious type");
	            }
	            
	            surface1 = new Impervious(surfaceName, evpRate, length, width, rainRate, stormDur, surfTemp, rainTemp, sType);
	        }	        
	        return surface1;

	    }
	/**
	 * Creates a visual and numerical representation above global variables.  This 
	 *  allows the user to see how many of each kind of surface the simulation is running.
	 */
	@FXML
	private void MakeSurfaceLists() {
		int perm = 0;
		int impv = 0;
		int sat = 0;
		for (Surface surface: masterList) {
			if (surface.getType() == 1) {
				simulation.AddPermeableList((Pervious)surface);
				perm += 1;
			}else if (surface.getType() == 2) {
				simulation.AddImperviousList((Impervious)surface);
				impv += 1;
			}else if (surface.getType() == 3) {
				simulation.AddSaturatedList((Saturated)surface);
				sat += 1;
			}
		}
		if (perm > 0) {
			numberPervObjects.setVisible(true);
			labelnumberPerv.setVisible(true);
			numberPervObjects.setText(Integer.toString(perm));
			labelnumberPerv.setText("Pervious Surfaces");
		}
		if (impv > 0) {
			numberImpervObjects.setVisible(true);
			labelImperItems.setVisible(true);
			numberImpervObjects.setText(Integer.toString(impv));
			labelImperItems.setText("Impervious Surfaces");
		}
		if (sat > 0) {
			numberSatObjects.setVisible(true);
			labelNumSat.setVisible(true);
			numberSatObjects.setText(Integer.toString(sat));
			labelNumSat.setText("Saturated Surfaces");
		}
		
		simulation.PopulateTotalSimList();
		
	}
	 
    /**
     * Exit Screen/Program
     */
    @FXML
    private void ExitProgram() {
    	System.exit(0);
    }

    
    /**
     * Toggle between Celsius and Ferenheit labels for input/output
     * @param event
     */
    @FXML
    private void ToggleToFarenheit(ActionEvent event) {
    	tempLabel1.setText("F");
    	tempLabel2.setText("F");
    	tempLabel3.setText("F");
    	
    }
    /**
     * Toggle between Celsius and Ferenheit labels for input/output
     * @param event
     */
	@FXML
	private void ToggleToCelsius(ActionEvent event) {
	    	tempLabel1.setText("C");
	    	tempLabel2.setText("C");
	    	tempLabel3.setText("C");
	    	
	}
	
	/** 
	 * Creates an alert box when the menu option about is selected with a brief summary of the systema and lists creators
	 * @param event
	 */
	private void aboutAlert(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About Storm Water Simulator");
		alert.setHeaderText(null);
		
		String aboutContent = "Storm Water Simulator is an application that creates virtual scenarios where multiple environments " +
								"can be set to emulate a real property with unique weather settings. Once these parameters have been " +
								"established the simulation calculates the effects of the rainfall on all environments and produces " + 
								"their outcomes.\n\nThe simulator can import properly formatted csv files and export in both csv and pdf." +
								"\n\nCreated by Charles Hammock, Kevin Bryant, Derek Owens and Ibrahim ";
		
		alert.setContentText(aboutContent);

		alert.showAndWait();
	}
	
	/**
	 * Sets predetermined value to display to user for evap rate depending upon the month.
	 */
	@FXML
	private void SetEvapRate() {
		
		Calendar date = Calendar.getInstance();
		int month = date.get(Calendar.MONTH);

		switch (month) {
			case 0: gEvaporationRatesVariable.getValueFactory().setValue(.59);		//Jan. rate & mins 
						break;
			case 1: gEvaporationRatesVariable.getValueFactory().setValue(.83);		//Feb. rate & mins
						break;
			case 2: gEvaporationRatesVariable.getValueFactory().setValue(1.68);		//March rate & mins
						break;
			case 3: gEvaporationRatesVariable.getValueFactory().setValue(2.69);		//April rate & mins
						break;
			case 4: gEvaporationRatesVariable.getValueFactory().setValue(3.96);		//May rate & mins
						break;
			case 5: gEvaporationRatesVariable.getValueFactory().setValue(4.43);		//June rate & mins
						break;
			case 6: gEvaporationRatesVariable.getValueFactory().setValue(4.76);		//July rate & mins
						break;
			case 7: gEvaporationRatesVariable.getValueFactory().setValue(4.11);		//August rate & mins
						break;
			case 8: gEvaporationRatesVariable.getValueFactory().setValue(2.90);		//Sept. rate & mins
						break;
			case 9: gEvaporationRatesVariable.getValueFactory().setValue(1.88);		//Oct. rate & mins
						break;
			case 10: gEvaporationRatesVariable.getValueFactory().setValue(.96);		//Nov. rate & mins
						 break;
			case 11: gEvaporationRatesVariable.getValueFactory().setValue(.58);		//Dec. rate & mins
						 break;
		}
	}
	/**
	 * Starts graphical output for total runoff.
	 */
    @FXML 
	private void StartTotalSimulation() {	
		XYChart.Series series = new XYChart.Series();
		Task task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				int i = 0;
				while(i < simulation.TotalList.size()) {
					int order = i;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
								allTotalRunOff.setText(Double.toString(simulation.TotalList.get(order)));
								totalTimeExpired.setText(Integer.toString(order));
								series.getData().add(new XYChart.Data(Integer.toString(order),simulation.TotalList.get(order)));
								
								if (order >= simulation.TotalList.size() - 1) {
									isRunningLabel.setText("Simulation Ended....");
									setFinalRainTemp();
									SetTotalRain();
									ViewTempList();
									PopulateEvaporationLostLabel();
								}else {	isRunningLabel.setText("Running....");}
							
						}
					});
					
					i++;
					if (simulationSpeed.getValue() <= 1.0) { Thread.sleep(2000); }
					if (simulationSpeed.getValue() > 1 && simulationSpeed.getValue() <= 2) { Thread.sleep(1500); }
					if (simulationSpeed.getValue() > 2 && simulationSpeed.getValue() <= 3.0) { Thread.sleep(1000); }
					if (simulationSpeed.getValue() > 3 && simulationSpeed.getValue() <= 4.0) { Thread.sleep(500); }

					while(pauseState == true) {
						Thread.sleep(1);

					}



	
				}
				return null;
			}
			
		};
		TotalSurfaceRunoffGraph.getData().addAll(series);
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

		
	}
    /**
     * Starts graphical output for Permeable runoff.
     */
    @FXML 
	private void StartPermSimulation() {	
		XYChart.Series series = new XYChart.Series();
		Task task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				int i = 0;
				while(i < simulation.TotalRunoffPermList.size()) {
					int order = i;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
								totalPermeablePerMinute.setText(Double.toString(simulation.TotalRunoffPermList.get(order)));
								series.getData().add(new XYChart.Data(Integer.toString(order),simulation.TotalRunoffPermList.get(order)));								

							
						}
					});
					
					i++;
					if (simulationSpeed.getValue() <= 1.0) { Thread.sleep(2000); }
					if (simulationSpeed.getValue() > 1 && simulationSpeed.getValue() <= 2) { Thread.sleep(1500); }
					if (simulationSpeed.getValue() > 2 && simulationSpeed.getValue() <= 3.0) { Thread.sleep(1000); }
					if (simulationSpeed.getValue() > 3 && simulationSpeed.getValue() <= 4.0) { Thread.sleep(500); }
					while(pauseState == true) {
						Thread.sleep(50);
					}
	
				}
				return null;
			}
			
		};
		permeableGraph.getData().addAll(series);
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();	

		
	}
	
    /**
     * Starts graphical output for Impervious runoff.
     */
    @FXML 
	private void StartImperviousSimulation() {	
		XYChart.Series series = new XYChart.Series();
		Task task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				int i = 0;
				while(i < simulation.TotalRunoffImpervList.size()) {
					int order = i;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
								totalImperviousPerMinute.setText(Double.toString(simulation.TotalRunoffImpervList.get(order)));
								series.getData().add(new XYChart.Data(Integer.toString(order),simulation.TotalRunoffImpervList.get(order)));								

						}
					});
					
					i++;
					if (simulationSpeed.getValue() <= 1.0) { Thread.sleep(2000); }
					if (simulationSpeed.getValue() > 1 && simulationSpeed.getValue() <= 2) { Thread.sleep(1500); }
					if (simulationSpeed.getValue() > 2 && simulationSpeed.getValue() <= 3.0) { Thread.sleep(1000); }
					if (simulationSpeed.getValue() > 3 && simulationSpeed.getValue() <= 4.0) { Thread.sleep(500); }
					while(pauseState == true) {
						Thread.sleep(50);
					}
	
				}
				return null;
			}
			
		};
		imperviousGraph.getData().addAll(series);
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();	

		
	}
    /**
     * Starts graphical output for Saturated runoff.
     */
    @FXML 
	private void StartSaturatedSimulation() {	
		XYChart.Series series = new XYChart.Series();
		Task task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				int i = 0;
				while(i < simulation.TotalRunoffSatList.size()) {
					int order = i;
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
								totalSaturatedPerMinute.setText(Double.toString(simulation.TotalRunoffSatList.get(order)));
								series.getData().add(new XYChart.Data(Integer.toString(order),simulation.TotalRunoffSatList.get(order)));								

							
						}
					});
					
					i++;
					if (simulationSpeed.getValue() <= 1.0) { Thread.sleep(2000); }
					if (simulationSpeed.getValue() > 1 && simulationSpeed.getValue() <= 2) { Thread.sleep(1500); }
					if (simulationSpeed.getValue() > 2 && simulationSpeed.getValue() <= 3.0) { Thread.sleep(1000); }
					if (simulationSpeed.getValue() > 3 && simulationSpeed.getValue() <= 4.0) { Thread.sleep(500); }
					while(pauseState == true) {
						Thread.sleep(50);
					}
	
				}
				return null;
			}
			
		};
		saturatedGraph.getData().addAll(series);
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();	

		
	}
    
    
	
	
}
