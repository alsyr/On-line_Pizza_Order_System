import javax.persistence.*;

@Embeddable
public class Address {

  private String streetName;
  private String city;
  private String state;
  private String zipCode;

  public Address() {
  }

  public Address(String streetName, String city, String state, String zipCode) {
    this.streetName = streetName;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
  }
}
