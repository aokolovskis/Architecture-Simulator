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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class Register implements ArchitectureObjectWithBusConnection {

    Bus Input = null;
    Bus Output = null;
    private int bitLength = 0;
    private String name = "";
    private Long Value = 0L;
    private Dimension lastDimension = new Dimension(0, 0);
    private Orientation orientation = Orientation.north;
    private boolean ChipSelected = false;
    private boolean OutputEnabled = false;

    public void resetState() {
        ChipSelected = false;
        OutputEnabled = false;
    }

    public Register() {
    }

    public Dimension getDimension(FontMetrics metrics) {
        String description = getName() + ": ";
        String string1 = "1";
        String string0 = "0";



        int bitwidth = (metrics.stringWidth(string0) > metrics.stringWidth(string1)) ? metrics.stringWidth(string0) : metrics.stringWidth(string1);
        bitwidth = bitwidth
 * bitLength;


        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(description);

        adv = adv + bitwidth;
        Dimension sizeofbox = new Dimension(adv + 2 + 1, hgt + 4 + 1);
        lastDimension = sizeofbox;
        return sizeofbox;
    }

    public Register(int bitLength) {
        this.bitLength = bitLength;
    }

    public Register(int bitLength, String name) {
        this.bitLength = bitLength;
        this.name = name;
    }

    public Register(int bitLength, String name, Bus Input, Bus Output) {
        this.bitLength = bitLength;
        this.name = name;
        this.Input = Input;
        this.Output = Output;
    }

    /**
    
 * @return the bitLength
    
 */
    public int getBitLength() {
        return bitLength;
    }

    /**
    
 * @param bitLength the bitLength to set
    
 */
    public void setBitLength(int bitLength) {
        this.bitLength = bitLength;
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

    /**
    
 * @return the Value
    
 */
    public String getValue() {
        String result = BinaryFunctions.normalizeStringlengt(Long.toBinaryString(Value), bitLength);
        return result;
        /*
        int length = Long.toBinaryString(Value).length();
        if (length > bitLength) {
        int from = length - bitLength;
        return Long.toBinaryString(Value).substring(from+outputhighestbitmask, length);
        } else {
        return Long.toBinaryString(Value).substring(outputhighestbitmask, length);
        }*/


    }

    /**
    
 * @param Value the Value to set
    
 */
    private void setValue(String BinaryValue) {
        this.Value = Long.parseLong(BinaryValue, 2);
    }

    public void paint(Graphics g) {

        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs =
                ge.getScreenDevices();
        for (int j = 0; j < gs.length; j++) {
            GraphicsDevice gd = gs[j];
            GraphicsConfiguration[] gc =
                    gd.getConfigurations();
            for (int i = 0; i < gc.length; i++) {
                virtualBounds =
                        virtualBounds.union(gc[i].getBounds());
            }
        }


        Graphics2D g2d = (Graphics2D) g;
        Font f = g2d.getFont();
        String description = getName() + ": ";
        FontMetrics metrics = g2d.getFontMetrics(f);

        String string1 = "1";
        String string0 = "0";



        int bitwidth = (metrics.stringWidth(string0) > metrics.stringWidth(string1)) ? metrics.stringWidth(string0) : metrics.stringWidth(string1);
        bitwidth = bitwidth
 * bitLength;


        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(description);

        adv = adv + bitwidth;
        Dimension sizeofbox = new Dimension(adv + 2, hgt + 4);
        Color old = g2d.getColor();

        if (ChipSelected & OutputEnabled) {
            g2d.setColor(Color.YELLOW);
        } else if (ChipSelected) {
            g2d.setColor(Color.RED);
        } else if (OutputEnabled) {
            g2d.setColor(Color.GREEN);
        } else {
            g2d.setColor(defaultBackgroundColor);
        }

        g2d.fillRect(0, 0, sizeofbox.width, sizeofbox.height);
        g2d.setColor(old);
        g2d.drawRect(0, 0, sizeofbox.width, sizeofbox.height);
        g2d.drawString(description + getValue(), 1, hgt);



    }

    public Bus getInput() {
        return Input;
    }

    public Bus getOutput() {
        return Output;
    }

    public void setInput(Bus Input) {
        this.Input = Input;
    }

    public void setOutput(Bus Output) {
        this.Output = Output;
    }

    public void chipEnable() {
        this.setValue(Input.getValue());
        ChipSelected = true;

    }

    public void outputEnable() {
        Output.setValue(getValue());
        OutputEnabled = true;
    }

    public Dimension getDimension() {
        return new Dimension();
    }

    public State getState() {
        return (OutputEnabled | ChipSelected) ? State.Active : State.Inactive;
    }

    public Collection<Bus> getInvoldedBuses() {
        ArrayList<Bus> t = new ArrayList<Bus>();
        if (Input != null) {
            t.add(Input);
        }
        if (Output != null) {
            t.add(Output);
        }

        return t;
    }

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

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
