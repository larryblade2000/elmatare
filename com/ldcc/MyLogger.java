package com.ldcc;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
  static private FileHandler fileTxt;
  static private SimpleFormatter formatterTxt;

  static private FileHandler fileHTML;
  static private Formatter formatterHTML;

  static public void setup(Logger logger) throws IOException {

    // Get the global logger to configure it
    logger.setLevel(Level.INFO);
    fileTxt = new FileHandler("Logging.txt");
 //   fileHTML = new FileHandler("Logging.html");

    // create txt Formatter
    formatterTxt = new SimpleFormatter();
    fileTxt.setFormatter(formatterTxt);
    logger.addHandler(fileTxt);

    // create HTML Formatter
 //   formatterHTML = new MyHtmlFormatter();
 //   fileHTML.setFormatter(formatterHTML);
 //    logger.addHandler(fileHTML);
  }
}
 