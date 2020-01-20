package logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name="Trener")
public class Trener {
    @Id
    @Column(name="idTrener")
    public Integer idTrener;
    @Column(name="jmeno")
    public String jmeno;
    @Column(name="telefon")
    public String telefon;
    @Column(name="email")
    public String email;
    @Column(name="datumNarozeni")
    public Date datumNarozeni;
    @Column(name="uvazek")
    public Integer uvazek;

    public Trener(Integer idTrener, String jmeno, String telefon, String email, Date datumNarozeni, Integer uvazek) {
        this.idTrener = idTrener;
        this.jmeno = jmeno;
        this.telefon = telefon;
        this.email = email;
        this.datumNarozeni = datumNarozeni;
        this.uvazek = uvazek;
    }

    public Trener() {
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

    public String getDatumNarozeni() throws ParseException {
        String datum = datumNarozeni.toString().substring(0,10);
        String dny = datum.substring(8,10);
        String mesic = datum.substring(5,7);
        String rok = datum.substring(0,4);
        return dny+"."+mesic+"."+rok;
    }

    public void setDatumNarozeni(Date datumNarozeni) {
        this.datumNarozeni = datumNarozeni;
    }

    public Integer getUvazek() {
        return uvazek;
    }

    public void setUvazek(Integer uvazek) {
        this.uvazek = uvazek;
    }
}
