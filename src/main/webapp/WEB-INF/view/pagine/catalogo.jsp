<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera dalla request la lista dei prodotti inserita dalla Servlet.
     * La Servlet CatalogoServlet salva questo attributo prima di fare forward.
     */
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");

    String ricerca = (String) request.getAttribute("ricerca");
    String gioco = (String) request.getAttribute("gioco");
    String categoria = (String) request.getAttribute("categoria");

    /*
     * Recupero l'eventuale utente loggato dalla sessione.
     * Serve per mostrare Login oppure Logout nel menu.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
    
    if (ricerca == null) {
        ricerca = "";
    }

    if (gioco == null) {
        gioco = "";
    }

    if (categoria == null) {
        categoria = "";
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Catalogo - TCGShop</title>
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

    <!-- Contenuto principale della pagina catalogo -->
    <main class="contenitore contenuto-pagina">

        <h2>Catalogo prodotti</h2>

        <!-- Form di filtro prodotti -->
        <form method="get" action="${pageContext.request.contextPath}/catalogo">

            <label for="ricerca">Cerca prodotto</label>
            <input 
                type="text" 
                id="ricerca" 
                name="ricerca" 
                value="<%= ricerca %>"
                placeholder="Es. Charizard, Box, Magic">

            <label for="gioco">Gioco</label>
            <select id="gioco" name="gioco">
                <option value="">Tutti</option>
                <option value="Pokémon" <%= "Pokémon".equals(gioco) ? "selected" : "" %>>Pokémon</option>
                <option value="Magic" <%= "Magic".equals(gioco) ? "selected" : "" %>>Magic</option>
                <option value="One Piece" <%= "One Piece".equals(gioco) ? "selected" : "" %>>One Piece</option>
                <option value="Accessori" <%= "Accessori".equals(gioco) ? "selected" : "" %>>Accessori</option>
            </select>

            <label for="categoria">Categoria</label>
            <select id="categoria" name="categoria">
                <option value="">Tutte</option>
                <option value="Box" <%= "Box".equals(categoria) ? "selected" : "" %>>Box</option>
                <option value="Bustina" <%= "Bustina".equals(categoria) ? "selected" : "" %>>Bustina</option>
                <option value="Mazzo" <%= "Mazzo".equals(categoria) ? "selected" : "" %>>Mazzo</option>
                <option value="Carta singola" <%= "Carta singola".equals(categoria) ? "selected" : "" %>>Carta singola</option>
                <option value="Accessori" <%= "Accessori".equals(categoria) ? "selected" : "" %>>Accessori</option>
            </select>

            <button class="bottone" type="submit">Filtra</button>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Rimuovi filtri
            </a>
        </form>

        <hr>

        <!-- Lista prodotti -->
        <section>

            <%
                if (prodotti == null || prodotti.isEmpty()) {
            %>

                <p>Nessun prodotto trovato.</p>

            <%
                } else {
            %>

                <div class="griglia-prodotti">

                    <%
                        for (Prodotto prodotto : prodotti) {
                    %>

                        <article class="card-prodotto">

                            <h3><%= prodotto.getNome() %></h3>

                            <p><strong>Gioco:</strong> <%= prodotto.getGioco() %></p>
                            <p><strong>Categoria:</strong> <%= prodotto.getCategoria() %></p>
                            <p><strong>Rarità:</strong> <%= prodotto.getRarita() %></p>
                            <p><strong>Prezzo:</strong> € <%= prodotto.getPrezzo() %></p>
                            <p><strong>Disponibilità:</strong> <%= prodotto.getQuantita() %> pezzi</p>

                            <a class="bottone" href="${pageContext.request.contextPath}/dettaglio-prodotto?id=<%= prodotto.getId() %>">
                                Dettaglio
                            </a>

                        </article>

                    <%
                        }
                    %>

                </div>

            <%
                }
            %>

        </section>

    </main>

</body>
</html>