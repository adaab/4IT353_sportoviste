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

    private Boolean pridavamNovouPolozku;

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

    }

    public void inicializuj(App app){
        this.app = app;
        app.register(this);

        sportoviste = FXCollections.observableArrayList();
        app.getSportoviste().forEach(s -> sportoviste.add(s.getIdSportoviste()+": "+s.getNazev()));
        seznamSportoviste.setItems(sportoviste);

        update();
    }

    public void novaPolozkaSportoviste(){
        pridavamNovouPolozku = true;

        idSportoviste.clear();
        nazevSportoviste.clear();
        povrchSportoviste.clear();
        rozmerySportoviste.clear();

        nazevSportoviste.setDisable(false);
        povrchSportoviste.setDisable(false);
        rozmerySportoviste.setDisable(false);
    }

    public void ulozitSportoviste(){
        if(pridavamNovouPolozku){
            String nazev = nazevSportoviste.getText();
            String povrch = povrchSportoviste.getText();
            String rozmery = rozmerySportoviste.getText();

            if(nazev.isEmpty() || povrch.isEmpty() || rozmery.isEmpty()){
                //TODO vrátit chybu
            } else{
                app.noveSportoviste(nazev, povrch, rozmery);
            }

        }
    }

    public void vyberSportoviste(){
        String volba = String.valueOf(seznamSportoviste.getSelectionModel().getSelectedItem());
        String[] parsed = volba.split(": ");
        Integer id = Integer.parseInt(parsed[0]);
        Sportoviste sportoviste = app.getSportovisteDetail(id);

        nazevSportoviste.setDisable(true);
        povrchSportoviste.setDisable(true);
        rozmerySportoviste.setDisable(true);

        idSportoviste.setText(sportoviste.getIdSportoviste().toString());
        nazevSportoviste.setText(sportoviste.getNazev());
        povrchSportoviste.setText(sportoviste.getPovrch());
        rozmerySportoviste.setText(sportoviste.getRozmery());
    }



}
