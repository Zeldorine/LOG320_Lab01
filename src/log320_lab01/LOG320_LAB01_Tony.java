package log320_lab01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author Zeldorine
 */
public class LOG320_LAB01_Tony {

    private static int[] resultat;
    private static char[][] wordstab;
    private static char[][] dicstab;
    private static String[] tmpWords;
    private static boolean useThread = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Erreur nombre d'argument");
            System.exit(0);
        }

        tmpWords = Files.readAllLines(Paths.get(args[0])).toArray(new String[0]);
        wordstab = initTab(removeSpace(Files.readAllLines(Paths.get(args[0])).toArray(new String[0])));
        dicstab = initTab(removeSpace(Files.readAllLines(Paths.get(args[1])).toArray(new String[0])));
        resultat = new int[wordstab.length];

        preprog();

        Runtime runtime = Runtime.getRuntime();
        runtime.freeMemory();

        if (!needThread()) {
            Timer.start();
            prog();
            Timer.stop();
        } else {
            useThread = true;
            ForkJoinPool mainPool = new ForkJoinPool();
            RecursiveAction mainTask = new LOG320_LAB01_Tony_Algo_AT(0, wordstab.length);
            Timer.start();
            mainPool.invoke(mainTask);
            Timer.stop();
        }

        affichierResultat();
    }

    private static boolean needThread() {
        return (wordstab.length >= 20 && dicstab.length >= 8000)
                || (wordstab.length >= 30 && dicstab.length >= 5000)
                || (wordstab.length >= 40 && dicstab.length >= 4000)
                || (wordstab.length >= 50 && dicstab.length >= 3000)
                || (wordstab.length >= 60 && dicstab.length >= 3000)
                || (wordstab.length >= 70 && dicstab.length >= 2000)
                || (wordstab.length >= 80 && dicstab.length >= 2000)
                || (wordstab.length >= 90 && dicstab.length >= 2000)
                || (wordstab.length >= 100 && dicstab.length >= 1000);
    }

    private static char[][] initTab(String[] tab) {
        char[][] tmp = new char[tab.length][];

        for (int i = 0; i < tab.length; i++) {
            tmp[i] = tab[i].toCharArray();
        }

        return tmp;
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
        for (char[] ligne : wordstab) {
            for (char[] ligne2 : dicstab) {
                if (EstUnAnagrammeV2(ligne, ligne2)) {
                }
            }
        }
    }

    private static void prog() throws IOException {
        int maxWords = wordstab.length;
        int maxDic = dicstab.length;

        for (int i = 0; i < maxWords; i++) {
            for (int j = 0; j < maxDic; j++) {
                if (EstUnAnagrammeV2(wordstab[i], dicstab[j])) {
                    resultat[i]++;
                }

            }
        }
    }

    private static void affichierResultat() {
        int nbTotal = 0;

        for (int i = 0; i < resultat.length; i++) {
            StringBuilder sb = new StringBuilder("Il y a ");
            sb.append(resultat[i]);
            sb.append(" anagrammes pour le mot ");
            sb.append(tmpWords[i]);
            System.out.println(sb.toString());
            nbTotal += resultat[i];
        }

        StringBuilder sb = new StringBuilder("(use thread = " + useThread + ") Il y a un total de ");
        sb.append(nbTotal);
        System.out.println(sb.toString());

        System.out.format("Temps d'execution : %.20f secondes", Timer.getTime());
    }

    private static boolean EstUnAnagrammeV1(char[] chaine1, char[] chaine2) {
        char[] tmp = new char[chaine2.length];
        System.arraycopy(chaine2, 0, tmp, 0,
                chaine2.length);

        for (char c1 : chaine1) {
            boolean trouver = false;
            for (int i = 0; i < chaine2.length; i++) {
                if (tmp[i] != '\0' && c1 == tmp[i]) {
                    trouver = true;
                    tmp[i] = '\0';
                    break;
                }
            }

            if (trouver == false) {
                return false;
            }
        }

        return chaineIsEmpty(tmp);
    }

    private static boolean EstUnAnagrammeV2(char[] ch1, char[] ch2) {
        if (ch1.length != ch2.length) {
            return false;
        }

        int nbCh1 = ch1.length;
        int[] counts = new int[36];

        for (int i = 0; i < nbCh1; i++) {
            int c1 = (int) ch1[i];
            int c2 = (int) ch2[i];

            if (c1 < 97) {
                counts[(int) ch1[i] - 26]++;
            } else {
                counts[(int) ch1[i] - 97]++;
            }

            if (c2 < 97) {
                counts[(int) ch2[i] - 26]--;
            } else {
                counts[(int) ch2[i] - 97]--;
            }
        }

        for (int i = 0; i < 36; i++) {
            if (counts[i] != 0) {
                return false;
            }
        }

        return true;
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
            return ((float) (endTime - startTime)) / 1000000000.0f;
        }
    }

    /**
     * Divise seulement le tableau de word
     */
    public static class LOG320_LAB01_Tony_Algo_AT
            extends RecursiveAction {

        private final int debutWord;
        private final int finWord;

        public LOG320_LAB01_Tony_Algo_AT(int debutWord, int finWord) {
            super();
            this.debutWord = debutWord;
            this.finWord = finWord;
        }

        @Override
        protected void compute() {
            if (finWord - debutWord < 10) {
                int dicsLength = dicstab.length;
                for (int i = debutWord; i < finWord; i++) {
                    for (int j = 0; j < dicsLength; j++) {
                        if (EstUnAnagrammeV2(wordstab[i], dicstab[j])) {
                            resultat[i]++;
                        }
                    }
                }
            } else {
                int mid = (debutWord + finWord) >>> 1;
                invokeAll(new LOG320_LAB01_Tony_Algo_AT(debutWord, mid),
                        new LOG320_LAB01_Tony_Algo_AT(mid, finWord));
            }
        }
    }
}
