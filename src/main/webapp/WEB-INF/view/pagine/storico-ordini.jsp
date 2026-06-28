<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera la lista degli ordini passata dalla StoricoOrdiniServlet.
     */
    ArrayList<Ordine> ordini = (ArrayList<Ordine>) request.getAttribute("ordini");

    /*
     * Recupera l'utente loggato dalla sessione.
     * Serve per mostrare il menu Login/Logout e per proteggere la pagina.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
    
    /*
     * Recupera eventuale messaggio di successo salvato in sessione.
     * Viene usato, ad esempio, dopo un checkout completato.
     */
    String messaggioSuccesso = (String) session.getAttribute("messaggioSuccesso");

    /*
     * Rimuove subito il messaggio dalla sessione.
     * In questo modo viene mostrato una sola volta.
     */
    session.removeAttribute("messaggioSuccesso");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Storico ordini - TCGShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/stile.css">
</head>
<body>

    <!-- Intestazione principale del sito -->
    <header class="intestazione-sito">
        <div class="contenitore">
            <h1>TCGShop</h1>

            <!-- Menu principale pubblico -->
            <nav class="menu-principale">
                <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
                <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a>
                <a href="${pageContext.request.contextPath}/carrello">Carrello</a>
                <a href="${pageContext.request.contextPath}/storico-ordini">I miei ordini</a>

                <%
                    /*
                     * Se l'utente è loggato mostra Logout.
                     * Se invece non è loggato mostra Login.
                     */
                    if (utenteLoggato != null) {
                %>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                <%
                    } else {
                %>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                <%
                    }
                %>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della pagina storico ordini -->
    <main class="contenitore contenuto-pagina">

        <h2>I miei ordini</h2>
        
        <%
    		/*
    		 * Se è presente un messaggio di successo, lo mostro all'utente.
    		 */
    		if (messaggioSuccesso != null) {
		%>
    	<p class="messaggio-successo"><%= messaggioSuccesso %></p>
		<%
    	}
		%>

        <%
            /*
             * Se l'utente non ha ancora effettuato ordini, mostra un messaggio.
             */
            if (ordini == null || ordini.isEmpty()) {
        %>

            <p>Non hai ancora effettuato ordini.</p>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Vai al catalogo
            </a>

        <%
            } else {
        %>

            <!-- Tabella contenente gli ordini dell'utente -->
            <table class="tabella-carrello">
                <thead>
                    <tr>
                        <th>ID ordine</th>
                        <th>Data</th>
                        <th>Totale</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                </thead>

                <tbody>
                    <%
                        for (Ordine ordine : ordini) {
                    %>

                        <tr>
                            <td>#<%= ordine.getId() %></td>
                            <td><%= ordine.getDataOrdine() %></td>
                            <td>€ <%= ordine.getTotale() %></td>
                            <td><%= ordine.getStato() %></td>
                            <td>
                                <a class="bottone" href="${pageContext.request.contextPath}/dettaglio-ordine?id=<%= ordine.getId() %>">
                                    Dettaglio
                                </a>
                            </td>
                        </tr>

                    <%
                        }
                    %>
                </tbody>
            </table>

        <%
            }
        %>

    </main>

</body>
</html>