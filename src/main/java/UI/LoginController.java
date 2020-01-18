package UI;

import logic.App;
import logic.Observer;

public class LoginController implements Observer {
    public App app;

    @Override
    public void update() {
    }

    public void inicializuj(App app) {
        this.app = app;
        app.register(this);
        update();
    }

}
