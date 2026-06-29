package control.admin;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet che gestisce la homepage dell'area amministratore.
 * 
 * La Servlet viene raggiunta tramite l'URL /admin/home.
 * L'accesso viene protetto da FiltroAdmin, che controlla che l'utente
 * sia loggato e abbia ruolo ADMIN.
 */
@WebServlet("/admin/home")
public class AdminHomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Mostra la dashboard principale dell'amministratore.
     *
     * @param request richiesta HTTP ricevuta dal browser
     * @param response risposta HTTP inviata al browser
     * @throws ServletException in caso di errore nel forward
     * @throws IOException in caso di errore di input/output
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Inoltra la richiesta alla JSP della dashboard admin.
         */
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/home.jsp");
        dispatcher.forward(request, response);
    }
}