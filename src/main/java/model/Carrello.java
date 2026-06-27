package model;

import java.util.ArrayList;

/**
 * Classe model che rappresenta il carrello dell'utente.
 * 
 * Il carrello viene salvato in sessione e contiene una lista di elementi.
 * Ogni elemento contiene un prodotto e la relativa quantità.
 */
public class Carrello {

    private ArrayList<ElementoCarrello> elementi;

    /**
     * Costruttore del carrello.
     * Inizializza la lista degli elementi come ArrayList vuoto.
     */
    public Carrello() {
        elementi = new ArrayList<ElementoCarrello>();
    }

    /**
     * Restituisce la lista degli elementi presenti nel carrello.
     *
     * @return lista degli elementi del carrello
     */
    public ArrayList<ElementoCarrello> getElementi() {
        return elementi;
    }

    /**
     * Imposta la lista degli elementi del carrello.
     *
     * @param elementi lista degli elementi del carrello
     */
    public void setElementi(ArrayList<ElementoCarrello> elementi) {
        this.elementi = elementi;
    }

    /**
     * Aggiunge un prodotto al carrello.
     * 
     * Se il prodotto è già presente, aumenta la quantità senza superare
     * la disponibilità massima del prodotto.
     * Se non è presente, crea una nuova riga nel carrello.
     *
     * @param prodotto prodotto da aggiungere
     * @param quantita quantità da aggiungere
     */
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        ElementoCarrello elementoEsistente = trovaElemento(prodotto.getId());

        /*
         * La quantità disponibile viene letta dal prodotto recuperato dal database.
         * Serve per evitare che nel carrello ci siano più pezzi di quelli disponibili.
         */
        int quantitaDisponibile = prodotto.getQuantita();

        if (elementoEsistente != null) {
            int nuovaQuantita = elementoEsistente.getQuantita() + quantita;

            /*
             * Se la nuova quantità supera la disponibilità,
             * la limito alla quantità massima disponibile.
             */
            if (nuovaQuantita > quantitaDisponibile) {
                nuovaQuantita = quantitaDisponibile;
            }

            elementoEsistente.setQuantita(nuovaQuantita);
        } else {
            /*
             * Anche quando il prodotto viene aggiunto per la prima volta,
             * controllo che la quantità richiesta non superi la disponibilità.
             */
            if (quantita > quantitaDisponibile) {
                quantita = quantitaDisponibile;
            }

            ElementoCarrello nuovoElemento = new ElementoCarrello(prodotto, quantita);
            elementi.add(nuovoElemento);
        }
    }

    /**
     * Aggiorna la quantità di un prodotto già presente nel carrello.
     * 
     * Se la nuova quantità è minore o uguale a zero, il prodotto viene rimosso.
     * Se la nuova quantità supera la disponibilità del prodotto, viene limitata
     * alla quantità massima disponibile.
     *
     * @param idProdotto id del prodotto da aggiornare
     * @param quantita nuova quantità
     */
    public void aggiornaQuantita(int idProdotto, int quantita) {
        ElementoCarrello elemento = trovaElemento(idProdotto);

        if (elemento != null) {
            if (quantita <= 0) {
                rimuoviProdotto(idProdotto);
            } else {
                int quantitaDisponibile = elemento.getProdotto().getQuantita();

                if (quantita > quantitaDisponibile) {
                    quantita = quantitaDisponibile;
                }

                elemento.setQuantita(quantita);
            }
        }
    }

    /**
     * Rimuove un prodotto dal carrello tramite id.
     *
     * @param idProdotto id del prodotto da rimuovere
     */
    public void rimuoviProdotto(int idProdotto) {
        ElementoCarrello elementoDaRimuovere = trovaElemento(idProdotto);

        if (elementoDaRimuovere != null) {
            elementi.remove(elementoDaRimuovere);
        }
    }

    /**
     * Svuota completamente il carrello.
     */
    public void svuota() {
        elementi.clear();
    }

    /**
     * Controlla se il carrello è vuoto.
     *
     * @return true se non ci sono elementi, false altrimenti
     */
    public boolean isVuoto() {
        return elementi.isEmpty();
    }

    /**
     * Calcola il totale complessivo del carrello.
     *
     * @return totale del carrello
     */
    public double getTotale() {
        double totale = 0;

        for (ElementoCarrello elemento : elementi) {
            totale += elemento.getTotaleRiga();
        }

        return totale;
    }
    
    /**
     * Restituisce la quantità di un determinato prodotto già presente nel carrello.
     * 
     * Questo metodo serve, ad esempio, nella pagina dettaglio prodotto per capire
     * quanti pezzi l'utente ha già aggiunto e quanti può ancora aggiungere.
     *
     * @param idProdotto id del prodotto da cercare nel carrello
     * @return quantità già presente nel carrello, oppure 0 se il prodotto non è presente
     */
    public int getQuantitaProdotto(int idProdotto) {
        ElementoCarrello elemento = trovaElemento(idProdotto);

        if (elemento != null) {
            return elemento.getQuantita();
        }

        return 0;
    }

    /**
     * Cerca nel carrello un elemento tramite id prodotto.
     * 
     * Questo metodo è privato perché serve solo alla logica interna
     * della classe Carrello.
     *
     * @param idProdotto id del prodotto da cercare
     * @return elemento trovato oppure null
     */
    private ElementoCarrello trovaElemento(int idProdotto) {
        for (ElementoCarrello elemento : elementi) {
            if (elemento.getProdotto().getId() == idProdotto) {
                return elemento;
            }
        }

        return null;
    }
}