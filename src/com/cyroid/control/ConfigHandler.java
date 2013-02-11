package com.cyroid.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Scanner;

import android.util.Log;

import com.cyroid.data.CyroidData;

/**
 * 
 * @author William Tran Class to handle the config file. Such as generating a
 *         config file if it is the first time Parsing the config file to check
 *         last updated
 * 
 */
public class ConfigHandler {

	/**
	 * Create a config file.
	 */
	public static void generateConfig() {
		File configFile = new File("data/data/com.cyroid/CyroidConfig.cfg");

		if (!configFile.exists()) {

			try {
				configFile.createNewFile();

				PrintWriter writer = new PrintWriter(configFile);
				writer.println("Update Interval");

				/*
				 * 0 - 3 months 1 - 6 months 3 - 1 year DEFAULTS TO 1
				 */
				writer.println("1");
				writer.println("\n");
				writer.println("LastUpdated");
				writer.println(System.currentTimeMillis());
				writer.println("\n");
				writer.println("Radius");
				writer.println("2000");

				writer.close();

			} catch (IOException e) {
				//e.printStackTrace();
				Log.v("JJE_ERROR", "Failed  at generateConfig in ConfigHandler: "+e.getMessage());
			}
		}
	}

	public static void parseConfig(CyroidData Data) {

		Scanner input;
		try {
			input = new Scanner(new FileInputStream(new File(
					"data/data/com.cyroid/CyroidConfig.cfg")));

			String tempLine;

			int tempInterval = 1;
			Calendar tempCalendar = null;
			int radius = 2000;
			while (input.hasNextLine()) {

				tempLine = input.nextLine();

				if (tempLine.toUpperCase().contains("UPDATE INTERVAL")) {

					Data.getConfig().setUpdateInterval(
							Integer.parseInt(input.nextLine().trim()));
					Log.v("PARSECONFIG", "In Update Interval VALUE : "
							+ tempInterval);

				} else if (tempLine.toUpperCase().contains("LASTUPDATED")) {

					tempCalendar = Calendar.getInstance();
					tempCalendar.setTimeInMillis(Long.parseLong(input
							.nextLine().trim()));
					Data.getConfig().setLastUpdated(tempCalendar);

					Log.v("parseConfig",
							"last Updated " + tempCalendar.getTimeInMillis());

				}  else if (tempLine.toUpperCase().contains("RADIUS")) {

					radius = Integer.parseInt(input.nextLine().trim());
					Log.v("parseConfig", "RADIUS " + radius);
					Data.getConfig().setRadius(radius);
				}

			}
			input.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.v("JJE_ERROR", "Failed  at parseConfig in ConfigHandler: "+e.getMessage());
		}

	}

	public static boolean configFileExists() {
		File configFile = new File("data/data/com.cyroid/CyroidConfig.cfg");
		if (configFile.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void deleteConfigFile(){
		File configFile = new File("data/data/com.cyroid/CyroidConfig.cfg");
		if (configFile.exists()){
			configFile.delete();
		}
	}

	/**
	 * Update the config with the given CyroidData.
	 * @param data
	 */
	public static void updateConfig(CyroidData data) {

		File configFile = new File("data/data/com.cyroid/CyroidConfig.cfg");

		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileOutputStream(configFile));

			writer.println("Update Interval");

			/*
			 * 0 - 3 months 1 - 6 months 3 - 1 year DEFAULTS TO 1
			 */
			writer.println(data.getConfig().getUpdateInterval());
			writer.println("\n");
			writer.println("Last Updated");
			writer.println(System.currentTimeMillis());
			writer.println("\n");
			writer.println("Radius");
			writer.println(data.getConfig().getRadius());

			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.v("JJE_ERROR", "Failed at updateConfig in ConfigHandler: "+e.getMessage());
			
		}

	}

}
