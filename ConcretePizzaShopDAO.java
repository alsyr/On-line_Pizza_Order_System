import java.util.List;

import org.hibernate.*;

public class ConcretePizzaShopDAO implements PizzaShopDAO {

  private Session session;
  private Transaction transaction;

  public ConcretePizzaShopDAO(Session session) {
    this.session = session;
    this.transaction = null;
  }

  //Function to save a new customer in the database
  public Customer saveNewCustomer(Customer customer) {
    try {
      transaction = session.beginTransaction();
      session.save(customer);
      transaction.commit();
      return customer;
    } catch (HibernateException he) {
      transaction.rollback();
      System.out.println("This userName is already used. Choose another one!");
      return null;
    }
  }

  //Function to retrieve a customer from the database
  public Customer retrieveCustomer(Customer customer) {
    String userName = customer.getUserName();
    String password = customer.getPassword();
    int userId;
    String queryString = "from Customer where userName = :userName";
    Query query = session.createQuery(queryString).setParameter("userName", userName);
    Customer theCustomer = (Customer) query.uniqueResult();

    if (theCustomer == null) {
      System.out.println("no customer with that name");
      Customer newCustomer = customer;
      return saveNewCustomer(newCustomer);
    } else if (!theCustomer.getPassword().equals(customer.getPassword())) {
      System.out.println("Wrong Password. Try again!");
      return null;
    } else {
      Customer actualCustomer = (Customer) session.get(Customer.class, new Integer(theCustomer.getUserId()));
      return actualCustomer;
    }
  }

  //Function to create all toppings in the database from the beginning
  public void createAllTopping() {
    try {
      transaction = session.beginTransaction();

      if (retrieveTopping(1) == null) {
//								if(true){

        Topping aTopping1 = new Topping();
        aTopping1.setToppingId(1);
        aTopping1.setName("Pepperoni");
        aTopping1.setPrice(1);
        session.save(aTopping1);

        Topping aTopping2 = new Topping();
        aTopping2.setToppingId(2);
        aTopping2.setName("Mushrooms");
        aTopping2.setPrice(0.25);
        session.save(aTopping2);

        Topping aTopping3 = new Topping();
        aTopping3.setToppingId(3);
        aTopping3.setName("Onions");
        aTopping3.setPrice(0.75);
        session.save(aTopping3);

        Topping aTopping4 = new Topping();
        aTopping4.setToppingId(4);
        aTopping4.setName("Sausage");
        aTopping4.setPrice(1.5);
        session.save(aTopping4);

        Topping aTopping5 = new Topping();
        aTopping5.setToppingId(5);
        aTopping5.setName("Bacon");
        aTopping5.setPrice(1.75);
        session.save(aTopping5);

        Topping aTopping6 = new Topping();
        aTopping6.setToppingId(6);
        aTopping6.setName("Extra cheese");
        aTopping6.setPrice(1.25);
        session.save(aTopping6);

        Topping aTopping7 = new Topping();
        aTopping7.setToppingId(7);
        aTopping7.setName("Black olives");
        aTopping7.setPrice(0.5);
        session.save(aTopping7);

        Topping aTopping8 = new Topping();
        aTopping8.setToppingId(8);
        aTopping8.setName("Green peppers");
        aTopping8.setPrice(0.25);
        session.save(aTopping8);

        Topping aTopping9 = new Topping();
        aTopping9.setToppingId(9);
        aTopping9.setName("Pineapple");
        aTopping9.setPrice(0.50);
        session.save(aTopping9);

        Topping aTopping10 = new Topping();
        aTopping10.setToppingId(10);
        aTopping10.setName("Spinach");
        aTopping10.setPrice(0.25);
        session.save(aTopping10);
      }

      transaction.commit();
    } catch (HibernateException he) {
      transaction.rollback();
    }
  }

  //Function to retrieve a topping
  public Topping retrieveTopping(int toppingId) {
    Topping actualTopping = (Topping) session.get(Topping.class, new Integer(toppingId));
    return actualTopping;
  }

  //Function to create an order
  public void createOrder(Customer customer, Order newOrder) {
    transaction = session.beginTransaction();
    newOrder.setCustomer(customer);
    customer.addOrder(newOrder);
    session.save(customer);
    transaction.commit();
  }

  //Function to update an order
  public void updateOrder(Order orderToChange) {
    transaction = session.beginTransaction();
    session.update(orderToChange);
    transaction.commit();
  }

  //Function to retrieve all orders made by a customer
  public List<Order> retrieveOrders(Customer customer) {
    int userId = customer.getUserId();
    String queryString = "from Order where userId = :userId";
    Query query = session.createQuery(queryString).setParameter("userId", userId);
    List<Order> a = (List<Order>) query.list();

    for (Order o : a) {
      System.out.printf("%-6s | %-10s | %-50s | %-20s | %-10s | %-10s",
          "id: " + o.getOrderId(), "size: " + o.getSize(),
          "topping(s): " + o.getToppings(), "deliveryTime: " + o.getDeliveryTime(),
          "payment: " + o.getPayment(), "price: " + o.getPrice());
      System.out.printf("\n-----------------------------------------------------"
          + "-----------------------------------------------------\n");
    }
    return a;
  }

  //Function to retrieve a particular order
  public Order retrieveOrder(int orderId) {
    return (Order) session.get(Order.class, new Integer(orderId));
  }

  //Function to delete a particular order
  public void deleteOrder(Order orderToCancel) {
    transaction = session.beginTransaction();
    session.clear();
    session.delete(orderToCancel);
    transaction.commit();
  }
}
