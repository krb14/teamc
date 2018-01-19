package surfaces;

import java.util.Calendar;
import java.util.ArrayList;


/** Surface.java - This is the abstract base class for the SWS project
 * @author <a href="mailto:krb14@hood.edu">Kevin R Bryant</a> 
 * Date : 8/10/2017
 * @version 1.0g Beta
*/

/** Surface is the abstract base class 
 *	<br>
 *	<br>
 * evRate:	evaporation rate in inches per minute<br>
 * wTblDpth:	water table depth in feet<br>
 * length:	length of a given surface in feet<br>
 * width: 	width of a given surface in feet<br>
 * sqFeet: 	calculated square footage of a given surface<br>
 * rnRate: 	rate of the storm in inches per hour<br>
 * strmDuration:	duration of the storm in minutes<br>
 * srfcTemp:	temperature of a given surface in Celcius<br>
 * rnTemp:	temperature of the rain in Celcius<br>
 * ACRE:	size of an acre in square feet<br>
 * GCFT:	size of a gallon in cubic feet<br>
 * WSHEAT:	specific heat of water in j/kg C<br>
 * WMASS: Mass of 1 gallon of water expressed in kg<br>
 *
*/
public abstract class Surface
{

	private String Name;
	private double evRate;	
	private double wTblDpth;	
	private int length;			
	private int width;			
	private int sqFeet;	
	private double rnRate;
	private int strmDuration;	
	private double srfcTemp;
	private double rnTemp;

	public static final int ACRE = 43560;
	public static final double GCFT = .133681;
	public static final double GSFT = .2614;
	public static final int WSHEAT = 4182;
	public static final double WMASS = 3.7854118;

		public Surface( String name, double erate, double wtdepth, int lnth, int wdth, double rrate, int sdura, double stemp, double rtemp ) {

			setName(name);
			setwTblDpth(wtdepth);
			setLngth(lnth);
			setWdth(wdth);
			setSqFeet(lnth,wdth);
			setRnRate(rrate);
			setStrmDuration(sdura);
			setSrfcTemp(stemp);
			setRnTemp(rtemp);

			Calendar date = Calendar.getInstance();
			int month = date.get(Calendar.MONTH);
		
			switch (month) {
				case 0: setEvRate(erate, 44640);	//Jan. custom rate & mins
								break;
				case 1: setEvRate(erate, 40320);	//Feb. custom rate & mins
								break;
				case 2: setEvRate(erate, 44640);	//March custom rate & mins
								break;
				case 3: setEvRate(erate, 43200);	//April custom rate & mins
								break;
				case 4: setEvRate(erate, 44640);	//May custom rate & mins
								break;
				case 5: setEvRate(erate, 43200);	//June custom rate & mins	
								break;
				case 6: setEvRate(erate, 44640);	//July custom rate & mins
								break;
				case 7: setEvRate(erate, 44640);	//Aug. custom rate & mins
								break;
				case 8: setEvRate(erate, 43200);	//Sept. custom rate & mins
								break;
				case 9: setEvRate(erate, 44640);	//Oct. custom rate & mins
								break;
				case 10: setEvRate(erate, 43200);	//Nov. custom rate & mins
							   break;
				case 11: setEvRate(erate, 44640);	//Dec. custom rate & mins
							 	 break;
			
			}
		}

		public Surface( String name, double erate, int lnth, int wdth, double rrate, int sdura, double stemp, double rtemp ) {
		
			setName(name);
			setLngth(lnth);
			setWdth(wdth);
			setSqFeet(lnth,wdth);
			setRnRate(rrate);
			setStrmDuration(sdura);
			setSrfcTemp(stemp);
			setRnTemp(rtemp);

			Calendar date = Calendar.getInstance();
			int month = date.get(Calendar.MONTH);
		
			switch (month) {
				case 0: setEvRate(erate, 44640);	//Jan. custom rate & mins
								break;
				case 1: setEvRate(erate, 40320);	//Feb. custom rate & mins
								break;
				case 2: setEvRate(erate, 44640);	//March custom rate & mins
								break;
				case 3: setEvRate(erate, 43200);	//April custom rate & mins
								break;
				case 4: setEvRate(erate, 44640);	//May custom rate & mins
								break;
				case 5: setEvRate(erate, 43200);	//June custom rate & mins	
								break;
				case 6: setEvRate(erate, 44640);	//July custom rate & mins
								break;
				case 7: setEvRate(erate, 44640);	//Aug. custom rate & mins
								break;
				case 8: setEvRate(erate, 43200);	//Sept. custom rate & mins
								break;
				case 9: setEvRate(erate, 44640);	//Oct. custom rate & mins
								break;
				case 10: setEvRate(erate, 43200);	//Nov. custom rate & mins
							   break;
				case 11: setEvRate(erate, 44640);	//Dec. custom rate & mins
							 	 break;
			
			}
		}

		//Set the name for a particular surface object [needed for GUI]
		private void setName( String name ) {
			Name = name;
		}

