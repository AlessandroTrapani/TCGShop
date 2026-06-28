package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Carrello;
import model.DettaglioOrdine;
import model.ElementoCarrello;
import model.Ordine;
import util.ConnessioneDatabase;

/**
 * DAO dedicato alla gestione degli ordini.
 * Contiene le query SQL per salvare ordini, dettagli ordine
 * e recuperare lo storico degli acquisti.
 */
public class OrdineDAO {

    /**
     * Salva un ordine nel database partendo dai dati dell'ordine
     * e dal carrello dell'utente.
     *
     * Il metodo esegue tre operazioni principali:
     * 1. inserisce una riga nella tabella ordini;
     * 2. inserisce una riga in dettagli_ordine per ogni prodotto nel carrello;
     * 3. aggiorna la quantità disponibile dei prodotti acquistati.
     *
     * @param ordine oggetto Ordine con i dati generali dell'acquisto
     * @param carrello carrello contenente i prodotti acquistati
     * @return true se il salvataggio è riuscito, false altrimenti
     */
    public boolean salvaOrdine(Ordine ordine, Carrello carrello) {
        boolean salvato = false;

        String sqlOrdine = "INSERT INTO ordini "
                + "(id_utente, totale, email_consegna, indirizzo_spedizione, metodo_pagamento, stato) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection connessione = null;

        try {
            connessione = ConnessioneDatabase.getConnessione();

            /*
             * Disattivo l'autocommit perché il salvataggio dell'ordine
             * comprende più query collegate tra loro.
             * Se una query fallisce, annullo tutto con rollback.
             */
            connessione.setAutoCommit(false);

            PreparedStatement statementOrdine = connessione.prepareStatement(
                    sqlOrdine,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            statementOrdine.setInt(1, ordine.getIdUtente());
            statementOrdine.setDouble(2, ordine.getTotale());
            statementOrdine.setString(3, ordine.getEmailConsegna());
            statementOrdine.setString(4, ordine.getIndirizzoSpedizione());
            statementOrdine.setString(5, ordine.getMetodoPagamento());
            statementOrdine.setString(6, ordine.getStato());

            int righeOrdine = statementOrdine.executeUpdate();

            if (righeOrdine > 0) {
                ResultSet chiaviGenerate = statementOrdine.getGeneratedKeys();

                if (chiaviGenerate.next()) {
                    int idOrdine = chiaviGenerate.getInt(1);

                    /*
                     * Salvo tutti i prodotti del carrello nella tabella dettagli_ordine
                     * e aggiorno la quantità residua dei prodotti.
                     */
                    for (ElementoCarrello elemento : carrello.getElementi()) {
                        inserisciDettaglioOrdine(connessione, idOrdine, elemento);
                        scalaQuantitaProdotto(connessione, elemento);
                    }

                    connessione.commit();
                    salvato = true;
                }

                chiaviGenerate.close();
            }

            statementOrdine.close();

        } catch (SQLException e) {
            e.printStackTrace();

            /*
             * Se una delle query fallisce, annullo tutte le operazioni
             * già eseguite nella transazione.
             */
            if (connessione != null) {
                try {
                    connessione.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }

        } finally {
            if (connessione != null) {
                try {
                    connessione.setAutoCommit(true);
                    connessione.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return salvato;
    }

    /**
     * Inserisce una riga nella tabella dettagli_ordine.
     *
     * Ogni riga rappresenta un prodotto acquistato in un ordine.
     * Nome e prezzo vengono copiati dal prodotto per mantenere lo storico
     * anche se il prodotto viene modificato successivamente.
     *
     * @param connessione connessione SQL già aperta
     * @param idOrdine id dell'ordine appena creato
     * @param elemento elemento del carrello da salvare
     * @throws SQLException se la query fallisce
     */
    private void inserisciDettaglioOrdine(Connection connessione, int idOrdine, ElementoCarrello elemento)
            throws SQLException {

        String sql = "INSERT INTO dettagli_ordine "
                + "(id_ordine, id_prodotto, nome_prodotto, prezzo, quantita) "
                + "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = connessione.prepareStatement(sql);

        statement.setInt(1, idOrdine);
        statement.setInt(2, elemento.getProdotto().getId());
        statement.setString(3, elemento.getProdotto().getNome());
        statement.setDouble(4, elemento.getProdotto().getPrezzo());
        statement.setInt(5, elemento.getQuantita());

        statement.executeUpdate();
        statement.close();
    }

    /**
     * Scala dal database la quantità acquistata di un prodotto.
     *
     * @param connessione connessione SQL già aperta
     * @param elemento elemento del carrello acquistato
     * @throws SQLException se la query fallisce
     */
    private void scalaQuantitaProdotto(Connection connessione, ElementoCarrello elemento)
            throws SQLException {

        String sql = "UPDATE prodotti SET quantita = quantita - ? WHERE id = ?";

        PreparedStatement statement = connessione.prepareStatement(sql);

        statement.setInt(1, elemento.getQuantita());
        statement.setInt(2, elemento.getProdotto().getId());

        statement.executeUpdate();
        statement.close();
    }

    /**
     * Recupera tutti gli ordini effettuati da un determinato utente.
     *
     * Verrà usato nella pagina storico ordini.
     *
     * @param idUtente id dell'utente loggato
     * @return lista degli ordini dell'utente
     */
    public ArrayList<Ordine> trovaOrdiniPerUtente(int idUtente) {
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        String sql = "SELECT * FROM ordini WHERE id_utente = ? ORDER BY data_ordine DESC";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setInt(1, idUtente);

            try (ResultSet risultato = statement.executeQuery()) {
                while (risultato.next()) {
                    Ordine ordine = creaOrdineDaResultSet(risultato);
                    ordini.add(ordine);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordini;
    }

    /**
     * Recupera un ordine tramite id e id utente.
     *
     * Il controllo sull'id utente serve per evitare che un utente possa
     * visualizzare ordini appartenenti ad altri utenti.
     *
     * @param idOrdine id dell'ordine
     * @param idUtente id dell'utente loggato
     * @return ordine trovato oppure null
     */
    public Ordine trovaOrdinePerUtente(int idOrdine, int idUtente) {
        Ordine ordine = null;

        String sql = "SELECT * FROM ordini WHERE id = ? AND id_utente = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setInt(1, idOrdine);
            statement.setInt(2, idUtente);

            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    ordine = creaOrdineDaResultSet(risultato);
                    ordine.setDettagli(trovaDettagliOrdine(idOrdine));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordine;
    }

    /**
     * Recupera i dettagli di un ordine.
     *
     * @param idOrdine id dell'ordine
     * @return lista dei prodotti acquistati nell'ordine
     */
    public ArrayList<DettaglioOrdine> trovaDettagliOrdine(int idOrdine) {
        ArrayList<DettaglioOrdine> dettagli = new ArrayList<DettaglioOrdine>();

        String sql = "SELECT * FROM dettagli_ordine WHERE id_ordine = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setInt(1, idOrdine);

            try (ResultSet risultato = statement.executeQuery()) {
                while (risultato.next()) {
                    DettaglioOrdine dettaglio = new DettaglioOrdine();

                    dettaglio.setId(risultato.getInt("id"));
                    dettaglio.setIdOrdine(risultato.getInt("id_ordine"));
                    dettaglio.setIdProdotto(risultato.getInt("id_prodotto"));
                    dettaglio.setNomeProdotto(risultato.getString("nome_prodotto"));
                    dettaglio.setPrezzo(risultato.getDouble("prezzo"));
                    dettaglio.setQuantita(risultato.getInt("quantita"));

                    dettagli.add(dettaglio);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dettagli;
    }

    /**
     * Converte una riga della tabella ordini in un oggetto Ordine.
     *
     * @param risultato ResultSet posizionato su una riga valida
     * @return oggetto Ordine valorizzato
     * @throws SQLException se la lettura dei dati fallisce
     */
    private Ordine creaOrdineDaResultSet(ResultSet risultato) throws SQLException {
        Ordine ordine = new Ordine();

        ordine.setId(risultato.getInt("id"));
        ordine.setIdUtente(risultato.getInt("id_utente"));
        ordine.setTotale(risultato.getDouble("totale"));
        ordine.setEmailConsegna(risultato.getString("email_consegna"));
        ordine.setIndirizzoSpedizione(risultato.getString("indirizzo_spedizione"));
        ordine.setMetodoPagamento(risultato.getString("metodo_pagamento"));
        ordine.setStato(risultato.getString("stato"));
        ordine.setDataOrdine(risultato.getTimestamp("data_ordine"));

        return ordine;
    }
}