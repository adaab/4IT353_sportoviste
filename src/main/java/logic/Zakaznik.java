package logic;

import javax.persistence.*;

@Entity
@Table(name="Zakaznik")
public class Zakaznik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idZakaznik")
    private Integer idZakaznik;
    @Column(name="heslo")
    private String heslo;
    @Column(name="email")
    private String email;
    @Column(name="jeAdmin")
    private Boolean jeAdmin;

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

    public Boolean getAdmin() {
        return jeAdmin;
    }

    public void setAdmin(Boolean admin) {
        jeAdmin = admin;
    }
}
