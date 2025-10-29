package com.jm.simpletron;

import java.util.Scanner;



public class Processor {
    int accumulator = 0;
    boolean halted = false;

    public void execute(int opcode, int operand) throws Exception {
        Memory mem = Memory.getInstance();
        Scanner scan = new Scanner(System.in);

        int value;

        switch (opcode) {
            case Instructions.READ:
                System.out.print("? ");
                String instruction = scan.nextLine();

                mem.addItem(String.format("%04d", Integer.parseInt(instruction)), operand);

                ProgramCounter.increment();
                break;
            case Instructions.WRITE:
                System.out.println(mem.readItem(operand).strip());

                ProgramCounter.increment();
                break;
            case Instructions.LOADM:
                accumulator = Integer.parseInt(mem.readItem(operand));

                ProgramCounter.increment();
                break;
            case Instructions.STORE:
                mem.addItem(String.format("%04d", accumulator), operand);

                ProgramCounter.increment();
                break;
            case Instructions.LOADI:
                accumulator = operand;

                ProgramCounter.increment();
                break;
            case Instructions.ADDM:
                value = Integer.parseInt(mem.readItem(operand));

                accumulator += value;
                ProgramCounter.increment();
                break;
            case Instructions.SUBM:
                value = Integer.parseInt(mem.readItem(operand));

                accumulator -= value;
                ProgramCounter.increment();
                break;
            case Instructions.DIVM:
                value = Integer.parseInt(mem.readItem(operand));

                accumulator /= value;
                ProgramCounter.increment();
                break;
            case Instructions.MODM:
                value = Integer.parseInt(mem.readItem(operand));

                accumulator %= value;
                ProgramCounter.increment();
                break;
            case Instructions.MULM:
                value = Integer.parseInt(mem.readItem(operand));

                accumulator *= value;
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
                System.out.println("Program halted\n");
                halted = true;

                break;
            default:
                // Unknown opcode
                Memory.getInstance().dump();
                throw new Exception("Invalid opcode: " + opcode);
        }
    }

    public boolean isHalted() {
        return halted;
    }

    public String getAccumulator() {
        return String.format("%+05d", accumulator);
    }
}