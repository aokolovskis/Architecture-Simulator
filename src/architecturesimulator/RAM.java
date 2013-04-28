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

import architecturesimulator.xmlparser.ParseException;
import architecturesimulator.xmlparser.ParserFunctions;
import architecturesimulator.xmlparser.architekturparser;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Aleksejs Okolovskis (oko@aloko.de)
 */
public class RAM implements CommandPerformer, ArchitectureObjectWithBusConnection, ScrollAndNonScrollableArchitectureObjectPart {

    int bit;
    boolean ChipSelect = false;
    boolean ReadNotWrite = true;
    SortedMap<Long, Long> ramTable = new TreeMap<Long, Long>();
    Bus DataBus = null;
    Bus AdressBus = null;
    Dimension currentSchrollDimension = new Dimension(0, 0);
    Dimension currentNonSchrollDimention = new Dimension(0, 0);
    Dimension preferedNonSchrollDimention = new Dimension(0, 0);
    private List<DimensionChangedListener> dimensionChangedListeners = new ArrayList<DimensionChangedListener>();
    private State currentState = State.Inactive;
    private viewBase currentViewBase = viewBase.hex;
    private int fiexedAdressSize = -1;
    private String name = "";
    final int base = 20;
    final int triangle = 10;
    final int maxheightNotScroll = 127;
    Command reloadRam = new Command() {

        public String getName() {
            return "Load Ram";
        }

        public String getDescription() {
            return "Loads the Ram from a File";
        }

        public void execute(ArchitectureObjectWithBusConnection aowbc) throws Exception {
            if (!(aowbc instanceof RAM)) {
                return;
            }
            RAM r = (RAM) aowbc;

            final JFileChooser chooser = new JFileChooser("Select file");
            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            final File file = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(file);
            chooser.setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    return f.isDirectory() | f.getName().endsWith(".ram.xml");
                }

                @Override
                public String getDescription() {
                    return "*.ram.xml";
                }
            });
            final SortedMap<Long, Long> new_ram_values = new TreeMap<Long, Long>();




            chooser.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                        final File tmpfile = (File) e.getNewValue();
                        if (tmpfile == null) {return;}
                        if (tmpfile.isDirectory()) {
                            return;
                        }

                    }
                }
            });




            chooser.setVisible(true);

            final int result = chooser.showOpenDialog(null);

            if (chooser.getSelectedFile() != null) {
                Document doc = architekturparser.readFile(chooser.getSelectedFile().getAbsolutePath());
                Node node = doc.getFirstChild();
                NodeList nl = node.getChildNodes();
                long j = -1;
                for (int i = 0; i < nl.getLength(); i++) {
                    Node tmp = nl.item(i);
                    if (tmp.getNodeName().equals("value")) {

                        if (ParserFunctions.hasAttribute("address", tmp)) {
                            j = Long.valueOf(ParserFunctions.getAttributeByName("address", tmp), 16);
                        } else {
                            j++;
                        }

                        new_ram_values.put(j, Long.parseLong(tmp.getFirstChild().getTextContent(), 16));

                    } else {
                        if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                            throw new ParseException("Error while reading Ram file.  Unexpected Node:" + tmp.getNodeName());
                        }
                    }
                }
                r.setRamTable(new_ram_values);
            }



        }
    };

    public Orientation getOrientation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOrientation(Orientation orientation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void paintNonScrollableArchitectureObjectPart(Graphics g) {

        Graphics2D gd = (Graphics2D) g;
        AffineTransform at = gd.getTransform();
        final Color oldColor = gd.getColor();

        gd.translate(0, maxheightNotScroll);
        gd.rotate(270 * Math.PI / 180.);

        Polygon p = new Polygon();
        p.addPoint(triangle - 1, 0);
        p.addPoint(maxheightNotScroll - triangle - 1, 0);
        p.addPoint(maxheightNotScroll - 1, base - 1);
        p.addPoint(0, base - 1);


        gd.setColor(defaultBackgroundColor);
        gd.fillPolygon(p);
        gd.setColor(oldColor);
        gd.draw(p);
        gd.drawLine((maxheightNotScroll - 1) / 2, 0, (maxheightNotScroll - 1) / 2, base - 1);

        final String address = "Address";
        final String data = "Data";
        final int datawidth = gd.getFontMetrics().stringWidth(data);
        final int addresswidth = gd.getFontMetrics().stringWidth(address);

        gd.drawString(data, (((maxheightNotScroll - 1) / 4) * 3) - (datawidth / 2), 15);
        gd.drawString(address, ((maxheightNotScroll - 1) / 4) - (addresswidth / 2), 15);
        gd.setTransform(at);

    }

    public Dimension getNonScrollableArchitectureObjectPartDimension() {
        return new Dimension(base, maxheightNotScroll);
    }

    public Dimension getNonScrollableArchitectureObjectPartDimension(FontMetrics fm) {
        return new Dimension(base, maxheightNotScroll);
    }

    public void paintScrollableArchitectureObjectPart(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1));
        Font f = g2d.getFont();

        String string1 = "";
        String string0 = "";

        for (int i = 0; i < bit; i++) {
            string1 = string1 + "1";
            string0 = string0 + String.valueOf(i % 2);

        }

        // get metrics from the graphics
        FontMetrics metrics = g2d.getFontMetrics(f);
        // get the height of a line of text in this font and render context
        int hgt = metrics.getHeight();
        // get the advance of my text in this font and render context
        //TODO: !!
        int adv = (metrics.stringWidth(string0) > metrics.stringWidth(string1)) ? metrics.stringWidth(string0) : metrics.stringWidth(string1);
        // calculate the valueDimension of a box to hold the text with some padding.
        Dimension valueDimension = new Dimension((adv) + 2, hgt + 2);

        AffineTransform at = g2d.getTransform();

        long lastadress;
        if (fiexedAdressSize > 0) {
            lastadress = fiexedAdressSize - 1;
        } else {
            lastadress = ramTable.lastKey();
        }

        Dimension adressDimension = new Dimension((metrics.stringWidth(Long.toString(lastadress, currentViewBase.index)) + 2), hgt + 2);


        int loops = 0;
        for (Long long1 : ramTable.keySet()) {
            g2d.drawRect(6, 0, valueDimension.width + adressDimension.width, (int) (valueDimension.height));
            g2d.drawRect(6, 0, adressDimension.width, valueDimension.height);
            g2d.drawString(Long.toString(long1, currentViewBase.index()), (6 + adressDimension.width) - (metrics.stringWidth(Long.toString(long1, currentViewBase.index())) /*+ 4*/), hgt - 1);

            g2d.drawString(Long.toString(ramTable.get(long1), currentViewBase.index()), (6 + valueDimension.width + adressDimension.width) - (metrics.stringWidth(Long.toString(ramTable.get(long1), currentViewBase.index())) + 1 /*+ 4*/), hgt - 1);
            g2d.translate(0, valueDimension.height);
            loops++;
        }
        g2d.drawRect(6, 0, valueDimension.width, (int) (valueDimension.height / 4));

        g2d.setTransform(at);
        g2d.drawRect(0, 0, 6, (int) (valueDimension.height * loops));

        updateDimension(new Dimension(valueDimension.width + adressDimension.width + 1 + 6, valueDimension.height * loops));

    }

    public Dimension getScrollableArchitectureObjectPartDimension() {
        return new Dimension();
    }

    public Dimension getScrollableArchitectureObjectPartDimension(FontMetrics fm) {
        return calcDimenstion(fm);
    }

    public void setPreferedNonScrollableArchitectureObjectPartDimension(Dimension d) {
        preferedNonSchrollDimention = d;
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

    public Collection<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<Command>();
        commands.add(reloadRam);
        return commands;
    }

    public ArchitectureObjectWithBusConnection getPerformer() {
        return this;
    }

    private enum viewBase {

        bin(2), dez(10), hex(16);
        private final int index;

        viewBase(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
    }

    public void nextViewBase() {
        int size = viewBase.values().length;
        int selected = 0;
        for (; selected < size; selected++) {
            if (viewBase.values()[selected].equals(currentViewBase)) {
                break;
            }
        }
        currentViewBase = viewBase.values()[((selected + 1) % (size))];
        fireDimensionChanged();
    }

    public RAM() {
    }

    public RAM(int bit) {

        this.bit = bit;
    }

    public RAM(int Bit, Bus DataBus, Bus AdressBus) {
        // init();
        this.bit = Bit;
        this.DataBus = DataBus;
        this.AdressBus = AdressBus;
    }

    public RAM(int Bit, Bus DataBus, Bus AdressBus, int adressSize) {
        // init();
        this.bit = Bit;
        this.DataBus = DataBus;
        this.AdressBus = AdressBus;
        this.fiexedAdressSize = adressSize;
    }

    public Bus getAdressBus() {
        return AdressBus;
    }

    public void setAdressBus(Bus AdressBus) {
        this.AdressBus = AdressBus;
    }

    public boolean isChipSelect() {
        return ChipSelect;
    }

    public void setChipSelect(boolean ChipSelect) throws IOException {
        this.ChipSelect = ChipSelect;
        action();
    }

    public Bus getDataBus() {
        return DataBus;
    }

    public void setDataBus(Bus DataBus) {
        this.DataBus = DataBus;
    }

    public boolean isReadNotWrite() {
        return ReadNotWrite;
    }

    public void setReadNotWrite(boolean ReadNotWrite) throws IOException {
        this.ReadNotWrite = ReadNotWrite;
        action();
    }

    public SortedMap<Long, Long> getRamTable() {
        return ramTable;
    }

    public void setRamTable(SortedMap<Long, Long> ramTable) {
        this.ramTable = ramTable;
    }

    public Collection<Bus> getInvoldedBuses() {
        Collection<Bus> t = new ArrayList<Bus>();
        if (AdressBus != null) {
            t.add(AdressBus);
        }
        if (DataBus != null) {
            t.add(DataBus);
        }
        return t;
    }

    public Point2D getConnectionPoint(Bus b) throws IOException {

        if (b == AdressBus) {
            return new Point2D.Double(0, (maxheightNotScroll / 4) * 3);
            //return new Point2D.Double(0, currentDimension.getHeight() * 0.5);
        } else if (b == DataBus) {
            return new Point2D.Double(0, (maxheightNotScroll / 4) * 1);
            //return new Point2D.Double(currentDimension.getWidth() * 0.5, currentDimension.getHeight());
        }
        throw new IOException("Bus not Connected to ram.");
    }

    public void paint(Graphics g) {
        Graphics2D gd = (Graphics2D) g;
        AffineTransform at = gd.getTransform();
        paintNonScrollableArchitectureObjectPart(gd);
        gd.translate(currentNonSchrollDimention.width, currentNonSchrollDimention.height);
        paintScrollableArchitectureObjectPart(gd);
        gd.setTransform(at);
    }

    public Dimension getDimension() {
        final Dimension NonScrollDimension = getNonScrollableArchitectureObjectPartDimension();
        final Dimension ScrollDimension = getScrollableArchitectureObjectPartDimension();
        return new Dimension(NonScrollDimension.width + ScrollDimension.width, NonScrollDimension.height + ScrollDimension.height);
    }

    public State getState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void action() throws IOException {
        if (ChipSelect) {
            if (ReadNotWrite) {
                int oldSize = ramTable.size();
                ramTable.put((Long.parseLong(AdressBus.getValue(), 2)),
                        (Long.parseLong(DataBus.getValue(), 2)));
                if (oldSize != ramTable.size()) {
                    fireDimensionChanged();
                }
            }
            if (!ReadNotWrite) {
                Long adress = Long.parseLong(AdressBus.getValue(), 2);
                if (!ramTable.containsKey(adress)) {
                    throw new IOException("RAM has not requested adress: " + adress);
                }

                String value = Long.toBinaryString((ramTable.get((adress))));
                DataBus.setValue(value);
            }
        }
    }

    public void setValue(long adress, long value) {
        ramTable.put(adress, value);

    }

    public Dimension getDimension(FontMetrics fm) {

        return (currentSchrollDimension.equals(new Dimension())) ? calcDimenstion(fm) : currentSchrollDimension;
    }

    private Dimension calcDimenstion(FontMetrics fm) {

        // get the height of a line of text in this font and render context
        String string1 = "";
        String string0 = "";

        for (int i = 0; i < bit; i++) {
            string1 = string1 + "1";
            string0 = string0 + "0";
        }
        int hgt = fm.getHeight();
        // get the advance of my text in this font and render context
        //TODO: !!
        int adv = (fm.stringWidth(string0) > fm.stringWidth(string1)) ? fm.stringWidth(string0) : fm.stringWidth(string1);
        // calculate the valueDimension of a box to hold the text with some padding.
        long lastadress;
        if (fiexedAdressSize > 0) {
            lastadress = fiexedAdressSize - 1;
        } else {
            lastadress = ramTable.lastKey();
        }

        Dimension adressDimension = new Dimension((fm.stringWidth(Long.toString(lastadress, currentViewBase.index)) + 2), hgt + 2);



        Dimension size = new Dimension((adv) + 2, hgt + 2);
        updateDimension(new Dimension(size.width + adressDimension.width + 7, size.height * ramTable.size() + 1));
        return currentSchrollDimension;
    }

    private void updateDimension(Dimension newDimension) {
        boolean hastobeupadated = !currentSchrollDimension.equals(newDimension);
        if (hastobeupadated) {
            currentSchrollDimension = newDimension;
            fireDimensionChanged();
        }


    }

    public void resetState() {
        currentState = State.Inactive;
    }

    public void addDimensionChangedListeners(DimensionChangedListener Listener) {
        if (Listener != null) {
            dimensionChangedListeners.add(Listener);
        }

    }

    private void fireDimensionChanged() {
        for (DimensionChangedListener dimensionChangedListener : dimensionChangedListeners) {
            dimensionChangedListener.OnDimensionChanged(this);
        }
    }
}
