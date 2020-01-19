package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import logic.App;
import logic.Observer;
import logic.Sportoviste;

import java.util.List;

public class AdminController implements Observer {
    private App app;
    private ObservableList<String> sportoviste;

    private Boolean pridavamNovouPolozku = false;
    private Sportoviste aktualni;

    @FXML
    public Button upravitSportoviste;
    @FXML
    public Button novaPolozkaSportoviste;
    @FXML
    public Button smazatSportoviste;
    @FXML
    public ListView seznamSportoviste;
    @FXML
    public Button ulozitSportoviste;
    @FXML
    public Button zrusitSportoviste;
    @FXML
    public TextField idSportoviste;
    @FXML
    public TextField nazevSportoviste;
    @FXML
    public TextField povrchSportoviste;
    @FXML
    public TextField rozmerySportoviste;

    @Override
    public void update() {
        //SPORTOVISTE TAB
        sportoviste = FXCollections.observableArrayList();
        app.getSportoviste().forEach(s -> sportoviste.add(s.getIdSportoviste()+": "+s.getNazev()));
        seznamSportoviste.setItems(sportoviste);

        pridavamNovouPolozku = false;
        aktualni = null;
        idSportoviste.clear();
        nazevSportoviste.clear();
        povrchSportoviste.clear();
        rozmerySportoviste.clear();

        ulozitSportoviste.setDisable(true);
        zrusitSportoviste.setDisable(true);
    }

    public void inicializuj(App app){
        this.app = app;
        app.register(this);
        update();
    }

    public void novaPolozkaSportoviste(){
        pridavamNovouPolozku = true;
        aktualni = null;

        idSportoviste.clear();
        nazevSportoviste.clear();
        povrchSportoviste.clear();
        rozmerySportoviste.clear();

        nazevSportoviste.setDisable(false);
        povrchSportoviste.setDisable(false);
        rozmerySportoviste.setDisable(false);

        ulozitSportoviste.setDisable(false);
        zrusitSportoviste.setDisable(false);

        upravitSportoviste.setDisable(true);
        smazatSportoviste.setDisable(true);
    }

    public void ulozitSportoviste() {
        String nazev = nazevSportoviste.getText();
        String povrch = povrchSportoviste.getText();
        String rozmery = rozmerySportoviste.getText();

        if (nazev.isEmpty() || povrch.isEmpty() || rozmery.isEmpty()) {
            //TODO vrátit chybu
        } else if (pridavamNovouPolozku) {
            app.noveSportoviste(nazev, povrch, rozmery);
            } else {
            app.updateSportoviste(aktualni.getIdSportoviste(), nazev, povrch, rozmery);
        }

        update();
    }

    public void vyberSportoviste(){
        String volba = String.valueOf(seznamSportoviste.getSelectionModel().getSelectedItem());
        String[] parsed = volba.split(": ");
        Integer id = Integer.parseInt(parsed[0]);
        aktualni = app.getSportovisteDetail(id);

        nazevSportoviste.setDisable(true);
        povrchSportoviste.setDisable(true);
        rozmerySportoviste.setDisable(true);

        idSportoviste.setText(aktualni.getIdSportoviste().toString());
        nazevSportoviste.setText(aktualni.getNazev());
        povrchSportoviste.setText(aktualni.getPovrch());
        rozmerySportoviste.setText(aktualni.getRozmery());

        upravitSportoviste.setDisable(false);
        smazatSportoviste.setDisable(false);
        novaPolozkaSportoviste.setDisable(false);
    }

    public void upravSportoviste(){
        nazevSportoviste.setDisable(false);
        povrchSportoviste.setDisable(false);
        rozmerySportoviste.setDisable(false);

        ulozitSportoviste.setDisable(false);
        zrusitSportoviste.setDisable(false);
        novaPolozkaSportoviste.setDisable(true);
        upravitSportoviste.setDisable(true);
        smazatSportoviste.setDisable(true);
    }

    public void smazSportoviste(){
        //TODO init confirmation popup
        app.removeSportoviste(aktualni.getIdSportoviste());
        update();
    }

    public void zrusitSportoviste(){
        //TODO init confirmation popup
        update();
    }



}
