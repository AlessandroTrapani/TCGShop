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
 * Servlet che gestisce la creazione di un nuovo prodotto dall'area admin.
 *
 * Con GET mostra il form di inserimento.
 * Con POST valida i dati e salva il prodotto nel database.
 */
@WebServlet("/admin/nuovo-prodotto")
public class AdminNuovoProdottoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra il form per la creazione di un nuovo prodotto.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/nuovo-prodotto.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce l'invio del form nuovo prodotto.
     *
     * Recupera i dati dalla request, esegue controlli lato server,
     * crea un oggetto Prodotto e lo salva tramite ProdottoDAO.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String gioco = request.getParameter("gioco");
        String categoria = request.getParameter("categoria");
        String rarita = request.getParameter("rarita");
        String prezzoParametro = request.getParameter("prezzo");
        String quantitaParametro = request.getParameter("quantita");
        String immagine = request.getParameter("immagine");
        String descrizione = request.getParameter("descrizione");

        String errore = null;

        double prezzo = 0;
        int quantita = 0;

        /*
         * Controlla i campi obbligatori.
         */
        if (nome == null || nome.trim().equals("")
                || gioco == null || gioco.trim().equals("")
                || categoria == null || categoria.trim().equals("")
                || prezzoParametro == null || prezzoParametro.trim().equals("")
                || quantitaParametro == null || quantitaParametro.trim().equals("")) {

            errore = "Nome, gioco, categoria, prezzo e quantità sono obbligatori.";
        }

        /*
         * Converte prezzo e quantità solo se i campi obbligatori sono presenti.
         */
        if (errore == null) {
            try {
                prezzo = Double.parseDouble(prezzoParametro.trim().replace(",","."));
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

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.setAttribute("nome", nome);
            request.setAttribute("gioco", gioco);
            request.setAttribute("categoria", categoria);
            request.setAttribute("rarita", rarita);
            request.setAttribute("prezzo", prezzoParametro);
            request.setAttribute("quantita", quantitaParametro);
            request.setAttribute("immagine", immagine);
            request.setAttribute("descrizione", descrizione);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/nuovo-prodotto.jsp");
            dispatcher.forward(request, response);
            return;
        }

        /*
         * Calcola lo stato iniziale del prodotto in base alla quantità.
         */
        String stato = "DISPONIBILE";

        if (quantita == 0) {
            stato = "NON_DISPONIBILE";
        }

        Prodotto prodotto = new Prodotto();

        prodotto.setNome(nome.trim());
        prodotto.setGioco(gioco.trim());
        prodotto.setCategoria(categoria.trim());
        prodotto.setRarita(rarita != null ? rarita.trim() : "");
        prodotto.setPrezzo(prezzo);
        prodotto.setQuantita(quantita);
        prodotto.setImmagine(immagine != null ? immagine.trim() : "");
        prodotto.setDescrizione(descrizione != null ? descrizione.trim() : "");
        prodotto.setStato(stato);

        ProdottoDAO prodottoDAO = new ProdottoDAO();
        boolean inserito = prodottoDAO.inserisciProdotto(prodotto);

        if (inserito) {
            response.sendRedirect(request.getContextPath() + "/admin/prodotti");
        } else {
            request.setAttribute("errore", "Errore durante l'inserimento del prodotto.");

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/nuovo-prodotto.jsp");
            dispatcher.forward(request, response);
        }
    }
}