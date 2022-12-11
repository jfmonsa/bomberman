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
	 * 200#0#3
	 * where 200 are the times; 0 are the points; and 3 are the lives*/

public class ReadInitialConfigs {
    public static int time;
    public static int points;
    public static int lives;

    /*
     * public static final int TIME = ReadInitialConfigs.getTime();
     * public static final int POINTS = ReadInitialConfigs.getPoints();
     * public static final int LIVES =
     */
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
                ReadInitialConfigs.points = Integer.parseInt(tokens.nextToken());
                ReadInitialConfigs.lives = Integer.parseInt(tokens.nextToken());
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
