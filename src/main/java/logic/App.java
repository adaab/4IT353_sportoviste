package logic;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Boolean jeOK;

        try {
            String heslo = em.createQuery("select heslo from Zakaznik where email = :emailUzivatele", String.class).setParameter("emailUzivatele", emailUzivatele).getSingleResult();
            if(jeAdmin){
                Boolean admin = em.createQuery("select jeAdmin from Zakaznik where email = :emailUzivatele", Boolean.class).setParameter("emailUzivatele", emailUzivatele).getSingleResult();
                jeOK = admin.equals(jeAdmin) && heslo.equals(hesloUzivatele);
            } else {
                jeOK = heslo.equals(hesloUzivatele);
            }
        } catch (NoResultException e){
            return false;
        }

        return jeOK;
    }

    public void initAdminScreen(){

    }

    public void initCommonScreen(){

    }

    public boolean zaregistrujZakaznika(String email, String heslo, String hesloKontrola){
        if(!heslo.equals(hesloKontrola)){
            return false;
        } else{
            if(existujeEmail(email)){
                return false;
            } else{
                EntityManager em = EMF.createEntityManager();
                em.getTransaction().begin();

                Zakaznik novy = new Zakaznik();
                novy.setIdZakaznik(1);
                novy.setEmail(email);
                novy.setHeslo(hashujHeslo(heslo));
                novy.setAdmin(false);

                em.merge(novy);
                em.getTransaction().commit();
                em.close();

                return true;
            }
        }
    }

    public boolean existujeEmail(String email){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        TypedQuery<String> dotaz = em.createQuery("select email from Zakaznik where email = :email", String.class);

        try {
            return (!dotaz.setParameter("email", email).getSingleResult().isEmpty());
        } catch (NoResultException e){
            return false;
        }
    }

    public String hashujHeslo(String heslo){
        String hesloHash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(heslo.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hesloHash = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hesloHash;
    }

}
