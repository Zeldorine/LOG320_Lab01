/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log320_lab01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Zeldorine
 */
public class LOG320_LAB01_Tony extends RecursiveAction {

    private static String[] words;
    private static String[] dics;
    private static int debutWord;
    private static int finWord;
    private static int debutDic;
    private static int finDic;
    private static int[] resultat;
    private static HashMap<String, Integer> map;

    public LOG320_LAB01_Tony(String[] words, String[] dics, int debutWord, int finWord, int debutDic, int finDic) {
        LOG320_LAB01_Tony.words = words;
        LOG320_LAB01_Tony.dics = dics;
        LOG320_LAB01_Tony.debutWord = debutWord;
        LOG320_LAB01_Tony.finWord = finWord;
        LOG320_LAB01_Tony.debutDic = debutDic;
        LOG320_LAB01_Tony.finDic = finDic;
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Erreur nombre d'argument");
            System.exit(0);
        }

        words = removeSpace(Files.readAllLines(Paths.get(args[0])).toArray(new String[0]));
        dics = removeSpace(Files.readAllLines(Paths.get(args[1])).toArray(new String[0]));

        map = new HashMap(words.length + 1);
        resultat = new int[words.length];

        preprog();

        Runtime runtime = Runtime.getRuntime();
        runtime.freeMemory();

        Timer.start();
        //prog();
        new LOG320_LAB01_Tony(words, dics, 0, words.length, 0, dics.length).compute();
        Timer.stop();

        affichierResultat();
    }

    private static String[] removeSpace(String[] list) {
        String[] newList = new String[list.length];
        for (int i = 0; i < list.length; i++) {
            newList[i] = list[i].replace(" ", "");
        }

        return newList;
    }

    /**
     * Lancer l'execution une fois avant le timer pour charger les classes dans
     * la JVM
     *
     * @throws InterruptedException
     */
    private static void preprog() throws IOException {
        for (String ligne : words) {
            for (String ligne2 : dics) {
                if (EstUnAnagrammeV2(ligne.toCharArray(), ligne2.toCharArray())) {
                }
            }
        }
    }

    private static void prog() throws IOException {
        int maxWords = words.length;
        int maxDic = dics.length;

        for (int i = 0; i < maxWords; i++) {// USE thread
            // int nbAna = 0;
            for (int j = 0; j < maxDic; j++) {
                if (EstUnAnagrammeV2(words[i].toCharArray(), dics[j].toCharArray())) {
                    resultat[i] += 1;
                    // nbAna++;
                }

            }
            //  map.put(words[i], nbAna);
        }
    }

    private static void affichierResultat() {
        int nbTotal = 0;

        for (int i = 0; i < resultat.length; i++) {
            StringBuilder sb = new StringBuilder("Il y a ");
            sb.append(resultat[i]);
            sb.append(" anagrammes pour le mot ");
            sb.append(words[i]);
            System.out.println(sb.toString());
            nbTotal += resultat[i];
        }

        StringBuilder sb = new StringBuilder("Il y a un total de ");
        sb.append(nbTotal);
        System.out.println(sb.toString());

        System.out.format("Temps execution : %.20f secondes", Timer.getTime());
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
        if (ch1.length != ch2.length) {
            return false;
        }
        return hash(ch1) == hash(ch2);
    }

    private static int hash(char[] tab) {
        int hash = 0;
        int maxElement = tab.length;

        for (int i = 0; i < maxElement; i++) {
            hash += (int) tab[i];
        }

        return hash;
    }

    private static boolean chaineIsEmpty(char[] chaine) {
        for (char c : chaine) {
            if (c != '\0') {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void compute() {
        int maxWord = finWord - debutWord;
        if (maxWord < 50) {
            int maxDic = finDic - debutDic;
            if (maxDic < 50000) {
                for (int i = 0; i < maxWord; i++) {// USE thread
                    for (int j = 0; j < maxDic; j++) {
                        if (EstUnAnagrammeV2(words[i].toCharArray(), dics[j].toCharArray())) {
                            resultat[i] += 1;
                        }
                    }
                }
            } else {
                // diviser le tableau dic en 2
                new LOG320_LAB01_Tony(words, dics, 0, words.length, 0, maxDic / 2).compute();
                new LOG320_LAB01_Tony(words, dics, 0, words.length, maxDic / 2 + 1, maxDic).compute();
            }

        } else {
            // diviser le tableau words en 2
            new LOG320_LAB01_Tony(words, dics, 0, maxWord / 2, 0, dics.length).compute();
            new LOG320_LAB01_Tony(words, dics, maxWord / 2 + 1, maxWord, 0, dics.length).compute();
        }
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
            return ((float) (endTime - startTime)) / 1000000000.0f;
        }
    }
}
