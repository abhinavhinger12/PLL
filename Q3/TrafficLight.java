import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*Traffic light class
id: Unique ID
Status: Green or Red
Time: left of green 
*/
public class TrafficLight
{
    int id;
    int status;
    int time;

    public TrafficLight(int idT, int statusT, int timeT){
        this.id = idT;
        this.status = statusT;
        this.time = timeT;
    }
   
    public void updateStatus(int statusT){
        if(statusT == 1){
            this.time = 60;
        }
        else if(statusT == 0){
            this.time = 120;
        }
        this.status = statusT;
    }

    public Boolean isGreen(){
        if(this.status==1){
            return true;
        }
        else{
            return false;
        }    
    }

    public Boolean isTimeZero(){
        if(this.time == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public void decreTime(){
        this.time--;
    }
}