import java.util.Scanner;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*; 

//This is the main class which contains main and PrintStatus method
public class Factory{
	//Method to print status of bottles
	public static void PrintStatus(Status status){
		System.out.println("B1 packaged : "+status.packagedB1+"\nB1 sealed : "+status.sealedB1+"\nB1 InGodown : "+status.finishedB1+"\nB2 packaged : "+status.packagedB2+"\nB2 sealed : "+status.sealedB2+"\nB2 InGodown: "+status.finishedB2);
	}

	//Main method in which we take input from user and start threads for execution of Packaging and Sealing Unit
	public static void main(String []args) {
		Scanner input = new Scanner(System.in);

		UnfinishedTray unfTray = new UnfinishedTray();
		
		Buffer bufferPackUnitB1 = new Buffer();
		Buffer bufferPackUnitB2 = new Buffer();
		Buffer bufferSealUnitB = new Buffer();
		Status status = new Status();

		//Taking User Input
		System.out.println("Input (Number of B1 <space> Number of B2 <space> Observation Time)");
		unfTray.numberOfB1 = input.nextInt();
		unfTray.numberOfB2 = input.nextInt();
		int clockTime = input.nextInt();
		
		if(unfTray.numberOfB1 < 0 || unfTray.numberOfB2 < 0 || clockTime < 0){
			System.out.println("Number of Bottles or Clock time either of them shouldn't be negative");
			return;
		}

		//currFillPackUnit and currFillSealUnit stores what type of bottle is currently present in Packaging and Sealing unit respectively
		Integ currFillPackUnit = new Integ(0);
		Integ currFillSealUnit = new Integ(0);
		//These are the clocks for Packaging and Sealing Unit
		Integ localTimePackUnit = new Integ(0);
		Integ localTimeSealUnit = new Integ(0);

		//Bottle type 0 = B1 and type 1 = B2. This will keep on alternating for Packaging and Sealing unit
		int bottleTypePackUnit = 0;
		int bottleTypeSealUnit = 1;
		
		//Global clock of the factory
		Time observationTime = new Time(clockTime);

		Bottle bottlePack = new Bottle(-1);
		Bottle bottleSeal = new Bottle(-1);

		while(!observationTime.isZero()){
			observationTime.decreClock();
			
			//If no bottle is there in Packaging Unit we will first look into the buffers If there is a bottle 
			//otherwise we will take a new bottle from unfinfished tray if available
			//In variable bottlePack we store the bottle which will be provided to the packaging unit to process
			if(currFillPackUnit.t == 0){
				if(bufferPackUnitB1.isEmpty() && bufferPackUnitB2.isEmpty()){
					if(bottleTypePackUnit == 0){
						if(unfTray.numberOfB1 != 0){
							bottlePack = new Bottle(bottleTypePackUnit+1);
							unfTray.numberOfB1--;
							bottleTypePackUnit = (bottleTypePackUnit+1)%2;
						}
						else if(unfTray.numberOfB2 != 0){
							bottlePack = new Bottle(bottleTypePackUnit+2);
							unfTray.numberOfB2--;
						}
						else{
							bottlePack = new Bottle(-1);
						}
					}
					else if(bottleTypePackUnit == 1){
						if(unfTray.numberOfB2 != 0){
							bottlePack = new Bottle(bottleTypePackUnit+1);
							unfTray.numberOfB2--;
							bottleTypePackUnit = (bottleTypePackUnit+1)%2;
						}
						else if(unfTray.numberOfB1 != 0){
							bottlePack = new Bottle(bottleTypePackUnit);
							unfTray.numberOfB1--;
						}
						else{
							bottlePack = new Bottle(-1);
						}
					}
				}
				else{
					if(bottleTypePackUnit == 0){
						if(!bufferPackUnitB1.isEmpty()){
							bottlePack = bufferPackUnitB1.get();
							bottleTypePackUnit = (bottleTypePackUnit+1)%2;
						}
						else{
							bottlePack = bufferPackUnitB2.get();
						}
					}
					else if(bottleTypePackUnit == 1){
						if(!bufferPackUnitB2.isEmpty()){
							bottlePack = bufferPackUnitB2.get();
							bottleTypePackUnit = (bottleTypePackUnit+1)%2;
						}
						else{
							bottlePack = bufferPackUnitB1.get();
						}
					}
				}
			}
			//Creating new Thread for Packaging unit and changing the state of thread to start
			Semaphore sem = new Semaphore(1);
			Process packPro = new Process(unfTray, 1,bottlePack, bufferPackUnitB1, bufferPackUnitB2, bufferSealUnitB, status, currFillPackUnit, currFillSealUnit, localTimePackUnit, localTimeSealUnit, sem);
			packPro.start();

			//If no bottle is there in sealing Unit we will first look into the buffers If there is a bottle 
			//otherwise we will take a new bottle from unfinfished tray if available
			//In variable bottlePack we store the bottle which will be provided to the packaging unit to process
			if(currFillSealUnit.t == 0){
				if(bufferSealUnitB.isEmpty()){
					if(bottleTypeSealUnit == 0){
						if(unfTray.numberOfB1 != 0){
							bottleSeal = new Bottle(bottleTypeSealUnit+1);
							unfTray.numberOfB1--;
							bottleTypeSealUnit = (bottleTypeSealUnit+1)%2;
						}
						else if(unfTray.numberOfB2 != 0){
							bottleSeal = new Bottle(bottleTypeSealUnit+2);
							unfTray.numberOfB2--;
						}
						else{
							bottleSeal = new Bottle(-1);
						}
					}
					else if(bottleTypeSealUnit == 1){
						if(unfTray.numberOfB2 != 0){
							bottleSeal = new Bottle(bottleTypeSealUnit+1);
							unfTray.numberOfB2--;
							bottleTypeSealUnit = (bottleTypeSealUnit+1)%2;
						}
						else if(unfTray.numberOfB1 != 0){
							bottleSeal = new Bottle(bottleTypeSealUnit);
							unfTray.numberOfB1--;
						}
						else{
							bottleSeal = new Bottle(-1);
						}
					}
				}
				else{
					bottleSeal = bufferSealUnitB.get();
					if(bottleSeal.type == 1)
						bottleTypeSealUnit = 1;
					else if(bottleSeal.type == 2)
						bottleTypeSealUnit = 0;
				}
			}

			//Creating new Thread for Sealing unit and changing the state of thread to start
			Process sealPro = new Process(unfTray, 2, bottleSeal, bufferPackUnitB1, bufferPackUnitB2, bufferSealUnitB, status, currFillPackUnit, currFillSealUnit, localTimePackUnit, localTimeSealUnit, sem);
			sealPro.start();

			try{
			    Thread.sleep(100);
			}
			catch(InterruptedException ex){
			    Thread.currentThread().interrupt();
			}
		}
		//Printing the status of factory after the factory has run for the observation time
		PrintStatus(status);
	}
}










