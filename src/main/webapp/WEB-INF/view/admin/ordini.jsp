<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera la lista degli ordini passata dalla AdminOrdiniServlet.
     */
    ArrayList<Ordine> ordini = (ArrayList<Ordine>) request.getAttribute("ordini");

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
    <title>Gestione ordini - TCGShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/stile.css">
</head>
<body>

    <!-- Intestazione principale dell'area admin -->
    <header class="intestazione-sito">
        <div class="contenitore">
            <h1>TCGShop - Area Admin</h1>

            <!-- Menu principale amministratore -->
            <nav class="menu-principale">
                <a href="${pageContext.request.contextPath}/admin/home">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/prodotti">Prodotti</a>
                <a href="${pageContext.request.contextPath}/admin/ordini">Ordini</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della gestione ordini -->
    <main class="contenitore contenuto-pagina">

        <h2>Gestione ordini</h2>

        <%
            /*
             * Mostra l'admin attualmente collegato.
             */
            if (utenteLoggato != null) {
        %>
            <p>
                Amministratore: <strong><%= utenteLoggato.getEmail() %></strong>
            </p>
        <%
            }
        %>

        <%
            /*
             * Se non sono presenti ordini, mostra un messaggio.
             */
            if (ordini == null || ordini.isEmpty()) {
        %>

            <p>Nessun ordine presente.</p>

        <%
            } else {
        %>

            <!-- Tabella degli ordini gestiti dall'amministratore -->
            <table class="tabella-carrello">
                <thead>
                    <tr>
                        <th>ID ordine</th>
                        <th>ID utente</th>
                        <th>Data</th>
                        <th>Email consegna</th>
                        <th>Totale</th>
                        <th>Metodo pagamento</th>
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
                            <td><%= ordine.getIdUtente() %></td>
                            <td><%= ordine.getDataOrdine() %></td>
                            <td><%= ordine.getEmailConsegna() %></td>
                            <td>€ <%= ordine.getTotale() %></td>
                            <td><%= ordine.getMetodoPagamento() %></td>
                            <td><%= ordine.getStato() %></td>
                            <td>
                                <a class="bottone" href="${pageContext.request.contextPath}/admin/dettaglio-ordine?id=<%= ordine.getId() %>">
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