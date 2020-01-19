package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.App;
import logic.Observer;
import logic.Sportoviste;

import java.util.List;
import java.util.Optional;

public class AdminController implements Observer {
    private App app;
    private ObservableList<String> sportoviste;

    private Boolean pridavamNovouPolozku = false;
    private Boolean upravujuPolozku = false;
    private Boolean zobrazujuPolozku = false;
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
    @FXML
    public DialogPane popupSportoviste;

    @Override
    public void update() {
        //SPORTOVISTE TAB
        sportoviste = FXCollections.observableArrayList();
        app.getSportoviste().forEach(s -> sportoviste.add(s.getIdSportoviste()+": "+s.getNazev()));
        seznamSportoviste.setItems(sportoviste);

        zobrazujuPolozku = false;
        upravujuPolozku = false;
        pridavamNovouPolozku = false;
        aktualni = null;
        idSportoviste.clear();
        nazevSportoviste.clear();
        povrchSportoviste.clear();
        rozmerySportoviste.clear();

        ulozitSportoviste.setDisable(true);
        zrusitSportoviste.setDisable(true);
        novaPolozkaSportoviste.setDisable(false);
    }

    public void inicializuj(App app){
        this.app = app;
        app.register(this);
        update();
    }

    public Boolean getConfirmationPopup(String typ){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if(typ.equals("smazat")) {
            alert.setTitle("Smazat záznam");
            alert.setContentText("Chystáte se nenávratně smazat zvolený záznam. Chcete pokračovat?");
        } else {
            alert.setTitle("Zrušit změny");
            alert.setContentText("Neuložené změny budou ztraceny. Chcete pokračovat?");
        }

        Optional<ButtonType> result = alert.showAndWait();
        return ((result.isPresent() && (result.get() == ButtonType.OK)));
    }

    public void novaPolozkaSportoviste() {
        if (upravujuPolozku || pridavamNovouPolozku) {
            if (!getConfirmationPopup("zrusit")) {
                return;
            }
        }

        if (zobrazujuPolozku) {
            update();
        }

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
        if(upravujuPolozku || pridavamNovouPolozku){
            if(!getConfirmationPopup("zrusit")){
                return;
            }
        }

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

        zobrazujuPolozku = true;
    }

    public void upravSportoviste(){
        upravujuPolozku = true;
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
        if(getConfirmationPopup("smazat")){
            app.removeSportoviste(aktualni.getIdSportoviste());
            update();
        }
    }

    public void zrusitSportoviste(){
        if(getConfirmationPopup("zrusit")){
            update();
        }
    }

}
