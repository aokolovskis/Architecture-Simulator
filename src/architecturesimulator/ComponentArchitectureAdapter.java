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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
  public class ComponentArchitectureAdapter extends Container {

        
        
        boolean setupedsize = false;
        architecturesimulator.ArchtekturObject ao;

        public ComponentArchitectureAdapter(ArchtekturObject ao) {
            this.ao = ao;
            addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
               
            }

            @Override
            public void mouseReleased(MouseEvent e) {
             
            }

            @Override
            public void mouseEntered(MouseEvent e) {
              
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        });
        }

        @Override
        public Dimension getPreferredSize() {
            setupsize();
            return super.getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            setupsize();
            return super.getMinimumSize();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            ao.paint(g);
            }

        private void setupsize() {
            if (!setupedsize) {
                Dimension d = ao.getDimension();
                d = (d.equals(new Dimension())) ? ao.getDimension(getFontMetrics(getFont())) : d;
                this.setPreferredSize(d);
                this.setMinimumSize(d);
            }


        }
    }
