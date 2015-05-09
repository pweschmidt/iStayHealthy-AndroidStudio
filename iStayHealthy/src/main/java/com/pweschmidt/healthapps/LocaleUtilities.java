package com.pweschmidt.healthapps;

public class LocaleUtilities 
{
	public static final int AMERICAS = 0x1;
	public static final int EU = 0x2;
	public static final int ROW = 0x3;
	
	
	public static int getRegionFromISO3Code(String isoLocale)
	{
		int region = LocaleUtilities.ROW;
		if(isoLocale.equals("USA") || 
				isoLocale.equals("CAN") || 
				isoLocale.equals("MEX") ||
				isoLocale.equals("DOM") || isoLocale.equals("PRI") || isoLocale.equals("ECU") ||
				isoLocale.equals("SLV") || isoLocale.equals("PER") || isoLocale.equals("PRY") ||
				isoLocale.equals("VEN") || isoLocale.equals("GTM") || isoLocale.equals("ARG") ||
				isoLocale.equals("BRA") || isoLocale.equals("CHL") || isoLocale.equals("BOL") ||
				isoLocale.equals("BRB") || isoLocale.equals("JAM") || isoLocale.equals("HTI") || 
				isoLocale.equals("GNB") || isoLocale.equals("GRD") || isoLocale.equals("ASM") ||
				isoLocale.equals("CYM") || isoLocale.equals("NIC") || isoLocale.equals("VIR") ||
				isoLocale.equals("GRL"))
		{
			region = LocaleUtilities.AMERICAS;
		}
		else if (isoLocale.equals("GBR") || 
				isoLocale.equals("DEU") ||
				isoLocale.equals("AUT") || isoLocale.equals("BEL") || isoLocale.equals("FRA")
				|| isoLocale.equals("NLD") || isoLocale.equals("PRT") || isoLocale.equals("CHE") ||
				isoLocale.equals("LIE") || isoLocale.equals("ESP") || isoLocale.equals("LUX") || isoLocale.equals("IRL") ||
				isoLocale.equals("ITA") || isoLocale.equals("DNK") || isoLocale.equals("NOR") || 
				isoLocale.equals("SWE") || isoLocale.equals("FIN") || isoLocale.equals("POL") || 
				isoLocale.equals("SVN") || isoLocale.equals("SVK") || isoLocale.equals("CZE") || 
				isoLocale.equals("GRC") || isoLocale.equals("HRV") || isoLocale.equals("HUN") ||
				isoLocale.equals("BGR") || isoLocale.equals("ROU") || isoLocale.equals("SRB") ||
				isoLocale.equals("BIH") || isoLocale.equals("ALB") || isoLocale.equals("MCO") ||
				isoLocale.equals("MNE") || isoLocale.equals("GIB") || isoLocale.equals("CYP") ||
				isoLocale.equals("MLT") || isoLocale.equals("VGB") || isoLocale.equals("GGY") ||
				isoLocale.equals("JEY") || isoLocale.equals("ISL"))
		{
			region = LocaleUtilities.EU;
		}
		
		
		
		return region;
	}

}
