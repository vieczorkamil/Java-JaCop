package core;
//import JaCoP and java standard libraries
import JaCoP.core.*;
import JaCoP.search.*;
import java.util.ArrayList;
import JaCoP.core.Var;
/**
 * @author Kamil Wieczorek
 */
public abstract class CLP {
    //create variables
    protected Store store = new Store();
    protected DepthFirstSearch search = new DepthFirstSearch();
    protected ArrayList<Var> vars = new ArrayList<Var>();
    public IntVar cost;
    protected long begin, end; //variable needed to calculate time of search
    //create abstract methods
    public abstract void model();
    public abstract void search();
    public abstract String result();
}
