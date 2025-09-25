package com.jm.simpletron;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lexical parser for Simpletron assembly language.
 * Compiles assembly code into machine code instructions.
 */
public class LexicalParser {
    private static final String VARIABLE_KEYWORD = "VARIABLE";
    private static final String VAR_KEYWORD = "VAR";
    
    /**
     * Compiles a Simpletron assembly file into machine code.
     * 
     * @param filePath Path to the assembly source file
     * @throws Exception if compilation fails
     */
    public static void compile(String filePath) throws Exception {
        Map<String, Integer> instructionSet = createInstructionSet();
        Map<String, Integer> variableMap = new HashMap<>();
        Memory memory = Memory.getInstance();
        int currentLineNumber = 0;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                line = preprocessLine(line);
                
                if (line.isEmpty()) {
                    continue;
                }
                
                String[] tokens = line.split(" ");
                String instruction = tokens[0];
                
                if (instructionSet.containsKey(instruction)) {
                    currentLineNumber = processInstruction(instruction, tokens, instructionSet, 
                                                         variableMap, memory, currentLineNumber);
                } else if (isVariableDeclaration(instruction)) {
                    processVariableDeclaration(tokens, variableMap, memory);
                } else if (variableMap.containsKey(instruction)) {
                    processVariableAssignment(instruction, tokens, variableMap, memory);
                } else {
                    throw new Exception("Unknown instruction: " + instruction);
                }
            }
        } catch (IOException e) {
            throw new Exception("Error reading file: " + filePath, e);
        }
    }
    
    /**
     * Creates the instruction set mapping from mnemonics to opcodes.
     */
    private static Map<String, Integer> createInstructionSet() {
        Map<String, Integer> instructions = new HashMap<>();
        
        // I/O Operations
        instructions.put("READ", Instructions.READ);
        instructions.put("WRITE", Instructions.WRITE);
        
        // Memory Operations
        instructions.put("LOADM", Instructions.LOADM);
        instructions.put("STORE", Instructions.STORE);
        instructions.put("LOADI", Instructions.LOADI);
        
        // Memory Arithmetic
        instructions.put("ADDM", Instructions.ADDM);
        instructions.put("SUBM", Instructions.SUBM);
        instructions.put("DIVM", Instructions.DIVM);
        instructions.put("MODM", Instructions.MODM);
        instructions.put("MULM", Instructions.MULM);
        
        // Immediate Arithmetic
        instructions.put("ADDI", Instructions.ADDI);
        instructions.put("SUBI", Instructions.SUBI);
        instructions.put("DIVI", Instructions.DIVI);
        instructions.put("MODI", Instructions.MODI);
        instructions.put("MULI", Instructions.MULI);
        
        // Control Flow
        instructions.put("JMP", Instructions.JMP);
        instructions.put("JN", Instructions.JN);
        instructions.put("JZ", Instructions.JZ);
        instructions.put("HALT", Instructions.HALT);
        
        return instructions;
    }
    
    /**
     * Preprocesses a line by removing comments and normalizing whitespace.
     */
    private static String preprocessLine(String line) {
        return line.replaceAll(";", " ")
                  .strip()
                  .toUpperCase();
    }
    
    /**
     * Checks if the instruction is a variable declaration.
     */
    private static boolean isVariableDeclaration(String instruction) {
        return VARIABLE_KEYWORD.equals(instruction) || VAR_KEYWORD.equals(instruction);
    }
    
    /**
     * Processes an instruction line and returns the updated line number.
     */
    private static int processInstruction(String instruction, String[] tokens, 
                                        Map<String, Integer> instructionSet,
                                        Map<String, Integer> variableMap, 
                                        Memory memory, int currentLineNumber) throws Exception {
        int opcode = instructionSet.get(instruction);
        int operand = 0;
        
        // HALT instruction doesn't require an operand
        if (opcode != Instructions.HALT) {
            if (tokens.length < 2) {
                throw new Exception("Missing operand for instruction: " + instruction);
            }
            
            String operandToken = tokens[1].strip();
            
            if (variableMap.containsKey(operandToken)) {
                operand = variableMap.get(operandToken);
            } else {
                operand = Integer.parseInt(operandToken);
            }
        }
        
        String machineCode = String.format("%02d%02d", opcode, operand);
        memory.addItem(machineCode, currentLineNumber);
        
        return currentLineNumber + 1;
    }
    
    /**
     * Processes a variable declaration line.
     */
    private static void processVariableDeclaration(String[] tokens, 
                                                  Map<String, Integer> variableMap, 
                                                  Memory memory) throws Exception {
        if (tokens.length < 2) {
            throw new Exception("Missing variable name for VAR instruction");
        }
        
        String variableName = tokens[1].strip();
        
        if (variableName.isEmpty()) {
            throw new Exception("Missing variable name for VAR instruction");
        }
        
        // Variables are stored at the end of memory
        int variableAddress = memory.getMemorySize() - variableMap.size() - 1;
        variableMap.put(variableName, variableAddress);
    }
    
    /**
     * Processes a variable assignment line.
     */
    private static void processVariableAssignment(String instruction, String[] tokens,
                                                 Map<String, Integer> variableMap, 
                                                 Memory memory) throws Exception {
        if (tokens.length < 2) {
            throw new Exception("Missing value for variable assignment: " + instruction);
        }
        
        int value = Integer.parseInt(tokens[1].strip());
        int address = variableMap.get(instruction);
        memory.addItem(value, address);
    }
}