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

import java.util.ArrayList;

/**

 *

 * @author Aleksejs Okolovskis (oko@aloko.de)

 */
public class Instruction {

    String name;
    String OpCode;
    ArrayList<InstructionStep> instructionSteps = new ArrayList<InstructionStep>();
    ArrayList<InstructionCondition> conditions = new ArrayList<InstructionCondition>();
    
    
    public Instruction(String name, String OpCode) {
        this.name = name;
        this.OpCode = OpCode;
    }

    public String getOpCode() {
        return OpCode;
    }

    public String getName() {
        return name;
    }


    public void addInstuctionStep(InstructionStep is) {
        instructionSteps.add(is);

    }

    public InstructionStep getStep(int step) {
        return instructionSteps.get(step);
    }

    public boolean hasStep(int step) {
       return instructionSteps.size() > step ? true : false;
    }
    
    public void addInstructionCondition (InstructionCondition ic) {
        conditions.add(ic);
    }
    
    public boolean isConditionSatisfied(){
        boolean r = true;
        for (InstructionCondition instructionCondition : conditions) {
            r = r & instructionCondition.isSatisfied();
        }
        return r;
    }
    
    
}
