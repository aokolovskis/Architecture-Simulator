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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public interface ScrollAndNonScrollableArchitectureObjectPart {
    public void paintNonScrollableArchitectureObjectPart (Graphics g);
    public Dimension getNonScrollableArchitectureObjectPartDimension();
    public Dimension getNonScrollableArchitectureObjectPartDimension(FontMetrics fm);
    public void setPreferedNonScrollableArchitectureObjectPartDimension(Dimension d);
    
    public void paintScrollableArchitectureObjectPart (Graphics g);
    public Dimension getScrollableArchitectureObjectPartDimension();
    public Dimension getScrollableArchitectureObjectPartDimension(FontMetrics fm);
}
