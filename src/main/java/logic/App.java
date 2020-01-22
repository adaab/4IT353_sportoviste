package logic;

import UI.AdminController;
import UI.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import javax.persistence.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class App implements Subject{
    public ArrayList<RozvrhovaAkce> akce;
    public EntityManagerFactory EMF;
    private int pocetZakazniku;
    private int pocetSportovist;
    private int pocetTreneru;
    private int pocetRozvrhoveAkce;

    private Stage stage;
    private LoginController loginController;
    private AdminController adminController;

    public App(LoginController controller, Stage stage) {
        this.loginController = controller;
        this.stage = stage;

        EMF = Persistence.createEntityManagerFactory("punit");
        getPocetZakazniku();
        pocetSportovist = getSportoviste().size();
        pocetTreneru = getTreneri().size();
        pocetRozvrhoveAkce = getRozvrhoveAkce().size();
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
                jeOK = admin.equals(jeAdmin) && heslo.equals(hashujHeslo(hesloUzivatele));
            } else {
                jeOK = heslo.equals(hashujHeslo(hesloUzivatele));
            }
        } catch (NoResultException e){
            System.out.println(e);
            return false;
        }

        return jeOK;
    }

    public void initAdminScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admin.fxml"));
        TabPane root = loader.load();
        adminController = loader.getController();
        Scene scene = new Scene(root, 800, 640);
        stage.setScene(scene);
        stage.setTitle("Sportoviště - administrátor");
        scene.getStylesheets().add("styles.css");
        adminController.inicializuj(this);
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
                novy.setIdZakaznik(pocetZakazniku+1);
                novy.setEmail(email);
                novy.setHeslo(hashujHeslo(heslo));
                novy.setAdmin(false);

                em.merge(novy);
                em.getTransaction().commit();
                em.close();

                pocetZakazniku++;
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

    public void getPocetZakazniku(){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        pocetZakazniku = em.createQuery("select count(idZakaznik) from Zakaznik", Long.class).getSingleResult().intValue();
    }

    public List<Sportoviste> getSportoviste(){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        List<Sportoviste> sportoviste = em.createQuery("select s from Sportoviste s",Sportoviste.class).getResultList();

        return sportoviste;
    }

    public void noveSportoviste(String nazev, String povrch, String rozmery){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Sportoviste novy = new Sportoviste();
        novy.setIdSportoviste(pocetSportovist+1);
        novy.setNazev(nazev);
        novy.setPovrch(povrch);
        novy.setRozmery(rozmery);

        em.merge(novy);
        em.getTransaction().commit();
        em.close();

        pocetSportovist++;
    }

    public Sportoviste getSportovisteDetail(Integer id){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Sportoviste sportoviste = em.createQuery("select s from Sportoviste s where s.idSportoviste = :id",Sportoviste.class).setParameter("id",id).getSingleResult();

        em.getTransaction().commit();
        em.close();

        return sportoviste;
    }

    public void updateSportoviste(Integer id, String nazev, String povrch, String rozmery){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Sportoviste upravovane = getSportovisteDetail(id);
        upravovane.setNazev(nazev);
        upravovane.setPovrch(povrch);
        upravovane.setRozmery(rozmery);

        em.merge(upravovane);
        em.getTransaction().commit();
        em.close();
    }

    public void removeSportoviste(Integer id){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Sportoviste sportoviste = em.createQuery("select s from Sportoviste s where s.idSportoviste = :id",Sportoviste.class).setParameter("id",id).getSingleResult();
        em.remove(sportoviste);

        em.getTransaction().commit();
        em.close();
    }

    public List<Trener> getTreneri(){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        List<Trener> treneri = em.createQuery("select t from Trener t",Trener.class).getResultList();

        return treneri;
    }

    public void novyTrener(String jmeno, String telefon, String email, Date datumNarozeni, Integer uvazek) {
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Trener novy = new Trener();
        novy.setIdTrener(pocetTreneru+1);
        novy.setJmeno(jmeno);
        novy.setTelefon(telefon);
        novy.setEmail(email);
        novy.setDatumNarozeni(datumNarozeni);
        novy.setUvazek(uvazek);

        em.merge(novy);
        em.getTransaction().commit();
        em.close();

        pocetTreneru++;
    }

    public void updateTrener(Integer id, String jmeno, String telefon, String email, Date datumNarozeni, Integer uvazek){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Trener upravovany = getTrenerDetail(id);
        upravovany.setJmeno(jmeno);
        upravovany.setTelefon(telefon);
        upravovany.setEmail(email);
        upravovany.setDatumNarozeni(datumNarozeni);
        upravovany.setUvazek(uvazek);

        em.merge(upravovany);
        em.getTransaction().commit();
        em.close();
    }

    public Trener getTrenerDetail(Integer id){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Trener trener = em.createQuery("select t from Trener t where t.idTrener = :id",Trener.class).setParameter("id",id).getSingleResult();

        em.getTransaction().commit();
        em.close();

        return trener;
    }

    public void removeTrener(Integer idTrener) {
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        Trener trener = em.createQuery("select t from Trener t where t.idTrener = :id",Trener.class).setParameter("id",idTrener).getSingleResult();
        em.remove(trener);

        em.getTransaction().commit();
        em.close();
    }

    public List<RozvrhovaAkce> getRozvrhoveAkce(){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        List<RozvrhovaAkce> akce = em.createQuery("select a from RozvrhovaAkce a",RozvrhovaAkce.class).getResultList();

        return akce;
    }

    public void novaRozvrhovaAkce(RozvrhovaAkce.TypLekce typLekce, Date datum, String casOd, String casDo, Integer volnaMista, Integer idTrener, Integer idSportoviste) {
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        RozvrhovaAkce novy = new RozvrhovaAkce();
        novy.setIdRozvrhovaAkce(pocetRozvrhoveAkce+1);
        novy.setTypLekce(typLekce);
        novy.setDatum(datum);
        novy.setCasOd(casOd);
        novy.setCasDo(casDo);
        novy.setVolnaMista(volnaMista);
        novy.setIdTrener(idTrener);
        novy.setIdSportoviste(idSportoviste);

        em.merge(novy);
        em.getTransaction().commit();
        em.close();

        pocetRozvrhoveAkce++;
    }

    public void updateRozvrhovaAkce(Integer id, RozvrhovaAkce.TypLekce typLekce, Date datum, String casOd, String casDo, Integer volnaMista, Integer idTrener, Integer idSportoviste){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        RozvrhovaAkce upravovany = getRozvrhoveAkceDetail(id);
        upravovany.setTypLekce(typLekce);
        upravovany.setDatum(datum);
        upravovany.setCasOd(casOd);
        upravovany.setCasDo(casDo);
        upravovany.setVolnaMista(volnaMista);
        upravovany.setIdTrener(idTrener);
        upravovany.setIdSportoviste(idSportoviste);

        em.merge(upravovany);
        em.getTransaction().commit();
        em.close();
    }

    public RozvrhovaAkce getRozvrhoveAkceDetail(Integer id){
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        RozvrhovaAkce akce = em.createQuery("select a from RozvrhovaAkce a where a.idRozvrhovaAkce = :id",RozvrhovaAkce.class).setParameter("id",id).getSingleResult();

        em.getTransaction().commit();
        em.close();

        return akce;
    }

    public void removeRozvrhovaAkce(Integer id) {
        EntityManager em = EMF.createEntityManager();
        em.getTransaction().begin();

        RozvrhovaAkce akce = em.createQuery("select a from RozvrhovaAkce a where a.idRozvrhovaAkce = :id",RozvrhovaAkce.class).setParameter("id",id).getSingleResult();
        em.remove(akce);

        em.getTransaction().commit();
        em.close();
    }
}
