package dataclass;

public class Customer {
  int customerId;
  String firstname;
  String lastname;
  String username;
  String password;
  String email;
  String address;
  String zipCode;
  String country;
  boolean isActive;

  public Customer(int customerId, String firstname, String lastname, String username, String password,
                  String email, String address, String zipCode, String country, boolean isActive) {
    this.customerId = customerId;
    this.firstname = firstname;
    this.lastname = lastname;
    this.username = username;
    this.password = password;
    this.email = email;
    this.address = address;
    this.zipCode = zipCode;
    this.country = country;
    this.isActive = isActive;
  }


  public void updateItem(Customer updateItem) {
    this.customerId = updateItem.customerId;
    this.firstname = updateItem.firstname;
    this.lastname = updateItem.lastname;
    this.username = updateItem.username;
    this.password = updateItem.password;
    this.email = updateItem.email;
    this.address = updateItem.address;
    this.zipCode = updateItem.zipCode;
    this.country = updateItem.country;
    this.isActive = updateItem.isActive;
  }


  public int getCustomerId() {
    return customerId;
  }
  public String getCustomerCode() {
    return String.format("C%05d", customerId);

  }
  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getFullName(){
    return firstname + " " + lastname;
  }

  public String getEmail() {
    return email;
  }

  public String getAddress() {
    return address;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCountry() {
    return country;
  }

  public String getIsActiveString() {
    return isActive ? "Active" : "Blocked";
  }

  public boolean getIsActive(){
    return isActive;
  }

  public void setActive() {
    isActive = !isActive;
  }

  public String objectToLineFormat(){
    String id = String.valueOf(customerId);
    return  id + ","+ firstname + "," + lastname + "," + username + ","
            + password + "," + email + ","
            + (!address.isEmpty() ? address : "-") + ","
            + (!zipCode.isEmpty() ? zipCode : "-") + ","
            + (!country.isEmpty() ? country : "-") + ","
            + ((isActive) ? "1" : "0");
  }

}