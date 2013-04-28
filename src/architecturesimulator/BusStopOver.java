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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class BusStopOver {

    ArchitectureObjectWithBusConnection start;
    ArchitectureObjectWithBusConnection end;
    List<Point2D> stopovers = new ArrayList<Point2D>();

    public BusStopOver(ArchitectureObjectWithBusConnection start,
            ArchitectureObjectWithBusConnection end) {
        this.start = start;
        this.end = end;
    }

    public void addPoint(Point2D p) {
        stopovers.add(p);
    }

    public ArchitectureObjectWithBusConnection getEnd() {
        return end;
    }

    public ArchitectureObjectWithBusConnection getStart() {
        return start;
    }

    public List<Point2D> getStopovers() {
        return stopovers;
    }

    
}
