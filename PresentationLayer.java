import java.util.Scanner;

import org.hibernate.*;

public class PresentationLayer {
  static SessionFactory sessionFactory;

  public static void main(String[] args) {

    sessionFactory = HibernateUtil.getSessionFactory();
    Session session = sessionFactory.openSession();
    ServiceLayer theService = new ServiceLayer(session);

    Scanner scan = new Scanner(System.in);
    String choice = null;

    Customer theCustomer = null;

    do {
      System.out.println("1. To sign in");
      System.out.println("2. To log in");
      System.out.println("3. To make a regular order");
      System.out.println("4. To make a discounted order");
      System.out.println("5. To view all orders");
      System.out.println("6. To change an order");
      System.out.println("7. To cancel an order");
      System.out.println("8. To quit");

      choice = scan.nextLine();
      switch (choice) {
        case "1":
          theCustomer = theService.signIn();
          break;
        case "2":
          theCustomer = theService.logIn();
          break;
        case "3":
          theService.makeOrder(theCustomer, false);
          break;
        case "4":
          theService.makeOrder(theCustomer, true);
          break;
        case "5":
          theService.viewAllOrders(theCustomer);
          break;
        case "6":
          theService.changeOrder(theCustomer);
          break;
        case "7":
          theService.cancelOrder(theCustomer);
          break;
      }
      System.out.println("\n");
    } while (!choice.equals("8"));

    session.close();
    sessionFactory.close();
  }
}
