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
        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        ObligSBinTre<Integer> tre = new ObligSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) {
            tre.leggInn(verdi);
        }
        System.out.println(tre);
        System.out.println(tre.postString());
        
    }
}
