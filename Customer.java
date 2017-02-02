import java.util.*;
import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

  @Id
  @GeneratedValue
  private int userId;
  @Column(name = "userName", unique = true)
  private String userName;
  private String password;
  @Embedded
  private Address address;

  @OneToMany(mappedBy = "customer", targetEntity = Order.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Order> orders;

  public Customer() {
    orders = new ArrayList<Order>();
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Address getAdress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void addOrder(Order order) {
    orders.add(order);
  }

  public String toString() {
    return userId + " " + userName + " " + password;
  }
}
