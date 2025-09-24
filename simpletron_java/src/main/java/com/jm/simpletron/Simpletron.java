package com.jm.simpletron;

import java.util.Scanner;

public class Simpletron {
    private static Memory mem;
    private static Processor proc;
    
    public static void main(String[] args) {
        clearScreen();
        
        if (args.length > 0) {
            try {
                if (args[0].endsWith(".sml")) {
                    loadProgram(args[0]);
                }
                if (args[0].endsWith(".cml")) {
                    compileProgram(args[0]);
                }
                boolean sequential = parseArguments(args);
                
                if (sequential) {
                    runSequentialMode();
                } else {
                    runNormalMode();
                }
            } catch (Exception e) {
                System.err.println("Failed to load program!");
                e.printStackTrace();
            }
        }
    }
    
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private static void loadProgram(String filePath) throws Exception {
        Loader.read(filePath);
        System.out.println("Successfully loaded program\n");
        mem = Memory.getInstance();
        proc = new Processor();
    }

    private static void compileProgram(String filePath) throws Exception {
        LexicalParser.compile(filePath);
        System.out.println("Successfully compiled program\n");
        mem = Memory.getInstance();
        proc = new Processor();
    }
    
    private static boolean parseArguments(String[] args) {
        if (args.length > 1 && args[1].strip().equals("-s")) {
            return true;
        }
        return false;
    }
    
    private static void runSequentialMode() throws Exception {
        try (Scanner scan = new Scanner(System.in)) {
            while (true) {
                printRegisters();
                mem.dump();

                System.out.print("\nPress enter to execute");
                scan.nextLine();
                clearScreen();
                
                if (executeNextInstruction()) {
                    break;
                }
            }
        }
    }
    
    private static void runNormalMode() throws Exception {
        while (true) {
            if (executeNextInstruction()) {
                break;
            }
        }
        
        printRegisters();
        mem.dump();
    }
    
    private static boolean executeNextInstruction() throws Exception {
        String instructionRegister = mem.readItem(ProgramCounter.getCounter());
        int opcode = Integer.parseInt(instructionRegister.substring(0, 2));
        int operand = Integer.parseInt(instructionRegister.substring(2, 4));
        
        proc.execute(opcode, operand);
        
        return proc.isHalted();
    }
    
    private static void printRegisters() {
        String instructionRegister = mem.readItem(ProgramCounter.getCounter());
        int opcode = Integer.parseInt(instructionRegister.substring(0, 2));
        int operand = Integer.parseInt(instructionRegister.substring(2, 4));
        
        System.out.println("REGISTERS: ");
        System.out.println("accumulator:\t\t" + proc.getAccumulator());
        System.out.println("programCounter:\t\t" + String.format("   %02d", ProgramCounter.getCounter()));
        System.out.println("instructionRegister:\t" + String.format("%+05d", Integer.parseInt(instructionRegister)));
        System.out.println("operationCode:\t\t" + String.format("   %02d", opcode));
        System.out.println("operand:\t\t" + String.format("   %02d", operand));
    }
}