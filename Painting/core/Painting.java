package core;

import JaCoP.constraints.Cumulative;
import JaCoP.constraints.XeqY;
import JaCoP.constraints.XplusYlteqZ;
import JaCoP.core.IntVar;
import JaCoP.core.Var;
import JaCoP.search.IndomainMin;
import JaCoP.search.SelectChoicePoint;
import JaCoP.search.SimpleSelect;
import JaCoP.search.SmallestMin;

/**
 * @author Kamil Wieczorek
 */
public class Painting extends CLP{
    
    //types of elements
    IntVar[] a = new IntVar[5];
    IntVar[] b = new IntVar[5];
    IntVar[] c = new IntVar[5];
    //durations of task for every element    
    IntVar[][] durations = new IntVar[5][];        
        
    @Override
    public void model() {
		
        //duration of every process
	IntVar[] red = new IntVar[3];
	red[0] = new IntVar(store, "durationARed", 15, 15);
	red[1] = new IntVar(store, "durationBRed", 15, 15);
	red[2] = new IntVar(store, "durationCRed", 14, 14);
        
        IntVar[] green = new IntVar[3];
	green[0] = new IntVar(store, "durationAGreen", 10, 10);
	green[1] = new IntVar(store, "durationBGreen", 30, 30);
	green[2] = new IntVar(store, "durationCGreen", 22, 22);
        
        IntVar[] oven = new IntVar[3];
	oven[0] = new IntVar(store, "durationAOven", 15, 15);
	oven[1] = new IntVar(store, "durationBOven", 8, 8);
	oven[2] = new IntVar(store, "durationCOven", 15, 15);
        
        IntVar[] lacquer = new IntVar[3];
	lacquer[0] = new IntVar(store, "durationALacquer", 10, 10);
	lacquer[1] = new IntVar(store, "durationBLacquer", 10, 10);
	lacquer[2] = new IntVar(store, "durationCLacquer", 8, 8);
      
        IntVar[] enamel = new IntVar[3];
	enamel[0] = new IntVar(store, "durationAEnamel", 5, 5);
	enamel[1] = new IntVar(store, "durationBEnamel", 15, 15);
	enamel[2] = new IntVar(store, "durationCEnamel", 3, 3);

	durations[0] = red;
	durations[1] = green;
	durations[2] = oven;
	durations[3] = lacquer;
        durations[4] = enamel;
   
        String[] nameProces = new String[] {"red", "green", "oven","lacquer","enamel"}; // id 
        for (int i = 0; i < 5; i++) {
            
            //we start painting all elements at the same time
            a[i] = new IntVar(store, "A [" + nameProces[i] + "]", 0, 1000);
            b[i] = new IntVar(store, "B [" + nameProces[i] + "]", 0, 1000);
            c[i] = new IntVar(store, "C [" + nameProces[i] + "]", 0,1000);

            vars.add(a[i]);
            vars.add(b[i]);
            vars.add(c[i]);
        }
        
	IntVar one = new IntVar(store, "one", 1, 1);
        IntVar two = new IntVar(store, "two", 1, 2);
	IntVar[] three = new IntVar[3];
	IntVar[] threeOnes = { one, one, one };

        //red
	three[0] = a[0];
	three[1] = b[0];
	three[2] = c[0];
	// red painting is visit at any time by only one element
	store.impose(new Cumulative(three, red, threeOnes, one));

        //green
	three[0] = a[1];
	three[1] = b[1];
	three[2] = c[1];
	//green painting is visit at any time by only one element
	store.impose(new Cumulative(three, green, threeOnes, one));

        //oven
        three[0] = a[2];
	three[1] = b[2];
	three[2] = c[2];
        //appropriate drying tasks should start at the same time
        store.impose(new XeqY(a[2],c[2]));
        store.impose(new Cumulative(three, oven, threeOnes, two));
        
        //lacquer
        three[0] = a[3];
	three[1] = b[3];
	three[2] = c[3];
	//lacquer machine is visit at any time by only one element
	store.impose(new Cumulative(three, lacquer, threeOnes, one));
        
        //enamel
        three[0] = a[4];
	three[1] = b[4];
	three[2] = c[4];
	//enamel machine is visit at any time by only one element
	store.impose(new Cumulative(three, enamel, threeOnes, one));

        //***************If want change possible time of finish process needs to be changed this line of code******************
	IntVar endTime = new IntVar(store, "endTime", 0, 150);//limit - finish the whole process in: 2:30 hours
        cost = endTime; //will be minimized
	
        
        int[] aPrecedence = { 1, 2, 3, 4, 5 }; //precedence of proces for element a 
	// Constraints imposed below in for loop make sure that
        // all element are applied for machine in order
	for (int i = 0; i < 4; i++)
            store.impose(new XplusYlteqZ
                (a[aPrecedence[i] - 1],
		 durations[aPrecedence[i] - 1][0],
                 a[aPrecedence[i + 1] - 1])
        );

	// The limit is at least equal to
	// the time point when a finishes enamel
	store.impose(new XplusYlteqZ(a[4], enamel[0], endTime));

	int[] bPrecedence = { 2, 1, 3, 5, 4 };//precedence of proces for element b
	// Constraints imposed below in for loop make sure that
        // all element are applied for machine in order
	for (int i = 0; i < 4; i++)
            store.impose(new XplusYlteqZ
                (b[bPrecedence[i] - 1],
		 durations[bPrecedence[i] - 1][1],
                 b[bPrecedence[i + 1] - 1])
        );

	// The limit is at least equal to
	// the time point when b finishes lacquer
	store.impose(new XplusYlteqZ(b[3], lacquer[1], endTime));

	int[] cPrecedence = { 2, 1, 3, 4, 5 };//precedence of proces for element c 
	// Constraints imposed below in for loop make sure that
        // all element are applied for machine in order
	for (int i = 0; i < 4; i++)
            store.impose(new XplusYlteqZ
                (c[cPrecedence[i] - 1],
		 durations[cPrecedence[i] - 1][2],
                 c[cPrecedence[i + 1] - 1])
        );

	// The limit is at least equal to
	// the time point when c finishes enamel
	store.impose(new XplusYlteqZ(c[4], enamel[2], endTime));

	vars.add(endTime);
		
    }

