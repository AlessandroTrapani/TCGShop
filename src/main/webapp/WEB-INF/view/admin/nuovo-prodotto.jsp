<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera eventuali messaggi e valori ripassati dalla Servlet.
     * Questi valori servono per ripopolare il form in caso di errore.
     */
    String errore = (String) request.getAttribute("errore");
    String nome = (String) request.getAttribute("nome");
    String gioco = (String) request.getAttribute("gioco");
    String categoria = (String) request.getAttribute("categoria");
    String rarita = (String) request.getAttribute("rarita");
    String prezzo = (String) request.getAttribute("prezzo");
    String quantita = (String) request.getAttribute("quantita");
    String immagine = (String) request.getAttribute("immagine");
    String descrizione = (String) request.getAttribute("descrizione");

    /*
     * Recupera l'utente loggato dalla sessione.
     * L'accesso alla pagina viene protetto da FiltroAdmin.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    if (nome == null) {
        nome = "";
    }

    if (gioco == null) {
        gioco = "";
    }

    if (categoria == null) {
        categoria = "";
    }

    if (rarita == null) {
        rarita = "";
    }

    if (prezzo == null) {
        prezzo = "";
    }

    if (quantita == null) {
        quantita = "";
    }

    if (immagine == null) {
        immagine = "";
    }

    if (descrizione == null) {
        descrizione = "";
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Nuovo prodotto - TCGShop</title>
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

    <!-- Contenuto principale della pagina nuovo prodotto -->
    <main class="contenitore contenuto-pagina">

        <h2>Nuovo prodotto</h2>

        <%
            /*
             * Mostra eventuale messaggio di errore passato dalla Servlet.
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

        <!--
            Form per creare un nuovo prodotto.
            I dati vengono inviati alla AdminNuovoProdottoServlet.
        -->
        <form method="post" action="${pageContext.request.contextPath}/admin/nuovo-prodotto">

            <label for="nome">Nome prodotto</label>
            <input 
                type="text" 
                id="nome" 
                name="nome" 
                value="<%= nome %>">

            <label for="gioco">Gioco</label>
            <select id="gioco" name="gioco">
                <option value="">Seleziona</option>
                <option value="Pokémon" <%= "Pokémon".equals(gioco) ? "selected" : "" %>>Pokémon</option>
                <option value="Magic" <%= "Magic".equals(gioco) ? "selected" : "" %>>Magic</option>
                <option value="One Piece" <%= "One Piece".equals(gioco) ? "selected" : "" %>>One Piece</option>
                <option value="Accessori" <%= "Accessori".equals(gioco) ? "selected" : "" %>>Accessori</option>
            </select>

            <label for="categoria">Categoria</label>
            <select id="categoria" name="categoria">
                <option value="">Seleziona</option>
                <option value="Box" <%= "Box".equals(categoria) ? "selected" : "" %>>Box</option>
                <option value="Bustina" <%= "Bustina".equals(categoria) ? "selected" : "" %>>Bustina</option>
                <option value="Mazzo" <%= "Mazzo".equals(categoria) ? "selected" : "" %>>Mazzo</option>
                <option value="Carta singola" <%= "Carta singola".equals(categoria) ? "selected" : "" %>>Carta singola</option>
                <option value="Accessori" <%= "Accessori".equals(categoria) ? "selected" : "" %>>Accessori</option>
            </select>

            <label for="rarita">Rarità</label>
            <input 
                type="text" 
                id="rarita" 
                name="rarita" 
                value="<%= rarita %>">

            <label for="prezzo">Prezzo</label>
            <input 
                type="text" 
                id="prezzo" 
                name="prezzo" 
                value="<%= prezzo %>">

            <label for="quantita">Quantità</label>
            <input 
                type="number" 
                id="quantita" 
                name="quantita" 
                value="<%= quantita %>"
                min="0">

            <label for="immagine">Nome file immagine</label>
            <input 
                type="text" 
                id="immagine" 
                name="immagine" 
                value="<%= immagine %>"
                placeholder="es. box-pokemon.jpg">

            <label for="descrizione">Descrizione</label>
            <textarea 
                id="descrizione" 
                name="descrizione" 
                rows="5"><%= descrizione %></textarea>

            <br><br>

            <button class="bottone" type="submit">
                Salva prodotto
            </button>

            <a class="bottone" href="${pageContext.request.contextPath}/admin/prodotti">
                Annulla
            </a>

        </form>

    </main>

</body>
</html>