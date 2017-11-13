/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligsbintre;

import java.util.Comparator;

/**
 *
 * @author tomtr
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] a = {5, 3, 8, 7, 6, 11, 16, 14, 17, 12, 13};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) {
            tre.leggInn(verdi);
        }
        System.out.println(tre);
        System.out.println(tre.postString());
        
    }
}
