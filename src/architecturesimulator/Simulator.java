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

import architecturesimulator.gui.DebugWindow;
import architecturesimulator.xmlparser.architekturparser;
import architecturesimulator.xmlparser.layoutparser;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class Simulator extends Container {

    TimingAndControl TAC = new TimingAndControl();
    Map<Bus, BusPath> BusWithBuspath = new HashMap<Bus, BusPath>();
    Map<Bus, Path2D> BusWithPathtoDraw = new HashMap<Bus, Path2D>();
    Map<ArchitectureObjectWithBusConnection, Point2D> ArchtMap = new HashMap<ArchitectureObjectWithBusConnection, Point2D>();
    Map<Bus, HashSet<ArchitectureObjectWithBusConnection>> BusConnections = new HashMap<Bus, HashSet<ArchitectureObjectWithBusConnection>>();
    Map<String, architecturesimulator.ArchtekturObject> ArchtectureLookupTble =
            new HashMap<String, ArchtekturObject>();
    String architekturfile = null;
    String layoutfile = null;
    MouseListener ml = null;
    Rectangle preferedSize = new Rectangle();
    
    

    public Simulator(){}
    
    public Simulator(String architekturfile, String layoutfile) {
        this.architekturfile = architekturfile;
        this.layoutfile = layoutfile;
    }

    public void setup(String architekturfile, String layoutfile) throws ParserConfigurationException, SAXException, IOException {
        this.architekturfile = architekturfile;
        this.layoutfile = layoutfile;
        setup();
    }

    public void setup() throws ParserConfigurationException, SAXException, IOException {
        
        //Fontloading 
        try {
        File f = new File("res/font/Terminus.ttf");
        InputStream is = new FileInputStream(f);
        
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(16f);
            setFont(font);
        } catch (Exception ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        setLayout(null);
        removeAll();

        if (layoutfile == null || architekturfile == null) {throw new IOException("Architecture or Layout File not set."); }
        
        BusWithBuspath = new HashMap<Bus, BusPath>();
        TAC = new TimingAndControl();
        ArchtMap = new HashMap<ArchitectureObjectWithBusConnection, Point2D>();
        BusConnections = new HashMap<Bus, HashSet<ArchitectureObjectWithBusConnection>>();
        ArchtectureLookupTble =
                new HashMap<String, ArchtekturObject>();
        architekturparser.parseDocument(architekturfile, TAC, ArchtectureLookupTble);
        layoutparser.parseDocument(BusWithBuspath, layoutfile, ArchtMap, ArchtectureLookupTble);

        

        preferedSize = new Rectangle();
        

        //Setup Connection for Drawing
        for (ArchitectureObjectWithBusConnection architectureObjectWithBusConnection : ArchtMap.keySet()) {
            for (Bus bus : architectureObjectWithBusConnection.getInvoldedBuses()) {
                if (BusConnections.containsKey(bus)) {
                    BusConnections.get(bus).add(architectureObjectWithBusConnection);
                } else {
                    HashSet<ArchitectureObjectWithBusConnection> hs = new HashSet<ArchitectureObjectWithBusConnection>();
                    hs.add(architectureObjectWithBusConnection);
                    BusConnections.put(bus, hs);
                }
            }
        }
        
        //Adding the Architecute Objects to the Container if the obj is a RAM it becommes a spezial Scrollable view. 
        for (ArchitectureObjectWithBusConnection architectureObjectWithBusConnection : ArchtMap.keySet()) {
            Component c;
            if (architectureObjectWithBusConnection instanceof RAM) {
                c = new ScrollableComponentArchitecureAdapter((RAM) architectureObjectWithBusConnection);
                c.setPreferredSize(new Dimension(c.getPreferredSize().width, 130));
                System.out.println("Simp" + c.getPreferredSize());
            } else {
                c = new ComponentArchitectureAdapter(architectureObjectWithBusConnection);

            }

            add(c);
            Insets insets = this.getInsets();
            Point2D point = ArchtMap.get(architectureObjectWithBusConnection);
            Dimension size = new Dimension(c.getPreferredSize());
            Rectangle ComponentBounds = new Rectangle ((int) point.getX() + insets.left, (int) point.getY()+insets.top, size.width, size.height);
            c.setBounds(ComponentBounds);
            
            preferedSize = preferedSize.union(ComponentBounds);
        }

        /*Calcule Buspaths*/        
         for (Bus bus : BusWithBuspath.keySet()) {
            Path2D path2D = BusWithBuspath.get(bus).getPath();
            BusWithPathtoDraw.put(bus, path2D );
           // preferedSize = preferedSize.union(new Rectangle(path2D.getBounds().width + bus.getStrokeDimension() ,path2D.getBounds().height + bus.getStrokeDimension()));
           Rectangle bounds = new Rectangle(path2D.getBounds());
            bounds.grow(bus.getStrokeDimension(), bus.getStrokeDimension());
            preferedSize = preferedSize.union(bounds);
         }
        
        //Need to validete because otherwise the added Containers will not be painted.
        validate();
        setPreferredSize(preferedSize.getSize());
        getParent().repaint();
       // getParent().validate();
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;
        //AffineTransform at = g2d.getTransform();
        // g2d.translate(0, 30);
        g2d.setFont(new Font("Dialog", Font.PLAIN, 12));
        g2d.setPaint(Color.black);
        g2d.setStroke(new BasicStroke(1));

        /*
        AffineTransform at = g2d.getTransform();
        for (ArchtekturObject archtekturObject : ArchtMap.keySet()) {
        
        Point2D point = ArchtMap.get(archtekturObject);
        g2d.translate(((int) (point.getX())), ((int) (point.getY())));
        archtekturObject.paint(g);
        g2d.setTransform(at);
        
        }*/

        for (Bus bus : BusConnections.keySet()) {
            if (BusWithPathtoDraw.containsKey(bus)) {
                bus.setBusPath2D(BusWithPathtoDraw.get(bus));
                bus.paint(g);
                continue;
            }

            Path2D.Double path = new Path2D.Double();
            boolean firstpoint = true;
            for (ArchitectureObjectWithBusConnection architectureObjectWithBusConnection : BusConnections.get(bus)) {
                try {
                    Point2D absulut = ArchtMap.get(architectureObjectWithBusConnection);
                    Point2D point = architectureObjectWithBusConnection.getConnectionPoint(bus);
                    point.setLocation(absulut.getX() + point.getX(), absulut.getY() + point.getY());

                    if (firstpoint) {
                        path.moveTo(point.getX(), point.getY());
                        firstpoint = false;
                    } else {
                        path.lineTo(point.getX(), point.getY());
                    }

                } catch (IOException ex) {
                    Logger.getLogger(DebugWindow.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
            bus.setBusPath2D(path);
            bus.paint(g);
        }
    }

    public void MicroStep() throws IOException {
       
        resetAllStates();
        
        TAC.performStep();
        repaint();              
    }
    
    public void resetAllStates (){
        resetArchitectureObjectStates();
        resetBusStates();
    }
    
    public void resetArchitectureObjectStates () {
         for (ArchitectureObjectWithBusConnection architectureObjectWithBusConnection : ArchtMap.keySet()) {
            architectureObjectWithBusConnection.resetState();
        }
       
    }
    
    public void resetBusStates() {
         for (Bus bus : BusWithBuspath.keySet()) {
            bus.resetState();
        }
    }
    
    
    
    public void InstructionStep() throws IOException {
        TAC.performInstruction();
        resetAllStates();
        repaint();
    }
    
    
    public TimingAndControl getTimeingandControll () {
    
    return TAC;
    }
    
    public  Collection<CommandPerformer.Entry>  getCommands () {
        Collection<CommandPerformer.Entry> CommandCollection = new ArrayList<CommandPerformer.Entry>(); 
        
        for (ArchitectureObjectWithBusConnection architectureObjectWithBusConnection : ArchtMap.keySet()) {
            if (architectureObjectWithBusConnection instanceof CommandPerformer){
                final CommandPerformer obj = (CommandPerformer) architectureObjectWithBusConnection;
                CommandCollection.add( new CommandPerformer.Entry() {

                    public ArchitectureObjectWithBusConnection getPerformer() {
                        return obj.getPerformer();
                    }

                    public Collection<Command> getCommands() {
                       return  obj.getCommands();
                    }
                });
            }
        }

        
        return CommandCollection;
    }
}
