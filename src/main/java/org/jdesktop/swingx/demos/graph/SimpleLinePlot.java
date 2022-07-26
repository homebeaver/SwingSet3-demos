/* Copyright 2007-2009 Sun Microsystems, Inc.  All Rights Reserved.
Copyright notice, list of conditions and disclaimer see LICENSE file
*/ 
package org.jdesktop.swingx.demos.graph;

import org.jdesktop.swingx.JXGraph.Plot;

/**
 * A simple graph plot that can be updated.
 * 
 * @author Karl George Schaefer
 */
public class SimpleLinePlot extends Plot {
    private double coefficient = 1.0;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double compute(double value) {
        return value * getCoefficient();
    }

    /**
     * @param coefficient the coefficient to set
     */
    public void setCoefficient(double coefficient) {
        double oldValue = getCoefficient();
        this.coefficient = coefficient;
        firePropertyChange("coefficient", oldValue, getCoefficient());
    }

    /**
     * @return the coefficient
     */
    public double getCoefficient() {
        return coefficient;
    }

}
