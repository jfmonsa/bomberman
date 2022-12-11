package com.carlosflorencio.bomberman.exceptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * If the file of initial settings doesn't exist, this creates it, and read it again
 */
public class FileInitialConfigsCustomExceptions extends Exception {
    public FileInitialConfigsCustomExceptions() {
        super("Bendita sea la recursividad :D");
        try {
            File f = new File("./res/initial_configs.txt");
            f.createNewFile();
            FileWriter fw = new FileWriter("./res/initial_configs.txt");
            fw.write("200#0#3");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
