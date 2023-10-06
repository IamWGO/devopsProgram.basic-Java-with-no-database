package view;
// For common text
public class OutPut {

  public void println(String text){
    System.out.println(text);
  }

  public void print(String text) {
    System.out.print(text);
  }

  public void printDashLine(int length){
    System.out.println("-".repeat(length));
  }
  public  void printEmptyItem() {
    System.out.println(
            " ".repeat(10) + "+".repeat(10)
            + " No Items "
            + "+".repeat(10) + " ".repeat(10)
    );
  }

  public String addWhiteSpace(String text, int maxAmount){
    if(text.length() > maxAmount){
      return text.substring(0, maxAmount - 3) + "...";
    }
    return text + " ".repeat(maxAmount - text.length());
  }

  public void printNoPermission(){
    System.out.println("You don't have permission");
  }

  public void printAddedResult(){
    System.out.println("Added new item !!!");
  }

  public void printUpdatedResult(){
    System.out.print("Updated item !!!");
  }

  public void printDeletedResult(){
    System.out.println("Deleted item !!!");
  }

  public void printNotFound(){
    System.out.println("Item not found.");
  }

  public void printCancel(){
    System.out.println("Cancel Process !!");
  }

  public void printMenuWarning(){
    System.out.println("Wrong command, Please try again");
  }

  public void printEmptyLine(){
    System.out.println();
  }

  public void printEndOfList() {
    System.out.println("+".repeat(20) + " End of the list " + "+".repeat(20));
  }

}
