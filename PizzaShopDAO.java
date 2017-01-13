import java.util.List;


public interface PizzaShopDAO {

	public Customer saveNewCustomer(Customer customer);
	public Customer retrieveCustomer(Customer customer);
	public void createOrder(Customer customer, Order newOrder);
	public Topping retrieveTopping(int toppingId);
	public List<Order> retrieveOrders(Customer customer);
	public void updateOrder(Order orderToChange);
	public void deleteOrder(Order orderToCancel);
	
}
