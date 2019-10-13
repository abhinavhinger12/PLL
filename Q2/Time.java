
//Time class which includes methods to manage the observation time for factory
public class Time{
	public int clockTime;

	public Time(int time){
		this.clockTime = time;
	}

	public void decreClock(){
		this.clockTime--;
	}

	public Boolean isZero(){
		if(clockTime <= 0)
			return true;
		else
			return false;
	}
}
