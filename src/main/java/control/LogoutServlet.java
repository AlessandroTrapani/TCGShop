package control;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet che gestisce il logout dell'utente.
 * 
 * Invalida la sessione corrente e riporta l'utente alla homepage.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la richiesta di logout.
     * 
     * Recupera la sessione esistente, se presente, e la invalida.
     * In questo modo vengono eliminati utenteLoggato, tokenAccesso
     * e tutti gli altri dati salvati in sessione.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * getSession(false) recupera la sessione solo se esiste già.
         * Non ne crea una nuova inutilmente.
         */
        HttpSession sessione = request.getSession(false);

        if (sessione != null) {
            sessione.invalidate();
        }

        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}