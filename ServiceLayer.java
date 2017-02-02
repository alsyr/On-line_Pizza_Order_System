import org.hibernate.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ServiceLayer {

  private ConcretePizzaShopDAO theShop;
  private Scanner scan = new Scanner(System.in);

  public ServiceLayer(Session session) {
    theShop = new ConcretePizzaShopDAO(session);
  }

  //Function to Sign In
  public Customer signIn() {
    System.out.print("Choose a userName: ");
    String userName = scan.nextLine();
    System.out.print("Choose a password: ");
    String password = scan.nextLine();
    System.out.print("Enter street name: ");
    String streetName = scan.nextLine();
    System.out.print("Enter city: ");
    String city = scan.nextLine();
    System.out.print("Enter state: ");
    String state = scan.nextLine();
    System.out.print("Enter zip code: ");
    String zipCode = scan.nextLine();
    Address address = new Address(streetName, city, state, zipCode);
    Customer customer = new Customer();
    customer.setUserName(userName);
    customer.setPassword(password);
    customer.setAddress(address);
    return theShop.saveNewCustomer(customer);
  }

  //Function to Log In
  public Customer logIn() {
    System.out.print("UserName: ");
    String userName = scan.nextLine();
    System.out.print("Password: ");
    String password = scan.nextLine();
    Customer customer = new Customer();
    customer.setUserName(userName);
    customer.setPassword(password);
    return theShop.retrieveCustomer(customer);
  }

  //Function to make an order
  public void makeOrder(Customer customer, Boolean withDiscount) {
    if (customer == null) {
      System.out.println("Sorry, you need to log in first!");
    } else {
      System.out.print("Pizza Size([S]MALL, [M]EDIUM or [L]ARGE): ");
      String choiceSize = scan.nextLine();
      double sizePrice = 0;
      PizzaSize pizzaSize = null;
      switch (choiceSize) {
        case "s":
        case "S":
          pizzaSize = PizzaSize.SMALL;
          sizePrice = pizzaSize.getPrice();
          break;
        case "m":
        case "M":
          pizzaSize = PizzaSize.MEDIUM;
          sizePrice = pizzaSize.getPrice();
          break;
        case "l":
        case "L":
          pizzaSize = PizzaSize.LARGE;
          sizePrice = pizzaSize.getPrice();
          break;
      }

      System.out.print("How many toppings do you want (1,2 or 3): ");
      int count = Integer.parseInt(scan.nextLine());
      theShop.createAllTopping();
      List<Topping> toppings = new ArrayList<Topping>();
      double toppingPrice = 0;
      for (int i = 0; i < count; i++) {
        System.out.print("Toppings([1]Pepperoni, [2]Mushrooms, [3]Onions, [4]Sausage, [5]Bacon, "
            + "[6]Extra cheese, [7]Black olives, [8]Green peppers, [9]Pineapple, [10]Spinach): ");
        String topping = scan.nextLine();
        Topping aTopping = null;
        switch (topping) {
          case "1":
            aTopping = theShop.retrieveTopping(1);
            break;
          case "2":
            aTopping = theShop.retrieveTopping(2);
            break;
          case "3":
            aTopping = theShop.retrieveTopping(3);
            break;
          case "4":
            aTopping = theShop.retrieveTopping(4);
            break;
          case "5":
            aTopping = theShop.retrieveTopping(5);
            break;
          case "6":
            aTopping = theShop.retrieveTopping(6);
            break;
          case "7":
            aTopping = theShop.retrieveTopping(7);
            break;
          case "8":
            aTopping = theShop.retrieveTopping(8);
            break;
          case "9":
            aTopping = theShop.retrieveTopping(9);
            break;
          case "10":
            aTopping = theShop.retrieveTopping(10);
            break;
        }
        toppingPrice += aTopping.getPrice();
        toppings.add(aTopping);
      }

      System.out.print("Delivery date and Time(in the format: yyyy-MM-dd hh:mm:ss): ");
      String deliveryDateString = scan.nextLine();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      Date parsedDate = null;
      Timestamp timestamp = null;
      try {
        parsedDate = dateFormat.parse(deliveryDateString); //1987-12-21 11:10:23
        timestamp = new Timestamp(parsedDate.getTime());
      } catch (ParseException e) {
        e.printStackTrace();
      }

      System.out.print("Payment Method:([C]ASH, [V]ISA or [M]ASTER): ");
      String choicePayment = scan.nextLine();
      PaymentMethod payment = null;
      switch (choicePayment) {
        case "c":
        case "C":
          payment = PaymentMethod.CASH;
          break;
        case "v":
        case "V":
          payment = PaymentMethod.VISA;
          break;
        case "m":
        case "M":
          payment = PaymentMethod.MASTER;
          break;
      }
      double totalPrice = (double) (sizePrice + toppingPrice);
      Order newOrder;
      if (!withDiscount) {
        newOrder = new Order();
        newOrder.setDate(timestamp);
        newOrder.setSize(pizzaSize);
        newOrder.setToppings(toppings);
        newOrder.setPayment(payment);
        newOrder.setPrice(totalPrice);
        System.out.println("The total price of your order is: " + totalPrice);
      } else {
        newOrder = new DiscountedOrder();
        newOrder.setDate(timestamp);
        newOrder.setSize(pizzaSize);
        newOrder.setToppings(toppings);
        newOrder.setPayment(payment);
        double discountPrice = totalPrice - totalPrice / 10;
        newOrder.setPrice(discountPrice);
        System.out.println("The total price of your order is: " + discountPrice);
      }
      theShop.createOrder(customer, newOrder);
    }
  }

  //Function to view all orders
  public void viewAllOrders(Customer theCustomer) {
    if (theCustomer == null) {
      System.out.println("Sorry, you need to log in first!");
    } else {
      theShop.retrieveOrders(theCustomer);
    }
  }

  //Function to change an order
  public void changeOrder(Customer theCustomer) {
    if (theCustomer == null) {
      System.out.println("Sorry, you need to log in first!");
    } else {
      theShop.retrieveOrders(theCustomer);
      System.out.print("Choose order to change by id: ");
      int orderId = Integer.parseInt(scan.nextLine());
      Order orderToChange = theShop.retrieveOrder(orderId);

      String choice = null;
      do {
        System.out.print("What do you want to change: [1]Size, [2]Toppings, " +
            "[3]DeliveryTime, [4]PaymentMode or [5]Quit: ");
        choice = scan.nextLine();
        switch (choice) {
          case "1":
            System.out.print("Pizza Size([S]MALL, [M]EDIUM or [L]ARGE): ");
            String choiceSize = scan.nextLine();
            double sizePrice = 0;
            PizzaSize pizzaSize = null;
            switch (choiceSize) {
              case "s":
              case "S":
                pizzaSize = PizzaSize.SMALL;
                sizePrice = pizzaSize.getPrice();
                break;
              case "m":
              case "M":
                pizzaSize = PizzaSize.MEDIUM;
                sizePrice = pizzaSize.getPrice();
                break;
              case "l":
              case "L":
                pizzaSize = PizzaSize.LARGE;
                sizePrice = pizzaSize.getPrice();
                break;
            }
            double newPrice = orderToChange.getPrice() - orderToChange.getSize().getPrice() + sizePrice;
            if (orderToChange instanceof DiscountedOrder)
              newPrice = newPrice - newPrice / 10;
            orderToChange.setPrice(newPrice);
            orderToChange.setSize(pizzaSize);
            break;
          case "2":
            System.out.print("How many toppings do you want (1,2 or 3): ");
            int count = Integer.parseInt(scan.nextLine());
            theShop.createAllTopping();
            List<Topping> toppings = new ArrayList<Topping>();
            double toppingPrice = 0;
            for (int i = 0; i < count; i++) {
              System.out.print("Toppings([1]Pepperoni, [2]Mushrooms, [3]Onions, [4]Sausage, [5]Bacon, "
                  + "[6]Extra cheese, [7]Black olives, [8]Green peppers, [9]Pineapple, [10]Spinach): ");
              String topping = scan.nextLine();
              Topping aTopping = null;
              switch (topping) {
                case "1":
                  aTopping = theShop.retrieveTopping(1);
                  break;
                case "2":
                  aTopping = theShop.retrieveTopping(2);
                  break;
                case "3":
                  aTopping = theShop.retrieveTopping(3);
                  break;
                case "4":
                  aTopping = theShop.retrieveTopping(4);
                  break;
                case "5":
                  aTopping = theShop.retrieveTopping(5);
                  break;
                case "6":
                  aTopping = theShop.retrieveTopping(6);
                  break;
                case "7":
                  aTopping = theShop.retrieveTopping(7);
                  break;
                case "8":
                  aTopping = theShop.retrieveTopping(8);
                  break;
                case "9":
                  aTopping = theShop.retrieveTopping(9);
                  break;
                case "10":
                  aTopping = theShop.retrieveTopping(10);
                  break;
              }
              toppingPrice += aTopping.getPrice();
              toppings.add(aTopping);
            }

            double newPrice2 = (orderToChange.getSize().getPrice()) + toppingPrice;
            if (orderToChange instanceof DiscountedOrder) {
              System.out.print("DiscountedOrder");
              newPrice2 = newPrice2 - newPrice2 / 10;
            } else {
              System.out.print("SimpleOrder");
            }
            orderToChange.setPrice(newPrice2);
            orderToChange.setToppings(toppings);
            break;
          case "3":
            System.out.print("Delivery date and Time(in the format: yyyy-MM-dd hh:mm:ss): ");
            String deliveryDateString = scan.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = null;
            Timestamp timestamp = null;
            try {
              parsedDate = dateFormat.parse(deliveryDateString); //1987-12-21 11:10:23
              timestamp = new Timestamp(parsedDate.getTime());
            } catch (ParseException e) {
              e.printStackTrace();
            }
            orderToChange.setDate(timestamp);
            break;
          case "4":
            System.out.print("Payment Method:([C]ASH, [V]ISA or [M]ASTER): ");
            String choicePayment = scan.nextLine();
            PaymentMethod payment = null;
            switch (choicePayment) {
              case "c":
              case "C":
                payment = PaymentMethod.CASH;
                break;
              case "v":
              case "V":
                payment = PaymentMethod.VISA;
                break;
              case "m":
              case "M":
                payment = PaymentMethod.MASTER;
                break;
            }
            orderToChange.setPayment(payment);
        }
      } while (!choice.equals("5"));
      theShop.updateOrder(orderToChange);
    }
  }

  //Function to cancel an order
  public void cancelOrder(Customer theCustomer) {
    if (theCustomer == null) {
      System.out.println("Sorry, you need to log in first!");
    } else {
      theShop.retrieveOrders(theCustomer);
      System.out.print("Choose order to cancel by id: ");
      int orderId = Integer.parseInt(scan.nextLine());
      Order orderToCancel = theShop.retrieveOrder(orderId);
      theShop.deleteOrder(orderToCancel);
    }
  }
}
