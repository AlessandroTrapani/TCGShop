package control;

import java.io.IOException;

import dao.ProdottoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Prodotto;

/**
 * Servlet che gestisce la visualizzazione del dettaglio di un prodotto.
 * 
 * Riceve l'id del prodotto dalla request, recupera il prodotto dal database
 * tramite ProdottoDAO e inoltra i dati alla JSP dettaglio-prodotto.jsp.
 */
@WebServlet("/dettaglio-prodotto")
public class DettaglioProdottoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce le richieste GET verso l'URL /dettaglio-prodotto.
     * 
     * L'id del prodotto viene letto dai parametri della request.
     * Se l'id non è valido o il prodotto non esiste, viene mostrato
     * un messaggio di errore nella JSP.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet o nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");

        Prodotto prodotto = null;
        String errore = null;

        if (idParametro == null || idParametro.trim().equals("")) {
            errore = "Prodotto non valido.";
        } else {
            try {
                int id = Integer.parseInt(idParametro);

                ProdottoDAO prodottoDAO = new ProdottoDAO();
                prodotto = prodottoDAO.trovaPerId(id);

                if (prodotto == null) {
                    errore = "Prodotto non trovato.";
                }

            } catch (NumberFormatException e) {
                errore = "Id prodotto non valido.";
            }
        }

        request.setAttribute("prodotto", prodotto);
        request.setAttribute("errore", errore);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/dettaglio-prodotto.jsp");
        dispatcher.forward(request, response);
    }
}