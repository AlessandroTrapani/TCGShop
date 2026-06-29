<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera il prodotto e l'eventuale messaggio di errore passati dalla Servlet.
     */
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    String errore = (String) request.getAttribute("errore");

    /*
     * Recupera l'utente loggato dalla sessione.
     * L'accesso alla pagina viene protetto da FiltroAdmin.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Modifica prodotto - TCGShop</title>
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

    <!-- Contenuto principale della pagina modifica prodotto -->
    <main class="contenitore contenuto-pagina">

        <h2>Modifica prodotto</h2>

        <%
            /*
             * Mostra eventuale messaggio di errore.
             */
            if (errore != null) {
        %>
            <p class="messaggio-errore"><%= errore %></p>
        <%
            }
        %>

        <%
            /*
             * Mostra l'admin attualmente collegato.
             */
            if (utenteLoggato != null) {
        %>
            <p>Admin: <strong><%= utenteLoggato.getEmail() %></strong></p>
        <%
            }
        %>

        <%
            /*
             * Se il prodotto non esiste, mostra solo un link di ritorno.
             */
            if (prodotto == null) {
        %>

            <a class="bottone" href="${pageContext.request.contextPath}/admin/prodotti">
                Torna ai prodotti
            </a>

        <%
            } else {
        %>

            <!--
                Form per modificare un prodotto.
                I dati vengono inviati alla AdminModificaProdottoServlet.
            -->
            <form method="post" action="${pageContext.request.contextPath}/admin/modifica-prodotto">

                <input type="hidden" name="id" value="<%= prodotto.getId() %>">

                <label for="nome">Nome prodotto</label>
                <input 
                    type="text" 
                    id="nome" 
                    name="nome" 
                    value="<%= prodotto.getNome() %>">

                <label for="gioco">Gioco</label>
                <select id="gioco" name="gioco">
                    <option value="">Seleziona</option>
                    <option value="Pokémon" <%= "Pokémon".equals(prodotto.getGioco()) ? "selected" : "" %>>Pokémon</option>
                    <option value="Magic" <%= "Magic".equals(prodotto.getGioco()) ? "selected" : "" %>>Magic</option>
                    <option value="One Piece" <%= "One Piece".equals(prodotto.getGioco()) ? "selected" : "" %>>One Piece</option>
                    <option value="Accessori" <%= "Accessori".equals(prodotto.getGioco()) ? "selected" : "" %>>Accessori</option>
                </select>

                <label for="categoria">Categoria</label>
                <select id="categoria" name="categoria">
                    <option value="">Seleziona</option>
                    <option value="Box" <%= "Box".equals(prodotto.getCategoria()) ? "selected" : "" %>>Box</option>
                    <option value="Bustina" <%= "Bustina".equals(prodotto.getCategoria()) ? "selected" : "" %>>Bustina</option>
                    <option value="Mazzo" <%= "Mazzo".equals(prodotto.getCategoria()) ? "selected" : "" %>>Mazzo</option>
                    <option value="Carta singola" <%= "Carta singola".equals(prodotto.getCategoria()) ? "selected" : "" %>>Carta singola</option>
                    <option value="Accessori" <%= "Accessori".equals(prodotto.getCategoria()) ? "selected" : "" %>>Accessori</option>
                </select>

                <label for="rarita">Rarità</label>
                <input 
                    type="text" 
                    id="rarita" 
                    name="rarita" 
                    value="<%= prodotto.getRarita() != null ? prodotto.getRarita() : "" %>">

                <label for="prezzo">Prezzo</label>
                <input 
                    type="text" 
                    id="prezzo" 
                    name="prezzo" 
                    value="<%= prodotto.getPrezzo() %>">

                <label for="quantita">Quantità</label>
                <input 
                    type="number" 
                    id="quantita" 
                    name="quantita" 
                    value="<%= prodotto.getQuantita() %>"
                    min="0">

                <label for="immagine">Nome file immagine</label>
                <input 
                    type="text" 
                    id="immagine" 
                    name="immagine" 
                    value="<%= prodotto.getImmagine() != null ? prodotto.getImmagine() : "" %>"
                    placeholder="es. box-pokemon.jpg">

                <label for="descrizione">Descrizione</label>
                <textarea 
                    id="descrizione" 
                    name="descrizione" 
                    rows="5"><%= prodotto.getDescrizione() != null ? prodotto.getDescrizione() : "" %></textarea>

                <br><br>

                <button class="bottone" type="submit">
                    Salva modifiche
                </button>

                <a class="bottone" href="${pageContext.request.contextPath}/admin/prodotti">
                    Annulla
                </a>

            </form>

        <%
            }
        %>

    </main>

</body>
</html>