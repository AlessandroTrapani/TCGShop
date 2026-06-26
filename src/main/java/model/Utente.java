package model;

import java.sql.Timestamp;

/**
 * Classe che rappresenta un utente registrato su TCGShop.
 * 
 * Ogni oggetto Utente corrisponde a un record della tabella utenti
 * del database.
 */
public class Utente {

    private int id;
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String ruolo;
    private Timestamp dataRegistrazione;

    /**
     * Costruttore vuoto.
     * Viene usato quando si vuole creare un oggetto Utente e valorizzare
     * i campi tramite i metodi setter.
     */
    public Utente() {
    }

    /**
     * Restituisce l'id dell'utente.
     * 
     * @return id dell'utente
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'id dell'utente.
     * 
     * @param id id dell'utente
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce il nome dell'utente.
     * 
     * @return nome dell'utente
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'utente.
     * 
     * @param nome nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     * 
     * @return cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'utente.
     * 
     * @param cognome cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce l'email dell'utente.
     * 
     * @return email dell'utente
     */
    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email dell'utente.
     * 
     * @param email email dell'utente
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Restituisce la password dell'utente.
     * In questo progetto didattico viene gestita in modo semplice.
     * 
     * @return password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     * 
     * @param password password dell'utente
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Restituisce il ruolo dell'utente.
     * Esempi: UTENTE, ADMIN.
     * 
     * @return ruolo dell'utente
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * Imposta il ruolo dell'utente.
     * 
     * @param ruolo ruolo dell'utente
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Restituisce la data di registrazione dell'utente.
     * 
     * @return data di registrazione
     */
    public Timestamp getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * Imposta la data di registrazione dell'utente.
     * 
     * @param dataRegistrazione data di registrazione
     */
    public void setDataRegistrazione(Timestamp dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
}