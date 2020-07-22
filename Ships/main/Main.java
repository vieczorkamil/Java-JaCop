/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import GUI.*;
import core.Ships;

/**
 *
 * @author Kamil Wieczorek
 */
public class Main {
    public static void main(String[] args){
        System.out.println("Kamil Wieczorek Combinatorial problem");
        Ships ship = new Ships(); //creat object Ships
        //run Ships methods
        ship.model();
        ship.search();
        System.out.println("Our solution: " + ship.result() + "\n");
        System.out.println("Search report: \n" + ship.getStatistic());
        
        GUI gui = new GUI(); //create JFrameForm object
	gui.setTitle("AiS Ships (Combinatorial problem)"); //set title for GUI
        gui.setVisible(true); //make GUI visible ;
  
    }
}
