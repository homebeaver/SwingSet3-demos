/*
 * Copyright (c) 2004 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
package swingset;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/*
 * The LayoutControlPanel contains controls for setting an AbstractButton's horizontal and vertical text position 
 * and horizontal and vertical alignment for ButtonDemo
 * 
 * It uses DirectionPanel
 */
public class LayoutControlPanel extends JPanel implements SwingConstants {

    private boolean  absolutePositions;
    private DirectionPanel textPosition = null;
    private DirectionPanel labelAlignment = null;
    private ButtonDemo demo = null;

    // private ComponentOrientChanger componentOrientChanger = null;

    LayoutControlPanel(ButtonDemo demo) {
        this.demo = demo;

        // this.componentOrientationChanger = componentOrientationChanger;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(LEFT_ALIGNMENT);
        setAlignmentY(TOP_ALIGNMENT);

        JLabel l;

        // If SwingSet has a ComponentOrientationChanger, then include control
        // for choosing between absolute and relative positioning.  This will
        // only happen when we're running on JDK 1.2 or above.
        //
        // if(componentOrientationChanger != null ) {
        //     l = new JLabel("Positioning:");
        //     add(l);
        //
        //    ButtonGroup group = new ButtonGroup();
        //    PositioningListener positioningListener = new PositioningListener();
        //    JRadioButton absolutePos = new JRadioButton("Absolute");
        //    absolutePos.setMnemonic('a');
        //    absolutePos.setToolTipText("Text/Content positioning is independant of line direction");
        //    group.add(absolutePos);
        //    absolutePos.addItemListener(positioningListener);
        //    add(absolutePos);
        //
        //    JRadioButton relativePos = new JRadioButton("Relative");
        //    relativePos.setMnemonic('r');
        //    relativePos.setToolTipText("Text/Content positioning depends on line direction.");
        //    group.add(relativePos);
        //    relativePos.addItemListener(positioningListener);
        //    add(relativePos);
        //
        //    add(Box.createRigidArea(demo.VGAP20));
        //
        //    absolutePositions = false;
        //    relativePos.setSelected(true);
        //
        //    componentOrientationChanger.addActionListener( new OrientationChangeListener() );
        //} else {
            absolutePositions = true;
        //}

        textPosition = new DirectionPanel(true, "E", new TextPositionListener());
        labelAlignment = new DirectionPanel(true, "C", new LabelAlignmentListener());

        // Make sure the controls' text position and label alignment match
        // the initial value of the associated direction panel.
        for(int i = 0; i < demo.getCurrentControls().size(); i++) {
            Component c = (Component) demo.getCurrentControls().elementAt(i);
            setPosition(c, RIGHT, CENTER);
            setAlignment(c,CENTER,CENTER);
        }

        l = new JLabel(StaticUtilities.getResourceAsString("LayoutControlPanel.textposition_label", "textposition"));
        ;
        add(l);
        add(textPosition);

        add(Box.createRigidArea(DemoModule.VGAP20));

        l = new JLabel(StaticUtilities.getResourceAsString("LayoutControlPanel.contentalignment_label", "contentalignment"));
        add(l);
        add(labelAlignment);

        add(Box.createGlue());
    }


    class OrientationChangeListener implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            if( !e.getActionCommand().equals("OrientationChanged") ){
                return;
            }
            if( absolutePositions ){
                return;
            }

            String currentTextPosition = textPosition.getSelection();
            if( currentTextPosition.equals("NW") )
                textPosition.setSelection("NE");
            else if( currentTextPosition.equals("NE") )
                textPosition.setSelection("NW");
            else if( currentTextPosition.equals("E") )
                textPosition.setSelection("W");
            else if( currentTextPosition.equals("W") )
                textPosition.setSelection("E");
            else if( currentTextPosition.equals("SE") )
                textPosition.setSelection("SW");
            else if( currentTextPosition.equals("SW") )
                textPosition.setSelection("SE");

