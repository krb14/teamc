package application;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;

import surfaces.Impervious;
import surfaces.Pervious;
import surfaces.Saturated;
import surfaces.Surface;
/**
 * Date: 12/10/17
 * @author C. Warren Hammock
 * An array management class for the simulation.  MainFXMLController takes the masterList
 * and populates PermeableList, SaturatedList, and ImperviousList with the applicable surface types.
 * This class takes those lists and generates values for final output. 
 * 
 *
 */
public class Simulation {

	/**
	 * Lists populated by surface type from MainFXMLController
	 */
	ArrayList<Pervious> PermeableList;
	ArrayList<Saturated> SaturatedList;
	ArrayList<Impervious> ImperviousList;
	/**
	 * Arrays of total runoff for PermeableList, SaturatedList, ImperviousList
	 */
	ArrayList<Double> TotalRunoffPermList;
	ArrayList<Double> TotalRunoffImpervList;
	ArrayList<Double> TotalRunoffSatList;
	/**
	 * Array of total of TotalRunoffPermList, TotalRunoffImpervList, and 
	 * TotalRunoffSatList
	 */
	ArrayList<Double> TotalList;

	/**
	 * Instantiates the class and creates arraylists
	 */
	public Simulation() {
		PermeableList = new ArrayList<>(); //pervious list populated in from controller
		SaturatedList = new ArrayList<>();  //saturated list populated by controller
		ImperviousList = new ArrayList<>(); //impervious list populated by controller
		
		TotalRunoffPermList = new ArrayList<>(); //populate with total for permeable surface
		TotalRunoffImpervList = new ArrayList<>(); //populate with total for impervious surfaces
		TotalRunoffSatList = new ArrayList<>();  //populate with saturated surfaces
		TotalList = new ArrayList<>();  //list of all surfaces totaled
	}
	/**
	 *  
	 * @return double of total of all numbers in TotalList
	 */
	public double GetTotalRunoff() {
		double total = 0.0;
		int sizeTotalList = TotalList.size();
		total =  TotalList.get(sizeTotalList-1);
		
		return total;
	}
	/**
	 * 
	 * @return an arraylist of all the surface names to be used by observableTempList
	 */
	public ArrayList<String> CreateTempList(){
		ArrayList<String> tempList = new ArrayList<>();
		
		if (PermeableList.size() > 0) {
			for(Pervious surface: PermeableList) {
				tempList.add(surface.getName() + " " + Double.toString(surface.finalTemp()));
			}
		}
		if (ImperviousList.size() > 0) {
			for(Impervious surface: ImperviousList) {
				tempList.add(surface.getName() + " " + Double.toString(surface.finalTemp()));
			}
		}
		if (SaturatedList.size() > 0) {
			for(Saturated surface: SaturatedList) {
				tempList.add(surface.getName() + " " + Double.toString(surface.finalTemp()));
			}
		}
		return tempList;
		
	}
	/**
	 * Adds all values for storm duration 
	 *  rain rate for each surface in each array of surfaces. 
	 * @return  a double of total amount of rain lost.  
	 */
	public double GetTotalRain() {
		double rainLost = 0.0;
		if (PermeableList.size() != 0) {
			for(Pervious pervious: PermeableList) {
				rainLost += pervious.getStrmDuration() * pervious.getRnRate();
			}
		}
		if (ImperviousList.size() != 0) {
			for(Impervious impervious: ImperviousList) {
				rainLost += impervious.getStrmDuration() * impervious.getRnRate();
			}
		}
		if (SaturatedList.size() != 0) {
			for(Saturated saturated: SaturatedList) {
				rainLost += saturated.getStrmDuration() * saturated.getRnRate();
			}
		}
		return rainLost;
	}
	/**
	 * adds each evaluation of evaporation for each surface on 
	 * the masterList.  Each is added together to create and return a total.
	 * @param masterList
	 * @return a double
	 */
	public double RainLostToEvaporation(ArrayList<Surface> masterList) {
		double rainLost = 0.0;
		for(Surface surface: masterList) {
			double evapLost = surface.evaporation();
			rainLost += evapLost;
		}
		return rainLost;
	}
	/**
	 * 
	 * @return double of the final rain temp
	 */
	public double FinalRainTemp(ArrayList<Surface> masterList) {
		double totalSum = 0.0;
		for(Surface surface : masterList) {
			totalSum += surface.finalTemp();
		}
		double finalTemp = totalSum / masterList.size();
		return finalTemp;
	}
	
