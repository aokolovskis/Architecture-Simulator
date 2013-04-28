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
package architecturesimulator.xmlparser;

import org.w3c.dom.Node;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class ParserFunctions {

    public static boolean hasAttribute(String attributeName, Node src) {
        boolean result = false;
        if (src != null) {
            try {
                result = (getAttributeByName(attributeName, src) != null) ? true : false;
            } catch (Exception e) {
            }
        }

        return result;
    }

    public static String getAttributeByName(String attributeName, Node src) throws ParseException {
        Node AtributeNode = src.getAttributes().getNamedItem(attributeName);
        if (AtributeNode != null) {
            return AtributeNode.getNodeValue();
        }
        throw new ParseException("Node " + src.getNodeName() + " has no " + attributeName + " atribute!");
    }

    public static String getNameAtribute(Node node) throws ParseException {
        return getAttributeByName("name", node);
    }

    public static String getTypeAtribute(Node node) throws ParseException {
        return getAttributeByName("type", node);

    }
}
