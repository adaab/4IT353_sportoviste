package logic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class App implements Subject{
    public ArrayList<RozvrhovaAkce> akce;
    public EntityManagerFactory EMF;

    public App() {
        EMF = Persistence.createEntityManagerFactory("punit");

    }

    @Override
    public Observer register(Observer observer) {
        return null;
    }

    @Override
    public Observer unregister(Observer observer) {
        return null;
    }

    @Override
    public void notifyObservers() {
    }

    public boolean overZakaznika(String emailUzivatele, String hesloUzivatele, Boolean jeAdmin){
        String hesloHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hesloUzivatele.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hesloHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<Zakaznik> dotaz = em.createQuery("select idZakaznik, email, heslo, jeAdmin from Zakaznik where email = :emailUzivatele", Zakaznik.class);
        Zakaznik zakaznik = dotaz.setParameter("emailUzivatele", emailUzivatele).getSingleResult();

        return (zakaznik.getEmail().equals(emailUzivatele) && zakaznik.getHeslo().equals(hesloHash) && zakaznik.getAdmin().equals(jeAdmin));
    }

    public void initAdminScreen(){

    }

    public void initCommonScreen(){

    }

}
