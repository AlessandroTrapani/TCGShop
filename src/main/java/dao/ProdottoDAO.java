package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Prodotto;
import util.ConnessioneDatabase;

/**
 * DAO dedicato alla gestione dei prodotti.
 * Contiene le query SQL sulla tabella prodotti.
 */
public class ProdottoDAO {

    /**
     * Cerca i prodotti disponibili applicando eventuali filtri.
     * Viene usato dalla Servlet del catalogo.
     *
     * @param ricerca testo cercato nel nome del prodotto
     * @param gioco gioco selezionato
     * @param categoria categoria selezionata
     * @return lista dei prodotti trovati
     */
    public ArrayList<Prodotto> cercaProdotti(String ricerca, String gioco, String categoria) {
        ArrayList<Prodotto> prodotti = new ArrayList<>();

        String sql = "SELECT * FROM prodotti "
                + "WHERE stato = 'DISPONIBILE' "
                + "AND quantita > 0 "
                + "AND (? IS NULL OR nome LIKE ?) "
                + "AND (? IS NULL OR gioco = ?) "
                + "AND (? IS NULL OR categoria = ?) "
                + "ORDER BY nome";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            if (ricerca == null || ricerca.trim().equals("")) {
                statement.setString(1, null);
                statement.setString(2, null);
            } else {
                statement.setString(1, ricerca.trim());
                statement.setString(2, "%" + ricerca.trim() + "%");
            }

            if (gioco == null || gioco.trim().equals("")) {
                statement.setString(3, null);
                statement.setString(4, null);
            } else {
                statement.setString(3, gioco.trim());
                statement.setString(4, gioco.trim());
            }

            if (categoria == null || categoria.trim().equals("")) {
                statement.setString(5, null);
                statement.setString(6, null);
            } else {
                statement.setString(5, categoria.trim());
                statement.setString(6, categoria.trim());
            }

            try (ResultSet risultato = statement.executeQuery()) {
                while (risultato.next()) {
                    Prodotto prodotto = new Prodotto();

                    prodotto.setId(risultato.getInt("id"));
                    prodotto.setNome(risultato.getString("nome"));
                    prodotto.setGioco(risultato.getString("gioco"));
                    prodotto.setCategoria(risultato.getString("categoria"));
                    prodotto.setRarita(risultato.getString("rarita"));
                    prodotto.setPrezzo(risultato.getDouble("prezzo"));
                    prodotto.setQuantita(risultato.getInt("quantita"));
                    prodotto.setImmagine(risultato.getString("immagine"));
                    prodotto.setDescrizione(risultato.getString("descrizione"));
                    prodotto.setStato(risultato.getString("stato"));

                    prodotti.add(prodotto);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prodotti;
    }

    /**
     * Cerca un prodotto tramite id.
     * Viene usato dalla pagina dettaglio prodotto e dal carrello.
     *
     * @param id id del prodotto da cercare
     * @return prodotto trovato oppure null
     */
    public Prodotto trovaPerId(int id) {
        Prodotto prodotto = null;

        String sql = "SELECT * FROM prodotti WHERE id = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    prodotto = new Prodotto();

                    prodotto.setId(risultato.getInt("id"));
                    prodotto.setNome(risultato.getString("nome"));
                    prodotto.setGioco(risultato.getString("gioco"));
                    prodotto.setCategoria(risultato.getString("categoria"));
                    prodotto.setRarita(risultato.getString("rarita"));
                    prodotto.setPrezzo(risultato.getDouble("prezzo"));
                    prodotto.setQuantita(risultato.getInt("quantita"));
                    prodotto.setImmagine(risultato.getString("immagine"));
                    prodotto.setDescrizione(risultato.getString("descrizione"));
                    prodotto.setStato(risultato.getString("stato"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prodotto;
    }
    
    /**
     * Recupera tutti i prodotti presenti nel database.
     * 
     * Questo metodo viene usato nell'area admin, dove devono essere visibili
     * anche i prodotti non disponibili o eliminati logicamente.
     *
     * @return lista completa dei prodotti
     */
    public ArrayList<Prodotto> trovaTuttiAdmin() {
        ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

        String sql = "SELECT * FROM prodotti ORDER BY data_inserimento DESC";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql);
            ResultSet risultato = statement.executeQuery()
        ) {
            while (risultato.next()) {
                Prodotto prodotto = new Prodotto();

                prodotto.setId(risultato.getInt("id"));
                prodotto.setNome(risultato.getString("nome"));
                prodotto.setGioco(risultato.getString("gioco"));
                prodotto.setCategoria(risultato.getString("categoria"));
                prodotto.setRarita(risultato.getString("rarita"));
                prodotto.setPrezzo(risultato.getDouble("prezzo"));
                prodotto.setQuantita(risultato.getInt("quantita"));
                prodotto.setImmagine(risultato.getString("immagine"));
                prodotto.setDescrizione(risultato.getString("descrizione"));
                prodotto.setStato(risultato.getString("stato"));
                prodotto.setDataInserimento(risultato.getTimestamp("data_inserimento"));

                prodotti.add(prodotto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prodotti;
    }
    
    /**
     * Inserisce un nuovo prodotto nella tabella prodotti.
     *
     * Il prodotto viene creato dall'area amministratore.
     * Lo stato viene calcolato dalla Servlet in base alla quantità inserita.
     *
     * @param prodotto prodotto da inserire nel database
     * @return true se l'inserimento è riuscito, false altrimenti
     */
    public boolean inserisciProdotto(Prodotto prodotto) {
        boolean inserito = false;

        String sql = "INSERT INTO prodotti "
                + "(nome, gioco, categoria, rarita, prezzo, quantita, immagine, descrizione, stato) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, prodotto.getNome());
            statement.setString(2, prodotto.getGioco());
            statement.setString(3, prodotto.getCategoria());
            statement.setString(4, prodotto.getRarita());
            statement.setDouble(5, prodotto.getPrezzo());
            statement.setInt(6, prodotto.getQuantita());
            statement.setString(7, prodotto.getImmagine());
            statement.setString(8, prodotto.getDescrizione());
            statement.setString(9, prodotto.getStato());

            int righeInserite = statement.executeUpdate();

            if (righeInserite > 0) {
                inserito = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inserito;
    }
    
    /**
     * Aggiorna i dati di un prodotto esistente nella tabella prodotti.
     *
     * Il metodo viene usato dall'area amministratore quando viene modificato
     * un prodotto già presente nel catalogo.
     *
     * @param prodotto prodotto con i nuovi dati da salvare
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    public boolean aggiornaProdotto(Prodotto prodotto) {
        boolean aggiornato = false;

        String sql = "UPDATE prodotti "
                + "SET nome = ?, gioco = ?, categoria = ?, rarita = ?, prezzo = ?, "
                + "quantita = ?, immagine = ?, descrizione = ?, stato = ? "
                + "WHERE id = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, prodotto.getNome());
            statement.setString(2, prodotto.getGioco());
            statement.setString(3, prodotto.getCategoria());
            statement.setString(4, prodotto.getRarita());
            statement.setDouble(5, prodotto.getPrezzo());
            statement.setInt(6, prodotto.getQuantita());
            statement.setString(7, prodotto.getImmagine());
            statement.setString(8, prodotto.getDescrizione());
            statement.setString(9, prodotto.getStato());
            statement.setInt(10, prodotto.getId());

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
     * Elimina logicamente un prodotto dal catalogo.
     *
     * Il prodotto non viene rimosso fisicamente dal database.
     * Viene invece aggiornato il campo stato con il valore ELIMINATO.
     *
     * @param idProdotto id del prodotto da eliminare logicamente
     * @return true se l'aggiornamento è riuscito, false altrimenti
     */
    public boolean eliminaLogicamente(int idProdotto) {
        boolean eliminato = false;

        String sql = "UPDATE prodotti SET stato = ? WHERE id = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, "ELIMINATO");
            statement.setInt(2, idProdotto);

            int righeAggiornate = statement.executeUpdate();

            if (righeAggiornate > 0) {
                eliminato = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eliminato;
    }
}