package filtro;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utente;

/**
 * Filtro che protegge le pagine riservate agli utenti normali.
 * 
 * Controlla che nella sessione siano presenti utenteLoggato e tokenAccesso.
 * Controlla inoltre che il ruolo dell'utente sia UTENTE.
 * Se l'utente non è autenticato, viene reindirizzato alla pagina di login.
 * Se l'utente è admin, viene reindirizzato all'area amministratore.
 */
@WebFilter(urlPatterns = {
        "/checkout",
        "/storico-ordini",
        "/dettaglio-ordine"
})
public class FiltroUtente implements Filter {

    /**
     * Intercetta le richieste verso le pagine utente protette.
     *
     * Se la sessione contiene un utente con ruolo UTENTE, la richiesta
     * prosegue verso la Servlet richiesta.
     * Se la sessione non è valida, l'utente viene mandato al login.
     * Se l'utente è ADMIN, viene mandato alla dashboard admin.
     *
     * @param request richiesta generica ricevuta dal filtro
     * @param response risposta generica inviata dal filtro
     * @param chain catena dei filtri e della risorsa richiesta
     * @throws IOException in caso di errore di input/output
     * @throws ServletException in caso di errore nel filtro
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest richiestaHttp = (HttpServletRequest) request;
        HttpServletResponse rispostaHttp = (HttpServletResponse) response;

        /*
         * Recupera la sessione esistente senza crearne una nuova.
         */
        HttpSession sessione = richiestaHttp.getSession(false);

        if (sessione == null
                || sessione.getAttribute("utenteLoggato") == null
                || sessione.getAttribute("tokenAccesso") == null) {

            /*
             * Se l'utente non è autenticato, viene mandato al login.
             */
            rispostaHttp.sendRedirect(richiestaHttp.getContextPath() + "/login");
            return;
        }

        Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");

        if ("ADMIN".equals(utenteLoggato.getRuolo())) {
            /*
             * Se l'utente è admin, non può usare le pagine riservate
             * al cliente e viene mandato alla dashboard admin.
             */
            rispostaHttp.sendRedirect(richiestaHttp.getContextPath() + "/admin/home");
            return;
        }

        if ("UTENTE".equals(utenteLoggato.getRuolo())) {
            /*
             * L'utente normale può proseguire verso la risorsa richiesta.
             */
            chain.doFilter(request, response);
            return;
        }

        /*
         * Qualsiasi altro ruolo non previsto viene mandato al login.
         */
        rispostaHttp.sendRedirect(richiestaHttp.getContextPath() + "/login");
    }
}