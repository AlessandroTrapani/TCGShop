package model;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Classe model che rappresenta un ordine effettuato da un utente.
 * 
 * Ogni oggetto Ordine corrisponde a un record della tabella ordini.
 * Contiene anche una lista di DettaglioOrdine, cioè i prodotti acquistati.
 */
public class Ordine {

    private int id;
    private int idUtente;
    private double totale;
    private String emailConsegna;
    private String indirizzoSpedizione;
    private String metodoPagamento;
    private String stato;
    private Timestamp dataOrdine;
    private ArrayList<DettaglioOrdine> dettagli;

    /**
     * Costruttore vuoto.
     * Inizializza la lista dei dettagli ordine.
     */
    public Ordine() {
        dettagli = new ArrayList<DettaglioOrdine>();
    }

    /**
     * Restituisce l'id dell'ordine.
     *
     * @return id dell'ordine
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'id dell'ordine.
     *
     * @param id id dell'ordine
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'id dell'utente che ha effettuato l'ordine.
     *
     * @return id utente
     */
    public int getIdUtente() {
        return idUtente;
    }

    /**
     * Imposta l'id dell'utente che ha effettuato l'ordine.
     *
     * @param idUtente id utente
     */
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    /**
     * Restituisce il totale dell'ordine.
     *
     * @return totale ordine
     */
    public double getTotale() {
        return totale;
    }

    /**
     * Imposta il totale dell'ordine.
     *
     * @param totale totale ordine
     */
    public void setTotale(double totale) {
        this.totale = totale;
    }

    /**
     * Restituisce l'email di consegna associata all'ordine.
     *
     * @return email di consegna
     */
    public String getEmailConsegna() {
        return emailConsegna;
    }

    /**
     * Imposta l'email di consegna associata all'ordine.
     *
     * @param emailConsegna email di consegna
     */
    public void setEmailConsegna(String emailConsegna) {
        this.emailConsegna = emailConsegna;
    }

    /**
     * Restituisce l'indirizzo di spedizione dell'ordine.
     *
     * @return indirizzo di spedizione
     */
    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    /**
     * Imposta l'indirizzo di spedizione dell'ordine.
     *
     * @param indirizzoSpedizione indirizzo di spedizione
     */
    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    /**
     * Restituisce il metodo di pagamento scelto.
     *
     * @return metodo di pagamento
     */
    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    /**
     * Imposta il metodo di pagamento scelto.
     *
     * @param metodoPagamento metodo di pagamento
     */
    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    /**
     * Restituisce lo stato dell'ordine.
     * Esempi: IN_ELABORAZIONE, SPEDITO, COMPLETATO, ANNULLATO.
     *
     * @return stato ordine
     */
    public String getStato() {
        return stato;
    }

    /**
     * Imposta lo stato dell'ordine.
     *
     * @param stato stato ordine
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la data in cui è stato effettuato l'ordine.
     *
     * @return data ordine
     */
    public Timestamp getDataOrdine() {
        return dataOrdine;
    }

    /**
     * Imposta la data in cui è stato effettuato l'ordine.
     *
     * @param dataOrdine data ordine
     */
    public void setDataOrdine(Timestamp dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    /**
     * Restituisce la lista dei prodotti acquistati nell'ordine.
     *
     * @return lista dettagli ordine
     */
    public ArrayList<DettaglioOrdine> getDettagli() {
        return dettagli;
    }

    /**
     * Imposta la lista dei prodotti acquistati nell'ordine.
     *
     * @param dettagli lista dettagli ordine
     */
    public void setDettagli(ArrayList<DettaglioOrdine> dettagli) {
        this.dettagli = dettagli;
    }
}