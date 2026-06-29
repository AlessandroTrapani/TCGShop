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
 *
 * La classe contiene i metodi necessari per salvare un ordine,
 * recuperare gli ordini di un utente, recuperare gli ordini lato admin
 * e aggiornare lo stato di un ordine.
 */
public class OrdineDAO {

    /**
     * Salva un ordine nel database partendo dai dati dell'ordine
     * e dagli elementi presenti nel carrello.
     *
     * Il metodo usa una transazione perché deve eseguire più operazioni:
     * inserimento ordine, inserimento dettagli ordine e aggiornamento quantità
     * dei prodotti acquistati.
     *
     * @param ordine ordine da salvare
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
             * Disattiva l'autocommit per gestire manualmente la transazione.
             */
            connessione.setAutoCommit(false);

            try (
                PreparedStatement statementOrdine = connessione.prepareStatement(
                        sqlOrdine,
                        PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                statementOrdine.setInt(1, ordine.getIdUtente());
                statementOrdine.setDouble(2, ordine.getTotale());
                statementOrdine.setString(3, ordine.getEmailConsegna());
                statementOrdine.setString(4, ordine.getIndirizzoSpedizione());
                statementOrdine.setString(5, ordine.getMetodoPagamento());
                statementOrdine.setString(6, ordine.getStato());

                int righeInserite = statementOrdine.executeUpdate();

                if (righeInserite > 0) {
                    ResultSet chiaviGenerate = statementOrdine.getGeneratedKeys();

                    if (chiaviGenerate.next()) {
                        int idOrdine = chiaviGenerate.getInt(1);

                        /*
                         * Inserisce una riga nella tabella dettagli_ordine
                         * per ogni prodotto presente nel carrello.
                         */
                        for (ElementoCarrello elemento : carrello.getElementi()) {
                            inserisciDettaglioOrdine(connessione, idOrdine, elemento);
                            scalaQuantitaProdotto(connessione, elemento);
                        }

                        /*
                         * Conferma tutte le operazioni della transazione.
                         */
                        connessione.commit();
                        salvato = true;
                    }
                }

            } catch (SQLException e) {
                /*
                 * In caso di errore annulla tutte le operazioni eseguite
                 * nella transazione.
                 */
                if (connessione != null) {
                    connessione.rollback();
                }

                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (connessione != null) {
                try {
                    /*
                     * Ripristina l'autocommit e chiude la connessione.
                     */
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
     * Recupera tutti gli ordini appartenenti a uno specifico utente.
     *
     * Il metodo viene usato nella pagina "I miei ordini".
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
     * Recupera un ordine specifico appartenente a uno specifico utente.
     *
     * Il controllo sull'id utente impedisce a un utente di visualizzare
     * ordini appartenenti ad altri account.
     *
     * @param idOrdine id dell'ordine da recuperare
     * @param idUtente id dell'utente loggato
     * @return ordine trovato con i relativi dettagli, oppure null se non esiste
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

                    /*
                     * Recupera anche i prodotti acquistati nell'ordine.
                     */
                    ordine.setDettagli(trovaDettagliOrdine(idOrdine));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordine;
    }

    /**
     * Recupera tutti gli ordini presenti nel database.
     *
     * Il metodo viene usato nell'area amministratore, dove devono essere
     * visibili gli ordini di tutti gli utenti.
     *
     * @return lista completa degli ordini
     */
    public ArrayList<Ordine> trovaTuttiOrdini() {
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        String sql = "SELECT * FROM ordini ORDER BY data_ordine DESC";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql);
            ResultSet risultato = statement.executeQuery()
        ) {
            while (risultato.next()) {
                Ordine ordine = creaOrdineDaResultSet(risultato);
                ordini.add(ordine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordini;
    }
    
    /**
     * Recupera gli ordini dell'area admin applicando filtri opzionali.
     *
     * I filtri disponibili sono:
     * - data inizio
     * - data fine
     * - id utente
     *
     * Se un filtro non viene valorizzato, non viene applicato.
     *
     * @param dataInizio data iniziale nel formato yyyy-MM-dd, oppure stringa vuota
     * @param dataFine data finale nel formato yyyy-MM-dd, oppure stringa vuota
     * @param idUtente id del cliente da filtrare, oppure 0 per tutti i clienti
     * @return lista degli ordini filtrati
     */
    public ArrayList<Ordine> trovaOrdiniAdminFiltrati(String dataInizio, String dataFine, int idUtente) {
        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        String sql = "SELECT * FROM ordini "
                + "WHERE (? IS NULL OR data_ordine >= ?) "
                + "AND (? IS NULL OR data_ordine <= ?) "
                + "AND (? = 0 OR id_utente = ?) "
                + "ORDER BY data_ordine DESC";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            /*
             * Prepara il filtro data inizio.
             * Se la data è vuota, il filtro non viene applicato.
             */
            if (dataInizio == null || dataInizio.trim().equals("")) {
                statement.setString(1, null);
                statement.setString(2, null);
            } else {
                statement.setString(1, dataInizio.trim() + " 00:00:00");
                statement.setString(2, dataInizio.trim() + " 00:00:00");
            }

            /*
             * Prepara il filtro data fine.
             * Viene impostata l'ora 23:59:59 per includere tutta la giornata.
             */
            if (dataFine == null || dataFine.trim().equals("")) {
                statement.setString(3, null);
                statement.setString(4, null);
            } else {
                statement.setString(3, dataFine.trim() + " 23:59:59");
                statement.setString(4, dataFine.trim() + " 23:59:59");
            }

            /*
             * Prepara il filtro cliente.
             * Se idUtente vale 0, vengono mostrati gli ordini di tutti i clienti.
             */
            statement.setInt(5, idUtente);
            statement.setInt(6, idUtente);

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
     * Recupera un ordine tramite il suo id senza controllare l'utente proprietario.
     *
     * Il metodo viene usato nell'area amministratore, dove l'admin può vedere
     * gli ordini di tutti gli utenti.
     *
     * @param idOrdine id dell'ordine da recuperare
     * @return ordine trovato con i relativi dettagli, oppure null se non esiste
     */
    public Ordine trovaOrdinePerAdmin(int idOrdine) {
        Ordine ordine = null;

        String sql = "SELECT * FROM ordini WHERE id = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setInt(1, idOrdine);

            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    ordine = creaOrdineDaResultSet(risultato);

                    /*
                     * Recupera anche i prodotti collegati all'ordine.
                     */
                    ordine.setDettagli(trovaDettagliOrdine(idOrdine));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ordine;
    }

    /**
     * Aggiorna lo stato di un ordine.
     *
     * Il metodo viene usato dall'area amministratore per modificare
     * l'avanzamento di un ordine.
     *
     * @param idOrdine id dell'ordine da aggiornare
     * @param stato nuovo stato dell'ordine
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    public boolean aggiornaStatoOrdine(int idOrdine, String stato) {
        boolean aggiornato = false;

        String sql = "UPDATE ordini SET stato = ? WHERE id = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, stato);
            statement.setInt(2, idOrdine);

            int righeAggiornate = statement.executeUpdate();

            if (righeAggiornate > 0) {
                aggiornato = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aggiornato;
    }

    /**
     * Inserisce un dettaglio ordine nella tabella dettagli_ordine.
     *
     * Ogni dettaglio rappresenta un prodotto acquistato in un ordine.
     *
     * @param connessione connessione usata nella transazione
     * @param idOrdine id dell'ordine appena creato
     * @param elemento elemento del carrello da salvare come dettaglio
     * @throws SQLException in caso di errore SQL
     */
    private void inserisciDettaglioOrdine(Connection connessione, int idOrdine, ElementoCarrello elemento)
            throws SQLException {

        String sql = "INSERT INTO dettagli_ordine "
                + "(id_ordine, id_prodotto, nome_prodotto, prezzo, quantita) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connessione.prepareStatement(sql)) {
            statement.setInt(1, idOrdine);
            statement.setInt(2, elemento.getProdotto().getId());
            statement.setString(3, elemento.getProdotto().getNome());
            statement.setDouble(4, elemento.getProdotto().getPrezzo());
            statement.setInt(5, elemento.getQuantita());

            statement.executeUpdate();
        }
    }

    /**
     * Scala la quantità disponibile di un prodotto dopo l'acquisto.
     *
     * Se dopo l'acquisto la quantità arriva a zero, aggiorna anche
     * lo stato del prodotto a NON_DISPONIBILE.
     *
     * @param connessione connessione usata nella transazione
     * @param elemento elemento del carrello acquistato
     * @throws SQLException in caso di errore SQL
     */
    private void scalaQuantitaProdotto(Connection connessione, ElementoCarrello elemento)
            throws SQLException {

        String sql = "UPDATE prodotti "
                + "SET quantita = quantita - ?, "
                + "stato = CASE "
                + "    WHEN quantita - ? <= 0 THEN 'NON_DISPONIBILE' "
                + "    ELSE stato "
                + "END "
                + "WHERE id = ?";

        try (PreparedStatement statement = connessione.prepareStatement(sql)) {
            statement.setInt(1, elemento.getQuantita());
            statement.setInt(2, elemento.getQuantita());
            statement.setInt(3, elemento.getProdotto().getId());

            statement.executeUpdate();
        }
    }

    /**
     * Recupera i dettagli di un ordine dalla tabella dettagli_ordine.
     *
     * @param idOrdine id dell'ordine di cui recuperare i dettagli
     * @return lista dei dettagli ordine
     */
    private ArrayList<DettaglioOrdine> trovaDettagliOrdine(int idOrdine) {
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
     * Crea un oggetto Ordine partendo da una riga del ResultSet.
     *
     * Questo metodo evita di ripetere lo stesso codice in più query.
     *
     * @param risultato ResultSet posizionato sulla riga dell'ordine
     * @return oggetto Ordine valorizzato
     * @throws SQLException in caso di errore nella lettura dei dati
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