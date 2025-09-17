package com.jm.simpletron;

import java.util.Scanner;

public class Simpletron {
    public static void main(String[] args) {
        boolean sequential = false;
        int opcode = 0, operand = 0;

        Memory mem;

        Processor proc = new Processor();
        String instructionRegister = "0000";

        if (args.length > 0) {
            try {
                Loader.read(args[0]);
                System.out.println("Successfully loaded program\n");

                mem = Memory.getInstance();

                if (args.length > 1) {
                    if (args[1].trim().equals("-s")) {
                        sequential = true;
                    }
                }

                if (sequential) {
                    while (true) {
                        Scanner scan = new Scanner(System.in);

                        System.out.println("Press enter to execute");
                        scan.nextLine();

                        instructionRegister = mem.readItem(ProgramCounter.getCounter());

                        opcode = Integer.parseInt(instructionRegister.substring(0, 2));
                        operand = Integer.parseInt(instructionRegister.substring(3, 4));

                        proc.execute(opcode, operand);

                        if (proc.isHalted()) {
                            break;
                        }
                        System.out.println("REGISTERS: ");
                        System.out.println("accumulator:\t\t" + proc.getAccumulator());
                        System.out.println("programCounter:\t\t" + String.format("   %02d", ProgramCounter.getCounter()));
                        System.out.println("instructionRegister:\t" + String.format("%+05d", Integer.parseInt(instructionRegister)));
                        System.out.println("operationCode:\t\t" + String.format("   %02d", opcode));
                        System.out.println("operand:\t\t" + String.format("   %02d\n", operand));

                        mem.dump();
                    }
                } else {
                    while (true) {
                        instructionRegister = mem.readItem(ProgramCounter.getCounter());

                        opcode = Integer.parseInt(instructionRegister.substring(0, 2));
                        operand = Integer.parseInt(instructionRegister.substring(3, 4));

                        proc.execute(opcode, operand);

                        if (proc.isHalted()) {
                            break;
                        }
                    }
                    
                    System.out.println("REGISTERS: ");
                    System.out.println("accumulator:\t\t" + proc.getAccumulator());
                    System.out.println("programCounter:\t\t" + String.format("   %02d", ProgramCounter.getCounter()));
                    System.out.println("instructionRegister:\t" + String.format("%+05d", Integer.parseInt(instructionRegister)));
                    System.out.println("operationCode:\t\t" + String.format("   %02d", opcode));
                    System.out.println("operand:\t\t" + String.format("   %02d\n", operand));

                    mem.dump();
                }
            } catch (Exception e) {
                System.err.println("Failed to load program!");

                e.printStackTrace();
            }
        }
    }
}