package com.ldcc;
/* Author Larry Dellblad
 * October 2013
 *
 * sudo java -classpath .:classes:/opt/pi4j/lib/'*' ElmatareApp
 *
 */
 
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.ldcc.MyLogger;

public class ElmatareApp {
	private static int numTicks = 0;
	private static int i = 0;
	private static ElmatarSender sender;
	private static Path tempFile;
	private static int tenMinuteCounter = 0;	//10 minutes = 30*20 = 600 seconds
	private static int tenMinuteTicks = 0;
	private final static Logger LOGGER = Logger.getLogger(ElmatareApp.class.getName());

	public static void main (String args[]) throws InterruptedException {
		
		try {
		      MyLogger.setup(LOGGER);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Problems with creating the log files");
		}

		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalInput elmatPT = gpio.provisionDigitalInputPin(RaspiPin.GPIO_16, PinPullResistance.OFF);
		sender = new ElmatarSender();
		elmatPT.addListener(new ElmatarListener());
		tempFile = Paths.get("/sys/bus/w1/devices/10-0008028ff7a3/w1_slave");
		LOGGER.info("Starting log file");

		for (;;) {
			//System.out.println("Elmatare App "+numTicks);
			(new Thread(new Sender())).start();
			Thread.sleep(20000);
		}
	/*	System.out.println("Elmatare App");
		
		try (BufferedReader reader = Files.newBufferedReader(tempFile,Charset.defaultCharset())) {
			String lineFromFile = "";
			while ((lineFromFile = reader.readLine()) != null) {
				if (lineFromFile.contains("t=")) {
					String[] temp = lineFromFile.split("t=");
					if (temp[1].length()>3) {
						StringBuilder result = new StringBuilder(temp[1]).insert(temp[1].length()-3,".");
						System.out.println("Temp is: "+result.toString());
					}
					
				}
			}
		}catch(IOException exception) {
			System.out.println("error while reading file");
		}*/
		
	}
	public static class ElmatarListener implements GpioPinListenerDigital {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				if (event.getState().isLow()) {
					numTicks++;
//					System.out.println(" --> GPIO STATE: "+event.getPin()+" = "+event.getState());
					//System.out.println("Ticks"+numTicks);
				}
				 
			}
	}
	
	public static class Sender implements Runnable {
		public void run() {
			tenMinuteTicks += numTicks;
			if (tenMinuteCounter >= 30) {
				String temperature = "ffff";
				try (BufferedReader reader = Files.newBufferedReader(tempFile,Charset.defaultCharset())) {
					String lineFromFile = "";
					while ((lineFromFile = reader.readLine()) != null) {
						if (lineFromFile.contains("t=")) {
							String[] temp = lineFromFile.split("t=");
							if (temp[1].length()>3) {
								StringBuilder result = new StringBuilder(temp[1]).insert(temp[1].length()-3,".");
								temperature = temp[1];
							} else {
								LOGGER.severe("Error while reading temp"+temp[1]);
							}
							
						} 
					}
				}catch(Exception exception) {
					LOGGER.severe("Error while reading file"+exception.getMessage());
				}
				try {
					if (temperature.equals("ffff")) {
						LOGGER.severe("Error while reading temperature file");
						temperature = "0000";
					}
					sender.sendPost(tenMinuteTicks, temperature);
					tenMinuteTicks = 0;
				} catch (Exception e) { LOGGER.severe(e.getMessage());}
				tenMinuteCounter = 0;
			} else {
				try {
					sender.sendCurrent(numTicks);
				} catch (Exception e) { LOGGER.severe(e.getMessage());}
			}
			numTicks = 0;
			tenMinuteCounter++;
		}
	}
}

