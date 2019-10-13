//Order class for processing each order
public class Order extends Thread{
	public Inventory inventory;
	public int orderNumber;
	public char merchandiseType;
	public int quantity;

	public Order(Inventory inventory, int orderNumber, char merchandiseType, int quantity){
		this.inventory = inventory;
		this.orderNumber = orderNumber;
		this.merchandiseType = merchandiseType;
		this.quantity= quantity;
	}


	//Override the run function of Thread
	@Override
	public void run(){
		char merchandiseType = this.merchandiseType;
		int quantity = this.quantity;
		int orderNumber = this.orderNumber;
		Inventory inventory = this.inventory;

		//According to merchandise we call their respective order function which are synchronized and only on order can access it at a time
		if(merchandiseType == 'S'){
			Boolean success = inventory.placeOrderSmall(merchandiseType,quantity);
			inventory.printInventory(orderNumber);
			if(success == true){
				System.out.println("Order "+orderNumber+" is successful");
			}
			else{
				System.out.println("Order "+orderNumber+" failed");
			}	
		}
		else if(merchandiseType == 'M'){
			Boolean success = inventory.placeOrderMedium(merchandiseType,quantity);
			inventory.printInventory(orderNumber);
			if(success == true){
				System.out.println("Order "+orderNumber+" is successful");
			}
			else{
				System.out.println("Order "+orderNumber+" failed");
			}	
		}
		else if(merchandiseType == 'L'){
			Boolean success = inventory.placeOrderLarge(merchandiseType,quantity);
			inventory.printInventory(orderNumber);
			if(success == true){
				System.out.println("Order "+orderNumber+" is successful");
			}
			else{
				System.out.println("Order "+orderNumber+" failed");
			}	
		}
		else if(merchandiseType == 'C'){
			Boolean success = inventory.placeOrderCap(merchandiseType,quantity);
			inventory.printInventory(orderNumber);
			if(success == true){
				System.out.println("Order "+orderNumber+" is successful");
			}
			else{
				System.out.println("Order "+orderNumber+" failed");
			}	
		}
	}
}