package model;

/**
 * Classe model che rappresenta una singola riga del carrello.
 * 
 * Ogni elemento del carrello contiene un prodotto e la quantità scelta
 * dall'utente.
 */
public class ElementoCarrello {

    private Prodotto prodotto;
    private int quantita;

    /**
     * Costruttore vuoto.
     * Permette di creare un elemento carrello e valorizzarlo tramite setter.
     */
    public ElementoCarrello() {
    }

    /**
     * Costruttore con parametri.
     * Permette di creare subito un elemento carrello con prodotto e quantità.
     *
     * @param prodotto prodotto aggiunto al carrello
     * @param quantita quantità scelta dall'utente
     */
    public ElementoCarrello(Prodotto prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    /**
     * Restituisce il prodotto associato a questa riga del carrello.
     *
     * @return prodotto nel carrello
     */
    public Prodotto getProdotto() {
        return prodotto;
    }

    /**
     * Imposta il prodotto associato a questa riga del carrello.
     *
     * @param prodotto prodotto da associare
     */
    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    /**
     * Restituisce la quantità scelta dall'utente.
     *
     * @return quantità del prodotto nel carrello
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * Imposta la quantità scelta dall'utente.
     *
     * @param quantita quantità del prodotto nel carrello
     */
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    /**
     * Calcola il totale della singola riga del carrello.
     * 
     * Il totale viene calcolato moltiplicando il prezzo del prodotto
     * per la quantità scelta.
     *
     * @return totale della riga
     */
    public double getTotaleRiga() {
        return prodotto.getPrezzo() * quantita;
    }
}
