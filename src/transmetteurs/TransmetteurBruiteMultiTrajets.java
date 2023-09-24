package transmetteurs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.sun.source.tree.ArrayAccessTree;
import destinations.DestinationInterface;
import information.Information;
import information.InformationNonConformeException;

public class TransmetteurBruiteMultiTrajets extends TransmetteurBruiteAnalogique {

    private final ArrayList<Integer> tau;
    private final ArrayList<Float> ar;
    private final Information<Float> multiTrajets;

    /**
     * Constructeur de la classe TransmetteurBruiteAnalogique.
     *
     * @param snr bruit ajouté au signal (en dB)
     * @param nb_sample nombre d'échantillons par symbole
     */
    public TransmetteurBruiteMultiTrajets(float snr, int nb_sample, ArrayList<Integer> tau, ArrayList<Float> ar) {
        super(snr, nb_sample);
        multiTrajets = new Information<>();
        this.tau = tau;
        this.ar = ar;
    }

    public void addMultiTrajets() {
        for(int tauX : tau) {
            System.out.println(tauX);
            for (int i = tauX; i < informationRecue.nbElements(); i++) {
                // pb d'ampl ?
                System.out.println("i:" + i + "ar:" + ar.get(i));
                System.out.println("taux:" + tauX);
                multiTrajets.add(informationRecue.iemeElement(i) + ar.get(i) * informationRecue.iemeElement(i - tauX));
            }
        }
    }

    @Override
    public void recevoir(Information<Float> information) throws InformationNonConformeException {
        // reception des informations
        for (Float i : information) {
            informationRecue.add(i);
            p_signal += (float) Math.pow(i, 2);
        }
        addMultiTrajets();

        generateNoise(multiTrajets, informationEmise);

        emettre();
    }

}

