import javax.persistence.*;

@Entity
@Table(name = "DISCOUNTED_ORDER")
public class DiscountedOrder extends Order {

  private static int discountRate = 10;

  public int getDiscountRate() {
    return discountRate;
  }

  public void setDiscountRate(int discountRate) {
    DiscountedOrder.discountRate = discountRate;
  }
}
