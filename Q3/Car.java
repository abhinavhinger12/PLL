import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*Car has the following properties
path:  Source to Destination path (6 possible paths : S,W,E)
carId: Unique identifier
status: Whether its Waiting, Crossing or Waiting
*/
public class Car 
{
    int carId;
    int path;
    int status;
    int timeLeft;
    
    public Car(int carIdT,int pathT){
        this.carId = carIdT;
        this.path = pathT;
    }

    public void updateStatus(int statusT){
        this.status = statusT;
    }

    public void decreTimeLeft(){
        this.timeLeft--;
    }   
}