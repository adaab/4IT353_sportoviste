package logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Zakaznik")
public class Zakaznik {
    @Id
    @Column(name="idZakaznik")
    public Integer idZakaznik;
    @Column(name="heslo")
    public String heslo;
    @Column(name="email")
    public String email;

    public Zakaznik(){
    }

    public Zakaznik(Integer idZakaznik, String email, String heslo) {
        this.idZakaznik = idZakaznik;
        this.email = email;
        this.heslo = heslo;
    }

    public Integer getIdZakaznik() {
        return idZakaznik;
    }

    public void setIdZakaznik(Integer idZakaznik) {
        this.idZakaznik = idZakaznik;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }
}
