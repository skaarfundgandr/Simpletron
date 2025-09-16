package com.jm.simpletron;

import java.util.Scanner;

class Instructions {
    public static final int 
        READ = 10, WRITE = 11,
        LOADM = 20, STORE = 21,
        LOADI = 22, ADDM = 30,
        SUBM = 31, DIVM = 32,
        MODM = 33, MULM = 34,
        ADDI = 35, SUBI = 36,
        DIVI = 37, MODI = 38,
        MULI = 39, JMP = 40,
        JN = 41, JZ = 42,
        HALT = 43;
}

public class Processor {
    int accumulator = 0;
    boolean halted = false;

    public void execute(int opcode, int operand) throws Exception {
        Memory mem = Memory.getInstance();
        Scanner scan = new Scanner(System.in);

        int value;

        switch (opcode) {
            case Instructions.READ:
                String instruction = scan.nextLine();

                mem.addItem(instruction, operand);

                ProgramCounter.increment();
                break;
            case Instructions.WRITE:
                System.out.println(mem.readItem(operand));

                ProgramCounter.increment();
                break;
            case Instructions.LOADM:
                accumulator = Integer.parseInt(mem.readItem(operand));

                ProgramCounter.increment();
                break;
            case Instructions.STORE:
                mem.addItem(String.format("%4d", accumulator), operand);

                ProgramCounter.increment();
                break;
            case Instructions.LOADI:
                accumulator = operand;

                ProgramCounter.increment();
                break;
            case Instructions.ADDM:
                value = Integer.parseInt(mem.readItem(operand));

                value += accumulator;

                mem.addItem(String.format("%4d", value), operand);

                ProgramCounter.increment();
                break;
            case Instructions.SUBM:
                value = Integer.parseInt(mem.readItem(operand));

                value -= accumulator;
                mem.addItem(String.format("%4d", value), operand);

                ProgramCounter.increment();
                break;
            case Instructions.DIVM:
                value = Integer.parseInt(mem.readItem(operand));

                value /= accumulator;

                mem.addItem(String.format("%4d", value), operand);

                ProgramCounter.increment();
                break;
            case Instructions.MODM:
                value = Integer.parseInt(mem.readItem(operand));

                value %= accumulator;

                mem.addItem(String.format("%4d", value), operand);

                ProgramCounter.increment();
                break;
            case Instructions.MULM:
                value = Integer.parseInt(mem.readItem(operand));

                value *= accumulator;

                mem.addItem(String.format("%4d", value), operand);

                ProgramCounter.increment();
                break;
            case Instructions.ADDI:
                accumulator += operand;

                ProgramCounter.increment();
                break;
            case Instructions.SUBI:
                accumulator -= operand;

                ProgramCounter.increment();
                break;
            case Instructions.DIVI:
                accumulator /= operand;

                ProgramCounter.increment();
                break;
            case Instructions.MODI:
                accumulator %= operand;

                ProgramCounter.increment();
                break;
            case Instructions.MULI:
                accumulator *= operand;

                ProgramCounter.increment();
                break;
            case Instructions.JMP:
                ProgramCounter.setCounter(operand);
                break;
            case Instructions.JN:
                if (accumulator < 0) {
                    ProgramCounter.setCounter(operand);
                } else {
                    ProgramCounter.increment();
                }
                break;
            case Instructions.JZ:
                if (accumulator == 0) {
                    ProgramCounter.setCounter(operand);
                } else {
                    ProgramCounter.increment();
                }

                break;
            case Instructions.HALT:
                System.out.println("\nProgram halted\n");
                halted = true;

                break;
            default:
                // Unknown opcode
                throw new Exception("Invalid opcode");
        }
    }

    public boolean isHalted() {
        return halted;
    }

    public String getAccumulator() {
        return String.format("%+05d", accumulator);
    }
}