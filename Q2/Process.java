import java.util.concurrent.*; 

public class Process extends Thread{
	public UnfinishedTray unfTray;
	public int processType;
	public Bottle bottle;
	public Buffer bufferPackUnitB1;
	public Buffer bufferPackUnitB2;
	public Buffer bufferSealUnitB;
	public Status status;
	public Integ currFillPackUnit;
	public Integ currFillSealUnit;
	public Integ localTimePackUnit;
	public Integ localTimeSealUnit;
	Semaphore sem;

	//Constructor for Process
	public Process(UnfinishedTray unfTrayT, int processTypeT, Bottle bottleT, Buffer bufferPackUnitB1T, Buffer bufferPackUnitB2T, Buffer bufferSealUnitBT, Status statusT, Integ currFillPackUnitT, Integ currFillSealUnitT, Integ localTimePackUnitT, Integ localTimeSealUnitT, Semaphore sem){
		this.processType = processTypeT;
		this.bottle = bottleT;
		this.bufferPackUnitB1 = bufferPackUnitB1T;
		this.bufferPackUnitB2 = bufferPackUnitB2T;
		this.bufferSealUnitB = bufferSealUnitBT;
		this.status = statusT;
		this.currFillPackUnit = currFillPackUnitT;
		this.currFillSealUnit = currFillSealUnitT;
		this.localTimePackUnit = localTimePackUnitT;
		this.localTimeSealUnit = localTimeSealUnitT;
		this.sem = sem;
	}

	@Override
	public void run(){
		//If process type is 1 i.e. packaging
		if(this.processType == 1){
			localTimePackUnit.t++;
			//If nothing is in Packakging unit give it the bottle to pack
			if(currFillPackUnit.t==0){
				currFillPackUnit.t = bottle.type;
			}
			else if(currFillPackUnit.t == -1){
				currFillPackUnit.t = -1;
			}
			//If two seconds are done packakging it will package the bottle
			//And check if it is sealed then send it to godown otherwise send it to seal unit buffer if it is empty.
			else if(localTimePackUnit.t >= 2){
				bottle.packaged();
				
				if(bottle.isSealed()){
					try { 
	                 // acquire method 
	                	sem.acquire(); 
	           		} catch (InterruptedException e) { 
	                	e.printStackTrace(); 
	            	} 
					if(bottle.type == 1){
						status.packagedB1++;
						status.finishedB1++;
					}
					else if(bottle.type == 2){
						status.packagedB2++;
						status.finishedB2++;
					}
					currFillPackUnit.t = 0;
					localTimePackUnit.t = 0;
					sem.release();
				}
				else if(bufferSealUnitB.size() < 2){
					bufferSealUnitB.add(bottle);
					if(bottle.type == 1){
						status.packagedB1++;
					}
					else if(bottle.type == 2){
						status.packagedB2++;
					}
					currFillPackUnit.t = 0;
					localTimePackUnit.t = 0;
				}
			}
		}
		//If Process type is 2 i.e. sealing
		else if(this.processType == 2){
			localTimeSealUnit.t++;
			//If nothing is in Sealing unit give it the bottle to pack
			if(currFillSealUnit.t==0){
				currFillSealUnit.t = bottle.type;
			}
			else if(currFillSealUnit.t == -1){
				currFillSealUnit.t = -1;
			}
			//If three seconds are done sealing it will seal the bottle
			//And check if it is packaged already then send it to godown otherwise send it to pack unit buffer according to bottle type and if it is empty.
			if(localTimeSealUnit.t >= 3){
				bottle.seal();
				if(bottle.isPackaged()){
					try { 
	                 // acquire method 
	                	sem.acquire(); 
	           		} catch (InterruptedException e) { 
	                	e.printStackTrace(); 
	            	}
					if(bottle.type == 1){
						status.sealedB1++;
						status.finishedB1++;
					}
					else if(bottle.type == 2){
						status.sealedB2++;
						status.finishedB2++;
					}
					currFillSealUnit.t = 0;
					localTimeSealUnit.t = 0;
					sem.release();
				}
				else if(bottle.type == 1){
					if(bufferPackUnitB1.size() < 2){
						bufferPackUnitB1.add(bottle);
						status.sealedB1++;
						currFillSealUnit.t = 0;
						localTimeSealUnit.t = 0;
					}
				}
				else if(bottle.type == 2){
					if(bufferPackUnitB2.size() < 3){
						bufferPackUnitB2.add(bottle);
						status.sealedB2++;
						currFillSealUnit.t = 0;
						localTimeSealUnit.t = 0;
					}	
				}
			}
		}
	}
}