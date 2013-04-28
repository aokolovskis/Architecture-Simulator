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

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class BusPath {

    ArchitectureObjectWithBusConnection startfrom;
    List<BusStopOver> stopOvers = new ArrayList<BusStopOver>();
    Bus pathforbus;
    Map<ArchitectureObjectWithBusConnection, Point2D> ArchtMap;

    public BusPath(Bus b,Map<ArchitectureObjectWithBusConnection, Point2D> ArchtMap , ArchitectureObjectWithBusConnection startfrom) {
        this.startfrom = startfrom;
        this.ArchtMap = ArchtMap;
        pathforbus = b;
    }

    public void addStopOver(BusStopOver bso) {
        stopOvers.add(bso);
    }

    public void setStartfrom(ArchitectureObjectWithBusConnection startfrom) {
        this.startfrom = startfrom;
    }

    

    public Path2D getPath() throws IOException {
        List<BusStopOver> bsos = new ArrayList<BusStopOver>(stopOvers);
        List<BusStopOver> sorted = new ArrayList<BusStopOver>();
        boolean zeroloop = false;



        while (bsos.size() > 0) {
            if (zeroloop) {
                throw new IOException("Bus path was not correctly created!... ");
            }
            zeroloop = true;
            for (int i = 0; i < bsos.size(); i++) {
                BusStopOver bso = bsos.get(i);
                if (sorted.isEmpty()) {
                    if (bso.getStart() == startfrom) {
                        sorted.add(bso);
                        bsos.remove(bso);
                        zeroloop = false;
                        break;
                    }
                } else {
                    if (sorted.get(sorted.size() - 1).getEnd() == bso.getStart()) {
                        sorted.add(bso);
                        bsos.remove(bso);
                        zeroloop = false;
                        break;

                    }

                }

            }
        }


        Path2D myPath = new Path2D.Double();
        Point2D absulut = ArchtMap.get(startfrom);
        Point2D p = startfrom.getConnectionPoint(pathforbus);
        myPath.moveTo(p.getX()+absulut.getX(), absulut.getY()+p.getY());

        for (BusStopOver busStopOver : sorted) {
          absulut = ArchtMap.get(busStopOver.getStart());
          p = busStopOver.getStart().getConnectionPoint(pathforbus);
          myPath.lineTo(p.getX()+absulut.getX(), absulut.getY()+p.getY());

            for (Point2D point2D : busStopOver.stopovers) {
                myPath.lineTo(point2D.getX(), point2D.getY());
            }

          absulut = ArchtMap.get(busStopOver.getEnd());
          p = busStopOver.getEnd().getConnectionPoint(pathforbus);
          myPath.lineTo(p.getX()+absulut.getX(), absulut.getY()+p.getY());
        }


        return myPath;
    }
}
