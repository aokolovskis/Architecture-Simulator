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

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class CompontentScrollAndNonScrollableArchitectureObjectPartAdapter {

    ScrollAndNonScrollableArchitectureObjectPart andNonScrollableArchitectureObjectPart;
    // Container parent;

    private class NonScrollableArchitectureObjectPart extends Container {

        boolean setupedsize = false;
        ScrollAndNonScrollableArchitectureObjectPart sansaop;

        public NonScrollableArchitectureObjectPart(ScrollAndNonScrollableArchitectureObjectPart sansaop) {
            this.sansaop = sansaop;
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
            sansaop.paintNonScrollableArchitectureObjectPart(g);
        }

        private void setupsize() {
            if (!setupedsize) {
                Dimension d = sansaop.getNonScrollableArchitectureObjectPartDimension();
                d = (d.equals(new Dimension())) ? sansaop.getNonScrollableArchitectureObjectPartDimension(getFontMetrics(getFont())) : d;
                this.setPreferredSize(d);
                this.setMinimumSize(d);
            }


        }
    }

    private class ScrollableArchitectureObjectPart extends Container {

        boolean setupedsize = false;
        ScrollAndNonScrollableArchitectureObjectPart sansaop;

        public ScrollableArchitectureObjectPart(ScrollAndNonScrollableArchitectureObjectPart sansaop) {
            this.sansaop = sansaop;
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
            sansaop.paintScrollableArchitectureObjectPart(g);
        }

        private void setupsize() {
            if (!setupedsize) {
                Dimension d = sansaop.getScrollableArchitectureObjectPartDimension();
                d = (d.equals(new Dimension())) ? sansaop.getScrollableArchitectureObjectPartDimension(getFontMetrics(getFont())) : d;
                this.setPreferredSize(d);
                this.setMinimumSize(d);
            }


        }
    }

    public CompontentScrollAndNonScrollableArchitectureObjectPartAdapter(ScrollAndNonScrollableArchitectureObjectPart andNonScrollableArchitectureObjectPart) {
        this.andNonScrollableArchitectureObjectPart = andNonScrollableArchitectureObjectPart;
    }

    public Container getScrollableArchitectureObjectPartAdapter() {
        return new ScrollableArchitectureObjectPart(andNonScrollableArchitectureObjectPart);
    }

    public Container getNonScrollableArchitectureObjectPartAdapter() {
        return new NonScrollableArchitectureObjectPart(andNonScrollableArchitectureObjectPart);
    }
}
