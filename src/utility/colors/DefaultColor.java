package utility.colors;

public class DefaultColor extends  Color{
  @Override
  String getText() {
    return "\u001B[33m"; //YELLOW
  }

  @Override
  String getBackground() {
    return "\u001B[43m"; //YELLOW
  }
}
