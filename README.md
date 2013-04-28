Architecture-Simulator
======================
The Architecture-Simulator is a software for educational purpose. The primary goal is to illustrate simple computer and processor
architectures. It visualizes simple architectures based on primitive components like buses, registers, alus, multiplexers and ram.


Features
======================
* Create your own architecture like the theoretical MU0 (MU0-2,...)
* Create your own assambler instructions for your architecture by setting register input or output on the falling or rising edge and by performing one or multiple alu opperations
* Write your own programs for your own architecture 



Getting Started 
======================

Load Simulation
----------------------
1. File->Open
2. Select the architecture you want to load e.g. ”mu0-2.architecture.xml”. 
3. Now you have to select the correct layout” mu0-2.layout.xml”. 
4. Setup. 

Usage
----------------------

### The two buttons (Micro Step / Instrucation)

#### Micro Step
Performes micro step.
#### Instruction 
Performes instruction, if an instructon is currently executed it will 
be finished. 

Reset 
----------------------
This resets the Architecture Simulator. Note: All Components will have the value witch they had after setup. 
Instuction Table
----------------------  
In this window you can see the detailed execution steps for all known instruction. Also you can see the the binary mask for each instruction. 
Execution Log 
----------------------
This window shows you all instructions that have been executed. 
Additonal functions 
----------------------
Some of the Components have additonal functionality. 
memory a click on it will chnge the displayed base. supported bases are 2,10,16 can load different values e.g. ”example-programm.ram.xml”
