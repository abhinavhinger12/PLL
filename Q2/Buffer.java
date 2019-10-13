import java.util.Queue;
import java.util.LinkedList; 

//Buffer for packaging and sealing units
public class Buffer{
	public Queue<Bottle> buffer;

	public Buffer(){
		buffer = new LinkedList<>();
	}

	public void add(Bottle bottle){
		this.buffer.add(bottle);
	}

	public Bottle get(){
		Bottle temp = this.buffer.remove();
		return temp;
	}

	public Bottle peek(){
		Bottle temp = this.buffer.peek();
		return temp;
	}

	public int size(){
		int temp = this.buffer.size();
		return temp;
	}

	public Boolean isEmpty(){
		if(this.buffer.size() == 0)
			return true;
		else
			return false;
	}
}
