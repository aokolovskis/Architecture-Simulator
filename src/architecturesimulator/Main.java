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
import architecturesimulator.gui.MainScreen;
import javax.swing.JFrame;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class Main {

    /**
    
 * @param args the command line arguments
    
 */
    public static void main(String[] args) {
        JFrame w;
        if (args.length == 1 && args[0].equals("-d")) {
            w = new DebugWindow();
        } else {
            w = new MainScreen();
        }



        w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
        w.setVisible(true);



        // TODO code application logic here
    }
}
