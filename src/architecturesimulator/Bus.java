package architecturesimulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collection;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
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
public class Bus implements ArchtekturObject {

    private final int StrokeDimension = 2;

    public int getStrokeDimension() {
        return StrokeDimension;
    }
    
    Collection<StateUpdateListener> stateChangedListenes = new ArrayList<StateUpdateListener>();

    public Dimension getDimension(java.awt.FontMetrics fm) {
        return getDimension();
    }
    String Value = "";
    State currentState = State.Inactive;

    public void resetState() {
        currentState = State.Inactive;
        fireStateUpdate();
    }
    Path2D BusPath2D = null;

    public Bus() {
    }

    public void paint(Graphics g) {
        if (BusPath2D == null) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        Color oldColor = g2d.getColor();
        if (currentState.equals(State.Active)) {
            g2d.setColor(Color.RED);
        }
        g2d.setStroke(new BasicStroke(StrokeDimension));
        /*  path.moveTo( 80.0, 50.0 );
        path.lineTo( 80.0, 100.0 );
        path.lineTo( 200.0,100.0 );
        path.lineTo( 200.0,200.0 );
        g2d.draw(path);*/
        g2d.draw(BusPath2D);
        
        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    public void setValue(String Value) {
        this.Value = Value;
        currentState = State.Active;
        fireStateUpdate();
    }

    public String getValue() {
        return Value;
    }

    @Override
    public Dimension getDimension() {
        if (BusPath2D == null) {
            return new Dimension();
        } else {
            Rectangle r = BusPath2D.getBounds();
            return new Dimension(r.width+StrokeDimension, r.height+StrokeDimension);
        }
    }

    @Override
    public State getState() {
        return currentState;
    }

    public Path2D getBusPath2D() {
        return BusPath2D;
    }

    public void setBusPath2D(Path2D BusPath2D) {
        this.BusPath2D = BusPath2D;
    }

    private void fireStateUpdate() {
        for (StateUpdateListener stateActiveListener : stateChangedListenes) {
            stateActiveListener.OnStateUpdate(this, currentState);
        }

    }
}
