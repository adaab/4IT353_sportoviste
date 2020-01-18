package logic;

public class Sportoviste {
    public Integer idSportoviste;
    public String nazev;
    public String povrch;
    public String rozmery;

    public Sportoviste(Integer idSportoviste, String nazev, String povrch, String rozmery) {
        this.idSportoviste = idSportoviste;
        this.nazev = nazev;
        this.povrch = povrch;
        this.rozmery = rozmery;
    }

    public Integer getIdSportoviste() {
        return idSportoviste;
    }

    public void setIdSportoviste(Integer idSportoviste) {
        this.idSportoviste = idSportoviste;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getPovrch() {
        return povrch;
    }

    public void setPovrch(String povrch) {
        this.povrch = povrch;
    }

    public String getRozmery() {
        return rozmery;
    }

    public void setRozmery(String rozmery) {
        this.rozmery = rozmery;
    }
}


