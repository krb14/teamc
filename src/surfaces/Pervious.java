package surfaces;

import java.util.ArrayList;
import java.lang.Math;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


/** Pervious.java This is the pervious subclass for the SWS project
 * @author <a href="mailto:krb14@hood.edu">Kevin R Bryant</a> 
 * Date : 8/10/2017
 * @version 1.0g Beta 
*/

/** Pervious is the subclass of Surface
 * <br>
 * <br>
 * iflRate: Infiltration rate of the surface in inches per min<br>
 * RoffCoef: Runoff Coefficient which varies based upon the infiltration rate<br>
 * ArrayList: ArrayList of doubles to hold the simulation results<br>
 * TYPE: Interger value assigned to a surface which is used for the GUI<br>
 * DSSHEAT: Specific heat of dry soil in j/kg C<br>
 * SMASS: Mass of 1 cubic foot of dry soil expressed in kg<br>
 *
*/

public class Pervious extends Surface
{

	private double iflRate;	
	private double RoffCoef;
	private final ArrayList<Double> arrayList = new ArrayList<>();

	public static final int TYPE = 1;		
	public static final int DSSHEAT = 800;				
	public static final double SMASS = 34.473;

	public Pervious (String name, double erate, double wtdepth, int lnth, int wdth,  double rrate, int sdura, double stemp, double rtemp, double irate) {
		super(name, erate, wtdepth, lnth, wdth, rrate, sdura, stemp, rtemp);
		setIflRate(irate, stemp);
	}
	

	//set in inches/min
	/** Sets the infiltration rate and Runoff Coefficient which varies based upon the infiltration rate
	* @param irate which is the infiltration rate for a surface
	* @param stemp which is the temperature of a surface
	*/
	public void setIflRate(double irate, double stemp) {
	
		if (stemp <= 0) {
			iflRate = 0;		//Ground is frozen
		} else {	
			iflRate = irate; //Set the infiltration rate
		}
	
		//Adjust the Runoff Coefficient based on infiltration rate	
		if (irate >= .2 && irate < .3) {
			RoffCoef = .6;
		} else if (irate >= .3 && irate < .8) {
			RoffCoef = .45;
		} else if (irate >= .8 && irate < 1.3) {
			RoffCoef = .35;
		} else if (stemp <= 0) {		//ground is frozen
			RoffCoef = 1;
		}
		
	}

  //get the surface type [used for GUI]
  /** Get the type of surface
  * @return TYPE
  */
	@Override
  public int getType() {
		return TYPE;
  }

	@Override
	//Not implemented in this subclass
	public int getSType() {
		return 0;
	}

	//get in inches/min
	/** Get the infiltration rate and return in inches/min
	*	@return iflRate in inches/min
	*/
	@Override
	public double getIflRate() {
		return iflRate;
	}

	//get the array list (for internal use only)
	/** Get the ArrayList
	* @return ArrayList of results
	*/
	public ArrayList<Double> getArrayList() {
		return arrayList;
	}

	/** Calculate the final surface and rain temperature
	* @return t_final which utilizes q = mcDeltaT
	*/
	@Override
	public double finalTemp() {
		double wtdepth = getwTblDpth();	//in feet
		int size = getSqFeet();					//in feet
		double tinfil = infiltration(); //total water infiltrated

		double soilt = getSrfcTemp(); 	//soil temp in C
		double watert = getRnTemp(); 		//rain temp in C

		double massSoil = size * wtdepth * SMASS; //mass of all the soil in kg
		double massWtr = tinfil * WMASS; 					//mass of all the water infiltrated in kg

		//Solve for -q(soil) = q(rain)
		//-(mc delt T) = (mc delt T)
		//-(mc (Tf - Ti)) = mc(Tf - Ti)

		//rain side (RHS)
		double mc_wtr = massWtr * WSHEAT;
		double ti_wtr = mc_wtr * watert;  //neg, but gets added to LHS
	
		//surface side (LHS)
		double mc_soil = massSoil * DSSHEAT; //neg, but gets added to RHS
		double ti_soil = mc_soil * soilt; //neg * neg is positive

		//arrange terms
		double t_final = (ti_wtr + ti_soil) / (mc_soil + mc_wtr);

		return t_final;	

	}
		