    @Override
    public void search() {
        begin = System.currentTimeMillis();//start time
        
        SelectChoicePoint select = new SimpleSelect(vars.toArray(new Var[1]), new SmallestMin(), new IndomainMin());
	boolean result = search.labeling(store, select, cost);//return true if find solutin
        
	end = System.currentTimeMillis();//stop time
	if (result){
            System.out.println("Solution(s) found");
        }
	else
            System.out.println(" No Solution ");
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
    public String timeConverter(int min){ //method needed to change minutes for hours
        final int hour = min / 60, minute = min % 60;
        String time = (hour < 10 ? "0" : "") + hour;
        time += ":";
        time += (minute < 10 ? "0" : "") + minute;
        return time;
    }
    
    @Override
    public String result(){
        /*
            in this method i change our scheduling solution for String and also changs times(minutes for hours)
            thanks to this we can display it in gui with better and clearly look
        */
        String stringResult = "\n";
        stringResult=stringResult+"A:\n";
            
            for (int i = 0; i < 5; i++) {
                String startTime = a[i].domain.toString();
                String endTime = durations[i][0].domain.toString();
                int clockTime = Integer.parseInt(startTime);
                int clockEnd = Integer.parseInt(endTime);
                 stringResult=stringResult+(a[i].id+" - Start time : "
                                    +timeConverter(clockTime)
                                    +"  End time : "+timeConverter(clockEnd+clockTime)
                                  );
                 stringResult=stringResult+"\n";
            }
            
        stringResult=stringResult+"\nB:\n";
            
            for (int i = 0; i < 5; i++) {
                String startTime = b[i].domain.toString();
                String endTime = durations[i][1].domain.toString();
                int clockTime = Integer.parseInt(startTime);
                int clockEnd = Integer.parseInt(endTime);
                stringResult=stringResult+(b[i].id+" - Start time : "
                                    +timeConverter(clockTime)
                                    +"  End time : "+timeConverter(clockEnd+clockTime)
                                  );
                stringResult=stringResult+"\n";
            }
            
        stringResult=stringResult+"\nC:\n";    
            for (int i = 0; i < 5; i++) {
                String startTime = c[i].domain.toString();
                String endTime = durations[i][2].domain.toString();
                int clockTime = Integer.parseInt(startTime);
                int clockEnd = Integer.parseInt(endTime);
                stringResult=stringResult+(c[i].id+" - Start time : "
                                    +timeConverter(clockTime)
                                    +"  End time : "+timeConverter(clockEnd+clockTime)
                                  );
                 stringResult=stringResult+"\n";
            }
   
        String solutionTime = cost.domain.toString();
        int clockSolution = Integer.parseInt(solutionTime);
        stringResult=stringResult+"\nEnd time of solution : "+timeConverter(clockSolution);
        
        return stringResult;
    }
}
