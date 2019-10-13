import java.util.Scanner;


public class Inventory{
	public int smallTshirtCount;
	public int mediumTshirtCount;
	public int largeTshirtCount;
	public int capCount;
	
	//Initialize the inventory using constructor
	public Inventory(){
		this.smallTshirtCount=0;
		this.mediumTshirtCount=0;
		this.largeTshirtCount=0;
		this.capCount=0;
	}

	//Class methods for placing orders for tshirts and caps
	//These are synchronized methods so no two threads will be able to access the same method simultaneously
	synchronized public Boolean placeOrderSmall(char merchandiseType, int quantity){
		Boolean success = false;
		
		if(this.smallTshirtCount >= quantity){
			this.smallTshirtCount = this.smallTshirtCount - quantity;
			success = true;
		}
		return success;
	}
	synchronized public Boolean placeOrderMedium(char merchandiseType, int quantity){
		Boolean success = false;
		
		if(this.mediumTshirtCount >= quantity){
			this.mediumTshirtCount = this.mediumTshirtCount - quantity;
			success = true;
		}
		return success;
	}
	synchronized public Boolean placeOrderLarge(char merchandiseType, int quantity){
		Boolean success = false;
		
		if(this.largeTshirtCount >= quantity){
			this.largeTshirtCount = this.largeTshirtCount - quantity;
			success = true;
		}
		return success;
	}
	synchronized public Boolean placeOrderCap(char merchandiseType, int quantity){
		Boolean success = false;
		
		if(this.capCount >= quantity){
			this.capCount = this.capCount - quantity;
			success = true;
		}
		return success;
	}

	//Method to print inventory
	public void printInventory(int orderNumber){
    	System.out.printf("Order Number:%s S:%s M:%s L:%s C:%s\n",orderNumber ,this.smallTshirtCount ,this.mediumTshirtCount ,this.largeTshirtCount ,this.capCount );
	}

	//From this method code starts it's execution
	public static void main(String []args) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("****Merchandise sale for Alcheringa 2020****");
		System.out.println("Initialize Inventory (Provide 4 integers in order of T shirt small, T shirt medium, T shirt large, Cap)\nYour Input : ");
		
		Inventory inventory = new Inventory();
		
		//Taking Input to initialize inventory
		inventory.smallTshirtCount = input.nextInt();
		inventory.mediumTshirtCount = input.nextInt();
		inventory.largeTshirtCount = input.nextInt();
		inventory.capCount = input.nextInt();

		if(inventory.smallTshirtCount < 0 || inventory.mediumTshirtCount<0 || inventory.largeTshirtCount < 0 || inventory.capCount < 0){
			System.out.println("Enter Positive values for Inventory");
			return;
		}

		inventory.printInventory(0);
		
		System.out.println("Number of Students Ordering :");
		int numberOfOrders = input.nextInt();
		
		if(numberOfOrders <= 0){
			System.out.println("There should be atleast 1 order");
			return;
		}

		int[] orderNumber = new int[numberOfOrders];  
		char[] merchandiseType = new char[numberOfOrders];  
		int[] quantity = new int[numberOfOrders];

		int currOrder;

		//Taking all the orders and saving them to start the their execution concurrently
		for(currOrder=0;currOrder<numberOfOrders;currOrder++){
			int tempOrderNumber = input.nextInt();
			orderNumber[currOrder] = tempOrderNumber;

			char tempMerchandiseType = input.next().charAt(0);
			merchandiseType[currOrder] = tempMerchandiseType;

			int tempQuantity = input.nextInt();
			if(tempQuantity < 0){
				System.out.println("You cannot order negative amount of merch");
				return;
			}
			quantity[currOrder] = tempQuantity;
		}


		//Create separate threads for each order and queue them
		for(currOrder=0;currOrder<numberOfOrders;currOrder++){
			Order order = new Order(inventory, orderNumber[currOrder], merchandiseType[currOrder], quantity[currOrder]);
			order.start();
		}
	} 
}


