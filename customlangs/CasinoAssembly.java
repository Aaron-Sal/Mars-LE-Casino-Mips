    package mars.mips.instructions.customlangs;
    import mars.simulator.*;
    import mars.mips.hardware.*;
    import mars.mips.instructions.syscalls.*;
    import mars.*;
    import mars.util.*;
    import java.util.*;
    import java.io.*;
    import mars.mips.instructions.*;
    import java.util.Random;


public class CasinoAssembly extends CustomAssembly{
   int cardOne;
   int cardTwo;
   int playerSum;
    @Override
    public String getName(){
        return "Casino Assembly";
    }

    @Override
    public String getDescription(){
        return "Try your luck";
    }

    RegisterFile reg = new RegisterFile();
    @Override
    protected void populate(){
         this.instructionList.add(new BasicInstruction("mge $t1,$t2,$t3", "Merge with overflow : set $t1 to ($t2 plus $t3)", BasicInstructionFormat.R_FORMAT, "000000 sssss ttttt fffff 00000 100000", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            int var3 = RegisterFile.getValue(var2[1]);
            int var4 = RegisterFile.getValue(var2[2]);
            int var5 = var3 + var4;
            if ((var3 < 0 || var4 < 0 || var5 >= 0) && (var3 >= 0 || var4 >= 0 || var5 < 0)) {
               RegisterFile.updateRegister(var2[0], var5);
            } else {
               throw new ProcessingException(var1, "arithmetic overflow", 12);
            }
         }
      }));
               this.instructionList.add(new BasicInstruction("mgei  $t1,$t2,-100", "Merge immediate with overflow : set $t1 to ($t2 plus signed 16-bit immediate)", BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            int var3 = RegisterFile.getValue(var2[1]);
            int var4 = var2[2] << 16 >> 16;
            int var5 = var3 + var4;
            if ((var3 < 0 || var4 < 0 || var5 >= 0) && (var3 >= 0 || var4 >= 0 || var5 < 0)) {
               RegisterFile.updateRegister(var2[0], var5);
            } else {
               throw new ProcessingException(var1, "arithmetic overflow", 12);
            }
         }
      }));
      this.instructionList.add(new BasicInstruction("rev $t1,$t2,$t3", 
      "Remove with overflow : set $t1 to ($t2 minus $t3)",
       BasicInstructionFormat.R_FORMAT, "000000 sssss ttttt fffff 00000 100010", 
       new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            int var4 = RegisterFile.getValue(var2[1]);
            int var3 = RegisterFile.getValue(var2[2]); 
            int var5 = var3 - var4;
            if ((var3 < 0 || var4 >= 0 || var5 >= 0) && (var3 >= 0 || var4 < 0 || var5 < 0)) {
               RegisterFile.updateRegister(var2[0], var5);
            } else {
               throw new ProcessingException(var1, "arithmetic overflow", 12);
            }
         }

      }));
      this.instructionList.add(new BasicInstruction("bi  $t1,-100", "Buy in with overflow : set $t1 to ($t2 plus signed 16-bit immediate)", BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            int var4 = var2[1] << 16 >> 16;
            int var5 = var4;
            if ((var4 < 0 || var5 >= 0) && (var4 >= 0 || var5 < 0)) {
               RegisterFile.updateRegister(var2[0], var5);
            } else {
               throw new ProcessingException(var1, "arithmetic overflow", 12);
            }
         }
      }));
      this.instructionList.add(new BasicInstruction("sm $t1,$t2,label", "Same same: Branch to statement at label's address if $t1 and $t2 are equal", BasicInstructionFormat.I_BRANCH_FORMAT, "000100 fffff sssss tttttttttttttttt", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            if (RegisterFile.getValue(var2[0]) == RegisterFile.getValue(var2[1])) {
               Globals.instructionSet.processBranch(var2[2]);
            }

         }
      }));
      this.instructionList.add(new BasicInstruction("syscall", "Issue a system call : Execute the system call specified by value in $v0", BasicInstructionFormat.R_FORMAT, "000000 00000 00000 00000 00000 001100", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            Globals.instructionSet.findAndSimulateSyscall(RegisterFile.getValue(2), var1);
         }
      }));
      this.instructionList.add(new BasicInstruction("lui $t1,100", "Load upper immediate : Set high-order 16 bits of $t1 to 16-bit immediate and low-order 16 bits to 0", BasicInstructionFormat.I_FORMAT, "001111 00000 fffff ssssssssssssssss", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], var2[1] << 16);
         }
      }));
       this.instructionList.add(new BasicInstruction("ori $t1,$t2,100", "Bitwise OR immediate : Set $t1 to bitwise OR of $t2 and zero-extended 16-bit immediate", BasicInstructionFormat.I_FORMAT, "001101 sssss fffff tttttttttttttttt", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], RegisterFile.getValue(var2[1]) | var2[2] & '\uffff');
         }
      }));
      this.instructionList.add(new BasicInstruction("ns $t1,$t2,label", "but different  : Branch to statement at label's address if $t1 and $t2 are not equal", BasicInstructionFormat.I_BRANCH_FORMAT, "000101 fffff sssss tttttttttttttttt", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            if (RegisterFile.getValue(var2[0]) != RegisterFile.getValue(var2[1])) {
               Globals.instructionSet.processBranch(var2[2]);
            }

         }
      }));
      this.instructionList.add(new BasicInstruction("m target", "Move unconditionally : Jump to statement at target address", BasicInstructionFormat.J_FORMAT, "000010 ffffffffffffffffffffffffff", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            Globals.instructionSet.processJump(RegisterFile.getProgramCounter() & -268435456 | var2[0] << 2);
         }
      }));
      this.instructionList.add(new BasicInstruction("dm $t1,$t2,$t3", "Multiplication without overflow  : Set HI to high-order 32 bits, LO and $t1 to low-order 32 bits of the product of $t2 and $t3 (use mfhi to access HI, mflo to access LO)", BasicInstructionFormat.R_FORMAT, "011100 sssss ttttt fffff 00000 000010", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            long var3 = (long)RegisterFile.getValue(var2[1]) * (long)RegisterFile.getValue(var2[2]);
            RegisterFile.updateRegister(var2[0], (int)(var3 << 32 >> 32));
            RegisterFile.updateRegister(33, (int)(var3 >> 32));
            RegisterFile.updateRegister(34, (int)(var3 << 32 >> 32));
         }
      }));
      this.instructionList.add(new BasicInstruction("lo $t1", "Move from LO register : Set $t1 to contents of LO (see multiply and divide operations)", BasicInstructionFormat.R_FORMAT, "000000 00000 00000 fffff 00000 010010", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], RegisterFile.getValue(34));
         }
      }));
      this.instructionList.add(new BasicInstruction("hi $t1", "Move from HI register : Set $t1 to contents of HI (see multiply and divide operations)", BasicInstructionFormat.R_FORMAT, "000000 00000 00000 fffff 00000 010000", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], RegisterFile.getValue(33));
         }
      }));
      this.instructionList.add(new BasicInstruction("addu $t1,$t2,$t3", "Addition unsigned without overflow : set $t1 to ($t2 plus $t3), no overflow", BasicInstructionFormat.R_FORMAT, "000000 sssss ttttt fffff 00000 100001", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], RegisterFile.getValue(var2[1]) + RegisterFile.getValue(var2[2]));
         }
      }));
      this.instructionList.add(new BasicInstruction("addiu $t1,$t2,-100", "Addition immediate unsigned without overflow : set $t1 to ($t2 plus signed 16-bit immediate), no overflow", BasicInstructionFormat.I_FORMAT, "001001 sssss fffff tttttttttttttttt", new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], RegisterFile.getValue(var2[1]) + (var2[2] << 16 >> 16));
         }
      }));

      //Deal two cards to the player display thenn save them  in selected registers
      this.instructionList.add(new BasicInstruction("deal $t1, $t2" , 
      "Deal two cards and save each card in the selcted registers", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            Random random = new Random();

            //Create two cards 
            cardOne = random.nextInt(11) + 1;
            cardTwo = random.nextInt(11) + 1;
            
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], cardOne);
            RegisterFile.updateRegister(var2[1], cardTwo);

            StringBuilder builder = new StringBuilder();

            builder.append("You have been dealt: " + cardOne + " " + cardTwo + " cards\n");
            //print cards 
            SystemIO.printString(builder.toString());


         }
      }));
      this.instructionList.add(new BasicInstruction("spin $t1" , 
      "Test your luck spin the roulette wheel result saved in register", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            Random random = new Random();

            
            int rouletteSpin = random.nextInt(37);
            
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], rouletteSpin);
            

            StringBuilder builder = new StringBuilder();

            builder.append("The wheel has Spoken! ");
            builder.append(rouletteSpin);

            if(rouletteSpin % 2 == 0){
               builder.append(" RED\n");

            }
            else{
               builder.append(" BLACK\n");

            }
            //print cards 
            SystemIO.printString(builder.toString());



         }
      }));
      this.instructionList.add(new BasicInstruction("dd $t1" , 
      "Feeling down on your luck? Pick any register you would like to multipy by two", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
         
            
            int[] var2 = var1.getOperands();
            int var3 = RegisterFile.getValue(var2[0]);
            int var4 = var3 * 2;

            RegisterFile.updateRegister(var2[0], var4);
         
         }
      }));
      this.instructionList.add(new BasicInstruction("blc $t1" , 
      "Don't know how much you won? Pick a register to check the value ", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
         
            
            int[] var2 = var1.getOperands();
            int var3 = RegisterFile.getValue(var2[0]);
            SystemIO.printString("You currently have: " + var3 + " dollars\n");
         
         }
      }));

      this.instructionList.add(new BasicInstruction("ai $t1,$t2,$t3" , 
      "All in! add up all your registers and go all in ", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            
            int[] var2 = var1.getOperands();
            int var3 = RegisterFile.getValue(var2[0]);
            int var4 = RegisterFile.getValue(var2[1]);
            int var5 = RegisterFile.getValue(var2[2]);
            int var6 = var3 + var4 + var5;


            RegisterFile.updateRegister(var2[0], var6);

            SystemIO.printString("All in for " + RegisterFile.getValue(var2[0]) + " dollars\n");
         
         }
      }));
      this.instructionList.add(new BasicInstruction("hit $t0" , 
      "All in! add up all your registers and go all in ", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
         
            Random random = new Random();

            int nextCard = random.nextInt(11) + 1; 
            
         

            int[] var2 = var1.getOperands();

            playerSum = cardOne + cardTwo + nextCard;
            RegisterFile.updateRegister(var2[0], playerSum);

            SystemIO.printString("Player has " + (playerSum) + " count\n" );

            if(cardOne + cardTwo + nextCard > 21){
               SystemIO.printString("BUST!\n");
            }
            if(cardOne + cardTwo + nextCard == 21){
               SystemIO.printString("BLACKJACK!\n");
            }
            
         
         }
      }));
      this.instructionList.add(new BasicInstruction("ch $t0" , 
      "Down on your luck? Call home to see if you can change the night around(adds a random amount to the regiter) ", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
         
            Random random = new Random();

            int save = random.nextInt(1001); 
            
         

            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], save);

            SystemIO.printString("You have a extra " + (save) + " dollars\n" );
            
         
         }
      }));
      this.instructionList.add(new BasicInstruction("cl $t0" , 
      "Pick any register and clear it back to zero ", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            
            int[] var2 = var1.getOperands();
            RegisterFile.updateRegister(var2[0], 0);
            
         
         }
      }));
      this.instructionList.add(new BasicInstruction("ur $t0" , 
      "Pick any register and times it by -1", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            
            int[] var2 = var1.getOperands();
            int oppsite  =  -1 * RegisterFile.getValue(var2[0]);
            RegisterFile.updateRegister(var2[0], oppsite);
            
         
         }
      }));
      this.instructionList.add(new BasicInstruction("sspin" , 
      "Test your luck spin the roulette wheel", 
      BasicInstructionFormat.I_FORMAT, "001000 sssss fffff tttttttttttttttt", 
      new SimulationCode() {
         public void simulate(ProgramStatement var1) throws ProcessingException {
            Random random = new Random();

            
            int rouletteSpin = random.nextInt(37);
            

            StringBuilder builder = new StringBuilder();

            builder.append("The wheel has Spoken! ");
            builder.append(rouletteSpin);

            if(rouletteSpin % 2 == 0){
               builder.append(" RED\n");

            }
            else{
               builder.append(" BLACK\n");

            }
            //print cards 
            SystemIO.printString(builder.toString());



         }
      }));
      
    }
}