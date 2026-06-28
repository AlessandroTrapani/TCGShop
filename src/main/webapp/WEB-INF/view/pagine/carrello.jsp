<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupero il carrello passato dalla CarrelloServlet.
     * Se non è presente, la pagina lo considera vuoto.
     */
    Carrello carrello = (Carrello) request.getAttribute("carrello");

    /*
     * Recupero l'eventuale utente loggato dalla sessione.
     * Serve per mostrare Login oppure Logout nel menu.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Carrello - TCGShop</title>
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

                <%
                    /*
                     * Se l'utente è loggato mostro Logout.
                     * Se invece non è loggato mostro Login.
                     */
                    if (utenteLoggato != null) {
                %>
                	<a href="${pageContext.request.contextPath}/storico-ordini">I miei ordini</a>
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

    <!-- Contenuto principale della pagina carrello -->
    <main class="contenitore contenuto-pagina">

        <h2>Carrello</h2>

        <%
            /*
             * Se il carrello non esiste o è vuoto, mostro un messaggio.
             */
            if (carrello == null || carrello.isVuoto()) {
        %>

            <p>Il carrello è vuoto.</p>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Vai al catalogo
            </a>

        <%
            } else {
        %>

            <!-- Tabella dei prodotti presenti nel carrello -->
            <table class="tabella-carrello">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Prezzo</th>
                        <th>Quantità</th>
                        <th>Totale riga</th>
                        <th>Azioni</th>
                    </tr>
                </thead>

                <tbody>
                    <%
                        for (ElementoCarrello elemento : carrello.getElementi()) {
                    %>

                        <tr>
                            <td><%= elemento.getProdotto().getNome() %></td>
                            <td>€ <%= elemento.getProdotto().getPrezzo() %></td>

                            <td>
                                <!-- Form per aggiornare la quantità del prodotto -->
                                <form method="post" action="${pageContext.request.contextPath}/carrello">
                                    <input type="hidden" name="azione" value="aggiorna">
                                    <input type="hidden" name="idProdotto" value="<%= elemento.getProdotto().getId() %>">

                                    <input 
    									type="number" 
    									name="quantita" 
    									value="<%= elemento.getQuantita() %>" 
    									min="1"
    									max="<%= elemento.getProdotto().getQuantita() %>">

                                    <button class="bottone" type="submit">
                                        Aggiorna
                                    </button>
                                </form>
                            </td>

                            <td>€ <%= elemento.getTotaleRiga() %></td>

                            <td>
                                <!-- Form per rimuovere il prodotto dal carrello -->
                                <form method="post" action="${pageContext.request.contextPath}/carrello">
                                    <input type="hidden" name="azione" value="rimuovi">
                                    <input type="hidden" name="idProdotto" value="<%= elemento.getProdotto().getId() %>">

                                    <button class="bottone" type="submit">
                                        Rimuovi
                                    </button>
                                </form>
                            </td>
                        </tr>

                    <%
                        }
                    %>
                </tbody>
            </table>

            <h3>Totale carrello: € <%= carrello.getTotale() %></h3>

            <!-- Form per svuotare completamente il carrello -->
            <form method="post" action="${pageContext.request.contextPath}/carrello">
                <input type="hidden" name="azione" value="svuota">

                <button class="bottone" type="submit">
                    Svuota carrello
                </button>
            </form>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Continua gli acquisti
            </a>

            <a class="bottone" href="${pageContext.request.contextPath}/checkout">
                Procedi al checkout
            </a>

        <%
            }
        %>

    </main>

</body>
</html>