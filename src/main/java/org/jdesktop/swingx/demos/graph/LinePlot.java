package org.jdesktop.swingx.demos.graph;

import org.jdesktop.swingx.JXGraph.Plot;

import com.expression.parser.Parser;
import com.expression.parser.exception.CalculatorException;
import com.expression.parser.util.Point;

/**
 * implements abstract class JXGraph.Plot
 * 
 * @author EUG https://github.com/homebeaver
 */
public class LinePlot extends Plot {
	
    private String f_x = "2*(x +3)";
    /*
       != null : Parser.eval faild
     */
    private String evalMessage = null;
    
    public LinePlot(String function) {
    	f_x = function;
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * The result can be Double.NaN in case Parser throws CalculatorException,
     * getEvalMessage() in this case
     */
    @Override
    public double compute(double value) {
    	final Point xo = new Point("x", Double.valueOf(value));   	
    	double result = Double.NaN;
    	if(evalMessage==null) try {
			result = Parser.eval(f_x, xo).getValue();
		} catch (CalculatorException e) {
			evalMessage = e.getMessage();
			e.printStackTrace();
		}
    	return result;
    }

    public void setFunction(String function) {
    	if(function!=null && !function.equals(f_x)) {
    		String oldValue = getFunction();
        	f_x = function;
        	evalMessage = null;
            firePropertyChange("function", oldValue, getFunction());
    	}
    }
    public String getFunction() {
    	return f_x;
    }

    public String getEvalMessage() {
    	return evalMessage;
    }

}
