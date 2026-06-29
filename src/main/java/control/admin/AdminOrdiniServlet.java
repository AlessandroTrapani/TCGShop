package control.admin;

import java.io.IOException;
import java.util.ArrayList;

import dao.OrdineDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Ordine;

/**
 * Servlet che gestisce la lista degli ordini nell'area amministratore.
 *
 * Recupera gli ordini dal database e permette di filtrarli
 * per data inizio, data fine e cliente.
 */
@WebServlet("/admin/ordini")
public class AdminOrdiniServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la lista degli ordini dell'area admin.
     *
     * Legge eventuali filtri dalla request, chiama OrdineDAO
     * e inoltra gli ordini alla JSP.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String dataInizio = request.getParameter("dataInizio");
        String dataFine = request.getParameter("dataFine");
        String idUtenteParametro = request.getParameter("idUtente");

        int idUtente = 0;
        String errore = null;

        /*
         * Converte l'id utente solo se viene inserito nel form.
         * Il valore 0 indica che non deve essere applicato il filtro cliente.
         */
        if (idUtenteParametro != null && !idUtenteParametro.trim().equals("")) {
            try {
                idUtente = Integer.parseInt(idUtenteParametro.trim());

                if (idUtente < 0) {
                    errore = "L'id cliente non può essere negativo.";
                }

            } catch (NumberFormatException e) {
                errore = "L'id cliente deve essere un numero.";
            }
        }

        ArrayList<Ordine> ordini = new ArrayList<Ordine>();

        if (errore == null) {
            /*
             * Recupera gli ordini applicando i filtri ricevuti.
             */
            OrdineDAO ordineDAO = new OrdineDAO();
            ordini = ordineDAO.trovaOrdiniAdminFiltrati(dataInizio, dataFine, idUtente);
        }

        /*
         * Salva ordini, filtri ed eventuale errore nella request.
         */
        request.setAttribute("ordini", ordini);
        request.setAttribute("dataInizio", dataInizio);
        request.setAttribute("dataFine", dataFine);
        request.setAttribute("idUtente", idUtenteParametro);
        request.setAttribute("errore", errore);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/ordini.jsp");
        dispatcher.forward(request, response);
    }
}