            String currentLabelAlignment = labelAlignment.getSelection();
            if( currentLabelAlignment.equals("NW") )
                labelAlignment.setSelection("NE");
            else if( currentLabelAlignment.equals("NE") )
                labelAlignment.setSelection("NW");
            else if( currentLabelAlignment.equals("E") )
                labelAlignment.setSelection("W");
            else if( currentLabelAlignment.equals("W") )
                labelAlignment.setSelection("E");
            else if( currentLabelAlignment.equals("SE") )
                labelAlignment.setSelection("SW");
            else if( currentLabelAlignment.equals("SW") )
                labelAlignment.setSelection("SE");
        }
    }

    class PositioningListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            JRadioButton rb = (JRadioButton) e.getSource();
            if(rb.getText().equals("Absolute") && rb.isSelected()) {
                absolutePositions = true;
            } else if(rb.getText().equals("Relative") && rb.isSelected()) {
                absolutePositions = false;
            }

            for(int i = 0; i < demo.getCurrentControls().size(); i++) {
                Component c = (Component) demo.getCurrentControls().elementAt(i);
                int hPos, vPos, hAlign, vAlign;
                if( c instanceof AbstractButton ) {
                   hPos = ((AbstractButton)c).getHorizontalTextPosition();
                   vPos = ((AbstractButton)c).getVerticalTextPosition();
                   hAlign = ((AbstractButton)c).getHorizontalAlignment();
                   vAlign = ((AbstractButton)c).getVerticalAlignment();
                } else if( c instanceof JLabel ) {
                   hPos = ((JLabel)c).getHorizontalTextPosition();
                   vPos = ((JLabel)c).getVerticalTextPosition();
                   hAlign = ((JLabel)c).getHorizontalAlignment();
                   vAlign = ((JLabel)c).getVerticalAlignment();
                } else {
                    continue;
                }
                setPosition(c, hPos, vPos);
                setAlignment(c, hAlign, vAlign);
            }

            demo.invalidate();
            demo.validate();
            demo.repaint();
        }
    };


    // Text Position Listener
    class TextPositionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JRadioButton rb = (JRadioButton) e.getSource();
            if(!rb.isSelected()) {
                return;
            }
            String cmd = rb.getActionCommand();
            int hPos, vPos;
            if(cmd.equals("NW")) {
                    hPos = LEFT; vPos = TOP;
            } else if(cmd.equals("N")) {
                    hPos = CENTER; vPos = TOP;
            } else if(cmd.equals("NE")) {
                    hPos = RIGHT; vPos = TOP;
            } else if(cmd.equals("W")) {
                    hPos = LEFT; vPos = CENTER;
            } else if(cmd.equals("C")) {
                    hPos = CENTER; vPos = CENTER;
            } else if(cmd.equals("E")) {
                    hPos = RIGHT; vPos = CENTER;
            } else if(cmd.equals("SW")) {
                    hPos = LEFT; vPos = BOTTOM;
            } else if(cmd.equals("S")) {
                    hPos = CENTER; vPos = BOTTOM;
            } else /*if(cmd.equals("SE"))*/ {
                    hPos = RIGHT; vPos = BOTTOM;
            }
            for(int i = 0; i < demo.getCurrentControls().size(); i++) {
                Component c = (Component) demo.getCurrentControls().elementAt(i);
                setPosition(c, hPos, vPos);
            }
            demo.invalidate();
            demo.validate();
            demo.repaint();
        }
    };


    // Label Alignment Listener
    class LabelAlignmentListener implements  ActionListener {
        public void actionPerformed(ActionEvent e) {
            JRadioButton rb = (JRadioButton) e.getSource();
            if(!rb.isSelected()) {
                return;
            }
            String cmd = rb.getActionCommand();
            int hPos, vPos;
            if(cmd.equals("NW")) {
                    hPos = LEFT; vPos = TOP;
            } else if(cmd.equals("N")) {
                    hPos = CENTER; vPos = TOP;
            } else if(cmd.equals("NE")) {
                    hPos = RIGHT; vPos = TOP;
            } else if(cmd.equals("W")) {
                    hPos = LEFT; vPos = CENTER;
            } else if(cmd.equals("C")) {
                    hPos = CENTER; vPos = CENTER;
            } else if(cmd.equals("E")) {
                    hPos = RIGHT; vPos = CENTER;
            } else if(cmd.equals("SW")) {
                    hPos = LEFT; vPos = BOTTOM;
            } else if(cmd.equals("S")) {
                    hPos = CENTER; vPos = BOTTOM;
            } else /*if(cmd.equals("SE"))*/ {
                    hPos = RIGHT; vPos = BOTTOM;
            }
            for(int i = 0; i < demo.getCurrentControls().size(); i++) {
                Component c = (Component) demo.getCurrentControls().elementAt(i);
                setAlignment(c,hPos,vPos);
                c.invalidate();
            }
            demo.invalidate();
            demo.validate();
            demo.repaint();
        }
    };

    // Position
    void setPosition(Component c, int hPos, int vPos) {
        boolean ltr = true;
        ltr = c.getComponentOrientation().isLeftToRight();
        if( absolutePositions ) {
            if( hPos == LEADING ) {
                hPos = ltr ? LEFT : RIGHT;
            } else if( hPos == TRAILING ) {
                hPos = ltr ? RIGHT : LEFT;
            }
        } else {
            if( hPos == LEFT ) {
                hPos = ltr ? LEADING : TRAILING;
            } else if( hPos == RIGHT ) {
                hPos = ltr ? TRAILING : LEADING;
            }
        }
        if(c instanceof AbstractButton) {
            AbstractButton x = (AbstractButton) c;
            x.setHorizontalTextPosition(hPos);
            x.setVerticalTextPosition(vPos);
        } else if(c instanceof JLabel) {
            JLabel x = (JLabel) c;
            x.setHorizontalTextPosition(hPos);
            x.setVerticalTextPosition(vPos);
        }
    }

    void setAlignment(Component c, int hPos, int vPos) {
        boolean ltr = true;
        ltr = c.getComponentOrientation().isLeftToRight();
        if( absolutePositions ) {
            if( hPos == LEADING ) {
                hPos = ltr ? LEFT : RIGHT;
            } else if( hPos == TRAILING ) {
                hPos = ltr ? RIGHT : LEFT;
            }
        } else {
            if( hPos == LEFT ) {
                hPos = ltr ? LEADING : TRAILING;
            } else if( hPos == RIGHT ) {
                hPos = ltr ? TRAILING : LEADING;
            }
        }
        if(c instanceof AbstractButton) {
            AbstractButton x = (AbstractButton) c;
            x.setHorizontalAlignment(hPos);
            x.setVerticalAlignment(vPos);
        } else if(c instanceof JLabel) {
            JLabel x = (JLabel) c;
            x.setHorizontalAlignment(hPos);
            x.setVerticalAlignment(vPos);
        }
    }
}
