package utility.colors;

public class ProductColor extends Color{

  @Override
  String getText() {
    return "\u001B[32m"; //Green
  }

  @Override
  String getBackground() {
    return "\u001B[42m"; //Green
  }
}
