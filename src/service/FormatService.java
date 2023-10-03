package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class FormatService {
  public  static LocalDateTime stringToLocalDate(String dateString){
// Define the format pattern that matches the input string
    String pattern = "yyyy-MM-dd HH:mm:ss"; // Must match the format of the input string

    // Create a DateTimeFormatter with the specified pattern
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

    // Parse the input string into a LocalDateTime
    return LocalDateTime.parse(dateString, formatter);
  }

  public  static String localDateToString(LocalDateTime localDateTime){
    // Define the desired date and time format
    String pattern = "yyyy-MM-dd HH:mm:ss"; // Change this to your desired format
    // Create a DateTimeFormatter with the specified pattern
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    // Format the LocalDateTime as a String
    return localDateTime.format(formatter);
  }

  public static int stringToInt(String strNumber){
    try {
      return Integer.parseInt(strNumber);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  public static Double StringToDouble(String doubleString){
    try {
      return Double.parseDouble(doubleString);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  public static boolean isNumber(String inputNumber) {
    try {
      Integer.parseInt(inputNumber);
     return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }
}
