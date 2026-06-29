package control;

import java.io.IOException;

import dao.ProdottoDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Carrello;
import model.Prodotto;
import model.Utente;

/**
 * Servlet che gestisce il carrello dell'utente.
 * 
 * Il carrello viene salvato nella sessione HTTP, quindi resta disponibile
 * durante la navigazione dell'utente nel sito.
 */
@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la pagina del carrello.
     *
     * Se l'utente loggato è un amministratore, viene reindirizzato
     * alla dashboard admin perché non deve usare il carrello cliente.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessione = request.getSession();

        /*
         * Controlla se l'utente loggato è un amministratore.
         * In quel caso blocca l'accesso al carrello e lo rimanda
         * alla dashboard admin.
         */
        if (adminLoggato(sessione)) {
            response.sendRedirect(request.getContextPath() + "/admin/home");
            return;
        }

        /*
         * Recupera il carrello dalla sessione.
         * Se non esiste ancora, ne crea uno nuovo.
         */
        Carrello carrello = (Carrello) sessione.getAttribute("carrello");

        if (carrello == null) {
            carrello = new Carrello();
            sessione.setAttribute("carrello", carrello);
        }

        request.setAttribute("carrello", carrello);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/carrello.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce le azioni sul carrello.
     * 
     * Le azioni previste sono:
     * - aggiungi
     * - aggiorna
     * - rimuovi
     * - svuota
     *
     * Se l'utente loggato è un amministratore, viene reindirizzato
     * alla dashboard admin.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessione = request.getSession();

        /*
         * Blocca qualsiasi modifica al carrello se l'utente loggato
         * è un amministratore.
         */
        if (adminLoggato(sessione)) {
            response.sendRedirect(request.getContextPath() + "/admin/home");
            return;
        }

        /*
         * Recupera il carrello dalla sessione.
         * Se non esiste, lo crea.
         */
        Carrello carrello = (Carrello) sessione.getAttribute("carrello");

        if (carrello == null) {
            carrello = new Carrello();
            sessione.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");

        if (azione == null) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        if (azione.equals("aggiungi")) {
            aggiungiProdotto(request, carrello);
        } else if (azione.equals("aggiorna")) {
            aggiornaQuantita(request, carrello);
        } else if (azione.equals("rimuovi")) {
            rimuoviProdotto(request, carrello);
        } else if (azione.equals("svuota")) {
            carrello.svuota();
        }

        /*
         * Dopo ogni modifica salva il carrello aggiornato in sessione
         * e torna alla pagina carrello.
         */
        sessione.setAttribute("carrello", carrello);
        response.sendRedirect(request.getContextPath() + "/carrello");
    }

    /**
     * Controlla se nella sessione è presente un utente con ruolo ADMIN.
     *
     * @param sessione sessione HTTP corrente
     * @return true se l'utente loggato è admin, false altrimenti
     */
    private boolean adminLoggato(HttpSession sessione) {
        boolean admin = false;

        if (sessione != null) {
            Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");

            if (utenteLoggato != null && "ADMIN".equals(utenteLoggato.getRuolo())) {
                admin = true;
            }
        }

        return admin;
    }

    /**
     * Aggiunge un prodotto al carrello.
     * 
     * Recupera id prodotto e quantità dalla request, cerca il prodotto
     * nel database tramite ProdottoDAO e lo aggiunge al carrello.
     *
     * @param request richiesta HTTP con i parametri del prodotto
     * @param carrello carrello salvato in sessione
     */
    private void aggiungiProdotto(HttpServletRequest request, Carrello carrello) {
        try {
            int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
            int quantita = Integer.parseInt(request.getParameter("quantita"));

            if (quantita <= 0) {
                quantita = 1;
            }

            ProdottoDAO prodottoDAO = new ProdottoDAO();
            Prodotto prodotto = prodottoDAO.trovaPerId(idProdotto);

            /*
             * Aggiunge il prodotto solo se esiste, è disponibile
             * e la quantità richiesta non supera quella presente in magazzino.
             */
            if (prodotto != null
                    && "DISPONIBILE".equals(prodotto.getStato())
                    && prodotto.getQuantita() > 0) {

                if (quantita > prodotto.getQuantita()) {
                    quantita = prodotto.getQuantita();
                }

                carrello.aggiungiProdotto(prodotto, quantita);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna la quantità di un prodotto nel carrello.
     *
     * @param request richiesta HTTP con id prodotto e nuova quantità
     * @param carrello carrello salvato in sessione
     */
    private void aggiornaQuantita(HttpServletRequest request, Carrello carrello) {
        try {
            int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
            int quantita = Integer.parseInt(request.getParameter("quantita"));

            carrello.aggiornaQuantita(idProdotto, quantita);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rimuove un prodotto dal carrello.
     *
     * @param request richiesta HTTP con id prodotto da rimuovere
     * @param carrello carrello salvato in sessione
     */
    private void rimuoviProdotto(HttpServletRequest request, Carrello carrello) {
        try {
            int idProdotto = Integer.parseInt(request.getParameter("idProdotto"));
            carrello.rimuoviProdotto(idProdotto);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}