/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obligsbintre;

/**
 *
 * @author tomtr
 */
import java.util.*;

public class ObligSBinTre<T> implements Beholder<T> {

    private static final class Node<T> // en indre nodeklasse
    {

        private T verdi; // nodens verdi
        private Node<T> venstre, høyre; // venstre og høyre barn
        private Node<T> forelder; // forelder
        // konstruktør

        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder) // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }
    } // class Node
    private Node<T> rot; // peker til rotnoden
    private int antall; // antall noder
    private int endringer; // antall endringer
    private final Comparator<? super T> comp; // komparator

    public ObligSBinTre(Comparator<? super T> c) // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null) // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi, p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte
        p = new Node<>(verdi, q);                   // oppretter en ny node

        if (q == null) {
            rot = p;                  // p blir rotnode
        } else if (cmp < 0) {
            q.venstre = p;         // venstre barn til q
        } else {
            q.høyre = p;                        // høyre barn til q
        }
        antall++;                                // én verdi mer i treet
        return true;
    }

    @Override
    public boolean inneholder(T verdi) {
        if (verdi == null) {
            return false;
        }
        Node<T> p = rot;
        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) {
                p = p.venstre;
            } else if (cmp > 0) {
                p = p.høyre;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean fjern(T verdi) {
        if (verdi == null) {
            return false;  // treet har ingen nullverdier
        }
        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null) // leter etter verdi
        {
            int cmp = comp.compare(verdi, p.verdi);      // sammenligner
            if (cmp < 0) {
                q = p;
                p = p.venstre;
            } // går til venstre
            else if (cmp > 0) {
                q = p;
                p = p.høyre;
            } // går til høyre
            else {
                break;    // den søkte verdien ligger i p
            }
        }
        if (p == null) {
            return false;   // finner ikke verdi
        }
        if (p.venstre == null || p.høyre == null) // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
            } else if (p == q.venstre) {
                q.venstre = b;
                if (b != null) {
                    b.forelder = q;
                }
            } else {
                q.høyre = b;
                if (b != null) {
                    b.forelder = q;
                }
            }
        } else // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null) {
                s = r;    // s er forelder til r
                r = r.venstre;
            }

            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) {
                s.venstre = r.høyre;
            } else {
                s.høyre = r.høyre;
            }
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        if (tom()) {
            return 0;
        }
        int teller = 0;

        while (fjern(verdi) != false) {
            teller++;
        }
        return teller;
    }

    @Override
    public int antall() {
        return antall;
    }

    public int antall(T verdi) {
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");
        if (tom()) {
            return 0;
        }

        Node<T> p = rot;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel
        int teller = 0;
        while (p != null) // fortsetter til p er ute av treet
        {
            // q er forelder til p
            cmp = comp.compare(verdi, p.verdi);     // bruker komparatoren
            if (cmp == 0) {
                teller++;
            }
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }
        return teller;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public void nullstill() {
        if (tom()) {
            return;
        }
        nullStillnode(rot);

    }

    public void nullStillnode(Node<T> p) {
        if (p.venstre != null) {
            nullStillnode(p.venstre);
        }
        if (p.høyre != null) {
            nullStillnode(p.høyre);
        }
        fjern(p.verdi);
    }

    private static <T> Node<T> nesteInorden(Node<T> p) {

        if (p.høyre != null) {
            p = p.høyre;
            while (p.venstre != null) {
                p = p.venstre;
            }
            return p;
        }

        while (p.forelder != null && p != p.forelder.venstre) {
            p = p.forelder;
        }
        return p.forelder;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        if (tom()) {
            return "[]";
        }
        Node<T> p = rot;
        while (p.venstre != null) {
            p = p.venstre;
        }
        if (p == null) {
            return sj.toString();
        }
        sj.add(p.verdi.toString());
        Node<T> current = nesteInorden(p);
        while (current != null) {
            sj.add(current.verdi.toString());
            current = nesteInorden(current);
        }
        return sj.toString();
    }

    public String omvendtString() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String høyreGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener() {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier() {
        if (tom()) {
            return "[]";
        }
        Node<T> p = rot;

        StringJoiner sj = new StringJoiner(", ", "[", "]");

        finnBladnode(p, sj);

        return sj.toString();
    }

    public String finnBladnode(Node<T> p, StringJoiner sj) {
        if (p.venstre == null && p.høyre == null) {
            sj.add(p.verdi.toString());
        }
        if (p.venstre != null) {
            finnBladnode(p.venstre, sj);
        }
        if (p.høyre != null) {
            finnBladnode(p.høyre, sj);
        }

        return sj.toString();
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        p = p.forelder;
        if (p != null) {
            if (p.høyre != null) {
                while (p.venstre != null) {
                    p = p.høyre;
                    if (p.høyre != null || p.venstre != null) {
                        p = p.venstre != null ? p.venstre : p.høyre;
                        System.out.println(p.verdi);
                    }
                }
            }
            
        }
        return p;
    }

    public String postString() {
        StringJoiner tekst = new StringJoiner(", ", "[", "]");
        if (tom()) {
            return tekst.toString();
        }

        Node<T> p = rot;
        p = p.venstre != null ? p.venstre : p.høyre;
        while (p.venstre != null || p.høyre != null) {
            p = p.venstre != null ? p.venstre : p.høyre;
        }
        while (p != null) {
            tekst.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return tekst.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new BladnodeIterator();

    }

    private class BladnodeIterator implements Iterator<T> {

        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

        private BladnodeIterator() // konstruktør
        {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public boolean hasNext() {
            return p != null; // Denne skal ikke endres!
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Ikke kodet ennå!");
        }
    } // BladnodeIterator
} // ObligSBinTre
