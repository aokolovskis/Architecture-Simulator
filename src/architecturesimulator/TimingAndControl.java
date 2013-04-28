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

import architecturesimulator.ArchitectureObject.Gate.AndGate;
import com.sun.jndi.toolkit.ctx.HeadTail;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class TimingAndControl {
    //TODO Change Collection into Set else duplicats can aper.

    private Collection<ArchtekturObject> SceneObjects = new ArrayList<ArchtekturObject>();
    //  private Collection<Connection> Connections = new ArrayList<Connection>();
    private boolean initialized = false;
    private int step = 0;
    private boolean dofetch = true;
    private Instruction currentInstruction = null;
    private Instruction fetch = null;
    private int OpCodePosition, OpCodelength = 0;
    private Register InstructionRegister;
    private StringBuilder PerformedInstructionsNameBuffer = new StringBuilder(2048);
    private SortedMap<String, Instruction> instructions = new TreeMap<String, Instruction>(new Comparator<String>() {

        public int compare(String o1, String o2) {
            if (!BinaryFunctions.isBinaryString(o1)) {
                return -1;
            };

            if (!BinaryFunctions.isBinaryString(o2)) {
                return 1;
            };

            BinaryFunctions.normalizeStringlengt(new StringPair(o1, o2));

            if (o1.equals(o2)) {
                return 0;
            } else {
                return Integer.valueOf(o1, 2) < Integer.valueOf(o2, 2) ? -1 : 1;
            }



        }
    });


    public TimingAndControl() {
    }

    public void performStep() throws IOException {
        if (!initialized) {
            initialize();
        }


        if (dofetch) {

            performFetch();

        } else {
            if (step == 0 & !currentInstruction.isConditionSatisfied()) {
                performFetch();

            } else {

                InstructionStep instructionStep = currentInstruction.getStep(step);
                performInstructionStep(instructionStep);
                step++;

                if (!currentInstruction.hasStep(step)) {
                    dofetch = true;
                    step = 0;
                }
            }

        }
    }

    public void performInstruction() throws IOException {
        performStep();
        while (currentInstruction.hasStep(step + 1)) {
            performStep();
        }

    }

    public void add(ArchtekturObject e) {
        SceneObjects.add(e);
        initialized = false;


    }

    public void initialize() throws IOException {
        if (InstructionRegister == null) {
            throw new IOException("Instruction Register for Timeing And Controll ist nor set.!");
        }
        if (OpCodePosition == 0 || OpCodelength == 0) {
            throw new IOException("Not initalized opcode position and opcode length.");
        }

        if (fetch == null) {
            throw new IOException("No Fetch Instruction available");
        }
        initialized = true;
        step = 0;

    }

    public Collection<ArchtekturObject> getSceneObjects() {
        return SceneObjects;
    }

    public void addInstruction(Instruction i) {
        instructions.put(i.OpCode, i);
    }

    public void setInstructionRegister(Register r) {
        if (r == null) {
            return;
        } else {
            InstructionRegister = r;
        }
    }

    public Register getInstructionRegister() {
        return InstructionRegister;
    }

    private void performInstructionStep(InstructionStep instructionStep) throws IOException {


        //Output Enable
        for (Register register : instructionStep.getOutputEnable()) {
            register.outputEnable();
//TODO:: BAD FIX CALLBACKS NEEDED!!!
            if (register.getName().equals("IR")) {
                for (ArchtekturObject archtekturObject : SceneObjects) {
                    if (archtekturObject instanceof AndGate) {
                        ((AndGate) archtekturObject).action();
                    }
                }
            }
        }

        //Gate Action


        //MultiplexerAction
        for (Multiplexer multiplexer : instructionStep.getMultiPlexerChanel().keySet()) {
            multiplexer.selectBusNr((Integer) instructionStep.getMultiPlexerChanel().get(multiplexer));
            multiplexer.performeAction();
        }

        //Perform ALU Function
        for (Alu alu : instructionStep.getAluFunctions().keySet()) {
            alu.performFunction(instructionStep.getAluFunctions().get(alu));
        }


        for (RAM ram : instructionStep.getRAMRnW().keySet()) {
            ram.setReadNotWrite(instructionStep.getRAMRnW().get(ram));
        }

        for (RAM ram : instructionStep.getRAMChipSelect().keySet()) {
            ram.setChipSelect(instructionStep.getRAMChipSelect().get(ram));
        }


        //Perform ChipSelect
        for (Register register : instructionStep.getChipSelect()) {
            register.chipEnable();
        }

    }

    private Instruction findInstructionFromRegister() throws IOException {
        String RegisterValue = InstructionRegister.getValue();
        while ((OpCodePosition + 1) > RegisterValue.length()) {
            RegisterValue = "0" + RegisterValue;
        }

        String stringInstruction = RegisterValue.substring(RegisterValue.length() - (OpCodePosition + 1), (RegisterValue.length() - (OpCodePosition + 1)) + OpCodelength);
        Instruction I = instructions.get(stringInstruction);
        if (I == null) {
            throw new IOException("Unkonw Instruction! " + stringInstruction);
        }
        return I;

    }

    public int getOpCodePosition() {
        return OpCodePosition;
    }

    public void setOpCodePosition(int OpCodePosition) {
        this.OpCodePosition = OpCodePosition;
    }

    public int getOpCodelength() {
        return OpCodelength;
    }

    public void setOpCodelength(int OpCodelength) {
        this.OpCodelength = OpCodelength;
    }

    private void performFetch() throws IOException {
        InstructionStep instructionStep = fetch.getStep(step);
        performInstructionStep(instructionStep);
        step++;
        if (!fetch.hasStep(step)) {
            dofetch = false;
            if (currentInstruction != null) {
                logPerforedInstructionName(currentInstruction.getName());
            };
            currentInstruction = findInstructionFromRegister();
            step = 0;
        }


    }

    public Instruction getFetch() {
        return fetch;
    }

    public void setFetch(Instruction fetch) {
        this.fetch = fetch;
    }

    public AbstractTableModel generateInstuctionTable() {


        final Map<ArchitectureObjectWithBusConnection, Integer> architectureObjectWithBusConnectionColumnIndex = new HashMap<ArchitectureObjectWithBusConnection, Integer>();
        final SortedMap<Integer, String> columnNamebyIndex = new TreeMap<Integer, String>();

        int lastFreeColumnIndex = 0;

        columnNamebyIndex.put(lastFreeColumnIndex, "Instruction");
        lastFreeColumnIndex++;

        columnNamebyIndex.put(lastFreeColumnIndex, "Op. code");
        lastFreeColumnIndex++;


        for (ArchtekturObject archtekturObject : SceneObjects) {

            if (!(archtekturObject instanceof ArchitectureObjectWithBusConnection)) {
                continue;
            }
            final ArchitectureObjectWithBusConnection curentArchitectureObjectWithBusConnection = (ArchitectureObjectWithBusConnection) archtekturObject;

            if (archtekturObject instanceof Register) {
                final Register reg = (Register) curentArchitectureObjectWithBusConnection;
                architectureObjectWithBusConnectionColumnIndex.put(reg, lastFreeColumnIndex);
                columnNamebyIndex.put(lastFreeColumnIndex, reg.getName() + "_oe");
                lastFreeColumnIndex++;
                columnNamebyIndex.put(lastFreeColumnIndex, reg.getName() + "_cs");
                lastFreeColumnIndex++;

            } else if (archtekturObject instanceof RAM) {
                final RAM ram = (RAM) curentArchitectureObjectWithBusConnection;
                architectureObjectWithBusConnectionColumnIndex.put(ram, lastFreeColumnIndex);
                columnNamebyIndex.put(lastFreeColumnIndex, ram.getName() + "_cs");
                lastFreeColumnIndex++;
                columnNamebyIndex.put(lastFreeColumnIndex, ram.getName() + "_RnW");
                lastFreeColumnIndex++;

            } else if (archtekturObject instanceof Multiplexer) {
                final Multiplexer mux = (Multiplexer) curentArchitectureObjectWithBusConnection;
                architectureObjectWithBusConnectionColumnIndex.put(mux, lastFreeColumnIndex);
                columnNamebyIndex.put(lastFreeColumnIndex, mux.getName());
                lastFreeColumnIndex++;
            } else if (archtekturObject instanceof Alu) {
                final Alu alu = (Alu) curentArchitectureObjectWithBusConnection;
                architectureObjectWithBusConnectionColumnIndex.put(alu, lastFreeColumnIndex);
                columnNamebyIndex.put(lastFreeColumnIndex, alu.getName() + "_func");
                lastFreeColumnIndex++;
            }
        }

        DefaultTableModel actm = new DefaultTableModel(1, 0);


        actm.setColumnIdentifiers(columnNamebyIndex.values().toArray());

        final Vector EmpryRows = new Vector();
        for (int i = 0; i < lastFreeColumnIndex; i++) {
            EmpryRows.add("");

        }


        for (Instruction instruction : instructions.values()) {
            final String Instructionname = instruction.getName();
            if (Instructionname.equals("fetch")) {
                continue;
            }

            Vector RowData = new Vector(EmpryRows);

            RowData.set(0, Instructionname);
            RowData.set(1, instruction.getOpCode());


            for (int i = 0; getFetch().hasStep(i); i++) {
                InstructionStep is = getFetch().getStep(i);
                PutInstructionSetpIntoVector(is, RowData, architectureObjectWithBusConnectionColumnIndex);
                actm.addRow(RowData);
                RowData = new Vector(EmpryRows);
            }




            for (int i = 0; instruction.hasStep(i); i++) {
                RowData = new Vector(EmpryRows);
                InstructionStep is = instruction.getStep(i);
                PutInstructionSetpIntoVector(is, RowData, architectureObjectWithBusConnectionColumnIndex);
                actm.addRow(RowData);
            }

        }



        return actm;
    }

    private void logPerforedInstructionName(String Name) {
        if (Name == null ) {return;}
        if (Name.equals("")) { Name = "[unknown]";}
        
        if (PerformedInstructionsNameBuffer.length() > 0) {
            Name = "" + Name;
        }

        System.out.println("capacty : " + PerformedInstructionsNameBuffer.capacity());
        System.out.println("length : " + PerformedInstructionsNameBuffer.length());
        
        if (PerformedInstructionsNameBuffer.length() + Name.length() >= PerformedInstructionsNameBuffer.capacity()) {
            PerformedInstructionsNameBuffer.delete(0, PerformedInstructionsNameBuffer.indexOf("", Name.length()));
           
        }

        PerformedInstructionsNameBuffer.append(Name);

    }

    public String collectPerformedInstruction() {
        final String out = PerformedInstructionsNameBuffer.toString();
        PerformedInstructionsNameBuffer.delete(0, PerformedInstructionsNameBuffer.length());
        return out;
    }

    private void PutInstructionSetpIntoVector(InstructionStep is, Vector RowData, Map<ArchitectureObjectWithBusConnection, Integer> architectureObjectWithBusConnectionColumnIndex) {
        for (Register register : is.getOutputEnable()) {
            RowData.set(architectureObjectWithBusConnectionColumnIndex.get(register), "1");
        }

        for (Register register : is.getChipSelect()) {
            RowData.set(architectureObjectWithBusConnectionColumnIndex.get(register) + 1, "1");
        }


        for (Map.Entry ramEntry : is.getRAMChipSelect().entrySet()) {
            final RAM ram = (RAM) ramEntry.getKey();
            RowData.set(architectureObjectWithBusConnectionColumnIndex.get(ram), (((Boolean) ramEntry.getValue()) ? "1" : "0"));
        }

        for (Map.Entry ramEntry : is.getRAMRnW().entrySet()) {
            final RAM ram = (RAM) ramEntry.getKey();
            RowData.set(architectureObjectWithBusConnectionColumnIndex.get(ram) + 1, (((Boolean) ramEntry.getValue()) ? "1" : "0"));
        }

        for (Map.Entry<Multiplexer, Integer> muxentry : is.getMultiPlexerChanel().entrySet()) {
            final Multiplexer mux = (Multiplexer) muxentry.getKey();
            RowData.set(architectureObjectWithBusConnectionColumnIndex.get(mux), muxentry.getValue().toString());
        }

        for (Map.Entry<Alu, Alu.ALUFunction> aluentry : is.getAluFunctions().entrySet()) {
            RowData.set(architectureObjectWithBusConnectionColumnIndex.get(aluentry.getKey()), aluentry.getValue().toString());
        }
    }
}
