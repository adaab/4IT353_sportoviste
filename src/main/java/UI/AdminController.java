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
    private ObservableList<String> treneri;

    private Boolean pridavamNovouPolozkuSportoviste = false;
    private Boolean upravujuPolozkuSportoviste = false;
    private Boolean zobrazujuPolozkuSportoviste = false;
    private Sportoviste aktualniSportoviste;

    private Boolean pridavamNovouPolozkuTrener = false;
    private Boolean upravujuPolozkuTrener = false;
    private Boolean zobrazujuPolozkuTrener = false;
    private Sportoviste aktualniTrener;

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

        zobrazujuPolozkuSportoviste = false;
        upravujuPolozkuSportoviste = false;
        pridavamNovouPolozkuSportoviste = false;
        aktualniSportoviste = null;
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
        if (upravujuPolozkuSportoviste || pridavamNovouPolozkuSportoviste) {
            if (!getConfirmationPopup("zrusit")) {
                return;
            }
        }

        if (zobrazujuPolozkuSportoviste) {
            update();
        }

        pridavamNovouPolozkuSportoviste = true;
        aktualniSportoviste = null;

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
        } else if (pridavamNovouPolozkuSportoviste) {
            app.noveSportoviste(nazev, povrch, rozmery);
            } else {
            app.updateSportoviste(aktualniSportoviste.getIdSportoviste(), nazev, povrch, rozmery);
        }

        update();
    }

    public void vyberSportoviste(){
        if(upravujuPolozkuSportoviste || pridavamNovouPolozkuSportoviste){
            if(!getConfirmationPopup("zrusit")){
                return;
            }
        }

        String volba = String.valueOf(seznamSportoviste.getSelectionModel().getSelectedItem());
        String[] parsed = volba.split(": ");
        Integer id = Integer.parseInt(parsed[0]);
        aktualniSportoviste = app.getSportovisteDetail(id);

        nazevSportoviste.setDisable(true);
        povrchSportoviste.setDisable(true);
        rozmerySportoviste.setDisable(true);

        idSportoviste.setText(aktualniSportoviste.getIdSportoviste().toString());
        nazevSportoviste.setText(aktualniSportoviste.getNazev());
        povrchSportoviste.setText(aktualniSportoviste.getPovrch());
        rozmerySportoviste.setText(aktualniSportoviste.getRozmery());

        upravitSportoviste.setDisable(false);
        smazatSportoviste.setDisable(false);
        novaPolozkaSportoviste.setDisable(false);

        zobrazujuPolozkuSportoviste = true;
    }

    public void upravSportoviste(){
        upravujuPolozkuSportoviste = true;
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
            app.removeSportoviste(aktualniSportoviste.getIdSportoviste());
            update();
        }
    }

    public void zrusitSportoviste(){
        if(getConfirmationPopup("zrusit")){
            update();
        }
    }

}
