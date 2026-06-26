package util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Classe che gestisce il recupero delle connessioni al database.
 * 
 * I DAO usano questa classe per ottenere una connessione dal DataSource
 * 
 * Il DataSource è definito nel file META-INF/context.xml con il nome
 * jdbc/tcgshop e viene recuperato tramite JNDI.
 */
public class ConnessioneDatabase {

    /**
     * DataSource condiviso da tutti i DAO.
     * Viene inizializzato una sola volta quando la classe viene caricata.
     */
    private static DataSource dataSource;

    /**
     * Blocco statico eseguito quando la classe viene caricata.
     * Recupera il DataSource configurato in Tomcat tramite JNDI.
     */
    static {
        try {
            Context contesto = new InitialContext();
            dataSource = (DataSource) contesto.lookup("java:comp/env/jdbc/tcgshop");
        } catch (NamingException e) {
            System.out.println("Errore nel recupero del DataSource jdbc/tcgshop.");
            e.printStackTrace();
        }
    }

    /**
     * Restituisce una connessione al database tcgshop.
     * 
     * @return connessione SQL ottenuta dal DataSource
     * @throws SQLException se il DataSource non è stato inizializzato oppure
     *                      se non è possibile ottenere una connessione
     */
    public static Connection getConnessione() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource non inizializzato.");
        }

        return dataSource.getConnection();
    }
}