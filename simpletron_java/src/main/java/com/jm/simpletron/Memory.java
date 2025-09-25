package com.jm.simpletron;

public class Memory {
    final short MEMORY_SIZE = 100;

    private String[] memStrings = new String[MEMORY_SIZE];
    private static volatile Memory instance = null;

    private Memory() {
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memStrings[i] = "0000";
        }
    }

    public static Memory getInstance() {
        // Creates a thread safe singleton instance with double checking
        if (instance == null) {
            synchronized(Memory.class) {
                if (instance == null) {
                    instance = new Memory();
                }
            }
        }

        return instance;
    }

    public void addItem(String data, int index) {
        memStrings[index] = String.format("%04d", Integer.parseInt(data));
    }

    public void addItem(int data, int index) {
        memStrings[index] = String.format("%04d", data);
    }

    public String readItem(int index) {
        return memStrings[index];
    }

    public void dump() {
        try {
            System.out.println(this.toString());
        } catch (RuntimeException e) {
            System.err.println("Failed to dump memory: " + e.getMessage());
        }
    }

    public int getMemorySize() {
        return MEMORY_SIZE;
    }

    @Override
    public String toString() throws RuntimeException {
        String out;

        out = "Memory:\n";

        for (int i = 0; i < 10; i++) {
            if (i == 0) {
                out += "\t";
            }

            out += String.format("%5d\t", i);
        }

        out += "\n";

        for (int i = 0; i < MEMORY_SIZE; i++) {
            if ((i % 10) == 0) {
                out += String.format("%02d\t", i);
            }
            
            if (Integer.parseInt(readItem(i).strip()) >= 0) {
                out += "+";
            } else if (Integer.parseInt(readItem(i).strip()) < 0 ) {
                out += "-";
            } else {
                throw new RuntimeException("Invalid memory entry at index " + i);
            }

            out += (readItem(i) + "\t");

            if ((i % 10) == 9) {
                out += "\n";
            }            
        }

        return out;
    }
}
