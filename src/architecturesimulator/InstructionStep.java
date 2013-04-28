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

import architecturesimulator.Alu;
import architecturesimulator.Alu.ALUFunction;
import architecturesimulator.Multiplexer;
import architecturesimulator.RAM;
import architecturesimulator.Register;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class InstructionStep {        
        Map<Multiplexer, Integer> MultiPlexerChanel = new HashMap<Multiplexer, Integer>();
        ArrayList<Register> OutputEnable = new ArrayList<Register>();
        ArrayList<Register> ChipSelect = new ArrayList<Register>();
        Map<Alu, Alu.ALUFunction> AluFunctions= new HashMap<Alu, Alu.ALUFunction>();
        Map<RAM, Boolean>   RAMChipSelect = new HashMap<RAM, Boolean>();
        Map<RAM, Boolean>   RAMRnW = new HashMap<RAM, Boolean>();

    public void addRamChipselet (RAM r,boolean cs ){
        RAMChipSelect.put(r, cs);
    }

    public void addRamRnW (RAM r,boolean rnw ){
        RAMRnW.put(r, rnw);
    }

    public Map<RAM, Boolean> getRAMChipSelect() {
        return RAMChipSelect;
    }

    public Map<RAM, Boolean> getRAMRnW() {
        return RAMRnW;
    }

    public void addMultiplexerChanelSelect (Multiplexer mu, int ChanelSelect) {
        MultiPlexerChanel.put(mu, ChanelSelect);
    }

    public void addRegisterOutputEnanle (Register re){
        OutputEnable.add(re);
    }
    public void addRegisterChipSelect (Register re){
        ChipSelect.add(re);
    }

    public void addAluFunction (Alu alu, Alu.ALUFunction func){
        AluFunctions.put(alu, func);
    }

    public Map<Alu, ALUFunction> getAluFunctions() {
        return AluFunctions;
    }

    public void setAluFunctions(Map<Alu, ALUFunction> AluFunctions) {
        this.AluFunctions = AluFunctions;
    }

    public ArrayList<Register> getChipSelect() {
        return ChipSelect;
    }

    private void setChipSelect(ArrayList<Register> ChipSelect) {
        this.ChipSelect = ChipSelect;
    }

    public Map<Multiplexer, Integer> getMultiPlexerChanel() {
        return MultiPlexerChanel;
    }

    private void setMultiPlexerChanel(Map<Multiplexer, Integer> MultiPlexerChanel) {
        this.MultiPlexerChanel = MultiPlexerChanel;
    }

    public ArrayList<Register> getOutputEnable() {
        return OutputEnable;
    }

    public void setOutputEnable(ArrayList<Register> OutputEnable) {
        this.OutputEnable = OutputEnable;
    }


}
