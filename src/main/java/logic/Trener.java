package logic;

import java.text.DecimalFormat;

public class Trener {
    public Integer idTrener;
    public String jmeno;
    public String telefon;
    public String email;
    public String datumNarozeni;
    public Integer uvazek;

    public Trener(Integer idTrener, String jmeno, String telefon, String email, String datumNarozeni, Integer uvazek) {
        this.idTrener = idTrener;
        this.jmeno = jmeno;
        this.telefon = telefon;
        this.email = email;
        this.datumNarozeni = datumNarozeni;
        this.uvazek = uvazek;
    }

    public Integer getIdTrener() {
        return idTrener;
    }

    public void setIdTrener(Integer idTrener) {
        this.idTrener = idTrener;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDatumNarozeni() {
        return datumNarozeni;
    }

    public void setDatumNarozeni(String datumNarozeni) {
        this.datumNarozeni = datumNarozeni;
    }

    public Integer getUvazek() {
        return uvazek;
    }

    public void setUvazek(Integer uvazek) {
        this.uvazek = uvazek;
    }
}