	@Override
	public ArrayList<Double> runoff() {
		
		ArrayList<Double> prvsList = getArrayList();
		int sdura = getStrmDuration();
		double rrate = getRnRate();
		double tsqft = getSqFeet();
		double gpm; 	//gallons per minute
		double tgal = 0;	//total gallons	

		//96.23 is a conversion factor for a flow rate in gallons per minute
		gpm = (RoffCoef * rrate * tsqft) / 96.23;

		prvsList.add(tgal);	//start of storm
		for (int i=1; i<sdura+1; ++i) {
			tgal = gpm * i;
			prvsList.add(tgal);
		}
		

		return(prvsList);
	}

	@Override
	public double infiltration() {
		
		double siflRate = getIflRate() / 12; 			//Convert to feet/hr
		double sdura = getStrmDuration() / 60;	 	//Convert to hours
		double tsqft = getSqFeet();								//get surface square feet
		double evaploss = evaporation();					//need to subtract off evaporation loss

		double totalInfil;	

		if ( Math.signum(siflRate) == 0) {
			totalInfil = 0;
		}
		else {
			totalInfil = ((tsqft * siflRate * sdura) / GSFT) - evaploss;
		}
	
		return totalInfil;

	}

	//This writer generates the entire configuration of a surface object
	@Override
	public void genCCSV(String fname) {

		BufferedWriter writer = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(fname, true);
			writer = new BufferedWriter(fw);
			/* <---  Write the simulation parms ---> */
			writer.append(getName()).append(',');
			writer.append(String.valueOf(getType())).append(',');
			writer.append(String.valueOf(getEvRateInMo())).append(',');
			writer.append(String.valueOf(getwTblDpth())).append(',');
			writer.append(String.valueOf(getLngth())).append(',');
			writer.append(String.valueOf(getWdth())).append(',');
			writer.append(String.valueOf(getRnRate())).append(',');
			writer.append(String.valueOf(getStrmDuration())).append(',');
			writer.append(String.valueOf(getSrfcTemp())).append(',');
			writer.append(String.valueOf(getRnTemp())).append(',');
			writer.append(String.valueOf(getIflRate())).append(System.lineSeparator());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				}
			}

		}


	//This writer generates the configuration & data of a surface object
	@Override
	public void genCSV(String fname) {
		
		BufferedWriter writer = null;
		FileWriter fw = null;
		
		ArrayList<Double> prvsList = getArrayList();

		try {
			fw = new FileWriter(fname);
			writer = new BufferedWriter(fw);
			/* <---  Write the simulation parms ---> */
			writer.append(getName()).append(',');
			writer.append(String.valueOf(getType())).append(',');
			writer.append(String.valueOf(getEvRateInMo())).append(',');
			writer.append(String.valueOf(getwTblDpth())).append(',');
			writer.append(String.valueOf(getLngth())).append(',');
			writer.append(String.valueOf(getWdth())).append(',');
			writer.append(String.valueOf(getRnRate())).append(',');
			writer.append(String.valueOf(getStrmDuration())).append(',');
			writer.append(String.valueOf(getSrfcTemp())).append(',');
			writer.append(String.valueOf(getRnTemp())).append(',');
			writer.append(String.valueOf(getIflRate())).append(System.lineSeparator());
			
			/* <---  Write the simulation results ---> */
			writer.append(String.valueOf(infiltration())).append(',');
			writer.append(String.valueOf(finalTemp())).append(',');
			writer.append(String.valueOf(evaporation())).append(System.lineSeparator());

			/* <---  Write the array data ---> */
			int size = prvsList.size();
			for (int i=0; i<size; ++i) {
				if (i == size-1) {
					writer.append(prvsList.get(i).toString()).append(System.lineSeparator());
				} else {
					writer.append(prvsList.get(i).toString()).append(',');
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
				}
			}

		}

}
