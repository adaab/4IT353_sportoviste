package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import logic.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdminController implements Observer {
    private App app;
    private ObservableList<String> sportoviste;
    private ObservableList<String> treneri;
    private ObservableList<String> akce;

    private ArrayList<TextField> sportovistePole;
    private ArrayList<TextField> treneriPole;
    private ArrayList<ComboBox> akceComboBoxy;

    private Boolean pridavamNovouPolozkuSportoviste = false;
    private Boolean upravujuPolozkuSportoviste = false;
    private Boolean zobrazujuPolozkuSportoviste = false;
    private Sportoviste aktualniSportoviste;

    private Boolean pridavamNovouPolozkuTrener = false;
    private Boolean upravujuPolozkuTrener = false;
    private Boolean zobrazujuPolozkuTrener = false;
    private Trener aktualniTrener;

    private Boolean pridavamNovouPolozkuAkce = false;
    private Boolean upravujuPolozkuAkce = false;
    private Boolean zobrazujuPolozkuAkce = false;
    private RozvrhovaAkce aktualniAkce;
    private int aktualniTyden;

    private ObservableList<String> hodiny;

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

    @FXML
    public Button upravitRozvrh;
    @FXML
    public Button novaPolozkaRozvrh;
    @FXML
    public Button smazatRozvrh;
    @FXML
    public TableView rozvrh;
    @FXML
    public TableColumn sloupecDny;
    @FXML
    public TableColumn sloupec12;
    @FXML
    public TableColumn sloupec13;
    @FXML
    public TableColumn sloupec14;
    @FXML
    public TableColumn sloupec15;
    @FXML
    public TableColumn sloupec16;
    @FXML
    public TableColumn sloupec17;
    @FXML
    public TableColumn sloupec18;
    @FXML
    public TableColumn sloupec19;
    @FXML
    public Button ulozitRozvrh;
    @FXML
    public Button zrusitRozvrh;
    @FXML
    public TextField idRozvrhovaAkce;
    @FXML
    public TextField typLekce;
    @FXML
    public DatePicker datumRozvrhovaAkce;
    @FXML
    public TextField volnaMista;
    @FXML
    public ComboBox odRozvrhovaAkce;
    @FXML
    public ComboBox doRozvrhovaAkce;
    @FXML
    public ComboBox trenerRozvrhovaAkce;
    @FXML
    public ComboBox sportovisteRozvrhovaAkce;
    @FXML
    public Button nasledujiciTyden;
    @FXML
    public Button predchoziTyden;
    @FXML
    public ListView seznamAkci;
    @FXML
    public Pane loaderPane;
    @FXML
    public Label loaderLabel;

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

        sportovistePole.forEach(s -> s.clear());
        sportovistePole.forEach(s -> s.setDisable(true));

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

        treneriPole.forEach(t -> t.clear());
        treneriPole.forEach(t -> t.setDisable(true));

        ulozitTrener.setDisable(true);
        zrusitTrener.setDisable(true);
        novaPolozkaTrener.setDisable(false);

        //AKCE TAB
        akce = FXCollections.observableArrayList();
        app.getRozvrhoveAkce().forEach(a -> akce.add(a.getIdRozvrhovaAkce()+": "+a.getDatum()+" "+a.getCasOd()+"-"+a.getCasDo()+": "+a.getTypLekce()));
        seznamAkci.setItems(akce);

        zobrazujuPolozkuAkce = false;
        upravujuPolozkuAkce = false;
        pridavamNovouPolozkuAkce = false;
        aktualniAkce = null;

        idRozvrhovaAkce.clear();
        typLekce.clear();
        datumRozvrhovaAkce.getEditor().clear();
        volnaMista.clear();

        akceComboBoxy.forEach(c -> c.setValue(null));
        akceComboBoxy.forEach(c -> c.setDisable(true));

        ulozitRozvrh.setDisable(true);
        zrusitRozvrh.setDisable(true);
        novaPolozkaRozvrh.setDisable(false);

        nasledujiciTyden.setDisable(false);
        predchoziTyden.setDisable(false);

        hodiny = FXCollections.observableArrayList();
        hodiny.addAll("12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00");
        odRozvrhovaAkce.setItems(hodiny);
        doRozvrhovaAkce.setItems(hodiny);
        trenerRozvrhovaAkce.setItems(treneri);
        sportovisteRozvrhovaAkce.setItems(sportoviste);

    }

    public void inicializuj(App app){
        this.app = app;
        app.register(this);

        sportovistePole = new ArrayList<>();
        sportovistePole.add(idSportoviste);
        sportovistePole.add(nazevSportoviste);
        sportovistePole.add(povrchSportoviste);
        sportovistePole.add(rozmerySportoviste);

        treneriPole = new ArrayList<>();
        treneriPole.add(idTrener);
        treneriPole.add(jmenoTrener);
        treneriPole.add(telefonTrener);
        treneriPole.add(emailTrener);
        treneriPole.add(datumNarozeniTrener);
        treneriPole.add(uvazekTrener);

        akceComboBoxy = new ArrayList<>();
        akceComboBoxy.add(odRozvrhovaAkce);
        akceComboBoxy.add(doRozvrhovaAkce);
        akceComboBoxy.add(trenerRozvrhovaAkce);
        akceComboBoxy.add(sportovisteRozvrhovaAkce);

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

    public void vratChybu(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Upozornění");
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void connectionLostBegin(String text){

    }

    public void connectionLostEnd(){

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

    public void novaPolozkaRozvrh() {
        if (upravujuPolozkuAkce || pridavamNovouPolozkuAkce) {
            if (!getConfirmationPopup("zrusit")) {
                return;
            }
        }

        if (zobrazujuPolozkuAkce) {
            update();
        }

        pridavamNovouPolozkuAkce = true;

        typLekce.setDisable(false);
        datumRozvrhovaAkce.setDisable(false);
        volnaMista.setDisable(false);
        odRozvrhovaAkce.setDisable(false);
        doRozvrhovaAkce.setDisable(false);
        trenerRozvrhovaAkce.setDisable(false);
        sportovisteRozvrhovaAkce.setDisable(false);

        ulozitRozvrh.setDisable(false);
        zrusitRozvrh.setDisable(false);

        upravitRozvrh.setDisable(true);
        smazatRozvrh.setDisable(true);
        nasledujiciTyden.setDisable(true);
        predchoziTyden.setDisable(true);
    }

    public void ulozitSportoviste() {
        String nazev = nazevSportoviste.getText();
        String povrch = povrchSportoviste.getText();
        String rozmery = rozmerySportoviste.getText();

        if (nazev.isEmpty() || povrch.isEmpty() || rozmery.isEmpty()) {
            vratChybu("Prosím, vyplňte všechna pole");
            return;
        } else if (pridavamNovouPolozkuSportoviste) {
            app.noveSportoviste(nazev, povrch, rozmery);
            } else {
            app.updateSportoviste(aktualniSportoviste.getIdSportoviste(), nazev, povrch, rozmery);
        }

        update();
    }

    public void ulozitTrener() {
        if(!datumNarozeniTrener.getText().matches("\\d{2}\\.\\d{2}\\.\\d{4}")){
            vratChybu("Prosím, vložte datum narození ve formátu dd.MM.yyyy");
            return;
        }

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
            vratChybu("Prosím, vyplňte všechna pole");
            return;
        } else if (pridavamNovouPolozkuTrener) {
            app.novyTrener(jmeno, telefon, email, datumNarozeni, uvazek);
        } else {
            app.updateTrener(aktualniTrener.getIdTrener(), jmeno, telefon, email, datumNarozeni, uvazek);
        }

        update();
    }

    public void ulozitRozvrh() {
        String lekce = typLekce.getText();
        Date datum = null;
        try {
            datum = new SimpleDateFormat("dd.MM.yyyy").parse(datumRozvrhovaAkce.getEditor().getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Integer mista = Integer.parseInt(volnaMista.getText());
        String casOd = odRozvrhovaAkce.getValue().toString();
        String casDo = doRozvrhovaAkce.getValue().toString();

        String trener = trenerRozvrhovaAkce.getValue().toString();
        String[] parsed = trener.split(": ");
        Integer idTrener = Integer.parseInt(parsed[0]);

        String sportoviste = sportovisteRozvrhovaAkce.getValue().toString();
        String[] parsed2 = sportoviste.split(": ");
        Integer idSportoviste = Integer.parseInt(parsed[0]);


        if (lekce.isEmpty() || datumRozvrhovaAkce.getEditor().getText().isEmpty() || mista.toString().isEmpty() || casOd.isEmpty() || casDo.isEmpty() || trener.isEmpty() || sportoviste.isEmpty()){
            vratChybu("Prosím, vyplňte všechna pole");
            return;
        } else if (pridavamNovouPolozkuAkce) {
            app.novaRozvrhovaAkce(lekce, datum, casOd, casDo, mista, idTrener, idSportoviste);
        } else {
            app.updateRozvrhovaAkce(aktualniAkce.getIdRozvrhovaAkce(), lekce, datum, casOd, casDo, mista, idTrener, idSportoviste);
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

    public void vyberAkci(){
        if(upravujuPolozkuAkce || pridavamNovouPolozkuAkce){
            if(!getConfirmationPopup("zrusit")){
                return;
            }
        }

        String volba = String.valueOf(seznamAkci.getSelectionModel().getSelectedItem());
        String[] parsed = volba.split(": ");
        Integer id = Integer.parseInt(parsed[0]);
        aktualniAkce = app.getRozvrhoveAkceDetail(id);

        akceComboBoxy.forEach(a -> a.setDisable(true));
        typLekce.setDisable(true);
        datumRozvrhovaAkce.setDisable(true);
        volnaMista.setDisable(true);

        idRozvrhovaAkce.setText(aktualniAkce.getIdRozvrhovaAkce().toString());
        typLekce.setText(aktualniAkce.getTypLekce());
        datumRozvrhovaAkce.getEditor().setText(aktualniAkce.getDatum());
        odRozvrhovaAkce.setValue(aktualniAkce.getCasOd());
        doRozvrhovaAkce.setValue(aktualniAkce.getCasDo());
        volnaMista.setText(aktualniAkce.getVolnaMista().toString());
        sportovisteRozvrhovaAkce.setValue(aktualniAkce.getIdSportoviste()+": "+app.getSportovisteDetail(aktualniAkce.getIdSportoviste()).getNazev());
        trenerRozvrhovaAkce.setValue(aktualniAkce.getIdTrener()+": "+app.getTrenerDetail(aktualniAkce.getIdTrener()).getJmeno());

        upravitRozvrh.setDisable(false);
        smazatRozvrh.setDisable(false);
        novaPolozkaRozvrh.setDisable(false);

        zobrazujuPolozkuAkce = true;
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

    public void upravAkce(){
        upravujuPolozkuAkce = true;
        typLekce.setDisable(false);
        datumRozvrhovaAkce.setDisable(false);
        odRozvrhovaAkce.setDisable(false);
        doRozvrhovaAkce.setDisable(false);
        volnaMista.setDisable(false);
        trenerRozvrhovaAkce.setDisable(false);
        sportovisteRozvrhovaAkce.setDisable(false);

        ulozitRozvrh.setDisable(false);
        zrusitRozvrh.setDisable(false);
        novaPolozkaRozvrh.setDisable(true);
        upravitRozvrh.setDisable(true);
        smazatRozvrh.setDisable(true);
        nasledujiciTyden.setDisable(true);
        predchoziTyden.setDisable(true);
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

    public void smazAkce(){
        if(getConfirmationPopup("smazat")){
            app.removeRozvrhovaAkce(aktualniAkce.getIdRozvrhovaAkce());
            update();
        }
    }

    public void zrusit(){
        if(getConfirmationPopup("zrusit")){
            update();
        }
    }

    public void nasledujiciTyden(){
        aktualniTyden++;
        update();
    }

    public void pristiTyden(){
        aktualniTyden--;
        update();
    }

    public void konec(){
        if(getConfirmationPopup("zrusit")) {
            app.close();
        }
    }
}