	/**
	 * 
	 * To check if the array is empty and max array size to populate an empty
	 * list. 
	 * @return int 
	 */
	private int CheckForEmpty() {
		int maxArrays = 0;
		if (TotalRunoffPermList.size() != 0) {
			maxArrays = TotalRunoffPermList.size();
		}
		if (TotalRunoffSatList.size() != 0) {
			maxArrays = TotalRunoffSatList.size();
		}
		if (TotalRunoffImpervList.size() != 0) {
			maxArrays = TotalRunoffImpervList.size();
		}
		return maxArrays;	
	}
	/**
	 * populates an empty array with 0.0 values for the length of populated arrays.
	 */
	private void PopulateEmptyArrays() {
		int defaultArraySize = CheckForEmpty();
		if (TotalRunoffPermList.size() == 0) {
			for(int i = 0; i < defaultArraySize; i++) {
				TotalRunoffPermList.add(0.0);
			}
		}
		if (TotalRunoffSatList.size() == 0) {
			for(int i = 0; i < defaultArraySize; i++) {
				TotalRunoffSatList.add(0.0);
			}
		}
		if (TotalRunoffImpervList.size() == 0) {
			for(int i = 0; i < defaultArraySize; i++) {
				TotalRunoffImpervList.add(0.0);
			}
		}
	}
	/**
	 * Creates total runoff values for each of the surface types.
	 */
	public void PopulateTotalSimList() {
		TotalRunoffPermList = CreatePermTotal();
		TotalRunoffImpervList = CreateImperviousTotal();
		TotalRunoffSatList = CreateSaturatedTotal();
		PopulateEmptyArrays();
		TotalList = CreateTotalList();
		//System.out.println(Integer.toString(PermeableList.get(0).runoff().size()));
		
	}
	
	/**
	 * clears all arrays for resets
	 */
	public void ClearArrays() {
		PermeableList.clear();
		SaturatedList.clear();
		ImperviousList.clear();
		TotalRunoffImpervList.clear();
		TotalRunoffPermList.clear();
		TotalRunoffSatList.clear();
		TotalList.clear();
	}
	/**
	 * 
	 * takes each list of surfaces and outputs an array of 
	 * values.  Each one of those arrays are added for each [i] position.  All values that 
	 * are at [i] position are added an placed in another array.   That  array is created and 
	 * returned. 
	 *  @return arraylist of doubles. 
	 */
	private ArrayList<Double> CreatePermTotal(){

		double total = 0.0;
		//holds array of arrays of inviduals surface runoffs
		ArrayList<ArrayList<Double>> tempList = new ArrayList<>();
		//holds totals for added runoff values at i
		ArrayList<Double> countList = new ArrayList<>();
		//break if there is nothing in the array
		if (PermeableList.size() != 0) {
			
			//create arraylist of double arrays
			for(int i = 0; i < PermeableList.size(); i++) {
				tempList.add(new ArrayList<Double>(PermeableList.get(i).runoff()));
				System.out.println(tempList.get(i).size());
			}
			
			//takes size of array[i] and goes thru each array at a given index.  the
			//values are added and added to countList.  The countList[i] will have the totals an 
			//index[i] all arrays.
			for(int i = 0; i < tempList.get(0).size(); i++) {
				
				for (int x = 0; x < tempList.size(); x++) {
					double number = tempList.get(x).get(i);
					total += number;
				}
				countList.add(total);
				total = 0.0;
			}
		}
		
		return countList;
	}
	
