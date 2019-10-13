//Bottle class includes methods to manage the status of bottle

public class Bottle{
	public int type;
	public Boolean packaged;
	public Boolean sealed;

	public Bottle(int t){
		this.type = t;
		this.packaged = false;
		this.sealed = false;
	}

	public void packaged(){
		this.packaged = true;
	}

	public void seal(){
		this.sealed = true;
	}

	public Boolean isPackaged(){
		return this.packaged;
	}

	public Boolean isSealed(){
		return this.sealed;
	}
}