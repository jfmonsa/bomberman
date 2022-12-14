package com.carlosflorencio.bomberman;

import com.carlosflorencio.bomberman.exceptions.FileInitialConfigsCustomExceptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.util.StringTokenizer;
/* // initial configs
	 * the initial configs are saved in res/initial_configs.txt, the values are
	 * storage like this example:
	 * 200#3#3
	 * where 200 is the time; 0 is the number of level; and 3 is the number of lives*/

public class ReadInitialConfigs {
    public static int time;
    public static int number_level;
    public static int lives;

    ReadInitialConfigs() {
        readInitialConf();
    }

    public void readInitialConf() {
        try {
            File archivo = new File("./res/initial_configs.txt");
            FileReader lector = new FileReader(archivo);

            BufferedReader buffer = new BufferedReader(lector);
            String linea = "";

            while ((linea = buffer.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(linea, "#");

                ReadInitialConfigs.time = Integer.parseInt(tokens.nextToken());
                ReadInitialConfigs.number_level = Integer.parseInt(tokens.nextToken());
                ReadInitialConfigs.lives = Integer.parseInt(tokens.nextToken());
            }

            if (!(new File("./res/levels/Level" + number_level + ".txt")).exists()) {
                // Si el nivel no existe, lo pongo en el nivel 1 por default
                ReadInitialConfigs.number_level = 1;
            }
            lector.close();
        } catch (FileNotFoundException e) {
            new FileInitialConfigsCustomExceptions();
            System.out.println("Archivo configuraciones iniciales no encontrado");
            e.printStackTrace();
            // Execuate again this function (resursion)
            readInitialConf();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
