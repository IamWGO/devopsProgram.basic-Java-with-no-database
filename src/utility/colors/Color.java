package utility.colors;

public abstract class Color {
  public final String reset = "\u001B[0m";
  public final String warningText	= "\u001B[31m"; //RED
//  public final String warningBackground	= "\u001B[41m"; //RED
  public final String successText	= "\u001B[32m"; //GREEN
//  public final String successBackground	= "\u001B[42m"; //GREEN
//  public final String pendingText	= "\u001B[35m"; //PURPLE
//  public final String pendingBackground	= "\u001B[45m"; //PURPLE
//  public final String inActiveText	= "\u001B[36m";  //CYAN
//  public final String inActiveBackground	= "\u001B[46m"; //CYAN
  public final String infoText	= "\u001B[35m"; //PURPLE
//  public final String infoBackground	= "\u001B[46m"; //PURPLE

  public String background = getBackground();
  public String text = getText();

  abstract String getText();
  abstract String getBackground();

}
