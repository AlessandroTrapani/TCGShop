package control.admin;

import java.io.IOException;

import dao.ProdottoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet che gestisce l'eliminazione logica di un prodotto dall'area admin.
 *
 * Il prodotto non viene cancellato fisicamente dal database.
 * Viene aggiornato lo stato del prodotto a ELIMINATO.
 */
@WebServlet("/admin/elimina-prodotto")
public class AdminEliminaProdottoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Gestisce la richiesta POST di eliminazione logica prodotto.
     *
     * Recupera l'id del prodotto dalla request e chiama il DAO per aggiornare
     * lo stato del prodotto a ELIMINATO.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");

        if (idParametro != null && !idParametro.trim().equals("")) {
            try {
                int idProdotto = Integer.parseInt(idParametro);

                /*
                 * Esegue l'eliminazione logica del prodotto.
                 */
                ProdottoDAO prodottoDAO = new ProdottoDAO();
                prodottoDAO.eliminaLogicamente(idProdotto);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        /*
         * Dopo l'operazione torna sempre alla lista prodotti admin.
         */
        response.sendRedirect(request.getContextPath() + "/admin/prodotti");
    }
}