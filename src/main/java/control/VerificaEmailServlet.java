package control;

import java.io.IOException;

import dao.UtenteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet usata per verificare tramite AJAX se una email è già registrata.
 * 
 * Riceve una email come parametro della richiesta, controlla il database
 * tramite UtenteDAO e restituisce una risposta in formato JSON.
 */
@WebServlet("/verifica-email")
public class VerificaEmailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la richiesta AJAX inviata dal file validazione-registrazione.js.
     * 
     * La risposta non è una JSP, ma un oggetto JSON contenente:
     * - valida: true o false
     * - messaggio: testo da mostrare all'utente
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP in formato JSON
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        boolean valida = true;
        String messaggio = "Email disponibile.";

        /*
         * Imposta il tipo della risposta come JSON.
         * Questo permette al JavaScript di leggere la risposta con response.json().
         */
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (email == null || email.trim().equals("")) {
            valida = false;
            messaggio = "L'email è obbligatoria.";
        } else {
            UtenteDAO utenteDAO = new UtenteDAO();

            /*
             * Se l'email è già presente nel database,
             * non può essere usata per una nuova registrazione.
             */
            if (utenteDAO.emailEsistente(email.trim())) {
                valida = false;
                messaggio = "Email già registrata.";
            }
        }

        /*
         * Crea manualmente una risposta JSON semplice.
         * In questo progetto evitiamo librerie esterne per mantenere il codice
         * vicino a quanto visto nel corso.
         */
        String json = "{"
                + "\"valida\":" + valida + ","
                + "\"messaggio\":\"" + messaggio + "\""
                + "}";

        response.getWriter().write(json);
    }
}