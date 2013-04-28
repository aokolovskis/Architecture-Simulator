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
package architecturesimulator.ArchitectureObject.Gate;

import architecturesimulator.BinaryFunctions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class AndGate extends Gate{
    
   
    
    public AndGate(int size) {
        
        this.size = size;
    }

    @Override
    public void action() {
        super.action();
        Output.setValue(BinaryFunctions.and(Input.getValue(), BinaryFunctions.hexToBinary(mask)));
    }

    @Override
    public String getDescription() {
        return "AND";
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }       
}
