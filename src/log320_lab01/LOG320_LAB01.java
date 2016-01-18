/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log320_lab01;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zeldorine
 */
public class LOG320_LAB01 {

    private static int nbTotalAnagram;
    private static int nbAnagramme;
    private static Timer timer = new Timer();
    private static List<String> words;
    private static List<String> dics;
    private static HashMap map = new HashMap();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /* if (args.length != 2) {
            System.out.println("Erreur nombre d'argument");
            System.exit(0);
        }*/
        preprog();

        Runtime runtime = Runtime.getRuntime();
        runtime.freeMemory();

        nbTotalAnagram = 0;
        //timer.start();
        prog();
        //timer.stop();

        StringBuilder sb = new StringBuilder("Il y a un total de ");
        sb.append(nbTotalAnagram);
        System.out.println(sb.toString());

        System.out.format("Temps execution : %.20f secondes", timer.getTime());
    }

    /**
     * Lancer l'execution une fois avant le timer pour charger les classes dans
     * la JVM
     *
     * @throws InterruptedException
     */
    private static void preprog() throws InterruptedException {
        //List<String> words = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\words.txt"));
        //List<String> dics = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\dict.txt"));

        Thread readDWord = new readDWord();
        Thread readDic = new readDic();

        readDWord.start();
        readDic.start();
        readDWord.join();
        readDic.join();
        try {
            for (String ligne : words) {// USE thread
                for (String ligne2 : dics) {
                    if (EstUnAnagrammeV3(ligne.toCharArray(), ligne2.toCharArray())) {
                        nbTotalAnagram++;
                        nbAnagramme++;
                    }
                }
                nbAnagramme = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void prog() throws InterruptedException {
        //List<String> words = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\words.txt"));
        //List<String> dics = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\dict.txt"));
        Thread readDWord = new readDWord();
        Thread readDic = new readDic();

        readDWord.start();
        readDic.start();
        readDWord.join();
        readDic.join();

        try {
            timer.start();
            for (String ligne : words) {// USE thread
                for (String ligne2 : dics) {
                    //  timer.start();
                    /* if (EstUnAnagrammeV2(supEspace(ligne.toCharArray()), supEspace(ligne2.toCharArray()))) {
                        nbTotalAnagram++;
                        nbAnagramme++;
                    }*/
                    if (EstUnAnagrammeV3(ligne.toCharArray(), ligne2.toCharArray())) {
                        nbTotalAnagram++;
                        nbAnagramme++;
                    }
                    //  timer.stop();
                }

                map.put(ligne, nbAnagramme);
                
               /* StringBuilder sb = new StringBuilder("Il y a "); // Mettre ca dans un hashMap et faire une methode display a la fin ?
                sb.append(nbAnagramme);
                sb.append(" anagrammes pour le mot ");
                sb.append(ligne);
                System.out.println(sb.toString());
                nbAnagramme = 0;*/
            }
            timer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class readDic extends Thread {

        @Override
        public void run() {
            try {
                dics = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\dict.txt"));
            } catch (IOException ex) {
                Logger.getLogger(LOG320_LAB01.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class readDWord extends Thread {

        @Override
        public void run() {
            try {
                words = Files.readAllLines(Paths.get("C:\\Users\\Zeldorine\\Downloads\\anagramme\\words.txt"));
            } catch (IOException ex) {
                Logger.getLogger(LOG320_LAB01.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    private static boolean EstUnAnagrammeV2(char[] ch1, char[] ch2) {
        if (ch1 == null || ch2 == null) {
            return false;
        }

        int length = ch1.length;
        if (length != ch2.length) {
            return false;
        }

        Arrays.parallelSort(ch1);
        Arrays.parallelSort(ch2);

        for (int i = 0; i < length; i++) {
            if (ch1[i] != ch2[i]) {
                return false;
            }
        }

        return true;
    }

    private static boolean EstUnAnagrammeV3(char[] ch1, char[] ch2) {
        /* if (ch1 == null || ch2 == null) { // essayer avec une ligne dans le fichier
            return false;
        }*/

 /* int length = ch1.length; // plus rapide mais marche pas
        if (length != ch2.length) {
            return false;
        }*/
        
 
      /*  if((ch1.length - nbEspace(ch1, ch1.length)) != (ch1.length - nbEspace(ch1, ch1.length))){
            return false;
        }*/
 
        return hash(ch1) == hash(ch2);
    }

    private static int hash(char[] tab) {
        int hash = 0;

        for (char c : tab) {
            if (!Character.isWhitespace(c)) {
                hash += c;
            }
        }

        return hash;
    }

    private static int nbEspace(char[] tab, int nbElement) {
        int nb = 0;

        for (int i = 0; i < nbElement; i++) {
            if (!Character.isWhitespace(tab[i])) {
                nb++;
            }
        }

        return nb;
    }

    //TODO a verifier le white space
    public static char[] supEspace(char[] word) {
        char[] tmp = new char[word.length];
        int index = 0;
        for (int i = 0; i < word.length; i++) {
            if (!Character.isWhitespace(word[i])) {
                tmp[index] = word[i];
                index++;
            }
        }

        if (index == word.length) {
            return word;
        }

        char[] tmp2 = new char[index];
        System.arraycopy(tmp, 0, tmp2, 0, index); // System.arraycopy is better than manuel copy fort large array

        /* for (int i = 0; i < index; i++) {
            tmp2[i] = tmp[i];
        }*/
        return tmp2;
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
            startTime = System.nanoTime();
        }

        public static void stop() {
            endTime = System.nanoTime();
        }

        public static float getTime() {
            return (endTime - startTime) / 1000000000.000000000f;
            //return TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS) / 1000.000000f;
        }
    }
}
