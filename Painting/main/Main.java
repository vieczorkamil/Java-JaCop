package main;

import GUI.*;
import core.Painting;

/**
 * @author Kamil Wieczorek
 */
public class Main {
    public static void main(String[] args){
        System.out.println("Kamil Wieczorek Scheduling problem");
        Painting paint = new Painting(); //creat object Painting
        //run Painting methods
        paint.model();
        paint.search();
        System.out.println("Search report: \n" + paint.getStatistic());
        System.out.println(paint.result());
       
        GUI gui = new GUI(); //create JFrameForm object
        gui.setTitle("AiS Painting (Scheduling problem)"); //set title for GUI
        gui.setVisible(true); //make GUI visible ;
    }
}
