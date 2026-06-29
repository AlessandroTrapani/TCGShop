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
 * Recupera tutti gli ordini presenti nel database e li inoltra alla JSP
 * dell'area admin.
 */
@WebServlet("/admin/ordini")
public class AdminOrdiniServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la lista completa degli ordini.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Recupera tutti gli ordini tramite OrdineDAO.
         */
        OrdineDAO ordineDAO = new OrdineDAO();
        ArrayList<Ordine> ordini = ordineDAO.trovaTuttiOrdini();

        /*
         * Salva la lista ordini nella request per renderla disponibile alla JSP.
         */
        request.setAttribute("ordini", ordini);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/ordini.jsp");
        dispatcher.forward(request, response);
    }
}