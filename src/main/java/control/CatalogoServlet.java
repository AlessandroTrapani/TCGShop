package control;

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
 * Servlet che gestisce la visualizzazione del catalogo prodotti.
 * 
 * Riceve eventuali parametri di filtro dalla richiesta, chiama ProdottoDAO
 * per recuperare i prodotti dal database e inoltra i dati alla JSP
 * catalogo.jsp.
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce le richieste GET verso l'URL /catalogo.
     * 
     * Recupera i parametri ricerca, gioco e categoria dalla request,
     * li passa al DAO e salva la lista dei prodotti come attributo
     * della request.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP da inviare al browser
     * @throws ServletException in caso di errore nella Servlet o nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ricerca = request.getParameter("ricerca");
        String gioco = request.getParameter("gioco");
        String categoria = request.getParameter("categoria");

        ProdottoDAO prodottoDAO = new ProdottoDAO();
        ArrayList<Prodotto> prodotti = prodottoDAO.cercaProdotti(ricerca, gioco, categoria);

        request.setAttribute("prodotti", prodotti);
        request.setAttribute("ricerca", ricerca);
        request.setAttribute("gioco", gioco);
        request.setAttribute("categoria", categoria);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/catalogo.jsp");
        dispatcher.forward(request, response);
    }
}