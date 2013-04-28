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
import architecturesimulator.xmlparser.ParserFunctions;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class InstructionCondition {

    conditionfunction todo;

    private interface ValueAdapter {

        String getValue();
    }

    private class RegisterValueAdapter implements ValueAdapter {

        Register r;

        public RegisterValueAdapter(Register r) {
            this.r = r;
        }

        @Override
        public String getValue() {
            return r.getValue();
        }
    }

    private interface conditionfunction {

        public boolean verfiy();
    }

    private class biggerOrEqual2k implements conditionfunction {
 
        ValueAdapter v;
        String s;

        public biggerOrEqual2k(ValueAdapter va, String s) {
            v = va;
            this.s = s;
        }

        public boolean verfiy() {
            StringPair sp = BinaryFunctions.normalizeStringlengt(new StringPair(v.getValue(), s));
            
            String result = BinaryFunctions.subTwoK(sp.getS1(), s, sp.getS1().length(), null);
            boolean bigger = !result.startsWith("1");
            boolean equals = sp.getS1().equals(sp.getS2()) ? true : false;
          //  return bigger & equals; gaga
            return bigger | equals;
        }
    }

    private class equals implements conditionfunction {

        ValueAdapter v;
        String s;

        public equals(ValueAdapter va, String s) {
            v = va;
            this.s = s;
        }

        public boolean verfiy() {
            StringPair sp = BinaryFunctions.normalizeStringlengt(new StringPair(v.getValue(), s));
            return sp.getS1().equals(sp.getS2()) ? true : false;
        }
    }

    private class equalsNot implements conditionfunction {

        ValueAdapter v;
        String s;

        public equalsNot(ValueAdapter va, String s) {
            v = va;
            this.s = s;
        }

        public boolean verfiy() {
            StringPair sp = BinaryFunctions.normalizeStringlengt(new StringPair(v.getValue(), s));
            return sp.getS1().equals(sp.getS2()) ? false : true;
        }
    }

    public InstructionCondition(Map<String, architecturesimulator.ArchtekturObject> ArchtectureLookupTble, Node node) throws ParseException {

        if (node.getNodeName().equals("condition")) {
            NodeList nl = node.getChildNodes();

            for (int i = 0; i < nl.getLength(); i++) {
                Node tmp = nl.item(i);
                if (tmp.getNodeName().equals("register")) {
                    try {
                        String name = ParserFunctions.getNameAtribute(tmp);
                        Register r = (Register) ArchtectureLookupTble.get(name);
                        if (r == null) {
                            throw new ParseException("Trying to construct an instruction: Register not found by name:" + name);
                        }

                        NodeList conditionfunctionstringlist = tmp.getChildNodes();

                        for (int j = 0; j < conditionfunctionstringlist.getLength(); j++) {
                            Node conditionfunctionstring = conditionfunctionstringlist.item(j);
                            if (conditionfunctionstring.getNodeName().equals("equals")) {
                                todo = new equals(new RegisterValueAdapter(r), ParserFunctions.getAttributeByName("value", conditionfunctionstring));
                            } else if (conditionfunctionstring.getNodeName().equals("equalsNot")) {
                                todo = new equalsNot(new RegisterValueAdapter(r), ParserFunctions.getAttributeByName("value", conditionfunctionstring));
                            } else if (conditionfunctionstring.getNodeName().equals("biggerOrEqual2k")) {
                                todo = new biggerOrEqual2k(new RegisterValueAdapter(r), ParserFunctions.getAttributeByName("value", conditionfunctionstring));
                            } else {
                                if (conditionfunctionstring.getNodeType() != Node.TEXT_NODE && conditionfunctionstring.getNodeType() != Node.COMMENT_NODE) {
                                    throw new ParseException("Unknown condion type supportet is register : " + tmp.getNodeName());
                                }
                            }
                        }



                    } catch (Exception e) {
                        throw new ParseException("Trying to construct an instruction: " + e.getMessage());
                    }
                } else {
                    if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                        throw new ParseException("Unknown condion type supportet is register : " + tmp.getNodeName());
                    }
                }
            }

        } else {
            throw new ParseException("Trying to construct an instruction condition should be condition but got : " + node.getNodeName());
        }
    }

    public boolean isSatisfied() {
        return todo.verfiy();
    }
}
