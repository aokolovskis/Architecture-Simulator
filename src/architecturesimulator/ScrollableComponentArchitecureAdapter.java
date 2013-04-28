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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class ScrollableComponentArchitecureAdapter extends JPanel {

    JScrollPane scrollPane;
    RAM ram = null;
  //  Component c;
    JPanel jp;
    JPanel nonscrollablePart;
     Component SchrollablerComponent;
    public ScrollableComponentArchitecureAdapter(RAM r) {
        super();
        ram = r;
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createEmptyBorder());
        jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder());
        jp.setLayout(new BoxLayout(jp, BoxLayout.PAGE_AXIS));
 
        if (r instanceof ScrollAndNonScrollableArchitectureObjectPart) {
            ScrollAndNonScrollableArchitectureObjectPart splittedArchitectureObj = (ScrollAndNonScrollableArchitectureObjectPart) r;
            CompontentScrollAndNonScrollableArchitectureObjectPartAdapter adapter = new CompontentScrollAndNonScrollableArchitectureObjectPartAdapter(splittedArchitectureObj);
            final Component NotScrollableCompontent = adapter.getNonScrollableArchitectureObjectPartAdapter();
            add(NotScrollableCompontent);
            SchrollablerComponent = adapter.getScrollableArchitectureObjectPartAdapter();
            System.out.println("size NotScrollableCompontent" + NotScrollableCompontent.getPreferredSize());
            jp.add(SchrollablerComponent);
            System.out.println("size jp" + jp.getPreferredSize());
            scrollPane = new JScrollPane(jp);
            scrollPane.setPreferredSize(new Dimension(SchrollablerComponent.getPreferredSize().width + scrollPane.getVerticalScrollBar().getPreferredSize().getSize().width + 3 , 200));
            scrollPane.setMinimumSize(new Dimension(SchrollablerComponent.getPreferredSize().width + scrollPane.getVerticalScrollBar().getPreferredSize().getSize().width + 3 , 200));
            System.out.println("size scrollPane" + scrollPane.getPreferredSize());
            
        } else {

           SchrollablerComponent  = new ComponentArchitectureAdapter(ram);
            jp.add(SchrollablerComponent);
            //jp.add(new JTextArea(5, 10),BorderLayout.CENTER);
            scrollPane = new JScrollPane(jp);

            scrollPane.setPreferredSize(new Dimension(SchrollablerComponent.getPreferredSize().width + scrollPane.getVerticalScrollBar().getPreferredSize().getSize().width + 3, 200));
            scrollPane.setMinimumSize(scrollPane.getPreferredSize());
            System.out.println("size scrollPane" + scrollPane.getPreferredSize());
        }

        

        add(scrollPane);
        System.out.println(getPreferredSize());
        ram.addDimensionChangedListeners(new DimensionChangedListener() {

            public void OnDimensionChanged(ArchtekturObject sender) {
                SchrollablerComponent.invalidate();
                //jp.invalidate();
                //invalidate();
                //scrollPane.invalidate();

                scrollPane.setPreferredSize(new Dimension(SchrollablerComponent.getPreferredSize().width + scrollPane.getVerticalScrollBar().getPreferredSize().getSize().width + 3, 200));
                scrollPane.setMinimumSize(new Dimension(SchrollablerComponent.getPreferredSize().width + scrollPane.getVerticalScrollBar().getPreferredSize().getSize().width + 3 , 200));
                scrollPane.validate();
                scrollPane.repaint();
                //validateTree();
               
                Dimension d = new Dimension();
                
                for (Component component : getComponents()) {
                    d.setSize(d.width+component.getWidth(), d.height+component.getHeight());
                }
                
                
                setBounds(getBounds().x, getBounds().y, d.width, getPreferredSize().height);
                getParent().validate();
                //setBounds();
            }
        });

        SchrollablerComponent.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                ram.nextViewBase();

            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        // scrollPane.setViewportView(c);
        //  setPreferredSize(new Dimension(200, 100));
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        scrollPane.setPreferredSize(new Dimension(preferredSize.width/*-4*/, preferredSize.height/*-4*/));
    }
}
