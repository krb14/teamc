
package application;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;
import surfaces.Impervious;
import surfaces.Pervious;
import surfaces.Saturated;
import surfaces.Surface;


/**
 * Date: 12/10/17
 * This is an export class for the .  It takes an the output from the PermeableList, SaturatedList, and
 *  ImperviousList.  The output is organized and output to .pdf.  The output is applicable only to the 
 *  last output of the simulation.  Application uses ITEXT(https://itextpdf.com) as a framework for 
 *  outputting from java to pdf.   
 * @author C. Warren Hammock
 * @version 3.0
 *
 */
public class Export {

	public Export(ArrayList<Surface> masterList, Simulation simulation) throws DocumentException {
		/**
		 * Get a random naming convention for the file.  
		 */
		Random random = new Random();
		int randNumber = random.nextInt(100000);
		File file = new File("SWS" + randNumber + ".pdf");
		
		/**
		 * Create a new itext document to be populated.
		 */
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				 				 
		document.open();
		String title = "STORM WATER SIMULATION";
		/**
		 * Creates document and then an anchor and then a paragraph to build off.  works similar to javafx
		 */
		Anchor anchorTarget = new Anchor(title, 
				FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, new CMYKColor(100, 255, 150, 0)));
	    anchorTarget.setName("BackToTop");
	    Paragraph paragraph1 = new Paragraph();
	    paragraph1.setSpacingBefore(50);
	    paragraph1.add(anchorTarget);
	    try {
			document.add(paragraph1);
			document.add(new Paragraph(System.lineSeparator()));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    /**
	     * Adds title and date to the ouput
	     */
	    document.add(new Paragraph("Simulation On Date: " + new Date(), 
				FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, new CMYKColor(100, 255, 150, 0))));
	    document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph(System.lineSeparator()));
	    document.add(new Paragraph("INDIVIDUAL SURFACE TEMPERATURE VALUES",
	    			FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, new CMYKColor(0, 0, 0, 255))));
	    document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph("TOTAL TIME OF SIMUALTION IN MINUTES: " + Integer.toString(masterList.get(0).getStrmDuration())));
		/**
		 * Goes thru every value in the masterList.  For each surface, it is determined which kind of surface it is.  Dependent upon which surface it is, 
		 * a predefined template is populated and output to the pdf.
		 */
		if (masterList.size() > 0) {
			for (Surface element : masterList) {
				if(element.getType() == 1) {
					Pervious pervElement = (Pervious)element;
					document.add(new Paragraph("Surface Name: " + pervElement.getName() + " "
							+ System.lineSeparator()  
							+ "Length: " + pervElement.getLngth() + " "
							+ System.lineSeparator()  
							+ "Width: " + pervElement.getWdth() + " " 
							+ System.lineSeparator()  
							+ "Rain Rate: " + pervElement.getRnRate() + " " 
							+ System.lineSeparator()  
							+ "Storm Duration: " + pervElement.getStrmDuration() + " "
							+ System.lineSeparator()  
							+ "Surface Temp: " + pervElement.getSrfcTemp() + " "
							+ System.lineSeparator()  
							+ "Rain Temp: " + pervElement.getRnTemp() + " "  
							+ System.lineSeparator()  
							+ "Infiltration: " + pervElement.infiltration() + " "
							+ System.lineSeparator()  
							+ "Final Temp: " + pervElement.finalTemp() + " "
							+ System.lineSeparator()  
							+ "Evaporation: " + pervElement.evaporation() + " " + 
							System.lineSeparator() + 
							"Individual Runoff Array Per Minute: " + pervElement.getArrayList() +
							System.lineSeparator() + 
							System.lineSeparator()
							));
				}
				if(element.getType() == 2) {
					Impervious impervElement = (Impervious)element;
					document.add(new Paragraph("Surface Name: " + impervElement.getName() + " "
							+ System.lineSeparator()  
							+ "Length: " + impervElement.getLngth() + " "
							+ System.lineSeparator()  
							+ "Width: " + impervElement.getWdth() + " " 
							+ System.lineSeparator()  
							+ "Rain Rate: " + impervElement.getRnRate() + " " 
							+ System.lineSeparator()  
							+ "Storm Duration: " + impervElement.getStrmDuration() + " "
							+ System.lineSeparator()  
							+ "Surface Temp: " + impervElement.getSrfcTemp() + " "
							+ System.lineSeparator()  
							+ "Rain Temp: " + impervElement.getRnTemp() + " "  
							+ System.lineSeparator()  
							+ "Infiltration: " + impervElement.infiltration() + " "
							+ System.lineSeparator()  
							+ "Final Temp: " + impervElement.finalTemp() + " "
							+ System.lineSeparator()  
							+ "Evaporation: " + impervElement.evaporation() + " " + 
							System.lineSeparator() + 
							"Individual Runoff Array Per Minute: " + impervElement.getArrayList() +
							System.lineSeparator() + 
							System.lineSeparator()
							));
				}
				if(element.getType() == 3) {
					Saturated satElement = (Saturated)element;
					document.add(new Paragraph("Surface Name: " + satElement.getName() + " "
							+ System.lineSeparator()  
							+ "Length: " + satElement.getLngth() + " "
							+ System.lineSeparator()  
							+ "Width: " + satElement.getWdth() + " " 
							+ System.lineSeparator()  
							+ "Rain Rate: " + satElement.getRnRate() + " " 
							+ System.lineSeparator()  
							+ "Storm Duration: " + satElement.getStrmDuration() + " "
							+ System.lineSeparator()  
							+ "Surface Temp: " + satElement.getSrfcTemp() + " "
							+ System.lineSeparator()  
							+ "Rain Temp: " + satElement.getRnTemp() + " "  
							+ System.lineSeparator()  
							+ "Infiltration: " + satElement.infiltration() + " "
							+ System.lineSeparator()  
							+ "Final Temp: " + satElement.finalTemp() + " "
							+ System.lineSeparator()  
							+ "Evaporation: " + satElement.evaporation() + " " + 
							System.lineSeparator() + 
							"Individual Runoff Array Per Minute: " + satElement.getArrayList() +
							System.lineSeparator() + 
							System.lineSeparator()
							));
				}
			}
		document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph(System.lineSeparator()));
		
		document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph(System.lineSeparator()));
		/**
		 * Title block for final output temperatures. 
		 */
		document.add(new Paragraph("FINAL TEMPERATURE VALUES",
				FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, new CMYKColor(150, 150, 150, 200))));
		document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph(System.lineSeparator()));
		/**
		 * Gets the values from the total output and populates them in a predefined template form.  
		 */
		if (simulation.TotalList.size() > 0) {
				document.add(new Paragraph("Total Runoff: " + simulation.TotalList + " "
							+ System.lineSeparator()  
							+ "Total Permeable Runoff: " + simulation.TotalRunoffPermList + " "
							+ System.lineSeparator()  
							+ "Total Saturated Runoff: " + simulation.TotalRunoffSatList + " " 
							+ System.lineSeparator()  
							+ "Total Impervious Runoff: " + simulation.TotalRunoffImpervList + " " 
							+ System.lineSeparator()  
							+ "Final Rain Temp: "  + Double.toString(simulation.FinalRainTemp(masterList)) + " "
							+ System.lineSeparator()  
							+ "Rain Lost To Evaporation: "  + Double.toString(simulation.RainLostToEvaporation(masterList)) + " "
							+ System.lineSeparator()  
							+ "Total Rain Fall Amount: " + Double.toString(simulation.GetTotalRain()) + " "
							+ System.lineSeparator()  
							+ "Total Runoff For All: " + Double.toString(simulation.GetTotalRunoff())  + " " + 
							System.lineSeparator() + 
							System.lineSeparator() +
							"pdf generation brough to you by ITEXTPDF.com"
							));
		}	 
		document.add(new Paragraph(System.lineSeparator()));
		document.add(new Paragraph(System.lineSeparator()));	
		}
	    document.close();		 
	    }
}
	
	

