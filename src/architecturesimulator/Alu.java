/*
 * Copyright 2012 Aleksejs Okolovskis <oko@aloko.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package architecturesimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class Alu implements ArchitectureObjectWithBusConnection {

    public Dimension getDimension(java.awt.FontMetrics fm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void resetState() {
        performedAluFunction = null;
    }

    public Orientation getOrientation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOrientation(Orientation orientation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
    
 * @return the name
    
 */
    public String getName() {
        return name;
    }

    /**
    
 * @param name the name to set
    
 */
    public void setName(String name) {
        this.name = name;
    }

    public enum ALUFunction {

        AplusB, AplusOne, AminusOne, AminusB, A, B
    }
    int bit = 0;
    Bus BusA = null;
    Bus BusB = null;
    Bus Out = null;
    int base = 30;
    ALUFunction performedAluFunction = null;
    private String name = "";

    private void selfcheck() throws IOException {
        if (BusA == null || BusB == null || Out == null) {
            throw new IOException("One of the Buses is null");
        }

    }

    public Alu() {
    }

    public Alu(int bit) {
        this.bit = bit;
    }

    public void performFunction(ALUFunction aluf) throws IOException {
        selfcheck();

        switch (aluf) {
            case AminusOne:
                Out.setValue(
                        BinaryFunctions.subTwoK(BusA.getValue(), "1", bit, null));
                break;
            case AminusB:
                Out.setValue(BinaryFunctions.subTwoK(BusA.getValue(), BusB.getValue(), bit, null));

                break;

            case AplusOne:
                Out.setValue(
                        BinaryFunctions.increment(BusA.getValue(), base, null));
                break;
            case AplusB:
                Out.setValue(
                        BinaryFunctions.add(BusA.getValue(), BusB.getValue(), base, null));
                break;
            case A:
                Out.setValue(BusA.getValue());
                break;
            case B:
                Out.setValue(BusB.getValue());
                break;


        }
        performedAluFunction = aluf;

    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Color oldColor = g2d.getColor();
        g2d.setColor(defaultBackgroundColor);
        g2d.fillPolygon(drawPolygon());
        g2d.setColor(oldColor);


        //  g2d.drawRect(0, 0, 10 , (int)(10*1.5));

        // g2d.setPaint(Color.black);
        // g2d.setStroke(new BasicStroke(1));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        Rectangle2D bounds;

        if (performedAluFunction != null) {
            switch (performedAluFunction) {
                case AminusOne:
                    bounds = metrics.getStringBounds("A-1", g);
                    g2d.drawString("A-1", (int) ((base
 * 0.75) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));

                    break;
                case AminusB:
                    bounds = metrics.getStringBounds("A -", g);
                    g2d.drawString("A -", (int) ((base
 * 0.75) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));
                    bounds = metrics.getStringBounds("B", g);
                    g2d.drawString("B", (int) ((base
 * 3.25) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));

                    break;

                case AplusOne:
                    bounds = metrics.getStringBounds("A+1", g);
                    g2d.drawString("A+1", (int) ((base
 * 0.75) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));

                    break;
                case AplusB:
                    bounds = metrics.getStringBounds("A +", g);
                    g2d.drawString("A +", (int) ((base
 * 0.75) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));
                    bounds = metrics.getStringBounds("B", g);
                    g2d.drawString("B", (int) ((base
 * 3.25) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));
          
                    break;
                case A:
                    bounds = metrics.getStringBounds("A", g);
                    g2d.drawString("A", (int) ((base
 * 0.75) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));
                    break;
                case B:
                    bounds = metrics.getStringBounds("B", g);
                    g2d.drawString("B", (int) ((base
 * 3.25) - (bounds.getWidth() / 2)), (int) ((base) - 2 /*- (bounds.getHeight()*0.4)*/));

                    break;


            }
        }

        g2d.drawPolygon(drawPolygon());
        g2d.setColor(oldColor);
    }

    private Polygon drawPolygon() {

        Polygon p = new Polygon();
        p.addPoint(base-1, 0);


        p.addPoint(base
 * 3, 0);
        p.addPoint(base
 * 4-1, base-1);
        p.addPoint(((int) (base
 * 3))-1, base-1);
        p.addPoint(((int) (base
 * 2.5)), ((int) (base
 * 0.5)));
        p.addPoint(((int) (base
 * 1.5))-1, ((int) (base
 * 0.5)));
        p.addPoint(((int) (base)), base-1);
        p.addPoint(0, base-1);
        return p;
    }

    public Bus getBusA() {
        return BusA;
    }

    public void setBusA(Bus BusA) {
        this.BusA = BusA;
    }

    public Bus getBusB() {
        return BusB;
    }

    public void setBusB(Bus BusB) {
        this.BusB = BusB;
    }

    public Bus getOut() {
        return Out;
    }

    public void setOut(Bus Out) {
        this.Out = Out;
    }

    public Dimension getDimension() {
        Rectangle2D re = drawPolygon().getBounds2D();
        Dimension d = new Dimension(((int) (re.getWidth())) + 1, ((int) (re.getHeight())) + 1);
        return d;
    }

    public State getState() {
        return (performedAluFunction != null) ? State.Active : State.Inactive;
    }

    public Collection<Bus> getInvoldedBuses() {
        ArrayList<Bus> t = new ArrayList<Bus>();
        if (Out != null) {
            t.add(Out);
        }
        if (BusA != null) {
            t.add(BusA);
        }

        if (BusB != null) {
            t.add(BusB);
        }

        return t;
    }

    public Point2D getConnectionPoint(Bus b) throws IOException {
        if (b == Out) {
            return new Point2D.Double(getDimension().width / 2, 0);
        } else if (b == BusA) {
            return new Point2D.Double(base
 * 0.5, base);
        } else if (b == BusB) {
            return new Point2D.Double(base
 * 3 + (base
 * 0.5), base);
        }
        throw new IOException("Bus not Connected to ALU.");
    }

    public static ALUFunction stringToALUfunction(String ALUFunctionstring) throws IOException {
        for (ALUFunction aLUFunction : ALUFunction.values()) {
            if (aLUFunction.toString().equals(ALUFunctionstring)) {
                return aLUFunction;
            }
        }
        throw new IOException(ALUFunctionstring + " is not a known ALU function");
    }
}
