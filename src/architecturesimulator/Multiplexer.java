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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class Multiplexer implements ArchitectureObjectWithBusConnection {
    private State currentState;

    public Dimension getDimension(java.awt.FontMetrics fm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    int base = 20;
    Bus Output = null;
    Map<Integer, Bus> InputBuses = new HashMap<Integer, Bus>();
    Bus SelectedInput = null;
    Point2D Position = new Point2D.Double(100, 200);
    private String name ="";

    public Multiplexer() {
    }

    public Multiplexer(Bus output) {
        this.Output = output;

    }

    public Multiplexer(Bus output, Map InputBuses) {
        this.Output = output;
        this.InputBuses = InputBuses;
    }

    public Map<Integer,Bus> getInputBuses() {
        return InputBuses;
    }

    public Bus getOutput() {
        return Output;
    }

 

    public void setOutput(Bus Output) {
        this.Output = Output;
    }

    public void selectBusNr(int NR) throws IOException {
        if (NR < 0) {
            throw new IOException("Negative Bus Nr.");
        }
        if (NR > InputBuses.size() - 1) {
            throw new IOException("Cannt Select Bus, Nr. is bigger than available Busses");
        }
        SelectedInput = InputBuses.get(NR);
        currentState = State.Active;
    }

  /*  public void selectBus(Bus b) throws IOException {
        if (InputBuses.containsValue(b)) {
            currentState = State.Active;
            SelectedInput = b;
        } else {
            throw new IOException("Your Bus is unkown by the multiplexer");
        }
    }
 */

    public Bus getSelectedInput() {
        return SelectedInput;
    }

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        //  g2d.drawRect(0, 0, 10 , (int)(10*1.5));
        Color oldColor = g2d.getColor();
        Color oldBackColor = g2d.getBackground();
        Stroke oldStroke = g2d.getStroke();
       // g2d.setPaint(Color.black);
       // g2d.setStroke(new BasicStroke(1));

        Polygon p = new Polygon();
        p.addPoint(base-1, 0);
        int x = 0;
        for (Bus bus : InputBuses.values()) {
            x = x + base;
        }
        x = (int) (x
 * 1.5);
        p.addPoint(x, 0);
        p.addPoint(x + base-1, base-1);
        p.addPoint(0, base-1);

        
        g2d.setColor(defaultBackgroundColor);
        g2d.fillPolygon(p);
        
        g2d.setColor(oldColor);
        g2d.drawPolygon(p);
        
        
        try {
          if (currentState.equals(State.Active)){
          g2d.setColor(Color.RED);
          }
          g2d.setStroke(new BasicStroke(2));
          Point2D start = getConnectionPoint(Output);
          Point2D end = getConnectionPoint(getSelectedInput());
          g2d.drawLine((int)start.getX(),(int) start.getY(), (int)end.getX(),(int) end.getY()-2);              
        } catch (Exception e) {
        }
        
        
        g2d.setColor(oldColor);
        g2d.setBackground(oldBackColor);
        g2d.setStroke(oldStroke);
    }

    public void addInputBus(Bus b, int channel) {
        InputBuses.put(channel,b);
    }

    public void performeAction() {
        Output.setValue(SelectedInput.getValue());

    }

    public Point2D getPosition() {
        return Position;
    }

    public void setPosition(Point2D Position) {
        this.Position = Position;
    }

    public Dimension getDimension() {
        return new Dimension((int)(base*InputBuses.size()*1.5+base)+1, base+1);
    }

  
    public State getState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Collection<Bus> getInvoldedBuses() {
        ArrayList<Bus> t = new ArrayList<Bus>();
        if (Output != null) {
            t.add(Output);
        }
        for (Bus bus : InputBuses.values()) {
            if (bus != null) {
                t.add(bus);
            }
        }

        return t;
    }

    public Point2D getConnectionPoint(Bus b) throws IOException {
        Dimension d = getDimension();
        if (b == Output) {
            return new Point2D.Double(d.getWidth() / 2, 0);
        } else if (InputBuses.containsValue(b)) {
            int i = 0;
            while (!(InputBuses.get(i).equals(b))){i++;}
            return new Point2D.Double((i+1)
 * (d.width / (InputBuses.size()+1)), base);
        }
        throw new IOException("Bus not Connected to Multiplexer.");
    }

    public boolean hasChannel (Integer i) {
       return InputBuses.containsKey(i);
    }

    public void resetState() {
         currentState = State.Inactive;
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
}
