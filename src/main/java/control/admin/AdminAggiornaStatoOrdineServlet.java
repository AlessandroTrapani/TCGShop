package control.admin;

import java.io.IOException;

import dao.OrdineDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet che gestisce l'aggiornamento dello stato di un ordine dall'area admin.
 *
 * Riceve l'id dell'ordine e il nuovo stato dal form presente nel dettaglio
 * ordine admin.
 */
@WebServlet("/admin/aggiorna-stato-ordine")
public class AdminAggiornaStatoOrdineServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la richiesta POST di aggiornamento stato ordine.
     *
     * Recupera id ordine e stato dalla request, controlla che siano validi
     * e aggiorna il database tramite OrdineDAO.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");
        String stato = request.getParameter("stato");

        int idOrdine = 0;

        /*
         * Controlla che l'id ordine sia presente e numerico.
         */
        if (idParametro != null && !idParametro.trim().equals("")) {
            try {
                idOrdine = Integer.parseInt(idParametro);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        /*
         * Aggiorna lo stato solo se l'id è valido e lo stato appartiene
         * alla lista degli stati ammessi.
         */
        if (idOrdine > 0 && statoValido(stato)) {
            OrdineDAO ordineDAO = new OrdineDAO();
            ordineDAO.aggiornaStatoOrdine(idOrdine, stato);

            request.getSession().setAttribute("messaggioSuccesso", "Stato ordine aggiornato correttamente.");
        } else {
            request.getSession().setAttribute("messaggioErrore", "Stato ordine non valido.");
        }

        /*
         * Dopo l'aggiornamento torna al dettaglio dell'ordine.
         */
        response.sendRedirect(request.getContextPath() + "/admin/dettaglio-ordine?id=" + idOrdine);
    }

    /**
     * Controlla che lo stato ricevuto dal form sia uno degli stati consentiti.
     *
     * @param stato stato da controllare
     * @return true se lo stato è valido, false altrimenti
     */
    private boolean statoValido(String stato) {
        return "IN_ELABORAZIONE".equals(stato)
                || "SPEDITO".equals(stato)
                || "COMPLETATO".equals(stato)
                || "ANNULLATO".equals(stato);
    }
}