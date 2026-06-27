package control;

import java.io.IOException;
import java.util.UUID;
import dao.UtenteDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Utente;

/**
 * Servlet che gestisce il login degli utenti.
 * 
 * Mostra il form di login con il metodo GET e controlla le credenziali
 * inserite dall'utente con il metodo POST.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la pagina JSP contenente il form di login.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/login.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce l'invio del form di login.
     * 
     * Recupera email e password dalla request, controlla le credenziali
     * tramite UtenteDAO e, se sono corrette, salva l'utente in sessione.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String errore = null;

        if (email == null || email.trim().equals("") || password == null || password.trim().equals("")) {
            errore = "Email e password sono obbligatorie.";
        } else {
            UtenteDAO utenteDAO = new UtenteDAO();
            Utente utente = utenteDAO.trovaPerEmailEPassword(email.trim(), password.trim());

            if (utente == null) {
                errore = "Credenziali non valide.";
            } else {
                /*
                 * Se il login è corretto, crea o recupera la sessione.
                 * Dentro la sessione salva l'utente loggato e un token di accesso.
                 */
                HttpSession sessione = request.getSession();
                sessione.setAttribute("utenteLoggato", utente);

                /*
                 * Il tokenAccesso verrà usato
                 * per verificare che l'utente abbia effettuato il login.
                 */
                String tokenAccesso = UUID.randomUUID().toString();
                sessione.setAttribute("tokenAccesso", tokenAccesso);

                /*
                 * Se l'utente è ADMIN, viene mandato verso l'area amministratore.
                 */
                if ("ADMIN".equals(utente.getRuolo())) {
                    response.sendRedirect(request.getContextPath() + "/admin/home");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                }

                return;
            }
        }

        /*
         * Se ci sono errori, torna alla pagina login mostrando il messaggio.
         */
        request.setAttribute("errore", errore);
        request.setAttribute("email", email);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/login.jsp");
        dispatcher.forward(request, response);
    }
}