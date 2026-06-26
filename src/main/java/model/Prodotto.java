package model;

import java.sql.Timestamp;

/**
 * Classe che rappresenta un prodotto venduto su TCGShop.
 * 
 * Ogni oggetto Prodotto corrisponde a un record della tabella prodotti
 * del database.
 */
public class Prodotto {

    private int id;
    private String nome;
    private String gioco;
    private String categoria;
    private String rarita;
    private double prezzo;
    private int quantita;
    private String immagine;
    private String descrizione;
    private String stato;
    private Timestamp dataInserimento;

    /**
     * Costruttore vuoto.
     * Viene usato quando si vuole creare un oggetto Prodotto e valorizzare
     * i campi tramite i metodi setter.
     */
    public Prodotto() {
    }

    /**
     * Restituisce l'id del prodotto.
     * 
     * @return id del prodotto
     */
    public int getId() {
        return id;
    }

    /**
     * Imposta l'id del prodotto.
     * 
     * @param id id del prodotto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Restituisce il nome del prodotto.
     * 
     * @return nome del prodotto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del prodotto.
     * 
     * @param nome nome del prodotto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il gioco a cui appartiene il prodotto.
     * Esempi: Pokémon, Magic, One Piece, Accessori.
     * 
     * @return gioco del prodotto
     */
    public String getGioco() {
        return gioco;
    }

    /**
     * Imposta il gioco del prodotto.
     * 
     * @param gioco gioco del prodotto
     */
    public void setGioco(String gioco) {
        this.gioco = gioco;
    }

    /**
     * Restituisce la categoria del prodotto.
     * Esempi: Box, Bustina, Carta singola, Mazzo, Accessori.
     * 
     * @return categoria del prodotto
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Imposta la categoria del prodotto.
     * 
     * @param categoria categoria del prodotto
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Restituisce la rarità del prodotto, se presente.
     * 
     * @return rarità del prodotto
     */
    public String getRarita() {
        return rarita;
    }

    /**
     * Imposta la rarità del prodotto.
     * 
     * @param rarita rarità del prodotto
     */
    public void setRarita(String rarita) {
        this.rarita = rarita;
    }

    /**
     * Restituisce il prezzo del prodotto.
     * 
     * @return prezzo del prodotto
     */
    public double getPrezzo() {
        return prezzo;
    }

    /**
     * Imposta il prezzo del prodotto.
     * 
     * @param prezzo prezzo del prodotto
     */
    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * Restituisce la quantità disponibile a magazzino.
     * 
     * @return quantità disponibile
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * Imposta la quantità disponibile a magazzino.
     * 
     * @param quantita quantità disponibile
     */
    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    /**
     * Restituisce il nome del file immagine associato al prodotto.
     * 
     * @return nome file immagine
     */
    public String getImmagine() {
        return immagine;
    }

    /**
     * Imposta il nome del file immagine associato al prodotto.
     * 
     * @param immagine nome file immagine
     */
    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    /**
     * Restituisce la descrizione del prodotto.
     * 
     * @return descrizione del prodotto
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta la descrizione del prodotto.
     * 
     * @param descrizione descrizione del prodotto
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce lo stato del prodotto.
     * Esempi: DISPONIBILE, NON_DISPONIBILE, ELIMINATO.
     * 
     * @return stato del prodotto
     */
    public String getStato() {
        return stato;
    }

    /**
     * Imposta lo stato del prodotto.
     * 
     * @param stato stato del prodotto
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la data di inserimento del prodotto.
     * 
     * @return data di inserimento
     */
    public Timestamp getDataInserimento() {
        return dataInserimento;
    }

    /**
     * Imposta la data di inserimento del prodotto.
     * 
     * @param dataInserimento data di inserimento
     */
    public void setDataInserimento(Timestamp dataInserimento) {
        this.dataInserimento = dataInserimento;
    }
}