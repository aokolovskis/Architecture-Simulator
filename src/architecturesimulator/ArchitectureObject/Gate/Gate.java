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
package architecturesimulator.ArchitectureObject.Gate;

import architecturesimulator.ArchitectureObjectWithBusConnection;
import architecturesimulator.Bus;
import architecturesimulator.Orientation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public abstract class Gate implements ArchitectureObjectWithBusConnection {
   protected  Bus Output = null;
   protected  String mask = "";
   protected  Bus Input = null;
   private Orientation orientation = Orientation.north;
   private Dimension lastDimension = new Dimension();
   private State currentState = State.Inactive;
   int size;
   
   

   public abstract String getDescription() ;
   
   public void action() {
        currentState = State.Active;
   } ;
    

    public Point2D getConnectionPoint(Bus b) throws IOException {
                if (b == Input) {
            switch (orientation) {
                case north:
                    return new Point2D.Double(lastDimension.width / 2, lastDimension.height);
                case east:
                    return new Point2D.Double(0, lastDimension.height / 2);
                case south:
                    return new Point2D.Double(lastDimension.width / 2, 0);
                case west:
                    return new Point2D.Double(lastDimension.width, lastDimension.height / 2);
                default:
                    throw new AssertionError();
            }
        } else if (b == Output) {
            switch (orientation) {
                case north:
                    return new Point2D.Double(lastDimension.width / 2, 0);
                case east:
                    return new Point2D.Double(lastDimension.width, lastDimension.height / 2);
                case south:
                    return new Point2D.Double(lastDimension.width / 2, lastDimension.height);
                case west:
                    return new Point2D.Double(0, lastDimension.height / 2);

                default:
                    throw new AssertionError();
            }
        }



        throw new IOException("Bus not Connected to Register");
    }

    public Dimension getDimension(FontMetrics metrics) {
        
        String string1 = "1";
        String string0 = "0";
        int bitwidth = (metrics.stringWidth(string0) > metrics.stringWidth(string1)) ? metrics.stringWidth(string0) : metrics.stringWidth(string1);
        bitwidth = bitwidth
 * size;
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(getDescription()+" ");
        adv = adv + bitwidth;
        Dimension sizeofbox = new Dimension(adv + 2 + 1, hgt + 4 + 1);
        lastDimension = sizeofbox;
        return sizeofbox;
    }

    public Dimension getDimension() {
        return new Dimension();
    }

    public Collection<Bus> getInvoldedBuses() {
        Collection<Bus> bs = new ArrayList<Bus>();
        bs.add(Input);
        bs.add(Output);
        return bs;
    }

    public State getState() {
        return currentState;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Font f = g2d.getFont();
        FontMetrics metrics = g2d.getFontMetrics(f);
        String string1 = "1";
        String string0 = "0";
        int bitwidth = (metrics.stringWidth(string0) > metrics.stringWidth(string1)) ? metrics.stringWidth(string0) : metrics.stringWidth(string1);
        bitwidth = bitwidth
 * size;
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(getDescription()+" ");
        adv = adv + bitwidth;
        Dimension sizeofbox = new Dimension(adv + 2, hgt + 4);
        Color old = g2d.getColor();
        g2d.setColor(defaultBackgroundColor);
        g2d.fillRect(0, 0, sizeofbox.width, sizeofbox.height);
        g2d.setColor(old);
        g2d.drawRect(0, 0, sizeofbox.width, sizeofbox.height);
        g2d.drawString(getDescription()+" " + mask, 1, hgt);
    }

    public void resetState() {
       currentState= State.Inactive;
    }

    public void setInput(Bus Input) {
        this.Input = Input;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setOutput(Bus Output) {
        this.Output = Output;
    }
    
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

}
