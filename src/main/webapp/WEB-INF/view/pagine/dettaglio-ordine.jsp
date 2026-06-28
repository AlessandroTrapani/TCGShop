<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.DettaglioOrdine" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupero l'ordine e l'eventuale errore passati dalla Servlet.
     */
    Ordine ordine = (Ordine) request.getAttribute("ordine");
    String errore = (String) request.getAttribute("errore");

    /*
     * Recupero l'utente loggato dalla sessione.
     * Serve per il menu e per confermare che l'utente sia autenticato.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dettaglio ordine - TCGShop</title>
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

    <!-- Contenuto principale della pagina dettaglio ordine -->
    <main class="contenitore contenuto-pagina">

        <%
            if (errore != null) {
        %>

            <h2>Errore</h2>
            <p class="messaggio-errore"><%= errore %></p>

            <a class="bottone" href="${pageContext.request.contextPath}/storico-ordini">
                Torna ai miei ordini
            </a>

        <%
            } else if (ordine != null) {
        %>

            <h2>Dettaglio ordine #<%= ordine.getId() %></h2>

            <!-- Dati generali dell'ordine -->
            <section class="box-info">
                <p><strong>Data:</strong> <%= ordine.getDataOrdine() %></p>
                <p><strong>Stato:</strong> <%= ordine.getStato() %></p>
                <p><strong>Email consegna:</strong> <%= ordine.getEmailConsegna() %></p>
                <p><strong>Indirizzo spedizione:</strong> <%= ordine.getIndirizzoSpedizione() %></p>
                <p><strong>Metodo pagamento:</strong> <%= ordine.getMetodoPagamento() %></p>
                <p><strong>Totale ordine:</strong> € <%= ordine.getTotale() %></p>
            </section>

            <h3>Prodotti acquistati</h3>

            <!-- Tabella dei prodotti acquistati -->
            <table class="tabella-carrello">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Prezzo storico</th>
                        <th>Quantità</th>
                        <th>Totale riga</th>
                    </tr>
                </thead>

                <tbody>
                    <%
                        for (DettaglioOrdine dettaglio : ordine.getDettagli()) {
                    %>

                        <tr>
                            <td><%= dettaglio.getNomeProdotto() %></td>
                            <td>€ <%= dettaglio.getPrezzo() %></td>
                            <td><%= dettaglio.getQuantita() %></td>
                            <td>€ <%= dettaglio.getTotaleRiga() %></td>
                        </tr>

                    <%
                        }
                    %>
                </tbody>
            </table>

            <a class="bottone" href="${pageContext.request.contextPath}/storico-ordini">
                Torna ai miei ordini
            </a>

        <%
            }
        %>

    </main>

</body>
</html>