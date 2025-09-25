package com.jm.simpletron;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;

public class LexicalParser {
    public static void compile(String filePath) throws Exception {
        HashMap<String, Integer> instructions = new HashMap<>();
        HashMap<String, Integer> variableMap = new HashMap<>();

        instructions.put("READ", Instructions.READ);
        instructions.put("WRITE", Instructions.WRITE);
        instructions.put("LOADM", Instructions.LOADM);
        instructions.put("STORE", Instructions.STORE);
        instructions.put("LOADI", Instructions.LOADI);
        instructions.put("ADDM", Instructions.ADDM);
        instructions.put("SUBM", Instructions.SUBM);
        instructions.put("DIVM", Instructions.DIVM);
        instructions.put("MODM", Instructions.MODM);
        instructions.put("MULM", Instructions.MULM);
        instructions.put("ADDI", Instructions.ADDI);
        instructions.put("SUBI", Instructions.SUBI);
        instructions.put("DIVI", Instructions.DIVI);
        instructions.put("MODI", Instructions.MODI);
        instructions.put("MULI", Instructions.MULI);
        instructions.put("JMP", Instructions.JMP);
        instructions.put("JN", Instructions.JN);
        instructions.put("JZ", Instructions.JZ);
        instructions.put("HALT", Instructions.HALT);
        
        try (BufferedReader bf = new BufferedReader(new FileReader(filePath))) {
            String current;
            int currLine = 0;

            while ((current = bf.readLine()) != null) {
                int operand = 0;

                current = current.replaceAll(";", " ").strip().toUpperCase();

                if (current.isEmpty()) continue;
                String[] splittedStr = current.strip().split(" ");

                Memory m = Memory.getInstance();

                String instruction = splittedStr[0].strip().toUpperCase();
                if (instructions.containsKey(instruction)) {
                    int opcode = instructions.get(instruction);

                    if (opcode != Instructions.HALT && splittedStr.length < 2) {
                        throw new Exception("Missing operand for instruction: " + instruction);
                    }

                    if (opcode != Instructions.HALT) {
                        if (variableMap.containsKey(splittedStr[1].strip())) {
                        operand = variableMap.get(splittedStr[1].strip());
                        } else {
                            operand = Integer.parseInt(splittedStr[1].strip());
                        }
                    }
                    String machineCode = String.format("%02d%02d", opcode, operand);
                    m.addItem(machineCode, currLine);
                    currLine++;
                } else if (instruction.equals("VARIABLE") || instruction.equals("VAR")) {
                    String varName = splittedStr[1].strip();

                    if (varName.isEmpty()) {
                        throw new Exception("Missing variable name for VAR instruction");
                    }
                    variableMap.put(varName, m.getMemorySize() - variableMap.size() - 1);
                } else if (variableMap.containsKey(instruction)) {
                    operand = Integer.parseInt(splittedStr[1].strip());

                    m.addItem(operand, variableMap.get(instruction));
                } else {
                    throw new Exception("Unknown instruction: " + instruction);
                }
            }
            
        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }
    }
}