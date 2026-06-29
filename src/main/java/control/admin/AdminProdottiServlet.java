package control.admin;

import java.io.IOException;
import java.util.ArrayList;

import dao.ProdottoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Prodotto;

/**
 * Servlet che gestisce la lista dei prodotti nell'area amministratore.
 * 
 * Recupera tutti i prodotti dal database e li inoltra alla JSP admin
 * prodotti.jsp.
 */
@WebServlet("/admin/prodotti")
public class AdminProdottiServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la lista completa dei prodotti presenti nel database.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Recupera tutti i prodotti tramite ProdottoDAO.
         */
        ProdottoDAO prodottoDAO = new ProdottoDAO();
        ArrayList<Prodotto> prodotti = prodottoDAO.trovaTuttiAdmin();

        /*
         * Salva la lista prodotti nella request per renderla disponibile alla JSP.
         */
        request.setAttribute("prodotti", prodotti);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/prodotti.jsp");
        dispatcher.forward(request, response);
    }
}