package logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="RozvrhovaAkce")
public class RozvrhovaAkce {
    @Id
    @Column(name="idRozvrhovaAkce")
    public Integer idRozvrhovaAkce;
    @Column(name="datum")
    public LocalDate datum;
    @Column(name="casOd")
    public String casOd;
    @Column(name="casDo")
    public String casDo;
    @Column(name="typLekce")
    public TypLekce typLekce;
    @Column(name="volnaMista")
    public Integer volnaMista;

    public RozvrhovaAkce() {
    }

    public enum TypLekce{
        aerobik,
        spinning,
        yoga,
        kruhovyTrenink,
        posilovani,
        pilates
    }

    public RozvrhovaAkce(Integer idRozvrhovaAkce, LocalDate datum, String casOd, String casDo, TypLekce typLekce, Integer volnaMista) {
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

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
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

    public TypLekce getTypLekce() {
        return typLekce;
    }

    public void setTypLekce(TypLekce typLekce) {
        this.typLekce = typLekce;
    }

    public Integer getVolnaMista() {
        return volnaMista;
    }

    public void setVolnaMista(Integer volnaMista) {
        this.volnaMista = volnaMista;
    }
}
