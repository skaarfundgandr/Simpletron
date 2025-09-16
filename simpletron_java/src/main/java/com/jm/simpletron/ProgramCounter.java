package com.jm.simpletron;

public class ProgramCounter {
    private int counter = 0;

    private static volatile ProgramCounter instance = null;

    private ProgramCounter() {}

    public static ProgramCounter getInstance() {
        // Creates a thread safe singleton instance with double checking
        if (instance == null) {
            synchronized(ProgramCounter.class) {
                if (instance == null) {
                    instance = new ProgramCounter();
                }
            }
        }

        return instance;
    }

    public static int getCounter() {
        return ProgramCounter.getInstance().counter;
    }

    public static void setCounter(int counter) {
        ProgramCounter.getInstance().counter = counter;
    }

    public static void increment() {
        ++ProgramCounter.getInstance().counter;
    }
}
