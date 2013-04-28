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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class TimingAndControlTest {
    
    public TimingAndControlTest() {
    }

    static Simulator s = new Simulator();
    
    static JFrame myFrame = new JFrame();
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        myFrame.getContentPane().add(s);
        s.setup(System.getProperty("user.dir")+"/mu0-2.archtektur",System.getProperty("user.dir")+"/mu0-2.layout");
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testPerfomeInstructionStep() {
        try {
            s.InstructionStep();
        } catch (IOException ex) {
            assertTrue("Exception!", false);
        }
    }
    
    @Test
    public void testPerfomeStepprettyOften() {
       
        for (int i = 0; i < Math.pow(2, 10); i++) {
            try {
            s.InstructionStep();
        } catch (IOException ex) {
            assertTrue("Exception!"+ex, false);
        }
            
        }
        
    }
}
