package com.jm.simpletron;

public class Memory {
    final short MEMORY_SIZE = 100;

    private String[] memStrings = new String[MEMORY_SIZE];
    private static volatile Memory instance = null;

    private Memory() {
        for (int i = 0; i < MEMORY_SIZE; i++) {
            memStrings[i] = new String("+0000");
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

    public void store(String data, int index) {
        memStrings[index] = data;
    }

    public String read(int index) {
        return memStrings[index];
    }

    @Override
    public String toString() {
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

            out += (memStrings[i] + "\t");

            if ((i % 10) == 9) {
                out += "\n";
            }            
        }

        return out;
    }

    public static void main(String[] args) {
        System.out.println(Memory.getInstance());
    }
}
