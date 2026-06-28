package control;

import java.io.IOException;

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
 * Servlet che gestisce la visualizzazione del dettaglio di un ordine utente.
 * 
 * L'utente può visualizzare solo gli ordini appartenenti al proprio account.
 */
@WebServlet("/dettaglio-ordine")
public class DettaglioOrdineServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la richiesta GET verso /dettaglio-ordine.
     * 
     * Recupera l'id dell'ordine dalla request, controlla che l'utente sia
     * loggato e recupera l'ordine tramite OrdineDAO.
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
         * Se non è presente, l'utente viene mandato alla pagina login.
         */
        Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");

        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idParametro = request.getParameter("id");

        Ordine ordine = null;
        String errore = null;

        /*
         * Controllo che l'id ordine sia presente.
         */
        if (idParametro == null || idParametro.trim().equals("")) {
            errore = "Ordine non valido.";
        } else {
            try {
                int idOrdine = Integer.parseInt(idParametro);

                /*
                 * Recupero l'ordine passando anche l'id dell'utente loggato.
                 * Questo evita che un utente possa vedere ordini di altri utenti.
                 */
                OrdineDAO ordineDAO = new OrdineDAO();
                ordine = ordineDAO.trovaOrdinePerUtente(idOrdine, utenteLoggato.getId());

                if (ordine == null) {
                    errore = "Ordine non trovato.";
                }

            } catch (NumberFormatException e) {
                errore = "Id ordine non valido.";
            }
        }

        request.setAttribute("ordine", ordine);
        request.setAttribute("errore", errore);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/dettaglio-ordine.jsp");
        dispatcher.forward(request, response);
    }
}