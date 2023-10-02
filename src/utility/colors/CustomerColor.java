package utility.colors;

public class CustomerColor extends Color{

  @Override
   String getText() {
    return "\u001B[34m"; //BLUE
  }

  @Override
   String getBackground() {
    return "\u001B[44m"; //BLUE
  }
}

