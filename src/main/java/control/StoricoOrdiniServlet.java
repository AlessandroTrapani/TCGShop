package control;

import java.io.IOException;
import java.util.ArrayList;

import dao.OrdineDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Ordine;
import model.Utente;

/**
 * Servlet che gestisce la visualizzazione dello storico ordini dell'utente.
 * 
 * Mostra solo gli ordini appartenenti all'utente attualmente loggato.
 */
@WebServlet("/storico-ordini")
public class StoricoOrdiniServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la richiesta GET verso /storico-ordini.
     * 
     * Controlla che l'utente sia loggato, recupera i suoi ordini tramite
     * OrdineDAO e inoltra i dati alla JSP storico-ordini.jsp.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessione = request.getSession();

        /*
         * Recupero l'utente loggato dalla sessione.
         * Se non è presente, l'utente non può vedere lo storico ordini.
         */
        Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");

        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        /*
         * Recupero dal database solo gli ordini dell'utente loggato.
         */
        OrdineDAO ordineDAO = new OrdineDAO();
        ArrayList<Ordine> ordini = ordineDAO.trovaOrdiniPerUtente(utenteLoggato.getId());

        request.setAttribute("ordini", ordini);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/storico-ordini.jsp");
        dispatcher.forward(request, response);
    }
}