	/**
	 * 
	 *  takes each list of surfaces and outputs an array of 
	 * values.  Each one of those arrays are added for each [i] position.  All values that 
	 * are at [i] position are added an placed in another array.   That  array is created and 
	 * returned. 
	 * @return arraylist of doubles. 
	 */
	private ArrayList<Double> CreateImperviousTotal(){

		double total = 0.0;
		//holds array of arrays of inviduals surface runoffs
		ArrayList<ArrayList<Double>> tempList = new ArrayList<>();
		//holds totals for added runoff values at i
		ArrayList<Double> countList = new ArrayList<>();
		//break if there is nothing in the array
		if (ImperviousList.size() != 0) {
			//create arraylist of double arrays
			for(int i = 0; i < ImperviousList.size(); i++) {
				ArrayList<Double> list = new ArrayList<>();
				list = ImperviousList.get(i).runoff();
				tempList.add(new ArrayList<Double>(list));
			}
			//takes size of array[i] and goes thru each array at a given index.  the
			//values are added and added to countList.  The countList[i] will have the totals an 
			//index[i] all arrays.
			for(int i = 0; i < tempList.get(0).size(); i++) {
					
				for (int x = 0; x < tempList.size(); x++) {
					double number = tempList.get(x).get(i);
					total += number;
				}
				countList.add(total);
				total = 0.0;
			}
		}
		return countList;
	}
	
	/**
	 * 
	 * takes each list of surfaces and outputs an array of 
	 * values.  Each one of those arrays are added for each [i] position.  All values that 
	 * are at [i] position are added an placed in another array.   That  array is created and 
	 * returned. 
	 * @return arraylist of doubles.
	 */
	private ArrayList<Double> CreateSaturatedTotal(){

		double total = 0.0;
		//holds array of arrays of inviduals surface runoffs
		ArrayList<ArrayList<Double>> tempList = new ArrayList<>();
		//holds totals for added runoff values at i
		ArrayList<Double> countList = new ArrayList<>();
		//break if there is nothing in the array
		if (SaturatedList.size() != 0) {
			//create arraylist of double arrays
			for(int i = 0; i < SaturatedList.size(); i++) {
				ArrayList<Double> list = new ArrayList<>();
				list = SaturatedList.get(i).runoff();
				tempList.add(new ArrayList<Double>(list));
			}
			//takes size of array[i] and goes thru each array at a given index.  the
			//values are added and added to countList.  The countList[i] will have the totals an 
			//index[i] all arrays.
			for(int i = 0; i < tempList.get(0).size(); i++) {
					
				for (int x = 0; x < tempList.size(); x++) {
					double number = tempList.get(x).get(i);
					total += number;
				}
				countList.add(total);
				total = 0.0;
			}
		}
		return countList;
	}
	/**
	 *  Creates a list of total amount of runoff.
	 * @return arraylist of doubles.  
	 */
	private ArrayList<Double> CreateTotalList(){
		int arraySize = CheckForEmpty();
		ArrayList<Double> countList = new ArrayList<>();
		for(int i = 0; i < arraySize; i++) {
			double total = TotalRunoffPermList.get(i) + TotalRunoffSatList.get(i) + TotalRunoffImpervList.get(i);
			countList.add(total);
		}
		
		return countList;
	}

	/**
	 * Adds Pervious surface to PermeableList
	 * @param pSurface
	 */
	public void AddPermeableList(Pervious pSurface){
		PermeableList.add(pSurface);
		
	}
	/**
	 * Adds Saturated surface to SaturatedList
	 * @param sSurface
	 */
	public void AddSaturatedList(Saturated sSurface){
		SaturatedList.add(sSurface);
		
	}
	/**
	 * Adds Impervious surface to ImperviousList
	 * @param iSurface
	 */
	public void AddImperviousList(Impervious iSurface){
		ImperviousList.add(iSurface);
		
	}
	
	
}
