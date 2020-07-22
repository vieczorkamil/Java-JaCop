/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import JaCoP.constraints.Alldifferent;
import JaCoP.constraints.Or;
import JaCoP.constraints.XeqC;
import JaCoP.constraints.XeqY;
import JaCoP.constraints.XplusCeqZ;
import JaCoP.core.IntVar;
import JaCoP.core.Var;
import JaCoP.search.IndomainMin;
import JaCoP.search.MostConstrainedStatic;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleSelect;

/**
 *
 * @author Kamil Wieczorek
 */
public class Ships extends CLP{
    
    @Override
    public void model() {
    
    String[] nation = {"Greek", "English", "French", "Belgian", "Spanish"};
    int iGreek=0, iEnglish=1, iFrench=2, iBelgian=3, iSpanish=4;
    
    String [] goods = {"Coffe", "Cocoa", "Corn", "Rice", "Tea"};
    int iCoffe=0, iCocoa=1, iCorn=2, iRice=3, iTea=4;
    
    String[] destination = {"Marseille", "Manila", "Genoa", "Hamburg", "PortSaid"};
    int iMarseille=0, iManila=1, iGenoa=2, iHamburg=3, iPortSaid=4;
    
    String[] chimney = {"Black", "Blue", "Green", "Red", "White"};
    int iBlack=0, iBlue=1, iGreen=2, iRed=3, iWhite=4;
    
    String[] hour = {"Six", "Nine", "Five", "Seven", "Eight"};
    int iSix=0, iNine=1, iFive=2, iSeven=3, iEight=4;
    
    IntVar Nation[] = new IntVar[5];
    IntVar Goods[] = new IntVar[5];
    IntVar Destination[] = new IntVar[5];
    IntVar Chimney[] = new IntVar[5];
    IntVar Hour[] = new IntVar[5];
    
    //Defining those arrays
    for(int i=0; i<5; i++){    
            Nation[i] = new IntVar(store, nation[i], 1, 5);
            Goods[i] = new IntVar(store, goods[i], 1, 5);
            Destination[i] = new IntVar(store, destination[i], 1, 5);
            Chimney[i] = new IntVar(store, chimney[i], 1, 5);
            Hour[i] = new IntVar(store, hour[i], 1, 5);
            vars.add(Nation[i]); 
            vars.add(Goods[i]); 
            vars.add(Destination[i]);
            vars.add(Chimney[i]);
            vars.add(Hour[i]);
        }
    //basic constraints
    store.impose(new Alldifferent(Nation));
    store.impose(new Alldifferent(Goods));
    store.impose(new Alldifferent(Destination));
    store.impose(new Alldifferent(Chimney));
    store.impose(new Alldifferent(Hour));
    //another constraints
    //The Greek ship leaves at six and carries coffee
    store.impose(new XeqY (Nation[iGreek], Goods[iCoffe]));
    store.impose(new XeqY(Nation[iGreek], Hour[iSix]));
    //The ship in the middle has a black chimney
    store.impose(new XeqC(Chimney[iBlack], 3));
    //The English ship leaves at nine
    store.impose(new XeqY(Nation[iEnglish], Hour[iNine]));
    //The French ship with a blue chimney is to the left of a ship that carries coffee.
    store.impose(new XeqY (Nation[iFrench], Chimney[iBlue]));
    store.impose(new XplusCeqZ(Nation[iFrench], 1, Goods[iCoffe]));
    //To the right of the ship carrying cocoa is a ship going to Marseille
    store.impose(new XplusCeqZ(Goods[iCocoa], 1, Destination[iMarseille]));
    //The Belgian ship is heading for Manila
    store.impose(new XeqY (Nation[iBelgian], Destination[iManila]));
    //Next to the ship carrying rice is a ship with a green chimney. 
    store.impose(new Or(new XplusCeqZ(Goods[iRice], 1, Chimney[iGreen]),new XplusCeqZ(Chimney[iGreen], 1, Goods[iRice])));
    //A ship going to Genoa leaves at five
    store.impose(new XeqY(Destination[iGenoa], Hour[iFive]));
    //The Spanish ship leaves at seven and is to the right of the ship going to Marseille
    store.impose(new XeqY(Nation[iSpanish], Hour[iSeven]));
    store.impose(new XplusCeqZ(Destination[iMarseille], 1, Nation[iSpanish]));
    //The ship with a red chimney goes to Hamburg
    store.impose(new XeqY(Chimney[iRed], Destination[iHamburg]));
    //Next to the ship leaving at seven is a ship with a white chimney
    store.impose(new Or(new XplusCeqZ(Hour[iSeven], 1, Chimney[iWhite]),new XplusCeqZ(Chimney[iWhite], 1, Hour[iSeven])));
    //The ship on the border carries corn
    store.impose(new Or(new XeqC(Goods[iCorn], 1),new XeqC(Goods[iCorn], 5)));
    //The ship with a black chimney leaves at eight.
    store.impose(new XeqY(Chimney[iBlack], Hour[iEight]));
    //The ship carrying corn is anchored next to the ship carrying rice
    store.impose(new Or(new XplusCeqZ(Goods[iCorn], 1, Goods[iRice]), new XplusCeqZ(Goods[iRice], 1, Goods[iCorn])));
    //The ship to Hamburg leaves at six
    store.impose(new XeqY(Destination[iHamburg], Hour[iSix]));
    
    }

    @Override
    public void search() {
        
        begin = System.currentTimeMillis();//start time
        
	SelectChoicePoint select = new SimpleSelect( vars.toArray(new Var[1]), new MostConstrainedStatic(), new IndomainMin());
	boolean result = search.labeling(store, select); //return true if find solutin
       
        end = System.currentTimeMillis();//stop time
        
	if (result)
            System.out.println("Solution(s) found");
        else
            System.out.println(" No Solution ");
    }

    @Override
    public String result() { //methods convert to String and return our solution. Needed in GUI
        String result;
        result = vars.toString();
        return result;
    }
    public String getStatistic(){ //methods return some statistic of our search. Needed in GUI
        String stats;
        stats = "Nodes : " + search.getNodes() + "\n" +
                "Decisions : " + search.getDecisions() + "\n" +
                "Wrong Decisions : " + search.getWrongDecisions() + "\n" +
                "Backtracks : " + search.getBacktracks() + "\n" +
                "Max Depth : " + search.getMaximumDepth() + "\n" +
                "Time of search in milliseconds: " + (end - begin) + "\n";
        return stats;
    }
}
