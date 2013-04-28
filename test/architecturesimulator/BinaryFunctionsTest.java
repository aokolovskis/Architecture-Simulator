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
public class BinaryFunctionsTest {

    public BinaryFunctionsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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

    /**
    
 * Test of xor method, of class BinaryFunctions.
    
 */
    
    @Test
    public void testAnd() {
        System.out.println("and");
        String s1 = "1001";
        String s2 = "1010";
        String expResult = "1000";
        String result = BinaryFunctions.and(s1, s2);
        assertEquals(expResult, result);

        s1 = "0";
        s2 = "";
        expResult = "0";
        result = BinaryFunctions.and(s1, s2);
        assertEquals(expResult, result);

        s1 = "000101010110";
        s2 = "0";
        expResult = "000000000000";
        result = BinaryFunctions.and(s1, s2);
        assertEquals(expResult, result);

        s1 = "000101010110";
        s2 = "1";
        expResult = "000000000000";
        result = BinaryFunctions.and(s1, s2);
        assertEquals(expResult, result);

        
        s1 = "";
        s2 = "1";
        expResult = "0";
        result = BinaryFunctions.and(s1, s2);
        assertEquals(expResult, result);

        s1 = "00110";
        s2 = "101";
        expResult = "00100";
        result = BinaryFunctions.and(s1, s2);
        assertEquals(expResult, result);
        

    }

    @Test
    public void testXor() {
        System.out.println("xor");
        String s1 = "1001";
        String s2 = "1010";
        String expResult = "0011";
        String result = BinaryFunctions.xor(s1, s2);
        assertEquals(expResult, result);

        s1 = "01001";
        s2 = "101010";
        expResult = "100011";
        result = BinaryFunctions.xor(s1, s2);
        assertEquals(expResult, result);

        s1 = "0";
        s2 = "101010";
        expResult = "101010";
        result = BinaryFunctions.xor(s1, s2);
        assertEquals(expResult, result);

        s1 = "0";
        s2 = "";
        expResult = "0";
        result = BinaryFunctions.xor(s1, s2);
        assertEquals(expResult, result);

        s1 = "";
        s2 = "1";
        expResult = "1";
        result = BinaryFunctions.xor(s1, s2);
        assertEquals(expResult, result);

        s1 = "11111";
        s2 = "100000";
        expResult = "111111";
        result = BinaryFunctions.xor(s1, s2);
        assertEquals(expResult, result);

        

    }

    @Test
    public void normalizeStringlengtTest (){
         System.out.println("normalizeStringlengt");
        String s1 = "1001";
        String expResult = "1001";
        String result = BinaryFunctions.normalizeStringlengt(s1, 4);
        assertEquals(expResult, result);

        s1 = "11111111";
        expResult = "1111";
        result = BinaryFunctions.normalizeStringlengt(s1, 4);
        assertEquals(expResult, result);

        s1 = "";
        expResult = "0000";
        result = BinaryFunctions.normalizeStringlengt(s1, 4);
        assertEquals(expResult, result);

        s1 = "1";
        expResult = "0001";
        result = BinaryFunctions.normalizeStringlengt(s1, 4);
        assertEquals("0001", BinaryFunctions.normalizeStringlengt(s1, 4));

    }
    
    @Test
    public void twoKTest(){
        System.out.println("twoKtest");
        String s1 = "0000";
        assertEquals("0000",BinaryFunctions.twoK(s1, 4));
        
        s1 = "0001";
        assertEquals("11111",BinaryFunctions.twoK(s1, 5));
        
         s1 = "1111";
        assertEquals("10001",BinaryFunctions.twoK(s1, 5));
        
    }

    @Test
    public void addTest(){
        System.out.println("addTest");
        String s1 = "0000";
        String s2 = "0000";
        Flag b = new Flag(false);
        assertEquals("00000",BinaryFunctions.add(s1,s2, 5, b));
        assertEquals(false,b.isFlag());

        s1 = "0000";
        s2 = "0001";
        b.setFlag(false);
        assertEquals("00001",BinaryFunctions.add(s1,s2, 5, b));
        assertEquals(false,b.isFlag());

         s1 = "1000";
         s2 = "1001";
         b.setFlag(false);
        assertEquals("10001",BinaryFunctions.add(s1,s2, 5, b));
        assertEquals(false,b.isFlag());

        s1 = "11100";
        s2 = "11010";
        b.setFlag(false);
        assertEquals("10110",BinaryFunctions.add(s1,s2, 5, b));
        assertEquals(true,b.isFlag());

    }


    @Test
    public void subTwoKTest(){
        System.out.println("addTest");
        String s1 = "0000";
        String s2 = "0000";
        Flag b = new Flag(false);
        assertEquals("00000",BinaryFunctions.subTwoK(s1,s2, 5, null));
        assertEquals(false,b.isFlag());

        s1 = "0000";
        s2 = "0001";
        b.setFlag(false);
        assertEquals("11111",BinaryFunctions.subTwoK(s1,s2, 5, null));
        assertEquals(false,b.isFlag());

        s1 = "1111";
        s2 = "0001";
        b.setFlag(false);
        assertEquals("1110",BinaryFunctions.subTwoK(s1,s2, 4, b));
        assertEquals(true,b.isFlag());

    }


}
