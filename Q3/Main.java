
import javax.swing.*;  
import java.awt.*;
import java.util.*;
import java.util.List; 

//Int to Object Utility class
class Integ{
  public int t;
  public Integ(int a){
    this.t = a;
  }
}

//THis class controls and updates the times of each car and Traffic lights
public class Main {

  int carCounter = 1;
  public static List<Car> carList = new ArrayList<Car>();
  //User interface
  public static GUI userInterface;
  //Three traffic lights for South, East and West
  public static TrafficLight t1;
  public static TrafficLight t2;
  public static TrafficLight t3;

  //lastCarWaiting stores the waiting time of last car on that pathway. There are 3 paths controlled by Traffic lights 
  public static Integ lastCarsWaitingT1;
  public static Integ lastCarsWaitingT2;
  public static Integ lastCarsWaitingT3;
  //Last waiting time of car for 3 paths on which cars are free to go
  public static Integ lastCarsWaitingF1;
  public static Integ lastCarsWaitingF2;
  public static Integ lastCarsWaitingF3;
  

  public static void main(String[] argv) throws Exception {
    t1 = new TrafficLight(1, 0, 0);
    t2 = new TrafficLight(2, 0, 0);
    t3 = new TrafficLight(3, 0, 0);

    lastCarsWaitingT1 = new Integ(0);
    lastCarsWaitingT2 = new Integ(0);
    lastCarsWaitingT3 = new Integ(0);
    lastCarsWaitingF1 = new Integ(0);
    lastCarsWaitingF2 = new Integ(0);
    lastCarsWaitingF3 = new Integ(0);
    
    int currLight = 1;
    t2.time = 60;
    t2.status = 0;
    userInterface = new GUI();
    //Controls when to update the status of traffic lights. True when first time state changes
    //Change the values of Traffic lights
    Boolean flag = true;
    while(true){
      if(currLight == 1 && flag){
        t1.updateStatus(1);
        t3.updateStatus(0);
      }
      else if(currLight == 2 && flag){
        t2.updateStatus(1);
        t1.updateStatus(0);
      }
      else if(currLight == 3 && flag){
        t3.updateStatus(1);
        t2.updateStatus(0);
      }

      if(t1.isGreen()){
        t1.decreTime();
        t2.decreTime();
        t3.decreTime();
        flag = false;
        if(t1.isTimeZero()){
          currLight = 2;
          flag = true;
        }
      }
      else if(t2.isGreen()){
        t1.decreTime();
        t2.decreTime();
        t3.decreTime();
        flag = false;
        if(t2.isTimeZero()){
          currLight = 3;
          flag= true;
        }
      }
      else if(t3.isGreen()){
        t1.decreTime();
        t2.decreTime();
        t3.decreTime();
        flag = false;
        if(t3.isTimeZero()){
          currLight = 1;
          flag = true;
        }
      }

      //Whether the last car has passed or not 
      if(lastCarsWaitingF1.t != 0)
        lastCarsWaitingF1.t--;
      if(lastCarsWaitingF2.t != 0)
        lastCarsWaitingF2.t--;
      if(lastCarsWaitingF3.t != 0)
        lastCarsWaitingF3.t--;
      if(lastCarsWaitingT1.t != 0)
        lastCarsWaitingT1.t--;
      if(lastCarsWaitingT2.t != 0)
        lastCarsWaitingT2.t--;
      if(lastCarsWaitingT3.t != 0)
        lastCarsWaitingT3.t--;
      
      //Decrease the time on all cars
      for(int i=0;i<carList.size();i++){
        if(carList.get(i).timeLeft !=0)
          carList.get(i).timeLeft--;
        if(carList.get(i).timeLeft <= 6)
          carList.get(i).status = 1;
        if(carList.get(i).timeLeft == 0)
          carList.get(i).status = 2;        
      }
      userInterface.UpdateCarsTable();
      userInterface.UpdateTrafficLights(t1,t2,t3);
      try {
          Thread.sleep(1000);
      } catch (Exception e) {
          System.out.println(e);
      }
     }
  }

  //New Car added using GUI
  public static void AddCar(int path){
    int carNum = carList.size();
    Car newCar = new Car(carNum+1,path);
    completeCarDetails(newCar);
    if(newCar.path != -1)
      carList.add(newCar);

    userInterface.AddCarGUI(newCar);
  }

  //This function updates the last car waiting time by dividing into 2 parts: Light Green or Red. THen we update the waiting time of the 
  //current car using the last car waiting time and the time left on the traffic lights
  public static int getRemainingTime(Car car,TrafficLight t,Integ lastCarsWaiting){
    System.out.println("traffic light :"+t.id+" status: "+t.status+" lastcarwait: "+lastCarsWaiting.t);
    if(t.status == 1){
      if(t.time >=(lastCarsWaiting.t+6)){
        lastCarsWaiting.t += 6;
        return lastCarsWaiting.t;
      }
      else if(t.time >= lastCarsWaiting.t){
        lastCarsWaiting.t = t.time+126;
        return lastCarsWaiting.t;
      }
      else{
        lastCarsWaiting.t+=6;
        return lastCarsWaiting.t;
      }
    }
    else{
      if(lastCarsWaiting.t == 0){
        lastCarsWaiting.t = t.time+6;
        return lastCarsWaiting.t;
      }
      else{
        lastCarsWaiting.t += 6;
        return lastCarsWaiting.t;
      }  
    }
  }

  //Fill the details
  public static void completeCarDetails(Car car){
    if(car.path == 0){
      car.timeLeft = getRemainingTime(car,t3,lastCarsWaitingT3);
    }
    else if(car.path == 1){
      lastCarsWaitingF1.t += 6;
      car.timeLeft = lastCarsWaitingF1.t;
    }
    else if(car.path == 2){
      car.timeLeft = getRemainingTime(car,t2,lastCarsWaitingT2);
    }
    else if(car.path == 3){
      lastCarsWaitingF2.t += 6;
      car.timeLeft = lastCarsWaitingF2.t;
    }
    else if(car.path == 4){
      car.timeLeft = getRemainingTime(car,t1,lastCarsWaitingT1);
    }
    else if(car.path == 5){
      lastCarsWaitingF3.t += 6;
      car.timeLeft = lastCarsWaitingF3.t;
    }

    if(car.timeLeft > 6)
      car.status = 0;
    else if(car.timeLeft == 0)
      car.status = 2;
    else
      car.status = 1;
  }
} 