package logic;

import UI.AdminController;
import UI.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class App implements Subject{
    static final Logger LOG = LoggerFactory.getLogger(App.class);

    public ArrayList<RozvrhovaAkce> akce;
    public EntityManagerFactory EMF;

    private Stage stage;
    private LoginController loginController;
    private AdminController adminController;

    public App(LoginController controller, Stage stage) {
        this.loginController = controller;
        this.stage = stage;

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
                jeOK = admin.equals(jeAdmin) && heslo.equals(hashujHeslo(hesloUzivatele));
            } else {
                jeOK = heslo.equals(hashujHeslo(hesloUzivatele));
            }
        } catch (NoResultException e){
            LOG.error("User not found."+e.toString());
            return false;
        }

        LOG.info("User verification successful");
        return jeOK;
    }

    public void initAdminScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admin.fxml"));
        AnchorPane root = loader.load();
        adminController = loader.getController();
        Scene scene = new Scene(root, 800, 640);
        stage.setScene(scene);
        stage.setTitle("Sportoviště - administrátor");
        scene.getStylesheets().add("styles.css");
        adminController.inicializuj(this);
        LOG.info("Admin screen initialized");
    }

    public void initCommonScreen(){

    }

    public boolean zaregistrujZakaznika(String email, String heslo, String hesloKontrola){
        if(!heslo.equals(hesloKontrola)){
            LOG.error("Inputed passwords do not match.");
            return false;
        } else{
            if(existujeEmail(email)){
                LOG.error("Inputed email already exists");
                return false;
            } else{
                EntityManager em = EMF.createEntityManager();
                em.getTransaction().begin();

                Zakaznik novy = new Zakaznik();
                novy.setEmail(email);
                novy.setHeslo(hashujHeslo(heslo));
                novy.setAdmin(false);

                em.merge(novy);
                em.getTransaction().commit();
                em.close();

                LOG.info("User successfully registered");
                return true;
            }
        }
    }

    public boolean existujeEmail(String email){
        EntityManager em = EMF.createEntityManager();

        TypedQuery<String> dotaz = em.createQuery("select email from Zakaznik where email = :email", String.class);

        try {
            boolean result = (!dotaz.setParameter("email", email).getSingleResult().isEmpty());
            em.close();
            return result;
        } catch (NoResultException e){
            LOG.error("Email not found. "+e.toString());
            em.close();
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
            LOG.error(e.toString());
        }

        return hesloHash;
    }

    public List<Sportoviste> getSportoviste() {
        EntityManager em = EMF.createEntityManager();
        List<Sportoviste> sportoviste = null;
        try {
            sportoviste = em.createQuery("select s from Sportoviste s", Sportoviste.class).getResultList();
        } catch (NoResultException e) {
            LOG.error(e.toString());
        } finally {
            em.close();
            return sportoviste;
        }
    }

    public void noveSportoviste(String nazev, String povrch, String rozmery){
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();

            Sportoviste novy = new Sportoviste();
            novy.setNazev(nazev);
            novy.setPovrch(povrch);
            novy.setRozmery(rozmery);

            em.merge(novy);
            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public Sportoviste getSportovisteDetail(Integer id) {
        EntityManager em = EMF.createEntityManager();

        Sportoviste sportoviste = null;
        try {
            sportoviste = em.createQuery("select s from Sportoviste s where s.idSportoviste = :id", Sportoviste.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            LOG.error(e.toString());
        } finally {
            em.close();
            return sportoviste;
        }
    }

    public void updateSportoviste(Integer id, String nazev, String povrch, String rozmery){
        EntityManager em = EMF.createEntityManager();
        try{
            em.getTransaction().begin();

            Sportoviste upravovane = getSportovisteDetail(id);
            upravovane.setNazev(nazev);
            upravovane.setPovrch(povrch);
            upravovane.setRozmery(rozmery);

            em.merge(upravovane);
            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
      } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public void removeSportoviste(Integer id){
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();

            Sportoviste sportoviste = em.createQuery("select s from Sportoviste s where s.idSportoviste = :id", Sportoviste.class).setParameter("id", id).getSingleResult();
            em.remove(sportoviste);

            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public List<Trener> getTreneri() {
        EntityManager em = EMF.createEntityManager();
        List<Trener> treneri = null;
        try {
            treneri = em.createQuery("select t from Trener t", Trener.class).getResultList();
        } catch (NoResultException e) {
            LOG.error(e.toString());
        } finally {
            em.close();
            return treneri;
        }
    }

    public void novyTrener(String jmeno, String telefon, String email, Date datumNarozeni, Integer uvazek) {
        EntityManager em = EMF.createEntityManager();

        try {
            em.getTransaction().begin();

            Trener novy = new Trener();
            novy.setJmeno(jmeno);
            novy.setTelefon(telefon);
            novy.setEmail(email);
            novy.setDatumNarozeni(datumNarozeni);
            novy.setUvazek(uvazek);

            em.merge(novy);
            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public void updateTrener(Integer id, String jmeno, String telefon, String email, Date datumNarozeni, Integer uvazek){
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();

            Trener upravovany = getTrenerDetail(id);
            upravovany.setJmeno(jmeno);
            upravovany.setTelefon(telefon);
            upravovany.setEmail(email);
            upravovany.setDatumNarozeni(datumNarozeni);
            upravovany.setUvazek(uvazek);

            em.merge(upravovany);
            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public Trener getTrenerDetail(Integer id) {
        EntityManager em = EMF.createEntityManager();
        Trener trener = null;
        try {
            trener = em.createQuery("select t from Trener t where t.idTrener = :id", Trener.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            LOG.error(em.toString());
        } finally {
            em.close();
            return trener;
        }
    }

    public void removeTrener(Integer idTrener) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();

            Trener trener = em.createQuery("select t from Trener t where t.idTrener = :id", Trener.class).setParameter("id", idTrener).getSingleResult();
            em.remove(trener);

            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public List<RozvrhovaAkce> getRozvrhoveAkce(){
        EntityManager em = EMF.createEntityManager();
        List<RozvrhovaAkce> akce = null;
        try {
            akce = em.createQuery("select a from RozvrhovaAkce a", RozvrhovaAkce.class).getResultList();
        } catch(NoResultException e){
            LOG.error(e.toString());
        } finally {
            em.close();
            return akce;
        }
    }

    public void novaRozvrhovaAkce(String typLekce, Date datum, String casOd, String casDo, Integer volnaMista, Integer idTrener, Integer idSportoviste) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();

            RozvrhovaAkce novy = new RozvrhovaAkce();
            novy.setTypLekce(typLekce);
            novy.setDatum(datum);
            novy.setCasOd(casOd);
            novy.setCasDo(casDo);
            novy.setVolnaMista(volnaMista);
            novy.setIdTrener(idTrener);
            novy.setIdSportoviste(idSportoviste);

            em.merge(novy);
            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public void updateRozvrhovaAkce(Integer id, String typLekce, Date datum, String casOd, String casDo, Integer volnaMista, Integer idTrener, Integer idSportoviste){
        EntityManager em = EMF.createEntityManager();
        try {
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
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public RozvrhovaAkce getRozvrhoveAkceDetail(Integer id){
        EntityManager em = EMF.createEntityManager();
        RozvrhovaAkce akce = null;
        try {
            akce = em.createQuery("select a from RozvrhovaAkce a where a.idRozvrhovaAkce = :id", RozvrhovaAkce.class).setParameter("id", id).getSingleResult();
        } catch(NoResultException e){
            LOG.error(e.toString());
        } finally {
            em.close();
            return akce;
        }
    }

    public void removeRozvrhovaAkce(Integer id) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();

            RozvrhovaAkce akce = em.createQuery("select a from RozvrhovaAkce a where a.idRozvrhovaAkce = :id", RozvrhovaAkce.class).setParameter("id", id).getSingleResult();
            em.remove(akce);

            em.getTransaction().commit();
        } catch(RollbackException e) {
            LOG.error(e.toString());
            adminController.vratChybu("Objevila se chyba při ukládání záznamu. Prosím, zkuste to znovu");
            adminController.update();
        /*} catch(JDBCConnectionException e){
            LOG.error(e.toString());
            adminController.connectionLostBegin("Objevily se potíže s připojením k databázi. Prosím vyčkejte");
        */
        } finally {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    public void close(){
        EMF.close();
        stage.close();
    }

}
