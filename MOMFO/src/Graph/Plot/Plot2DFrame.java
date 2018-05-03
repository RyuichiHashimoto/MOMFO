package Graph.Plot;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Plot2DFrame extends PlotFrame{


	public Plot2DFrame(){
		super("Scatterplot");

		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void plot() {
		    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //adding points
	        for(int a = 0; a < 100; a++)
	        {
	            points.add(new Point2D.Float(3, a));
	        }
	        //end adding points

	        JPanel panel = new JPanel() {
	            public void paintComponent(Graphics g) {
	                for(Iterator i=points.iterator(); i.hasNext(); ) {
	                    Point2D.Float pt = (Point2D.Float)i.next();
	                    g.drawString("", (int)(pt.x)+40,
	                        (int)(-pt.y+getHeight())- 40);
	                }
	                int width = getWidth();
	                int height = getHeight();
	                setVisible(true);
	                //axises (axes?)
	                g.drawLine(0, height - 40, width, height-40);
	                g.drawLine(40, height - 270, 40, height);

	                //y-axis labels below
	                for (int a = 1; a < 5; a++)
	                {
	                    String temp = 20*a + "--";
	              //      g.drawString(temp, 20, height - (36 + 20*(a)));
	                }
	                for (int a = 5; a < 11; a++)
	                {
	                    String temp = 20*a + "--";
	             //       g.drawString(temp, 11, height - (36 + 20*(a)));
	                }
	                //y-axis labels above

	                //x-axis labels below
	                for (int a = 1; a < 21; a++)
	                {
	              //      g.drawString("|", 40 + 50*a, height - 30);
	                    int x = 50*a;
	                    String temp = x + " ";
	             //       g.drawString(temp, 30 + 50*a, height - 18);
	                }
	                g.drawString(title, 400, 60);
	                //x-axis labels above
	            }

	        };
	        setContentPane(panel);
	        //last two numbers below change the initial size of the graph.

	        setBounds(20, 20, 110, 40);
	        setVisible(true);
	}

    public static void main(String[] args){
    	PlotFrame plot = new Plot2DFrame();
    	plot.plot();
    	System.out.println("end of");
    }



}
