package UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import logic.App;
import logic.Observer;

import java.io.IOException;

public class LoginController implements Observer {
    public App app;

    @FXML
    public TextField jmeno;
    @FXML
    public PasswordField heslo;
    @FXML
    public Button vstoupit;
    @FXML
    public Button novaRegistrace;
    @FXML
    public Label chyba;
    @FXML
    public CheckBox administrator;
    @FXML
    public GridPane loginPane;
    @FXML
    public GridPane signinPane;
    @FXML
    public PasswordField hesloKontrola;
    @FXML
    public Button zpet;

    @Override
    public void update() {
    }

    public void inicializuj(App app) {
        this.app = app;
        app.register(this);
        update();
    }

    public void vstoupit() throws IOException {
        String emailUzivatele = jmeno.getText();
        String hesloUzivatele = heslo.getText();
        Boolean jeAdmin = administrator.isSelected();

        if (emailUzivatele.isEmpty() || hesloUzivatele.isEmpty()){
            chyba.setVisible(true);
            chyba.setText("Prosím, vyplňte všechna pole.");
        } else {
            if(app.overZakaznika(emailUzivatele,hesloUzivatele,jeAdmin)){
                if (jeAdmin) {
                    app.initAdminScreen();
                } else {
                    app.initCommonScreen();
                }
            } else {
                jmeno.clear();
                heslo.clear();
                chyba.setVisible(true);
                chyba.setText("Přihlašovací údaje nejsou správné, prosím zkuste to znovu.");
            }
        }
    }

    public void novaRegistrace(){
        loginPane.setVisible(false);
        signinPane.setVisible(true);
        zpet.setVisible(true);
        chyba.setVisible(false);
    }

    public void zpetNaPrihlaseni(){
        loginPane.setVisible(true);
        signinPane.setVisible(false);
        zpet.setVisible(false);
    }

    public void zaregistrovat(){
        String emailUzivatele = jmeno.getText();
        String hesloUzivatele = heslo.getText();
        String hesloUzivateleKontrola = hesloKontrola.getText();

        if(app.zaregistrujZakaznika(emailUzivatele, hesloUzivatele, hesloUzivateleKontrola)){
            app.initCommonScreen();
        } else {
            chyba.setVisible(true);
            chyba.setText("Uživatel již existuje, nebo se neshodují hesla.");
        }
    }

    public void konec(){
        app.close();
    }
}
