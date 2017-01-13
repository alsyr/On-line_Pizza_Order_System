public enum PizzaSize {

	SMALL(3),
	MEDIUM(5),
	LARGE(7);

	private int price;

	PizzaSize(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}
	public void setPrice(int price){
		this.price = price;
	}
}
