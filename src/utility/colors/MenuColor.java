package utility.colors;

public class MenuColor extends Color {
  @Override
  String getText() {
    return "\u001B[36m"; //Magenta
  }

  @Override
  String getBackground() {
    return "\u001B[46m"; //Magenta
  }
}
