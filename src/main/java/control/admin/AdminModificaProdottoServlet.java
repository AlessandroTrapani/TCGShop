package control.admin;

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
 * Servlet che gestisce la modifica di un prodotto dall'area amministratore.
 *
 * Con GET recupera il prodotto dal database e mostra il form già compilato.
 * Con POST valida i dati ricevuti e aggiorna il prodotto nel database.
 */
@WebServlet("/admin/modifica-prodotto")
public class AdminModificaProdottoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra il form di modifica di un prodotto.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
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
                int idProdotto = Integer.parseInt(idParametro);

                /*
                 * Recupera il prodotto da modificare tramite il suo id.
                 */
                ProdottoDAO prodottoDAO = new ProdottoDAO();
                prodotto = prodottoDAO.trovaPerId(idProdotto);

                if (prodotto == null) {
                    errore = "Prodotto non trovato.";
                }

            } catch (NumberFormatException e) {
                errore = "Id prodotto non valido.";
            }
        }

        request.setAttribute("prodotto", prodotto);
        request.setAttribute("errore", errore);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/modifica-prodotto.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce il salvataggio delle modifiche di un prodotto.
     *
     * Recupera i dati dal form, esegue i controlli lato server e aggiorna
     * il prodotto tramite ProdottoDAO.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParametro = request.getParameter("id");
        String nome = request.getParameter("nome");
        String gioco = request.getParameter("gioco");
        String categoria = request.getParameter("categoria");
        String rarita = request.getParameter("rarita");
        String prezzoParametro = request.getParameter("prezzo");
        String quantitaParametro = request.getParameter("quantita");
        String immagine = request.getParameter("immagine");
        String descrizione = request.getParameter("descrizione");

        String errore = null;

        int idProdotto = 0;
        double prezzo = 0;
        int quantita = 0;

        /*
         * Controlla che l'id del prodotto sia valido.
         */
        if (idParametro == null || idParametro.trim().equals("")) {
            errore = "Prodotto non valido.";
        } else {
            try {
                idProdotto = Integer.parseInt(idParametro);
            } catch (NumberFormatException e) {
                errore = "Id prodotto non valido.";
            }
        }

        /*
         * Controlla i campi obbligatori.
         */
        if (errore == null
                && (nome == null || nome.trim().equals("")
                || gioco == null || gioco.trim().equals("")
                || categoria == null || categoria.trim().equals("")
                || prezzoParametro == null || prezzoParametro.trim().equals("")
                || quantitaParametro == null || quantitaParametro.trim().equals(""))) {

            errore = "Nome, gioco, categoria, prezzo e quantità sono obbligatori.";
        }

        /*
         * Converte prezzo e quantità solo se i campi obbligatori sono presenti.
         */
        if (errore == null) {
            try {
                prezzo = Double.parseDouble(prezzoParametro.trim().replace(",", "."));
                quantita = Integer.parseInt(quantitaParametro.trim());

                if (prezzo <= 0) {
                    errore = "Il prezzo deve essere maggiore di zero.";
                } else if (quantita < 0) {
                    errore = "La quantità non può essere negativa.";
                }

            } catch (NumberFormatException e) {
                errore = "Prezzo e quantità devono essere valori numerici.";
            }
        }

        /*
         * Calcola lo stato del prodotto in base alla quantità.
         */
        String stato = "DISPONIBILE";

        if (quantita == 0) {
            stato = "NON_DISPONIBILE";
        }

        Prodotto prodotto = new Prodotto();

        prodotto.setId(idProdotto);
        prodotto.setNome(nome != null ? nome.trim() : "");
        prodotto.setGioco(gioco != null ? gioco.trim() : "");
        prodotto.setCategoria(categoria != null ? categoria.trim() : "");
        prodotto.setRarita(rarita != null ? rarita.trim() : "");
        prodotto.setPrezzo(prezzo);
        prodotto.setQuantita(quantita);
        prodotto.setImmagine(immagine != null ? immagine.trim() : "");
        prodotto.setDescrizione(descrizione != null ? descrizione.trim() : "");
        prodotto.setStato(stato);

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.setAttribute("prodotto", prodotto);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/modifica-prodotto.jsp");
            dispatcher.forward(request, response);
            return;
        }

        ProdottoDAO prodottoDAO = new ProdottoDAO();
        boolean aggiornato = prodottoDAO.aggiornaProdotto(prodotto);

        if (aggiornato) {
            response.sendRedirect(request.getContextPath() + "/admin/prodotti");
        } else {
            request.setAttribute("errore", "Errore durante l'aggiornamento del prodotto.");
            request.setAttribute("prodotto", prodotto);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/modifica-prodotto.jsp");
            dispatcher.forward(request, response);
        }
    }
}