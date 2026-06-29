package control.admin;

import java.io.IOException;

import dao.OrdineDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Ordine;

/**
 * Servlet che gestisce il dettaglio di un ordine nell'area amministratore.
 *
 * L'admin può visualizzare qualsiasi ordine presente nel database.
 */
@WebServlet("/admin/dettaglio-ordine")
public class AdminDettaglioOrdineServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra il dettaglio completo di un ordine.
     *
     * Recupera l'id ordine dalla request, cerca l'ordine tramite OrdineDAO
     * e inoltra il risultato alla JSP dell'area admin.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");

        Ordine ordine = null;
        String errore = null;

        /*
         * Controlla che l'id ordine sia presente.
         */
        if (idParametro == null || idParametro.trim().equals("")) {
            errore = "Ordine non valido.";
        } else {
            try {
                int idOrdine = Integer.parseInt(idParametro);

                /*
                 * Recupera l'ordine senza filtro utente perché la pagina
                 * è riservata all'amministratore.
                 */
                OrdineDAO ordineDAO = new OrdineDAO();
                ordine = ordineDAO.trovaOrdinePerAdmin(idOrdine);

                if (ordine == null) {
                    errore = "Ordine non trovato.";
                }

            } catch (NumberFormatException e) {
                errore = "Id ordine non valido.";
            }
        }

        request.setAttribute("ordine", ordine);
        request.setAttribute("errore", errore);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/dettaglio-ordine.jsp");
        dispatcher.forward(request, response);
    }
}