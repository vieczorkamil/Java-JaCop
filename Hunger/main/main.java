package main;

//import GUI.*;
import GUI.GUI;
import core.HungryBogdan;
/**
 * @author Kamil Wieczorek
 */
public class main {
    public static void main(String[] args){
        System.out.println("Kamil Wieczorek Optimization problem");
        HungryBogdan Bogdan = new HungryBogdan(); //creat object Painting
        //run Painting methods
        Bogdan.model();
        Bogdan.search();
        System.out.println("Search report: \n" + Bogdan.getStatistic());
        System.out.println(Bogdan.result());
       
        GUI gui = new GUI(); //create JFrameForm object
        gui.setTitle("AiS lab 4 Hunger (Optimization problem)"); //set title for GUI
        gui.setVisible(true); //make GUI visible ;
    }
}
