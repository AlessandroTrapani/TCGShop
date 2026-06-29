<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.DettaglioOrdine" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera l'ordine e l'eventuale errore passati dalla Servlet.
     */
    Ordine ordine = (Ordine) request.getAttribute("ordine");
    String errore = (String) request.getAttribute("errore");

    /*
     * Recupera eventuali messaggi temporanei salvati in sessione.
     * Questi messaggi vengono usati dopo l'aggiornamento dello stato.
     */
    String messaggioSuccesso = (String) session.getAttribute("messaggioSuccesso");
    String messaggioErrore = (String) session.getAttribute("messaggioErrore");

    /*
     * Rimuove i messaggi dalla sessione per mostrarli una sola volta.
     */
    session.removeAttribute("messaggioSuccesso");
    session.removeAttribute("messaggioErrore");

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
    <title>Dettaglio ordine admin - TCGShop</title>
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

    <!-- Contenuto principale della pagina dettaglio ordine admin -->
    <main class="contenitore contenuto-pagina">

        <h2>Dettaglio ordine admin</h2>
        
        <%
    /*
     * Mostra eventuale messaggio di successo.
     */
    if (messaggioSuccesso != null) {
%>
    <p class="messaggio-successo"><%= messaggioSuccesso %></p>
<%
    }
%>

<%
    /*
     * Mostra eventuale messaggio di errore.
     */
    if (messaggioErrore != null) {
%>
    <p class="messaggio-errore"><%= messaggioErrore %></p>
<%
    }
%>

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
             * Se è presente un errore, mostra il messaggio e il link di ritorno.
             */
            if (errore != null) {
        %>

            <p class="messaggio-errore"><%= errore %></p>

            <a class="bottone" href="${pageContext.request.contextPath}/admin/ordini">
                Torna agli ordini
            </a>

        <%
            } else if (ordine != null) {
        %>

            <h3>Ordine #<%= ordine.getId() %></h3>

            <!-- Dati generali dell'ordine -->
            <section class="box-info">
                <p><strong>ID utente:</strong> <%= ordine.getIdUtente() %></p>
                <p><strong>Data:</strong> <%= ordine.getDataOrdine() %></p>
                <p><strong>Stato:</strong> <%= ordine.getStato() %></p>
                <p><strong>Email consegna:</strong> <%= ordine.getEmailConsegna() %></p>
                <p><strong>Indirizzo spedizione:</strong> <%= ordine.getIndirizzoSpedizione() %></p>
                <p><strong>Metodo pagamento:</strong> <%= ordine.getMetodoPagamento() %></p>
                <p><strong>Totale ordine:</strong> € <%= ordine.getTotale() %></p>
            </section>
            
            <h3>Aggiorna stato ordine</h3>

<!--
    Form per aggiornare lo stato dell'ordine.
    I dati vengono inviati alla AdminAggiornaStatoOrdineServlet.
-->
<form 
    method="post" 
    action="${pageContext.request.contextPath}/admin/aggiorna-stato-ordine">

    <input type="hidden" name="id" value="<%= ordine.getId() %>">

    <label for="stato">Stato ordine</label>
    <select id="stato" name="stato">
        <option value="IN_ELABORAZIONE" <%= "IN_ELABORAZIONE".equals(ordine.getStato()) ? "selected" : "" %>>
            IN_ELABORAZIONE
        </option>

        <option value="SPEDITO" <%= "SPEDITO".equals(ordine.getStato()) ? "selected" : "" %>>
            SPEDITO
        </option>

        <option value="COMPLETATO" <%= "COMPLETATO".equals(ordine.getStato()) ? "selected" : "" %>>
            COMPLETATO
        </option>

        <option value="ANNULLATO" <%= "ANNULLATO".equals(ordine.getStato()) ? "selected" : "" %>>
            ANNULLATO
        </option>
    </select>

    <br><br>

    <button class="bottone" type="submit">
        Aggiorna stato
    </button>
</form>

            <h3>Prodotti acquistati</h3>

            <!-- Tabella dei prodotti acquistati nell'ordine -->
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

            <a class="bottone" href="${pageContext.request.contextPath}/admin/ordini">
                Torna agli ordini
            </a>

        <%
            }
        %>

    </main>

</body>
</html>