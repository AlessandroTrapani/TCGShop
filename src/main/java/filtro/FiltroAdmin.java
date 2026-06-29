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
 * Filtro che protegge l'area amministratore.
 * 
 * Controlla che l'utente sia autenticato, che abbia un token di accesso
 * e che il suo ruolo sia ADMIN.
 */
@WebFilter("/admin/*")
public class FiltroAdmin implements Filter {

    /**
     * Intercetta tutte le richieste verso URL che iniziano con /admin.
     * 
     * Se l'utente è loggato ed è amministratore, la richiesta prosegue.
     * Se l'utente non è loggato o non ha ruolo ADMIN, viene reindirizzato.
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
         * Recupera la sessione solo se esiste già.
         */
        HttpSession sessione = richiestaHttp.getSession(false);

        boolean adminAutorizzato = false;

        if (sessione != null
                && sessione.getAttribute("utenteLoggato") != null
                && sessione.getAttribute("tokenAccesso") != null) {

            Utente utenteLoggato = (Utente) sessione.getAttribute("utenteLoggato");

            /*
             * Controlla che il ruolo dell'utente sia ADMIN.
             */
            if ("ADMIN".equals(utenteLoggato.getRuolo())) {
                adminAutorizzato = true;
            }
        }

        if (adminAutorizzato) {
            /*
             * L'amministratore può accedere alla risorsa richiesta.
             */
            chain.doFilter(request, response);
        } else {
            /*
             * Se l'utente non è autorizzato, viene reindirizzato alla homepage.
             */
            rispostaHttp.sendRedirect(richiestaHttp.getContextPath() + "/home");
        }
    }
}