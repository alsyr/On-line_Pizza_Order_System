import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="TOPPING")
public class Topping {

	@Id
	private int toppingId;

	private String name;
	private double price;

//	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@ManyToMany
	@JoinTable(name = "join_table",
	joinColumns = {@JoinColumn(name="toppingId")},
	inverseJoinColumns = {@JoinColumn(name = "orderId")}
			)
	private List<Order> orders;


	Topping() {
		orders = new ArrayList<Order>();
	}

	public int getToppingId() {
		return toppingId;
	}
	public void setToppingId(int toppingId) {
		this.toppingId = toppingId;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price){
		this.price = price;
	}

	public List<Order> getOrders()
	{ 
		return orders;
	}
	public void addOrder(Order order) {
		orders.add(order);
	}

	public String toString() {
		return name;
	}
}
