package core;
//import JaCoP and java standard libraries
import JaCoP.constraints.Or;
import JaCoP.constraints.PrimitiveConstraint;
import JaCoP.constraints.XeqC;
import JaCoP.constraints.XlteqC;
import JaCoP.constraints.XplusYeqC;
import JaCoP.constraints.knapsack.Knapsack;
import JaCoP.core.IntVar;
import JaCoP.search.IndomainMax;
import JaCoP.search.InputOrderSelect;
import JaCoP.search.SelectChoicePoint;


/**
 * @author Kamil Wieczorek
 */
public class HungryBogdan extends CLP{
    
    @Override
    public void model() {
            //15
        int noItems = 15; //number of item 
        int limit = 25; //prices limit
        String[] food = {"Tomato soup with rice", 
                         "Chicken soup with noodles", 
                         "Roasted chicken fillet with cheese", 
                         "Roasted pork",
                         "Bigos",
                         "Boiled potatoes",
                         "French fries",
                         "Rice",
                         "Pancakes with white cheese",
                         "Coleslaw salad",
                         "Red cabbage",
                         "Orange juice",
                         "Water",
                         "Cola",
                         "Beer"
                        };
        int[] prices = { 6, 7, 10, 6, 6, 3, 5, 3, 12, 4, 4, 4, 3, 5, 5 }; //weights 
        int[] calories = { 320, 340, 560, 160, 224, 130, 330, 250, 460, 100, 160, 100, 1, 100, 110 }; //profits
        //Water must be 1 calorie
        
        // I-th variable represents if i-th item is taken
        IntVar quantity[] = new IntVar[noItems];

        // Each quantity variable has a domain from 0 to max value
        for (int i = 0; i < quantity.length; i++) {
                quantity[i] = new IntVar(store, "Quantity_" + food[i], 0, 1);//0, 1 -  each dish may be chosen only once
                vars.add(quantity[i]);
            }

        IntVar sumCalories = new IntVar(store, " Sum of calories", 0, 1000000);
        IntVar maxPrices = new IntVar(store, " Max prices", 0, 25);//0,25
        
        PrimitiveConstraint healthyFood1 = new XeqC(quantity[9],1);//salad
        PrimitiveConstraint healthyFood2 = new XeqC(quantity[10],1);//salad
        PrimitiveConstraint healthyFood3 = new XeqC(quantity[11],1);//juice
        
        //PrimitiveConstraint[] healthyFood = {healthyFood1,healthyFood2,healthyFood3};
        store.impose(new Or(healthyFood1,new Or(healthyFood2,healthyFood3)));
        

        store.impose(new Knapsack(calories, prices, quantity, maxPrices, sumCalories));

        store.impose(new XlteqC(maxPrices, limit));

        IntVar profitNegation = new IntVar(store, " Sum of calories", -100000, 0);

        store.impose(new XplusYeqC(sumCalories, profitNegation, 0));

        cost = profitNegation;
        vars.add(sumCalories);
        vars.add(maxPrices);

    }

    @Override
    public void search() {
        begin = System.currentTimeMillis();//start time
	IntVar[] values = new IntVar[vars.size()];
        values=vars.toArray(values);
	SelectChoicePoint select = new InputOrderSelect(store, values, new IndomainMax());
	boolean result = search.labeling(store, select, cost);//return true if find solutin
	end = System.currentTimeMillis();//stop time
	if (result){
            System.out.println("Solution(s) found");
        }
	else
            System.out.println(" No Solution ");
    }

    @Override
    public String result() {
        String result;
        result = vars.toString();
        //making our print result looks better
        result=result.replace(",", "\n");
        result=result.replace("["," ");
        result=result.replace("Quantity_"," ");
        result=result.replace("]","");
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
