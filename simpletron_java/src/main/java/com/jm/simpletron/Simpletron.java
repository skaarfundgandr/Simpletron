package com.jm.simpletron;

import java.util.Scanner;

public class Simpletron {
    private static Memory mem;
    private static Processor proc;
    
    public static void main(String[] args) {
        clearScreen();
        
        if (args.length > 0) {
            try {
                // Determine which loader to use based on file extension and flags
                String filePath = args[0];
                boolean forceCompile = hasFlag(args, "-c");
                
                if (forceCompile || filePath.endsWith(".cml")) {
                    compileProgram(filePath);
                } else if (filePath.endsWith(".sml")) {
                    loadProgram(filePath);
                } else {
                    // Compiles by default
                    compileProgram(filePath);
                }
                
                boolean sequential = hasFlag(args, "-s");
                
                if (sequential) {
                    runSequentialMode();
                } else {
                    runNormalMode();
                }

                boolean dump = hasFlag(args, "-d");

                if (dump && !sequential) {
                    printRegisters();
                    mem.dump();
                }
            } catch (Exception e) {
                System.err.println("Failed to load program!");
                e.printStackTrace();
            }
        } else {
            System.out.println("No arguments provided.");
            System.out.println("Arguments: <filename>.<extension> <flags>");
            System.out.println("Flags:");
            System.out.println("  -s    runs the program in sequential mode.");
            System.out.println("  -c    force compiles the program.");
            System.out.println("  -d    dumps memory and registers after execution.");
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
    
    private static boolean hasFlag(String[] args, String flag) {
        for (int i = 1; i < args.length; i++) {
            if (args[i].strip().equals(flag)) {
                return true;
            }
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