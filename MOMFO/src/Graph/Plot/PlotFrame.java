package Graph.Plot;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


public abstract class PlotFrame extends JFrame{

    protected List points = new ArrayList();

    protected String title = "plot(Default)";


    public PlotFrame(String plotType){
    	super(plotType);
    }

    abstract public void plot();



    public static void main(String[] args){
//    	PlotFrame plot = new PlotFrame();

    	System.out.println("end of");
    }


}
