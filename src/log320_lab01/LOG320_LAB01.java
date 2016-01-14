/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log320_lab01;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author Zeldorine
 */
public class LOG320_LAB01 {

    private static int nbTotalAnagram = 0;
    private static Timer timer = new Timer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /* if (args.length != 2) {
            System.out.println("Erreur nombre d'argument");
            System.exit(0);
        }*/
        timer.start();

        List<String> words = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\words.txt"));
        List<String> dics = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\dict.txt"));

        try {
            for (String ligne : words) {
                int nbAnagramme = 0;
                //lecture du dictionnaire
                for (String ligne2 : dics) {
                    if (EstUnAnagrammeV2(ligne.replace(" ", "").toCharArray(), ligne2.replace(" ", "").toCharArray())) {
                        nbTotalAnagram++;
                        nbAnagramme++;
                    }
                }
                System.out.println("Il y a " + nbAnagramme + " anagramme pour le mot " + ligne);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        System.out.println(
                "Il y a un total de " + nbTotalAnagram);
        timer.stop();
        System.out.format("Temps execution : %.7f secondes", timer.getTime());
    }

    private static boolean EstUnAnagrammeV1(char[] chaine1, char[] chaine2) {
        for (char c1 : chaine1) {
            boolean trouver = false;
            for (int i = 0; i < chaine2.length; i++) {
                if (chaine2[i] != '\0' && c1 == chaine2[i]) {
                    trouver = true;
                    chaine2[i] = '\0';
                    break;
                }
            }

            if (trouver == false) {
                return false;
            }
        }

        if (!chaineIsEmpty(chaine2)) {
            return false;
        }

        return true;
    }

    public static boolean EstUnAnagrammeV2(char[] ch1, char[] ch2) {
        if (ch1.length != ch2.length) {
            return false;
        }
        Arrays.parallelSort(ch1);
        Arrays.parallelSort(ch2);
        return Arrays.equals(ch1, ch2);
    }

    private static boolean chaineIsEmpty(char[] chaine) {
        for (char c : chaine) {
            if (c != '\0') {
                return false;
            }
        }
        return true;
    }

    public static class Timer {

        private static long startTime, endTime;

        public static void start() {
            startTime = System.currentTimeMillis();
        }

        public static void stop() {
            endTime = System.currentTimeMillis();
        }

        public static float getTime() {
            return (endTime - startTime) / 1000.000000000f;
        }
    }
}
