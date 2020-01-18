package logic;

//import javafx.application.Application;
//import javafx.stage.Stage;

import UI.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Start extends Application {
    public static void main(String [] args){
        Start.launch(args);

        /*
        EntityManagerFactory EMF = Persistence.createEntityManagerFactory("punit");

        EntityManager em1 = EMF.createEntityManager();
        try {
            em1.getTransaction().begin();

            RozvrhovaAkce akce = new RozvrhovaAkce();
            akce.setIdRozvrhovaAkce(1);
            akce.setDatum(LocalDate.now());
            akce.setCasDo("18:00");
            akce.setCasOd("17:00");
            akce.setTypLekce(RozvrhovaAkce.TypLekce.aerobik);
            akce.setVolnaMista(25);

            Zakaznik zakaznik = new Zakaznik();
            zakaznik.setIdZakaznik(1);
            zakaznik.setEmail("ada@test.cz");
            zakaznik.setHeslo("asdf");


            //em1.persist(akce);
            em1.merge(zakaznik);
            em1.getTransaction().commit();
            //em1.close();
            //EMF.close();
        } catch(Exception e){
            em1.getTransaction().rollback();
        }
        System.out.println("DONE");*/
    }

    
    @Override
    public void start (Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/login.fxml"));
        GridPane root = loader.load();
        LoginController controller = loader.getController();

        primaryStage.setTitle("Fit centrum");
        Scene scene = new Scene(root, 800, 640);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("styles.css");
        primaryStage.show();

        App app = new App();
        controller.inicializuj(app);
    }
}
