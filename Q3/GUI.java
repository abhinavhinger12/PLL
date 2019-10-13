import javax.swing.*;  
import java.awt.*;
import java.util.ArrayList;

import javax.swing.table.*;


//User Interface classs
/*
Radio Buttons to select the car path
Two tables sstore the data for Traffic Lights and Car data and is updated at every second 

*/
class GUI {  

    //unique carID
    int carCounter = 1;
    JFrame f;
    JButton inputCar;
    JTable trafficLightStatus;
    JTable carStatus;
    // DefaultTableModel model = new DefaultTableModel();
    JLabel sourceLabel;
    JLabel destinationLabel;
    //Radiobuttons to choose source and destination of the input car
    DefaultTableModel trafficModel;
    DefaultTableModel carModel;
    JRadioButton sE;
    JRadioButton sW;
    JRadioButton sS;
    JRadioButton dE;
    JRadioButton dW;
    JRadioButton dS;

    ButtonGroup G1;
    ButtonGroup G2;

    JLabel timeLeft;
    Timer timer;

    public GUI(){

        JFrame f=new JFrame();//creating instance of JFrame  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridLayout(4,0));
        JButton inputCar=new JButton("Insert Car");//creating instance of JButton

        inputCar.setBounds(400,40,120, 30);//x axis, y axis, width, height 
        inputCar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                System.out.println(AddCarButtonClicked(evt));
                Main.AddCar(AddCarButtonClicked(evt));
            }
        });

        // f.add(inputCar);//adding button in JFrame  
        sW = new JRadioButton("West");
        //sW.setBounds(50,20,40,20);
        dW = new JRadioButton("West");
        //dW.setBounds(150,20,40,20);
        sE = new JRadioButton("East");
        //sE.setBounds(50,50,40,20);
        dE = new JRadioButton("East");
        //dE.setBounds(150,50,40,20);
        sS = new JRadioButton("South");
       //sS.setBounds(50,80,40,20);
        dS = new JRadioButton("South");
        //dS.setBounds(150,80,40,20);

        //Radio Group 
        G1 = new ButtonGroup();
        G1.add(sW);
        G1.add(sE);
        G1.add(sS);

        G2 = new ButtonGroup();
        G2.add(dW);
        G2.add(dE);
        G2.add(dS);

        sourceLabel = new JLabel();
        destinationLabel = new JLabel();

        sourceLabel.setText("Choose SOURCE");
        destinationLabel.setText("Choose DESTINATION");
        JPanel selector = new JPanel();
        selector.add(sourceLabel);
        selector.add(sW);
        selector.add(sE);
        selector.add(sS);
        JPanel selector2 = new JPanel();
        //selector2.setLocation(100,300);
        selector2.add(destinationLabel);
        selector2.add(dW);
        selector2.add(dE);
        selector2.add(dS);

        JPanel holder = new JPanel();
        holder.setLayout(new BoxLayout(holder,BoxLayout.Y_AXIS));
        holder.add(selector);
        holder.add(selector2);
        holder.add(inputCar);

        trafficModel = new DefaultTableModel();
        trafficLightStatus = new JTable(trafficModel);
        trafficModel.addColumn("Traffic Light");
        trafficModel.addColumn("Status");
        trafficModel.addColumn("Time");
        trafficModel.addRow(new Object[]{"T1","Red","--"});              
        trafficModel.addRow(new Object[]{"T2","Red","--"});              
        trafficModel.addRow(new Object[]{"T3","Red","--"});              

       
        JScrollPane selector3 = new JScrollPane(trafficLightStatus);
        
        carModel = new DefaultTableModel();
        carStatus = new JTable(carModel);
        carModel.addColumn("Vehicle");
        carModel.addColumn("Source");
        carModel.addColumn("Destination");
        carModel.addColumn("Status");
        carModel.addColumn("Remaining Time");


        
        JScrollPane selector4 = new JScrollPane(carStatus);

        f.add(holder);
        f.add(selector3);
        f.add(selector4);
        f.pack();
        
        f.setLayout(null);//using no layout managers  
        f.setVisible(true);//making the frame visible

        
         
    }

    //Add a Car to the GUI on click of Mouse1
    public void AddCarGUI(Car newCar){
        
        switch (newCar.path) { 
            case 0:
                carModel.addRow(new Object[]{newCar.carId,"East","West",newCar.status, newCar.timeLeft});
                break;
            case 1: 
                carModel.addRow(new Object[]{newCar.carId,"East","South",newCar.status, newCar.timeLeft});
                break; 
            case 2: 
                carModel.addRow(new Object[]{newCar.carId,"West","South",newCar.status, newCar.timeLeft});
                break; 
            case 3: 
                carModel.addRow(new Object[]{newCar.carId,"West","East",newCar.status, newCar.timeLeft}); 
                break; 
            case 4: 
                carModel.addRow(new Object[]{newCar.carId,"South","East",newCar.status, newCar.timeLeft}); 
                break; 
            case 5: 
                carModel.addRow(new Object[]{newCar.carId,"South","West",newCar.status, newCar.timeLeft});
                break; 
            default: 
                System.out.println("Car not reaching crossing"); 
                break; 
            } 
    }

    //Update the car Table receing data from Main every second
    public void UpdateCarsTable(){
        String statusWord;
        for(int i=0;i<Main.carList.size();i++){
            if(Main.carList.get(i).status == 0)
                statusWord = "Waiting";
            else if(Main.carList.get(i).status == 1)
                statusWord = "Crossing";
            else if(Main.carList.get(i).status == 2)
                statusWord = "Passed";
            else
                statusWord = "Unknown";
            carModel.setValueAt(statusWord, i, 3);
            carModel.setValueAt(Main.carList.get(i).timeLeft, i, 4);
        }
    }
    //Update the car Table receing data from Main every second
    public void UpdateTrafficLights(TrafficLight t1, TrafficLight t2, TrafficLight t3){
        String t1StatusWord;
        String t2StatusWord;
        String t3StatusWord;
        if(t1.status == 1)
            t1StatusWord = "Green";
        else if(t1.status == 0)
            t1StatusWord = "Red";
        else
            t1StatusWord = "Unknown";
        
        if(t2.status == 1)
            t2StatusWord = "Green";
        else if(t2.status == 0)
            t2StatusWord = "Red";
        else
            t2StatusWord = "Unknown";

        if(t3.status == 1)
            t3StatusWord = "Green";
        else if(t3.status == 0)
            t3StatusWord = "Red";
        else
            t3StatusWord = "Unknown";

        trafficModel.setValueAt(t1StatusWord, 0, 1);
        if(t1.status == 1)
            trafficModel.setValueAt(t1.time, 0, 2);
        else
            trafficModel.setValueAt("--", 0, 2);            
        trafficModel.setValueAt(t2StatusWord, 1, 1);
        if(t2.status == 1)
            trafficModel.setValueAt(t2.time, 1, 2);
        else
            trafficModel.setValueAt("--", 1, 2);
        trafficModel.setValueAt(t3StatusWord, 2, 1);
        if(t3.status == 1)
            trafficModel.setValueAt(t3.time, 2, 2);
        else
            trafficModel.setValueAt("--", 2, 2);
    }

    //Returns the car type where to where
    
    public int AddCarButtonClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        
        if(sE.isSelected() && dW.isSelected()) return 0;
        if(sE.isSelected() && dS.isSelected()) return 1;
        if(sW.isSelected() && dS.isSelected()) return 2;
        if(sW.isSelected() && dE.isSelected()) return 3;
        if(sS.isSelected() && dE.isSelected()) return 4;
        if(sS.isSelected() && dW.isSelected()) return 5;
        return -1;
        
    }

}  