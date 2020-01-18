package UI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import logic.App;
import logic.Observer;

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

    @Override
    public void update() {
    }

    public void inicializuj(App app) {
        this.app = app;
        app.register(this);
        update();
    }

    public void vstoupit(){
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

    public void novaRegistrace(){}
}
