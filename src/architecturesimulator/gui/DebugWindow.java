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
package architecturesimulator.gui;



import architecturesimulator.ArchitectureObjectWithBusConnection;
import architecturesimulator.Bus;
import architecturesimulator.Simulator;
import architecturesimulator.TimingAndControl;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class DebugWindow extends javax.swing.JFrame {

    JTextArea notificatinArea = new JTextArea(6, 30);
    JButton setupbtn = new JButton("[Setup]");
    JButton button = new JButton("Step");
    JButton Instruction = new JButton("Instuctions");
    JTextField archfile  = new JTextField(System.getProperty("user.dir")+"/res/mu0-2.architecture.xml");
    JTextField layoutfile = new JTextField(System.getProperty("user.dir")+"/res/mu0-2.layout.xml");

    TimingAndControl TAC = new TimingAndControl();
    Map<ArchitectureObjectWithBusConnection, Point2D> ArchtMap = new HashMap<ArchitectureObjectWithBusConnection, Point2D>();
    Map<Bus, HashSet<ArchitectureObjectWithBusConnection>> BusConnections = new HashMap<Bus, HashSet<ArchitectureObjectWithBusConnection>>();
    Simulator s = null;

    public DebugWindow() {
        super();



        Container pane = this.getContentPane();
        // mydrawingadre drawin = new mydrawingadre();
        
        //s.setup();
        s = new Simulator(
                archfile.getText(),
                layoutfile.getText());


        pane.setLayout(new BorderLayout());
        JPanel jp = new JPanel();
        BoxLayout b = new BoxLayout(jp, BoxLayout.X_AXIS);
        jp.setLayout(b);
        jp.add(button);
        jp.add(setupbtn);
        jp.add(Instruction);

        JPanel jp1 = new JPanel();
        BoxLayout b1 = new BoxLayout(jp1, BoxLayout.Y_AXIS);
        jp1.setLayout(b1);
        jp1.add(archfile);
        jp1.add(layoutfile);

        jp.add(jp1);
        
        pane.add(jp, BorderLayout.PAGE_START);
        pane.add(s,BorderLayout.CENTER);



        JScrollPane scrollPane = new JScrollPane(notificatinArea);
        pane.add(scrollPane,BorderLayout.PAGE_END);




        //pane.add(drawin, BorderLayout.CENTER);

        Instruction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFrame g =  new JFrame();
                JTable msct = new JTable(s.getTimeingandControll().generateInstuctionTable());
                g.getContentPane().add(new JScrollPane(msct), BorderLayout.CENTER);
                g.validate();
                g.pack();
                        
                g.setVisible(true);
                        
            }
        });
        
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    s.MicroStep();
                } catch (IOException ex) {
                    notificatinArea.setText(notificatinArea.getText() +"Fail: \n"+ ex.getMessage()+"\n");
                   
                }
                ((JButton) e.getSource()).getRootPane().repaint();

            }
        });

        setupbtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    notificatinArea.setText(notificatinArea.getText() + "\n" + "Trying to setup simulator..." + "\n");
                   s.setup(archfile.getText(), layoutfile.getText());
                    notificatinArea.setText(notificatinArea.getText() + "\n" + "Success!"+ "\n");
                   // ((JButton) e.getSource()).getRootPane().getContentPane().repaint();
                } catch (ParserConfigurationException ex) {
                    notificatinArea.setText(notificatinArea.getText() + "Fail: \n"+ ex.getMessage()+"\n");

                } catch (SAXException ex) {
                    notificatinArea.setText(notificatinArea.getText() +"Fail: \n"+ ex.getMessage()+"\n");
                } catch (IOException ex) {
                    notificatinArea.setText(notificatinArea.getText() +"Fail: \n"+ ex.getMessage()+"\n");
                    
                }
                
                
            }
        });


        this.setSize(900, 450);
        this.setLocation(400, 300);
        

        

    }

    public static void main(String[] args) {
        JFrame w = new DebugWindow();
        w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
        w.setVisible(true);
    }
}
