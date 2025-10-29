package com.jm.simpletron;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Helper class to represent labels
class Label {
    String name;
    int address;

    Label(String name, int address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAddress() {
        return address;
    }
}

/**
 * Lexical parser for Simpletron assembly language.
 * Compiles assembly code into machine code instructions.
 */
public class LexicalParser {
    private static final String VARIABLE_KEYWORD = "VARIABLE";
    private static final String VAR_KEYWORD = "VAR";
    
    /**
     * Compiles a Simpletron assembly file into machine code.
     * Uses two-pass compilation to handle labels.
     * 
     * @param filePath Path to the assembly source file
     * @throws Exception if compilation fails
     */
    public static void compile(String filePath) throws Exception {
        Map<String, Integer> instructionSet = createInstructionSet();
        Map<String, Integer> variableMap = new HashMap<>();
        Map<String, Label> labelMap = new HashMap<>();
        Memory memory = Memory.getInstance();
        
        // Read all lines
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new Exception("Error reading file: " + filePath, e);
        }
        
        // PASS 1A: First discover all variable declarations
        for (String line : lines) {
            line = preprocessLine(line);
            
            if (line.isEmpty()) {
                continue;
            }
            
            // Remove label if present for processing
            String processLine = line;
            if (line.contains(":")) {
                processLine = line.substring(line.indexOf(":") + 1).strip();
            }
            
            if (processLine.isEmpty()) {
                continue;
            }
            
            String[] tokens = processLine.split("\\s+");
            String firstToken = tokens[0].strip();
            
            if (isVariableDeclaration(firstToken)) {
                if (tokens.length >= 2) {
                    String variableName = tokens[1].strip();
                    int variableAddress = memory.getMemorySize() - variableMap.size() - 1;
                    variableMap.put(variableName, variableAddress);
                }
            }
        }
        
        // PASS 1B: Scan for labels and count instructions
        int currentLineNumber = 0;
        for (String line : lines) {
            line = preprocessLine(line);
            
            if (line.isEmpty()) {
                continue;
            }
            
            // Check if line contains a label
            if (line.contains(":")) {
                String labelName = line.substring(0, line.indexOf(":")).strip();
                labelMap.put(labelName, new Label(labelName, currentLineNumber));
                // Remove label
                line = line.substring(line.indexOf(":") + 1).strip();
                
                // If line is empty, continue
                if (line.isEmpty()) {
                    continue;
                }
            }
            
            // Count instructions
            String[] tokens = line.split("\\s+");
            String firstToken = tokens[0].strip();
            
            if (instructionSet.containsKey(firstToken)) {
                currentLineNumber++;
            } else if (isVariableDeclaration(firstToken)) {
                // Variable declarations don't occupy instruction space (already processed)
            } else if (variableMap.containsKey(firstToken)) {
                // Variable assignments don't occupy instruction space
            }
        }
        
        // PASS 2: Compile instructions with resolved labels
        currentLineNumber = 0;
        for (String line : lines) {
            line = preprocessLine(line);
            
            if (line.isEmpty()) {
                continue;
            }
            
            // Remove label if present
            if (line.contains(":")) {
                line = line.substring(line.indexOf(":") + 1).strip();
                if (line.isEmpty()) {
                    continue;
                }
            }
            
            String[] tokens = line.split("\\s+");
            String instruction = tokens[0].strip();
            
            if (instructionSet.containsKey(instruction)) {
                currentLineNumber = processInstruction(instruction, tokens, instructionSet, 
                                                     variableMap, labelMap, memory, currentLineNumber);
            } else if (isVariableDeclaration(instruction)) {
                // Already processed in pass 1
            } else if (variableMap.containsKey(instruction)) {
                processVariableAssignment(instruction, tokens, variableMap, memory);
            }
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
        return line.replaceAll(";.*", "")
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
                                        Map<String, Label> labelMap,
                                        Memory memory, int currentLineNumber) throws Exception {
        int opcode = instructionSet.get(instruction);
        int operand = 0;
        
        // HALT instruction doesn't require an operand
        if (opcode != Instructions.HALT) {
            if (tokens.length < 2) {
                throw new Exception("Missing operand for instruction: " + instruction);
            }
            
            String operandToken = tokens[1].strip();
            
            // Check if operand is a variable
            if (variableMap.containsKey(operandToken)) {
                operand = variableMap.get(operandToken);
            }
            // Check if operand is a label
            else if (labelMap.containsKey(operandToken)) {
                Label label = labelMap.get(operandToken);
                operand = label.getAddress();
            }
            else {
                try {
                    operand = Integer.parseInt(operandToken);
                } catch (NumberFormatException e) {
                    throw new Exception("Invalid operand for instruction " + instruction + ": " + operandToken);
                }
            }
        }
        
        String machineCode = String.format("%02d%02d", opcode, operand);
        memory.addItem(machineCode, currentLineNumber);
        
        return currentLineNumber + 1;
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