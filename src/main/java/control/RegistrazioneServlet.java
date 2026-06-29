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
 * Servlet che gestisce la registrazione degli utenti.
 * 
 * Con il metodo GET mostra il form di registrazione.
 * Con il metodo POST valida i dati ricevuti e inserisce il nuovo utente
 * nella tabella utenti.
 */
@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la pagina JSP contenente il form di registrazione.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/registrazione.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce l'invio del form di registrazione.
     * 
     * Recupera i dati dalla request, esegue controlli lato server,
     * verifica che l'email non sia già presente e salva il nuovo utente.
     *
     * @param request richiesta HTTP ricevuta dal form
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nella Servlet
     * @throws IOException in caso di errore di input/output
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("confermaPassword");

        String errore = null;

        /*
         * Controllo lato server dei campi obbligatori.
         * Anche se più avanti useremo JavaScript, il controllo server resta necessario.
         */
        if (nome == null || nome.trim().equals("")
                || cognome == null || cognome.trim().equals("")
                || email == null || email.trim().equals("")
                || password == null || password.trim().equals("")
                || confermaPassword == null || confermaPassword.trim().equals("")) {

            errore = "Tutti i campi sono obbligatori.";

        } else if (password.trim().length() < 5) {

            errore = "La password deve contenere almeno 5 caratteri.";

        } else if (!password.equals(confermaPassword)) {

            errore = "Le password non coincidono.";

        } else {
            UtenteDAO utenteDAO = new UtenteDAO();

            /*
             * Prima di inserire l'utente controllo che l'email
             * non sia già presente nel database.
             */
            if (utenteDAO.emailEsistente(email.trim())) {
                errore = "Email già registrata.";
            } else {
                Utente nuovoUtente = new Utente();

                nuovoUtente.setNome(nome.trim());
                nuovoUtente.setCognome(cognome.trim());
                nuovoUtente.setEmail(email.trim());
                nuovoUtente.setPassword(password.trim());
                nuovoUtente.setRuolo("UTENTE");

                boolean inserito = utenteDAO.inserisciUtente(nuovoUtente);

                if (inserito) {
                    /*
                     * Dopo la registrazione recupero l'utente appena creato
                     * e lo salvo in sessione, così l'utente risulta già loggato.
                     */
                    Utente utenteCreato = utenteDAO.trovaPerEmailEPassword(email.trim(), password.trim());

                    HttpSession sessione = request.getSession();
                    sessione.setAttribute("utenteLoggato", utenteCreato);

                    String tokenAccesso = UUID.randomUUID().toString();
                    sessione.setAttribute("tokenAccesso", tokenAccesso);

                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    return;
                } else {
                    errore = "Errore durante la registrazione.";
                }
            }
        }

        /*
         * Se c'è un errore, torno alla JSP mostrando il messaggio
         * e mantenendo i dati già inseriti, tranne la password.
         */
        request.setAttribute("errore", errore);
        request.setAttribute("nome", nome);
        request.setAttribute("cognome", cognome);
        request.setAttribute("email", email);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/pagine/registrazione.jsp");
        dispatcher.forward(request, response);
    }
}