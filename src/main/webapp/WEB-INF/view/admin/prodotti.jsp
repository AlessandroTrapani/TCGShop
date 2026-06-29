<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera la lista dei prodotti passata dalla AdminProdottiServlet.
     */
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");

    /*
     * Recupera l'utente loggato dalla sessione.
     * In questa pagina dovrebbe essere sempre un admin perché l'accesso
     * viene protetto da FiltroAdmin.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione prodotti - TCGShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/stile.css">
</head>
<body>

    <!-- Intestazione principale dell'area admin -->
    <header class="intestazione-sito">
        <div class="contenitore">
            <h1>TCGShop - Area Admin</h1>

            <!-- Menu principale amministratore -->
            <nav class="menu-principale">
                <a href="${pageContext.request.contextPath}/index.jsp">Sito</a>
                <a href="${pageContext.request.contextPath}/admin/home">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/prodotti">Prodotti</a>
                <a href="${pageContext.request.contextPath}/admin/ordini">Ordini</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della gestione prodotti -->
    <main class="contenitore contenuto-pagina">

        <h2>Gestione prodotti</h2>

        <%
            /*
             * Mostra un messaggio introduttivo all'amministratore loggato.
             */
            if (utenteLoggato != null) {
        %>
            <p>
                Amministratore: <strong><%= utenteLoggato.getEmail() %></strong>
            </p>
        <%
            }
        %>

        <a class="bottone" href="${pageContext.request.contextPath}/admin/nuovo-prodotto">
            Nuovo prodotto
        </a>

        <hr>

        <%
            /*
             * Se non sono presenti prodotti, mostra un messaggio.
             */
            if (prodotti == null || prodotti.isEmpty()) {
        %>

            <p>Nessun prodotto presente.</p>

        <%
            } else {
        %>

            <!-- Tabella dei prodotti gestiti dall'amministratore -->
            <table class="tabella-carrello">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Gioco</th>
                        <th>Categoria</th>
                        <th>Prezzo</th>
                        <th>Quantità</th>
                        <th>Stato</th>
                        <th>Azioni</th>
                    </tr>
                </thead>

                <tbody>
                    <%
                        for (Prodotto prodotto : prodotti) {
                    %>

                        <tr>
                            <td><%= prodotto.getId() %></td>
                            <td><%= prodotto.getNome() %></td>
                            <td><%= prodotto.getGioco() %></td>
                            <td><%= prodotto.getCategoria() %></td>
                            <td>€ <%= prodotto.getPrezzo() %></td>
                            <td><%= prodotto.getQuantita() %></td>
                            <td><%= prodotto.getStato() %></td>
                            <td>
                                <a class="bottone" href="${pageContext.request.contextPath}/admin/modifica-prodotto?id=<%= prodotto.getId() %>">
                                    Modifica
                                </a>

                                <a class="bottone" href="${pageContext.request.contextPath}/admin/elimina-prodotto?id=<%= prodotto.getId() %>">
                                    Elimina
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