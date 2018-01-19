package surfaces;

import java.util.ArrayList;
import java.lang.Math;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/** Impervious.java This is the impervious subclass for the SWS project
 * @author <a href="mailto:krb14@hood.edu">Kevin R Bryant</a>
 * Date 8/10/2017
 * @version 1.0g Beta
*/

/** Impervious is a subclass of Surface
 * <br>
 * <br>
 * RoffCoef: For impervious this value is 1 since impervious absorbs nothing<br>
 * SType: This is the type of surface we are working with (10:alum_root, 11:asphalt_root, 20:asphalt_ground, 21:concrete_ground)<br>
 * SSHEAT: Specific heat of the given surface which will vary based on the type of surface<br>
 * SDepth: The depth of the surface we are working with which will vary based on the type of surface<br>
 * SMass: The mass of the specific surface which will vary based on the type of surface<br>
 * ArrayList: ArrayList of doubles to hold the simulation results<br>
 * TYPE: Interger value assigned to a surface which is used for the GUI<br>
 *
*/

public class Impervious extends Surface
{

	private double RoffCoef = 1.0;
	private int SType;				
	private double SSHeat;	
	private double SDepth;	
	private double SMass;
	private final ArrayList<Double> arrayList = new ArrayList<>();

	private static final int TYPE = 2;	


	public Impervious (String name, double erate, int lnth, int wdth,  double rrate, int sdura, double stemp, double rtemp, int stype ) {
		super(name, erate, lnth, wdth, rrate, sdura, stemp, rtemp);
		setSType(stype);
	}	

	//setup the various values for roof/ground and type of surface
	/** Set the surface type and its associated values
	*	@param stype which is the type of surface
	*/
	public void setSType(int stype) {
		
		switch (stype) {
			case 10: SSHeat = 897;		//Specific heat of aluminum
							 SDepth = .0833;	//Roof thickness in feet
							 SMass = 76.6571;	//1 kg of aluminum is this per cubic foot
							 SType = 10;
							 break;
			case 11: SSHeat = 920;		//Specific heat of asphalt (roof)
							 SDepth = .0833; 	//Roof thickness in feet
							 SMass = 65.7709;	//1 kg of asphalt is this per cubic foot
							 SType = 11;
							 break;
			case 20: SSHeat = 920;		//Specific heat of asphalt (ground)
							 SDepth = .4167;	//asphalt/concrete thickness in feet
							 SMass = 65.7709;	//1 kg of asphalt is this per cubic foot
							 SType = 20;
							 break;
			case 21: SSHeat = 880;		//Specific heat of concrete
							 SDepth = .4167;	//asphalt/concrete thickness in feet
							 SMass = 68.0389;	//1 kg of concrete is this per cubic foot
							 SType = 21;
							 break;
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

	//Get the surface type
	/** Get the type of surface
	*	@return SType which is an integer value of 10,11,20 or 21
	*/
	@Override
	public int getSType() {
		return SType;
	}

	@Override
	//Not implemented in this subclass
	public double getIflRate() {
		return 0.0;
	}

	//Get the Specific heat of the surface
	/** Get the specific heat of the surface
	*	@return SSHeat which is the specific heat in j/kg C
	*/
	public double getSSHeat() {
		return SSHeat;
	}

	//Get the Surface depth
	/** Get the depth of the surface
	*	@return SDepth which is the surface depth expressed in feet
	*/
	public double getSDepth() {
		return SDepth;
	}

	//Get the Surface mass
	/** Get the mass of the surface
	*	@return SMass which is expressed in kg per cubic foot
	*/
	public double getSMass() {
		return SMass;
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

		//surface details
		double sdepth = getSDepth();		//in feet
		double smass = getSMass();			//in kg
		double ssheat = getSSHeat();		//in j per kg
		double srfct = getSrfcTemp();		//Surface temp in C
		int size = getSqFeet();					//in feet

		double rrate = getRnRate();			//rate in inches/min
		int sdura = getStrmDuration();	//duration of the storm in minutes
		double train = ((RoffCoef * rrate * size) / 96.23) * sdura;	//total gallons of rain in this surface area

		double watert = getRnTemp(); 		//rain temp in C

		double massSrfc = size * sdepth * smass; //mass of the surfacein kg
		double massWtr = train * WMASS; 				 //mass of all the water rained in kg

		//Solve for -q(soil) = q(rain)
		//-(mc delt T) = (mc delt T)
		//-(mc (Tf - Ti)) = mc(Tf - Ti)

		//rain side (RHS)
		double mc_wtr = massWtr * WSHEAT;
		double ti_wtr = mc_wtr * watert;  //neg, but gets added to LHS
	
		//surface side (LHS)
		double mc_srfc = massSrfc * ssheat; //neg, but gets added to RHS
		double ti_srfc = mc_srfc * srfct; //neg * neg is positive

		//arrange terms
		double t_final = (ti_wtr + ti_srfc) / (mc_srfc + mc_wtr);

		return t_final;	

	}
		

	@Override
	public ArrayList<Double> runoff() {
		
		ArrayList<Double> imprvsList = getArrayList();
		int sdura = getStrmDuration();
		double rrate = getRnRate();
		double tsqft = getSqFeet();
		double evrate = getEvRateInMin();
		double gpm; 	//gallons per minute
		double tgal = 0;	//total gallons	

		//96.23 is a conversion factor for a flow rate in gallons per minute
		gpm = (RoffCoef * rrate * tsqft) / 96.23;

		imprvsList.add(tgal);	//start of storm
		for (int i=1; i<sdura+1; ++i) {
			tgal = (gpm * i) - evrate;	//subtract off evaporation
			imprvsList.add(tgal);
		}
		

		return(imprvsList);
	}

	@Override
	public double infiltration() {
	
		//these surfaces infiltrate nothing	
		double totalInfil = 0;	
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
      writer.append(String.valueOf(getLngth())).append(',');
      writer.append(String.valueOf(getWdth())).append(',');
      writer.append(String.valueOf(getRnRate())).append(',');
      writer.append(String.valueOf(getStrmDuration())).append(',');
      writer.append(String.valueOf(getSrfcTemp())).append(',');
      writer.append(String.valueOf(getRnTemp())).append(',');
			writer.append(String.valueOf(getSType())).append(System.lineSeparator());
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
		
		ArrayList<Double> imprvsList = getArrayList();

		try {
			fw = new FileWriter(fname);
			writer = new BufferedWriter(fw);
			/* <---  Write the simulation parms ---> */
      writer.append(getName()).append(',');
      writer.append(String.valueOf(getType())).append(',');
			writer.append(String.valueOf(getEvRateInMo())).append(',');
			writer.append(String.valueOf(getLngth())).append(',');
			writer.append(String.valueOf(getWdth())).append(',');
			writer.append(String.valueOf(getRnRate())).append(',');
			writer.append(String.valueOf(getStrmDuration())).append(',');
			writer.append(String.valueOf(getSrfcTemp())).append(',');
			writer.append(String.valueOf(getRnTemp())).append(',');
			writer.append(String.valueOf(getSType())).append(System.lineSeparator());
			
			/* <---  Write the simulation results ---> */
			writer.append(String.valueOf(infiltration())).append(',');
			writer.append(String.valueOf(finalTemp())).append(',');
			writer.append(String.valueOf(evaporation())).append(System.lineSeparator());

			/* <---  Write the array data ---> */
			int size = imprvsList.size();
			for (int i=0; i<size; ++i) {
				if (i == size-1) {
					writer.append(imprvsList.get(i).toString()).append(System.lineSeparator());
				} else {
					writer.append(imprvsList.get(i).toString()).append(',');
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
