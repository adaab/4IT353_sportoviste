package logic;

/**
 * předmět pozorování
 */
public interface Subject {

    /**
     * metoda umožňuje zaregistrovat pozorovatele
     * @param observer
     * @return pozorovatel, nebo null při neúspěšné registraci
     */
    Observer register(Observer observer);

    /**
     * metoda umožňuje odebrat pozorovatele
     * @param observer
     * @return pozorovatel, nebo null při neúspěšné registraci
     */
    Observer unregister(Observer observer);

    /**
     * metoda upozorní pozorovatele na změnu
     */
    void notifyObservers();



}