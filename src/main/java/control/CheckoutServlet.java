package control;

import java.io.IOException;

import dao.OrdineDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Carrello;
import model.Ordine;
import model.Utente;

/**
 * Servlet che gestisce il checkout dell'utente.
 * 
 * Il checkout trasforma il carrello salvato in sessione in un ordine
 * persistente nel database.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la pagina di checkout.
     * 
     * L'utente deve essere loggato e il carrello non deve essere vuoto.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessione = request.getSession();

        Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");
        Carrello carrello = (Carrello) sessione.getAttribute("carrello");

        /*
         * Se l'utente non è loggato, non può completare l'acquisto.
         * Lo mando alla pagina login.
         */
        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        /*
         * Se il carrello non esiste o è vuoto, non ha senso mostrare il checkout.
         */
        if (carrello == null || carrello.isVuoto()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        request.setAttribute("carrello", carrello);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/checkout.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce la conferma dell'ordine.
     * 
     * Recupera i dati dal form, crea un oggetto Ordine e chiama OrdineDAO
     * per salvare l'ordine nel database.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessione = request.getSession();

        Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");
        Carrello carrello = (Carrello) sessione.getAttribute("carrello");

        if (utenteLoggato == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (carrello == null || carrello.isVuoto()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        String emailConsegna = request.getParameter("emailConsegna");
        String indirizzoSpedizione = request.getParameter("indirizzoSpedizione");
        String metodoPagamento = request.getParameter("metodoPagamento");
        String numeroCarta = request.getParameter("numeroCarta");
        String cvv = request.getParameter("cvv");

        String errore = null;

        /*
         * Controllo lato server dei campi obbligatori.
         * Il server controlla sempre i dati, anche se esiste già
         * la validazione JavaScript lato client.
         */
        if (emailConsegna == null || emailConsegna.trim().equals("")
                || indirizzoSpedizione == null || indirizzoSpedizione.trim().equals("")
                || metodoPagamento == null || metodoPagamento.trim().equals("")) {

            errore = "Email, indirizzo e metodo di pagamento sono obbligatori.";

        } else if (!emailConsegna.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

            errore = "Inserisci un indirizzo email valido.";

        } else if ("Carta".equals(metodoPagamento)) {

            /*
             * I dati della carta vengono richiesti solo se il metodo di pagamento
             * selezionato è Carta.
             */
            if (numeroCarta == null || numeroCarta.trim().equals("")
                    || cvv == null || cvv.trim().equals("")) {

                errore = "I dati della carta sono obbligatori.";

            } else if (!numeroCarta.trim().matches("\\d{12,19}")) {

                errore = "Il numero carta deve contenere solo cifre, da 12 a 19 caratteri.";

            } else if (!cvv.trim().matches("\\d{3}")) {

                errore = "Il CVV deve contenere 3 cifre.";
            }

        } else if (!"Contanti".equals(metodoPagamento)) {

            errore = "Metodo di pagamento non valido.";
        }

        if (errore != null) {
            request.setAttribute("errore", errore);
            request.setAttribute("carrello", carrello);
            request.setAttribute("emailConsegna", emailConsegna);
            request.setAttribute("indirizzoSpedizione", indirizzoSpedizione);
            request.setAttribute("metodoPagamento", metodoPagamento);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/checkout.jsp");
            dispatcher.forward(request, response);
            return;
        }

        /*
         * Creo l'oggetto Ordine con i dati generali dell'acquisto.
         * I dettagli dei prodotti vengono ricavati dal carrello dentro OrdineDAO.
         */
        Ordine ordine = new Ordine();

        ordine.setIdUtente(utenteLoggato.getId());
        ordine.setTotale(carrello.getTotale());
        ordine.setEmailConsegna(emailConsegna.trim());
        ordine.setIndirizzoSpedizione(indirizzoSpedizione.trim());
        ordine.setMetodoPagamento(metodoPagamento.trim());
        ordine.setStato("IN_ELABORAZIONE");

        OrdineDAO ordineDAO = new OrdineDAO();
        boolean salvato = ordineDAO.salvaOrdine(ordine, carrello);

        if (salvato) {
            /*
             * Dopo il salvataggio dell'ordine, il carrello viene svuotato.
             */
            carrello.svuota();
            sessione.setAttribute("carrello", carrello);

            response.sendRedirect(request.getContextPath() + "/storico-ordini");
        } else {
            request.setAttribute("errore", "Errore durante il salvataggio dell'ordine.");
            request.setAttribute("carrello", carrello);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/checkout.jsp");
            dispatcher.forward(request, response);
        }
    }
}