package surfaces;

import java.util.ArrayList;
import java.lang.Math;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/** Saturated.java This is the saturated subclass for SWS
 * @author <a href="mailto:krb14@hood.edu">Kevin R Bryant</a>
 * Date: 8/10/2017
 * @version 1.0g Beta 
*/

/** Saturated is a subclass of Surface
 * <br>
 * <br>
 * iflRate: Infiltration rate of the surface in inches per min<br>
 * RoffCoef: For saturated this value is .9 since saturated does not absorb much<br>
 * ArrayList: ArrayList of doubles to hold the simulation results<br>
 * WSSHEAT: The is the specific heat of wet soil expressed in j/kg C<br>
 * SMASS: Mass of 1 cubic foot of wet soil expressed in kg<br>
 * TYPE: Interger value assigned to a surface which is used for the GUI<br>
 *
*/


public class Saturated extends Surface
{

	private double iflRate;				
	private double RoffCoef = .9;
	private final ArrayList<Double> arrayList = new ArrayList<>();
	public static final int WSSHEAT = 1480;			
	public static final double SMASS = 35.3802;
	public static final int TYPE = 3;

	public Saturated (String name, double erate, double wtdepth, int lnth, int wdth,  double rrate, int sdura, double stemp, double rtemp, double irate) {
		super(name, erate, wtdepth, lnth, wdth, rrate, sdura, stemp, rtemp);
		setIflRate(irate, stemp);
	}	

	//set in inches/min
	/** Sets the infiltration rate and Runoff Coefficient which varies based upon the infiltration rate
	* @param irate which is the infiltration rate for a surface
	* @param stemp which is the temperature of a surface
	*/
	public void setIflRate(double irate, double stemp) {

		//Adjust the Runoff Coefficient based on infiltration rate 
		//and also the fact that its saturated 
		if (irate >= .2 && irate < .3) {
			iflRate = irate / 3;
		} else if (irate >= .3 && irate < .8) {
			iflRate = irate / 3;
		} else if (irate >= .8 && irate < 1.3) {
			iflRate = irate / 3;
		} else if (stemp <= 0) {    //ground is frozen
			iflRate = 0;
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
	* @return iflRate in inches/min	
	*/
	@Override
	public double getIflRate() {
		//convert back to what was entered
		double iflrate = Math.round(iflRate * 3 * 100.0) / 100.0;
		return iflrate;
	}

	//get the array list
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
		double mc_soil = massSoil * WSSHEAT; //neg, but gets added to RHS
		double ti_soil = mc_soil * soilt; //neg * neg is positive

		//arrange terms
		double t_final = (ti_wtr + ti_soil) / (mc_soil + mc_wtr);

		return t_final;	

	}
		

	@Override
	public ArrayList<Double> runoff() {
		
		ArrayList<Double> satList = getArrayList();
		int sdura = getStrmDuration();
		double rrate = getRnRate();
		double tsqft = getSqFeet();
		double gpm; 	//gallons per minute
		double tgal = 0;	//total gallons	

		//96.23 is a conversion factor for a flow rate in gallons per minute
		gpm = (RoffCoef * rrate * tsqft) / 96.23;

		satList.add(tgal);	//start of storm
		for (int i=1; i<sdura+1; ++i) {
			tgal = gpm * i;
			satList.add(tgal);
		}

		return(satList);
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

	@Override
	public void genCSV(String fname) {
		
		BufferedWriter writer = null;
		FileWriter fw = null;
		
		ArrayList<Double> satList = getArrayList();

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
			int size = satList.size();
			for (int i=0; i<size; ++i) {
				if (i == size-1) {
					writer.append(satList.get(i).toString()).append(System.lineSeparator());
				} else {
					writer.append(satList.get(i).toString()).append(',');
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
