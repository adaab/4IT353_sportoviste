package logic;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="RozvrhovaAkce")
public class RozvrhovaAkce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idRozvrhovaAkce")
    public Integer idRozvrhovaAkce;
    @Column(name="datum")
    public Date datum;
    @Column(name="casOd")
    public String casOd;
    @Column(name="casDo")
    public String casDo;
    @Column(name="typLekce")
    public String typLekce;
    @Column(name="volnaMista")
    public Integer volnaMista;
    @Column(name="Trener_idTrener")
    public Integer idTrener;
    @Column(name="Sportoviste_idSportoviste")
    public Integer idSportoviste;

    public RozvrhovaAkce() {
    }

    /*
    public enum TypLekce{
        aerobik,
        spinning,
        yoga,
        kruhovyTrenink,
        posilovani,
        pilates
    }*/

    public RozvrhovaAkce(Integer idRozvrhovaAkce, Date datum, String casOd, String casDo, String typLekce, Integer volnaMista) {
        this.idRozvrhovaAkce = idRozvrhovaAkce;
        this.datum = datum;
        this.casOd = casOd;
        this.casDo = casDo;
        this.typLekce = typLekce;
        this.volnaMista = volnaMista;
    }

    public Integer getIdRozvrhovaAkce() {
        return idRozvrhovaAkce;
    }

    public void setIdRozvrhovaAkce(Integer idRozvrhovaAkce) {
        this.idRozvrhovaAkce = idRozvrhovaAkce;
    }

    public String getDatum() {
        String datum2 = datum.toString().substring(0,10);
        String dny = datum2.substring(8,10);
        String mesic = datum2.substring(5,7);
        String rok = datum2.substring(0,4);
        return dny+"."+mesic+"."+rok;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getCasOd() {
        return casOd;
    }

    public void setCasOd(String casOd) {
        this.casOd = casOd;
    }

    public String getCasDo() {
        return casDo;
    }

    public void setCasDo(String casDo) {
        this.casDo = casDo;
    }

    public String getTypLekce() {
        return typLekce;
    }

    public void setTypLekce(String typLekce) {
        this.typLekce = typLekce;
    }

    public Integer getVolnaMista() {
        return volnaMista;
    }

    public void setVolnaMista(Integer volnaMista) {
        this.volnaMista = volnaMista;
    }

    public Integer getIdTrener() {
        return idTrener;
    }

    public void setIdTrener(Integer idTrener) {
        this.idTrener = idTrener;
    }

    public Integer getIdSportoviste() {
        return idSportoviste;
    }

    public void setIdSportoviste(Integer idSportoviste) {
        this.idSportoviste = idSportoviste;
    }
}
