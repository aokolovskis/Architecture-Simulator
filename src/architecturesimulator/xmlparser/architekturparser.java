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

import architecturesimulator.Alu;
import architecturesimulator.ArchitectureObject.Gate.AndGate;
import architecturesimulator.ArchtekturObject;
import architecturesimulator.Bus;
import architecturesimulator.Instruction;
import architecturesimulator.InstructionCondition;
import architecturesimulator.InstructionStep;
import architecturesimulator.Multiplexer;
import architecturesimulator.RAM;
import architecturesimulator.Register;
import architecturesimulator.TimingAndControl;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class architekturparser {

    private static int architecture = 0;

    public static void parseDocument(String filename, TimingAndControl TAC, Map<String, architecturesimulator.ArchtekturObject> ArchtectureLookupTble) throws ParserConfigurationException, SAXException, IOException {
        Document doc = readFile(filename);
        parseDocument(TAC, doc, ArchtectureLookupTble);

    }

    public static Document readFile(String filename) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = factory.newDocumentBuilder();
        Document doc = parser.parse(new File(filename));

        return doc;
    }

    private static void parseDocument(TimingAndControl TAC, Document doc,
            Map<String, architecturesimulator.ArchtekturObject> ArchtectureLookupTble) throws ParseException {

        Node N = doc.getFirstChild();


        if (N.getNodeName().equals("machine")) {
            if (N.getAttributes().getNamedItem("architecture") != null) {
                architecture = Integer.parseInt(N.getAttributes().getNamedItem("architecture").getNodeValue());
            }
        }
        NodeList nl = N.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("components")) {
                parseComponent(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("instructions")) {
                parseInstructions(TAC, tmp, ArchtectureLookupTble);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown <architecture> childName: " + tmp.getNodeName());
                }
            }
        }

    }

    private static void parseComponent(TimingAndControl TAC, Node node, Map<String, architecturesimulator.ArchtekturObject> ArchtectureLookupTble) throws ParseException {



        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("alu")) {
                parseAlu(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("andgate")) {
                parseAndGate(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("register")) {
                parseRegister(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("multiplexer")) {
                parseMultiplexer(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("ram")) {
                parseRam(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("buses")) {
                parseBuses(TAC, tmp, ArchtectureLookupTble);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown <component> : " + tmp.getNodeName());
                }
            }
        }
    }

    private static void parseInstructions(TimingAndControl TAC, Node node, Map<String, architecturesimulator.ArchtekturObject> ArchtectureLookupTble) throws ParseException {
        NodeList nl = node.getChildNodes();

        Register r = (Register) ArchtectureLookupTble.get(ParserFunctions.getAttributeByName("sourceregister", node));
        TAC.setInstructionRegister(r);
        TAC.setOpCodePosition(Integer.parseInt(ParserFunctions.getAttributeByName("startfrombit", node)));
        TAC.setOpCodelength(Integer.parseInt(ParserFunctions.getAttributeByName("length", node)));

        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("fetch")) {
                parseFetchInstruction(TAC, tmp, ArchtectureLookupTble);
            } else if (tmp.getNodeName().equals("instruction")) {
                parseInstruction(TAC, tmp, ArchtectureLookupTble);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown <component> : " + tmp.getNodeName());
                }
            }
        }

    }

    private static void parseAlu(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup) throws ParseException {

        Alu alu = new Alu(architecture);
        alu.setName(ParserFunctions.getNameAtribute(node));
        addtoTACandNameLookUp(TAC, node, namelookup, alu);

    }

    private static void parseRegister(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup) throws ParseException {
        Register reg = new Register(architecture);
        reg.setName(ParserFunctions.getNameAtribute(node));
        addtoTACandNameLookUp(TAC, node, namelookup, reg);
    }

    private static void parseMultiplexer(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup) throws ParseException {
        Multiplexer mux = new Multiplexer();
        mux.setName(ParserFunctions.getNameAtribute(node));
        addtoTACandNameLookUp(TAC, node, namelookup, mux);
    }

    private static void parseRam(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup) throws ParseException {
        RAM r = new RAM(architecture);
        r.setName(ParserFunctions.getNameAtribute(node));
        NodeList nl = node.getChildNodes();
        int j = 0;
        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("value")) {

                r.setValue(j, Long.parseLong(tmp.getFirstChild().getTextContent(), 16));
                j++;
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown <buses> : " + tmp.getNodeName());
                }
            }
        }
        addtoTACandNameLookUp(TAC, node, namelookup, r);

    }

    private static void parseBuses(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup) throws ParseException {
        NodeList nl = node.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("bus")) {
                parseAndConnectBus(TAC, tmp, namelookup);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown <buses> : " + tmp.getNodeName());
                }
            }
        }

    }

    private static void addtoTACandNameLookUp(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup, ArchtekturObject ao) throws ParseException {
        namelookup.put(ParserFunctions.getNameAtribute(node), ao);
        TAC.add(ao);
    }

    private static void parseAndConnectBus(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> namelookup) throws ParseException {
        Bus bus = new Bus();
        addtoTACandNameLookUp(TAC, node, namelookup, bus);

        NodeList nl = node.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("connectto")) {
                parseConnectto(tmp, namelookup, bus);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown should be connectto bus got : " + tmp.getNodeName());
                }
            }
        }


    }

    private static void parseConnectto(Node node, Map<String, ArchtekturObject> namelookup, Bus b) throws ParseException {
        NodeList nl = node.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("register")) {
                parseConnectToRegister(tmp, namelookup, b);
            } else if (tmp.getNodeName().equals("andgate")) {
                parseConnectToAndGate(tmp, namelookup, b);
            } else if (tmp.getNodeName().equals("multiplexer")) {
                parseConnectToMuliplexer(tmp, namelookup, b);
            } else if (tmp.getNodeName().equals("alu")) {
                parseConnectToAlu(tmp, namelookup, b);
            } else if (tmp.getNodeName().equals("ram")) {
                parseConnectToRam(tmp, namelookup, b);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown should be connectto bus got : " + tmp.getNodeName());
                }
            }
        }

    }

    private static void parseConnectToMuliplexer(Node node, Map<String, ArchtekturObject> namelookup, Bus b) throws ParseException {
        String name = ParserFunctions.getNameAtribute(node);
        Multiplexer mux = (Multiplexer) namelookup.get(name);
        if (mux == null) {
            throw new ParseException("Bus to Multiplexer connection failed, Multiplexer name:" + name + " was not found");
        }
        String channel = ParserFunctions.getAttributeByName("channel", node);

        if (channel.equals("out")) {
            mux.setOutput(b);
        } else {
            try {
                int ChannelNr = Integer.parseInt(channel);
                mux.addInputBus(b, ChannelNr);
            } catch (NumberFormatException e) {
                throw new ParseException("Unknown Mux to bus Connection should be out or a channel number but got : " + channel);
            }
        }
    }

    private static void parseConnectToRegister(Node node, Map<String, ArchtekturObject> namelookup, Bus b) throws ParseException {
        String name = ParserFunctions.getNameAtribute(node);
        Register re = (Register) namelookup.get(name);
        if (re == null) {
            throw new ParseException("Bus to Register connection failed, register name:" + name + " was not found");
        }
        String type = ParserFunctions.getTypeAtribute(node);
        if (type.equals("in")) {
            re.setInput(b);
        } else if (type.equals("out")) {
            re.setOutput(b);
        } else {
            throw new ParseException("Unknown Register to bus Connection should be in or out but got : " + type);
        }

    }

    private static void parseConnectToRam(Node node, Map<String, ArchtekturObject> namelookup, Bus b) throws ParseException {
        String name = ParserFunctions.getNameAtribute(node);
        RAM ram = (RAM) namelookup.get(name);
        if (ram == null) {
            throw new ParseException("Bus to RAM connection failed, ram name:" + name + " was not found");
        }

        String type = ParserFunctions.getTypeAtribute(node);
        if (type.equals("data")) {
            ram.setDataBus(b);
        } else if (type.equals("adress")) {
            ram.setAdressBus(b);
        } else {
            throw new ParseException("Unknown RAM to bus Connection should be adress or data but got : " + type);
        }

    }

    private static void parseConnectToAlu(Node node, Map<String, ArchtekturObject> namelookup, Bus b) throws ParseException {
        String name = ParserFunctions.getNameAtribute(node);
        Alu alu = (Alu) namelookup.get(name);
        if (alu == null) {
            throw new ParseException("Bus to ALU connection failed, alu name:" + name + " was not found");
        }
        String aluBus = ParserFunctions.getAttributeByName("bus", node);
        if (aluBus.equals("a")) {
            alu.setBusA(b);
        } else if (aluBus.equals("b")) {
            alu.setBusB(b);
        } else if (aluBus.equals("out")) {
            alu.setOut(b);
        } else {
            throw new ParseException("Unknown ALU to bus Connection should be a, b or out but got: " + aluBus);
        }

    }

    private static void parseFetchInstruction(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> ArchtectureLookupTble) throws ParseException {
        Instruction I = null;
        try {
            I = new Instruction("fetch", ParserFunctions.getAttributeByName("opcode", node));
        } catch (ParseException e) {
            I = new Instruction("fetch", "fetch");
        }


        NodeList nl = node.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("step")) {
                parseInstructionStep(tmp, ArchtectureLookupTble, I);
            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown be step but got : " + tmp.getNodeName());
                }
            }
        }
        TAC.setFetch(I);
        TAC.addInstruction(I);
    }

    private static void parseInstructionStep(Node node, Map<String, ArchtekturObject> ArchtectureLookupTble, Instruction I) throws ParseException {
        InstructionStep IS = new InstructionStep();
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                parseInstructionStepObjects(tmp, ArchtectureLookupTble, IS);
            }

        }
        I.addInstuctionStep(IS);
    }

    private static void parseInstructionStepObjects(Node node, Map<String, ArchtekturObject> AM, InstructionStep IS) throws ParseException {
        if (node.getNodeName().equals("register")) {
            String type = ParserFunctions.getTypeAtribute(node);
            Register R = (Register) AM.get(ParserFunctions.getNameAtribute(node));
            if (R == null) {
                throw new ParseException("Undifined Register in Instructions : " + node.getNodeName());
            }
            if (type.equals("out")) {
                IS.addRegisterOutputEnanle(R);
            } else if (type.equals("in")) {
                IS.addRegisterChipSelect(R);
            } else {
                throw new ParseException("Unknown Register instruction step : " + node.getNodeName());
            }

        } else if (node.getNodeName().equals("multiplexer")) {
            String channel = ParserFunctions.getAttributeByName("channel", node);
            Multiplexer M = (Multiplexer) AM.get(ParserFunctions.getNameAtribute(node));
            if (M == null) {
                throw new ParseException("Undifined Register in Instructions : " + node.getNodeName());
            }
            Integer chanelSelect = Integer.parseInt(channel);
            if (M.hasChannel(chanelSelect)) {
                IS.addMultiplexerChanelSelect(M, chanelSelect);
            } else {
                throw new ParseException("Unknown Register instruction step : " + node.getNodeName() + ". channel " + channel + " not Supported");
            }
        } else if (node.getNodeName().equals("alu")) {
            Alu alu = (Alu) AM.get(ParserFunctions.getNameAtribute(node));
            if (alu == null) {
                throw new ParseException("Undifined alu in Instructions : " + node.getNodeName());
            }
            String aluinstruction = ParserFunctions.getAttributeByName("instruction", node);
            try {
                IS.addAluFunction(alu, Alu.stringToALUfunction(aluinstruction));
            } catch (IOException ex) {
                Logger.getLogger(architekturparser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (node.getNodeName().equals("ram")) {
            RAM ram = (RAM) AM.get(ParserFunctions.getNameAtribute(node));
            if (ram == null) {
                throw new ParseException("Undifined ram in Instructions : " + node.getNodeName());
            }
            try {
                boolean CS = Boolean.valueOf(ParserFunctions.getAttributeByName("cs", node));
                boolean RNW = Boolean.valueOf(ParserFunctions.getAttributeByName("rnw", node));
                IS.addRamChipselet(ram, CS);
                IS.addRamRnW(ram, RNW);
            } catch (IOException ex) {
                Logger.getLogger(architekturparser.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else {
            if (node.getNodeType() != Node.TEXT_NODE && node.getNodeType() != Node.COMMENT_NODE) {
                throw new ParseException("Unknown instruction step : " + node.getNodeName());
            }
        }

    }

    private static void parseInstruction(TimingAndControl TAC, Node node, Map<String, ArchtekturObject> ArchtectureLookupTble) throws ParseException {
        Instruction I = new Instruction(ParserFunctions.getNameAtribute(node), ParserFunctions.getAttributeByName("opcode", node));
        NodeList nl = node.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            Node tmp = nl.item(i);
            if (tmp.getNodeName().equals("step")) {
                parseInstructionStep(tmp, ArchtectureLookupTble, I);
            } else if (tmp.getNodeName().equals("condition")) {
                parseInstructionCondition(tmp, ArchtectureLookupTble, I);

            } else {
                if (tmp.getNodeType() != Node.TEXT_NODE && tmp.getNodeType() != Node.COMMENT_NODE) {
                    throw new ParseException("Unknown be step but got : " + tmp.getNodeName());
                }
            }
        }
        TAC.addInstruction(I);
    }

    private static void parseAndGate(TimingAndControl TAC, Node tmp, Map<String, ArchtekturObject> ArchtectureLookupTble) throws ParseException {
        AndGate gate = new AndGate(architecture);

        NamedNodeMap nnm = tmp.getAttributes();
        Node mask = nnm.getNamedItem("mask");
        if (mask != null) {
            gate.setMask(mask.getNodeValue());
        }


        addtoTACandNameLookUp(TAC, tmp, ArchtectureLookupTble, gate);
    }

    private static void parseConnectToAndGate(Node node, Map<String, ArchtekturObject> namelookup, Bus b) throws ParseException {
        String name = ParserFunctions.getNameAtribute(node);
        AndGate ag = (AndGate) namelookup.get(name);
        if (ag == null) {
            throw new ParseException("Bus to AndGate connection failed, gate name:" + name + " was not found");
        }
        String type = ParserFunctions.getTypeAtribute(node);
        if (type.equals("in")) {
            ag.setInput(b);
        } else if (type.equals("out")) {
            ag.setOutput(b);
        } else {
        }
    }

    private static void parseInstructionCondition(Node tmp, Map<String, ArchtekturObject> ArchtectureLookupTble, Instruction I) throws ParseException {
        InstructionCondition ic = new InstructionCondition(ArchtectureLookupTble, tmp);
        I.addInstructionCondition(ic);
        System.out.println(I.isConditionSatisfied());
    }
    
 
}
