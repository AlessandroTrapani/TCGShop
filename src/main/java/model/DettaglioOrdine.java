package model;

/**
 * Classe model che rappresenta una riga di dettaglio di un ordine.
 * 
 * Ogni oggetto DettaglioOrdine corrisponde a un record della tabella
 * dettagli_ordine e rappresenta un prodotto acquistato in un ordine.
 */
public class DettaglioOrdine {

    private int id;
    private int idOrdine;
    private int idProdotto;
    private String nomeProdotto;
    private double prezzo;
    private int quantita;

    /**
     * Costruttore vuoto.
     * Permette di creare un dettaglio ordine e valorizzarlo tramite setter.
     */
    public DettaglioOrdine() {
    }

    /**
     * Restituisce l'id del dettaglio ordine.
     *
     * @return id dettaglio ordine
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'id del dettaglio ordine.
     *
     * @param id id dettaglio ordine
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce l'id dell'ordine a cui appartiene questo dettaglio.
     *
     * @return id ordine
     */
    public int getIdOrdine() {
        return idOrdine;
    }

    /**
     * Imposta l'id dell'ordine a cui appartiene questo dettaglio.
     *
     * @param idOrdine id ordine
     */
    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    /**
     * Restituisce l'id del prodotto acquistato.
     *
     * @return id prodotto
     */
    public int getIdProdotto() {
        return idProdotto;
    }

    /**
     * Imposta l'id del prodotto acquistato.
     *
     * @param idProdotto id prodotto
     */
    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    /**
     * Restituisce il nome storico del prodotto acquistato.
     * 
     * Il nome viene salvato nel dettaglio ordine per mantenere lo storico
     * anche se il prodotto viene modificato successivamente.
     *
     * @return nome prodotto acquistato
     */
    public String getNomeProdotto() {
        return nomeProdotto;
    }

    /**
     * Imposta il nome storico del prodotto acquistato.
     *
     * @param nomeProdotto nome prodotto acquistato
     */
    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    /**
     * Restituisce il prezzo storico del prodotto acquistato.
     * 
     * Il prezzo viene salvato nel dettaglio ordine per mantenere lo storico
     * anche se il prezzo del prodotto cambia successivamente.
     *
     * @return prezzo del prodotto al momento dell'acquisto
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo storico del prodotto acquistato.
     *
     * @param prezzo prezzo del prodotto al momento dell'acquisto
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce la quantità acquistata.
     *
     * @return quantità acquistata
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * Imposta la quantità acquistata.
     *
     * @param quantita quantità acquistata
     */
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    /**
     * Calcola il totale della riga di dettaglio.
     *
     * @return prezzo moltiplicato per quantità
     */
    public double getTotaleRiga() {
        return prezzo * quantita;
    }
}