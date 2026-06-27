package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Utente;
import util.ConnessioneDatabase;

/**
 * DAO dedicato alla gestione degli utenti.
 * Contiene le query SQL sulla tabella utenti.
 */
public class UtenteDAO {

    /**
     * Cerca un utente tramite email e password.
     * 
     * Questo metodo verrà usato dalla LoginServlet per verificare
     * le credenziali inserite nel form di login.
     *
     * @param email = email inserita dall'utente
     * @param password = password inserita dall'utente
     * @return utente trovato oppure null se le credenziali non sono valide
     */
    public Utente trovaPerEmailEPassword(String email, String password) {
        Utente utente = null;

        String sql = "SELECT * FROM utenti WHERE email = ? AND password = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    utente = new Utente();

                    utente.setId(risultato.getInt("id"));
                    utente.setNome(risultato.getString("nome"));
                    utente.setCognome(risultato.getString("cognome"));
                    utente.setEmail(risultato.getString("email"));
                    utente.setPassword(risultato.getString("password"));
                    utente.setRuolo(risultato.getString("ruolo"));
                    utente.setDataRegistrazione(risultato.getTimestamp("data_registrazione"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utente;
    }

    /**
     * Controlla se una email è già presente nel database.
     * 
     * Questo metodo verrà usato sia dalla registrazione lato server,
     * sia dalla verifica email AJAX.
     *
     * @param email = email da controllare
     * @return true se l'email esiste già, false altrimenti
     */
    public boolean emailEsistente(String email) {
        boolean esistente = false;

        String sql = "SELECT id FROM utenti WHERE email = ?";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, email);

            try (ResultSet risultato = statement.executeQuery()) {
                if (risultato.next()) {
                    esistente = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return esistente;
    }

    /**
     * Inserisce un nuovo utente nel database.
     * 
     * Il ruolo viene impostato normalmente a UTENTE, perché la registrazione
     * pubblica non deve creare amministratori.
     *
     * @param utente = oggetto Utente contenente i dati da salvare
     * @return true se l'inserimento è riuscito, false altrimenti
     */
    public boolean inserisciUtente(Utente utente) {
        boolean inserito = false;

        String sql = "INSERT INTO utenti (nome, cognome, email, password, ruolo) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection connessione = ConnessioneDatabase.getConnessione();
            PreparedStatement statement = connessione.prepareStatement(sql)
        ) {
            statement.setString(1, utente.getNome());
            statement.setString(2, utente.getCognome());
            statement.setString(3, utente.getEmail());
            statement.setString(4, utente.getPassword());
            statement.setString(5, utente.getRuolo());

            int righeInserite = statement.executeUpdate();

            if (righeInserite > 0) {
                inserito = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inserito;
    }
}