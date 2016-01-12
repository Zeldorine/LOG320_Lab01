/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log320_lab01;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Zeldorine
 */
public class LOG320_LAB01 {

    private static int nbTotalAnagram = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* if (args.length != 2) {
            System.out.println("Erreur nombre d'argument");
            System.exit(0);
        }*/

        long a = Calendar.getInstance().getTimeInMillis();
        
        //lecture du fichier de mots	
        try {
            InputStream ips = new FileInputStream("C:\\Users\\Zeldorine\\Downloads\\anagramme\\words.txt");
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);
            String ligne;
            while ((ligne = br.readLine()) != null) {
                int nbAnagramme = 0;
                //lecture du dictionnaire
                InputStream ips2 = new FileInputStream("C:\\Users\\Zeldorine\\Downloads\\anagramme\\dict.txt");
                InputStreamReader ipsr2 = new InputStreamReader(ips2);
                BufferedReader br2 = new BufferedReader(ipsr2);
                String ligne2;
                while ((ligne2 = br2.readLine()) != null) {
                    if (EstUnAnagramme(ligne.replace(" ", "").chars().mapToObj(c -> (char)c).toArray(Character[]::new), ligne2.replace(" ", "").chars().mapToObj(c -> (char)c).toArray(Character[]::new))) {
                        nbTotalAnagram++;
                        nbAnagramme++;
                    }
                }
                br2.close();
                System.out.println("Il y a " + nbAnagramme + " anagramme pour le mot " + ligne);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        System.out.println("Il y a un total de " + nbTotalAnagram);
        float time = (Calendar.getInstance().getTimeInMillis() - a) / 1000.000000000f;
        System.out.println("Temps execution : " + time + " secondes");
    }

    private static boolean EstUnAnagramme(Character[] chaine1, Character[] chaine2) {
        String chaine2cp = Arrays.toString(chaine2).trim();
        String chaine1cp = Arrays.toString(chaine1);
        for (char c1 : chaine1) {
            boolean trouver = false;
            for(int i=0; i<chaine2.length; i++) {
                if (chaine2[i] != null && c1 == chaine2[i]) {
                    trouver = true;
                    chaine2[i]= null;
                    break;
                }
            }
            
            if(trouver == false){
                return false;
            }
        }
        
       if(!chaineIsEmpty(chaine2)){
            return false;
        }
        

        return true;
    }

    private static boolean chaineIsEmpty(Character[] chaine) {
       for(Character c : chaine){
           if(c != null){
               return false;
           }
       }
       
       return true;
    }
}