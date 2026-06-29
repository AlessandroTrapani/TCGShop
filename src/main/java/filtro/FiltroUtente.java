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

/**
 * Filtro che protegge le pagine riservate agli utenti loggati.
 * 
 * Controlla che nella sessione siano presenti utenteLoggato e tokenAccesso.
 * Se l'utente non è autenticato, viene reindirizzato alla pagina di login.
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
     * Se la sessione contiene utenteLoggato e tokenAccesso, la richiesta
     * prosegue verso la Servlet richiesta. In caso contrario, l'utente viene
     * reindirizzato alla pagina di login.
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
         * Se la sessione non esiste, significa che l'utente non è loggato.
         */
        HttpSession sessione = richiestaHttp.getSession(false);

        boolean utenteAutenticato = false;

        /*
         * Controlla la presenza degli attributi salvati durante il login.
         */
        if (sessione != null
                && sessione.getAttribute("utenteLoggato") != null
                && sessione.getAttribute("tokenAccesso") != null) {

            utenteAutenticato = true;
        }

        if (utenteAutenticato) {
            /*
             * La richiesta prosegue verso la Servlet richiesta.
             */
            chain.doFilter(request, response);
        } else {
            /*
             * L'utente non autenticato viene mandato al login.
             */
            rispostaHttp.sendRedirect(richiestaHttp.getContextPath() + "/login");
        }
    }
}