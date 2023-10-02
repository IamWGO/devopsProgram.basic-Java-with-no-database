package utility;

import utility.colors.Color;

public class Theme {
  public Color color;

  public Theme(Color color) {
    this.color = color;
  }


  public void printDashLine(int length){
    println("-".repeat(length));
  }
  public  void printEmptyItem() {
    System.out.println(
            " ".repeat(10)
            + color.background
            + "+".repeat(10)
            + " No Items "
            + "+".repeat(10)
            + color.reset
            + " ".repeat(10)
    );
  }
  public void printSuccess(String text){
    System.out.println(color.successText+ text + color.reset);
  }

  public void printWarning(String text){
    System.out.println(color.warningText+ text + color.reset);
  }

  public void printInfo(String text) {
    System.out.println(color.infoText + text + color.reset);
  }

  public void printEndOfList() {
    println("+".repeat(20) + " End of the list " + "+".repeat(20));
  }

  public void println(String text) {
    System.out.println(color.text + text + color.reset);
  }

  public void print(String text) {
    System.out.print(color.text + text + color.reset);
  }

  public String addWhiteSpace(String text, int maxAmount){
    if(text.length() > maxAmount){
      return text.substring(0, maxAmount - 3) + "...";
    }
    return text + " ".repeat(maxAmount - text.length());
  }

}
