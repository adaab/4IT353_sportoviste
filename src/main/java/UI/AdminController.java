package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.App;
import logic.Observer;
import logic.Sportoviste;
import logic.Trener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Trener aktualniTrener;

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
    public Button upravitTrener;
    @FXML
    public Button novaPolozkaTrener;
    @FXML
    public Button smazatTrener;
    @FXML
    public ListView seznamTrener;
    @FXML
    public Button ulozitTrener;
    @FXML
    public Button zrusitTrener;
    @FXML
    public TextField idTrener;
    @FXML
    public TextField jmenoTrener;
    @FXML
    public TextField telefonTrener;
    @FXML
    public TextField emailTrener;
    @FXML
    public TextField datumNarozeniTrener;
    @FXML
    public TextField uvazekTrener;
    
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

        //TRENERI TAB
        treneri = FXCollections.observableArrayList();
        app.getTreneri().forEach(t -> treneri.add(t.getIdTrener()+": "+t.getJmeno()));
        seznamTrener.setItems(treneri);

        zobrazujuPolozkuTrener = false;
        upravujuPolozkuTrener = false;
        pridavamNovouPolozkuTrener = false;
        aktualniTrener = null;
        idTrener.clear();
        jmenoTrener.clear();
        telefonTrener.clear();
        emailTrener.clear();
        datumNarozeniTrener.clear();
        uvazekTrener.clear();

        ulozitTrener.setDisable(true);
        zrusitTrener.setDisable(true);
        novaPolozkaTrener.setDisable(false);


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

    public void novaPolozkaTrener() {
        if (upravujuPolozkuTrener || pridavamNovouPolozkuTrener) {
            if (!getConfirmationPopup("zrusit")) {
                return;
            }
        }

        if (zobrazujuPolozkuTrener) {
            update();
        }

        pridavamNovouPolozkuTrener = true;

        idTrener.clear();
        jmenoTrener.clear();
        telefonTrener.clear();
        emailTrener.clear();
        datumNarozeniTrener.clear();
        uvazekTrener.clear();

        jmenoTrener.setDisable(false);
        telefonTrener.setDisable(false);
        emailTrener.setDisable(false);
        datumNarozeniTrener.setDisable(false);
        uvazekTrener.setDisable(false);

        ulozitTrener.setDisable(false);
        zrusitTrener.setDisable(false);

        upravitTrener.setDisable(true);
        smazatTrener.setDisable(true);

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

    public void ulozitTrener() {
        String jmeno = jmenoTrener.getText();
        String telefon = telefonTrener.getText();
        String email = emailTrener.getText();
        Date datumNarozeni = null;
        try {
            datumNarozeni = new SimpleDateFormat("dd.MM.yyyy").parse(datumNarozeniTrener.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer uvazek = Integer.parseInt(uvazekTrener.getText());

        if (jmeno.isEmpty() || telefon.isEmpty() || email.isEmpty() || datumNarozeniTrener.getText().isEmpty() || uvazekTrener.getText().isEmpty()) {
            //TODO vrátit chybu
        } else if (pridavamNovouPolozkuTrener) {
            app.novyTrener(jmeno, telefon, email, datumNarozeni, uvazek);
        } else {
            app.updateTrener(aktualniTrener.getIdTrener(), jmeno, telefon, email, datumNarozeni, uvazek);
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

    public void vyberTrenera(){
        if(upravujuPolozkuTrener || pridavamNovouPolozkuTrener){
            if(!getConfirmationPopup("zrusit")){
                return;
            }
        }

        String volba = String.valueOf(seznamTrener.getSelectionModel().getSelectedItem());
        String[] parsed = volba.split(": ");
        Integer id = Integer.parseInt(parsed[0]);
        aktualniTrener = app.getTrenerDetail(id);

        jmenoTrener.setDisable(true);
        telefonTrener.setDisable(true);
        emailTrener.setDisable(true);
        datumNarozeniTrener.setDisable(true);
        uvazekTrener.setDisable(true);

        idTrener.setText(aktualniTrener.getIdTrener().toString());
        jmenoTrener.setText(aktualniTrener.getJmeno());
        telefonTrener.setText(aktualniTrener.getTelefon());
        emailTrener.setText(aktualniTrener.getEmail());
        try {
            datumNarozeniTrener.setText(aktualniTrener.getDatumNarozeni());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        uvazekTrener.setText(aktualniTrener.getUvazek().toString());

        upravitTrener.setDisable(false);
        smazatTrener.setDisable(false);
        novaPolozkaTrener.setDisable(false);

        zobrazujuPolozkuTrener = true;
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

    public void upravTrener(){
        upravujuPolozkuTrener = true;
        jmenoTrener.setDisable(false);
        telefonTrener.setDisable(false);
        emailTrener.setDisable(false);
        datumNarozeniTrener.setDisable(false);
        uvazekTrener.setDisable(false);

        ulozitTrener.setDisable(false);
        zrusitTrener.setDisable(false);
        novaPolozkaTrener.setDisable(true);
        upravitTrener.setDisable(true);
        smazatTrener.setDisable(true);
    }

    public void smazSportoviste(){
        if(getConfirmationPopup("smazat")){
            app.removeSportoviste(aktualniSportoviste.getIdSportoviste());
            update();
        }
    }

    public void smazTrener(){
        if(getConfirmationPopup("smazat")){
            app.removeTrener(aktualniTrener.getIdTrener());
            update();
        }
    }

    public void zrusit(){
        if(getConfirmationPopup("zrusit")){
            update();
        }
    }

}