		//Get the name for a particular surface object [needed for GUI]
		/** Get the object name and return a string
 	  * @return object name
 	  */
		public String getName() {
			return Name;
		}

		//Set evaporation rate [set in inches/min]
		private void setEvRate( double erate, int nmin ) {
			evRate = erate / nmin;
		}

		//Get evaporation rate [return in inches/min]
		/** Get the evaporation rate and return in inches/min
 		*	@return evRate in inches/min
 		*/ 
		public double getEvRateInMin() {
			return evRate;
		}

		//Get evaporation rate [return in inches/month]
		/** Get the evaporation rate and return in inches/month
 		*	@return evRate in inches/month
 		*/
		public double getEvRateInMo() {
			Calendar date = Calendar.getInstance();
			int month = date.get(Calendar.MONTH);

			double erate = 0;

      switch (month) {
      	case 0: erate = evRate * 44640;  //Jan. custom rate & mins
                break;
        case 1: erate = evRate * 40320;  //Feb. custom rate & mins
                break;
        case 2: erate = evRate * 44640;  //March custom rate & mins
                break;
        case 3: erate = evRate * 43200;  //April custom rate & mins
                break;
        case 4: erate = evRate * 44640;  //May custom rate & mins
                break;
        case 5: erate = evRate * 43200;  //June custom rate & mins 
                break;
        case 6: erate = evRate * 44640;  //July custom rate & mins
                break;
        case 7: erate = evRate * 44640;  //Aug. custom rate & mins
                break;
        case 8: erate = evRate * 43200;  //Sept. custom rate & mins
                break;
        case 9: erate = evRate * 44640;  //Oct. custom rate & mins
                break;
        case 10: erate = evRate * 43200; //Nov. custom rate & mins
                 break;
        case 11: erate = evRate * 44640; //Dec. custom rate & mins
                 break;
       }

			erate = Math.round(erate * 100.0) / 100.0;	
			return erate;

		}

		//Set the watertable depth [set in feet]
		private void setwTblDpth( double wtdepth ) {
			wTblDpth = wtdepth;
		}

		//Get the watertable depth [return in feet]
		/** Get the watertable depth and return in feet
 		*	@return wTblDpth in feet
 		*/
		public double getwTblDpth() {
			return wTblDpth;
		}

		//Set the surface length
		private void setLngth(int lnth) {
			length = lnth;
		}

		//Get the surface length
    /** Get the length of a given surface and return in feet
 		* @return length in feet
		*/
		public int getLngth() {
			return length;
		}

		//Set the surface width
		private void setWdth( int wdth) {
			width = wdth;
		}

		//Get the surface width
		/** Get the width of a given surface and return in feet
		*	@return width in feet
		*/
		public int getWdth() {
			return width;
		}

		//Set the surface area
		private void setSqFeet( int lnth, int wdth ) {
			sqFeet = lnth * wdth;
		}

		//Get the surface area
		/** Get the square feet of a surface and return in feet
		*	@return sqFeet in feet
		*/	
		public int getSqFeet() {
			return sqFeet;
		}

		//Set the rate of rainfall
		private void setRnRate( double rate ) {
			rnRate = rate;
		}

		//Get the rate of rainfall
		/** Get the rate of rainfall for a storm and return in inches/hour
		*	@return rnRate in inches/hour
		*/
		public double getRnRate () {
			return rnRate;
		}

		//Set the storm duration	
		private void setStrmDuration( int duration ) {
			strmDuration = duration;
		}

		//Get the storm duration
		/** Get the duration of a given storm and return in minutes
		*	@return strmDuration in minutes
		*/
		public int getStrmDuration() {
			return strmDuration;
		}

		//Set the surface temperature
		private void setSrfcTemp( double temp ) {
			srfcTemp = temp;
		}

		//Get the surface temperature
		/** Get the temperature of a given surface and return in Celcius
		*	@return srfcTemp in Celcius
		*/
		public double getSrfcTemp() {
			return srfcTemp;
		}

		//Set the rain temperature
		private void setRnTemp( double temp) {
			rnTemp = temp;
		}

		//Get the rain temperature
		/** Get the temperature of rain for a given storm and return in Celcius
		*	@return rnTemp in Celcius
		*/
		public double getRnTemp() {
			return rnTemp;
		}
	

	public abstract ArrayList<Double> runoff();
	public abstract int getType();
	public abstract int getSType();
	public abstract double getIflRate();
	public abstract double infiltration();
	public abstract double finalTemp();
	public abstract void genCCSV(String fname);
	public abstract void genCSV(String fname);

	/** Calculates the evaporation total for a given surface
	* @return tolEv total evaporation (in gallons) for a given surface
	*/
	public double evaporation() {

		int sdura = getStrmDuration();
		int tsqft = getSqFeet();	
		
		double tolEv = (((getEvRateInMin() * sdura) / 12) * tsqft) / GSFT;
		return tolEv;

	}

	@Override
	public String toString() {
		return super.toString();
	}

}
