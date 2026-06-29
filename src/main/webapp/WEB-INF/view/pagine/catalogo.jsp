<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera la lista dei prodotti passata dalla CatalogoServlet.
     */
    ArrayList<Prodotto> prodotti = (ArrayList<Prodotto>) request.getAttribute("prodotti");

    /*
     * Recupera i filtri usati nella ricerca.
     * Questi valori servono per mantenere compilato il form dei filtri.
     */
    String ricerca = (String) request.getAttribute("ricerca");
    String gioco = (String) request.getAttribute("gioco");
    String categoria = (String) request.getAttribute("categoria");

    /*
     * Recupera l'utente loggato dalla sessione.
     * Il valore viene usato per mostrare il menu corretto.
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
                     * Mostra i link in base allo stato dell'utente.
                     */
                    if (utenteLoggato == null) {
                %>
                    <a href="${pageContext.request.contextPath}/login">Login</a>
                <%
                    } else if ("ADMIN".equals(utenteLoggato.getRuolo())) {
                %>
                    <a href="${pageContext.request.contextPath}/admin/home">Area admin</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                <%
                    } else {
                %>
                    <a href="${pageContext.request.contextPath}/storico-ordini">I miei ordini</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                <%
                    }
                %>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della pagina catalogo -->
    <main class="contenitore contenuto-pagina">

        <h2>Catalogo prodotti</h2>

        <!--
            Form dei filtri.
            I dati vengono inviati alla CatalogoServlet tramite metodo GET.
        -->
        <form method="get" action="${pageContext.request.contextPath}/catalogo">

            <label for="ricerca">Cerca prodotto</label>
            <input 
                type="text" 
                id="ricerca" 
                name="ricerca" 
                value="<%= ricerca %>"
                placeholder="Es. Charizard">

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

            <br><br>

            <button class="bottone" type="submit">
                Filtra
            </button>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Reset
            </a>

        </form>

        <hr>

        <%
            /*
             * Se non ci sono prodotti, mostra un messaggio.
             */
            if (prodotti == null || prodotti.isEmpty()) {
        %>

            <p>Nessun prodotto trovato.</p>

        <%
            } else {
        %>

            <!-- Griglia dei prodotti disponibili nel catalogo -->
            <section class="griglia-prodotti">

                <%
                    for (Prodotto prodotto : prodotti) {
                %>

                    <article class="card-prodotto">

                        <%
                            /*
                             * Recupera il nome dell'immagine del prodotto.
                             * Se l'immagine non è presente, viene usato un placeholder.
                             */
                            String immagineProdotto = prodotto.getImmagine();

                            if (immagineProdotto == null || immagineProdotto.trim().equals("")) {
                                immagineProdotto = "placeholder.jpg";
                            }
                        %>

                        <img 
                            class="immagine-card-prodotto"
                            src="${pageContext.request.contextPath}/images/prodotti/<%= immagineProdotto %>" 
                            alt="<%= prodotto.getNome() %>">

                        <h3><%= prodotto.getNome() %></h3>

                        <p><strong>Gioco:</strong> <%= prodotto.getGioco() %></p>
                        <p><strong>Categoria:</strong> <%= prodotto.getCategoria() %></p>

                        <%
                            /*
                             * Mostra la rarità solo se presente.
                             */
                            if (prodotto.getRarita() != null && !prodotto.getRarita().trim().equals("")) {
                        %>
                            <p><strong>Rarità:</strong> <%= prodotto.getRarita() %></p>
                        <%
                            }
                        %>

                        <p><strong>Prezzo:</strong> € <%= prodotto.getPrezzo() %></p>
                        <p><strong>Disponibili:</strong> <%= prodotto.getQuantita() %></p>

                        <a class="bottone" href="${pageContext.request.contextPath}/dettaglio-prodotto?id=<%= prodotto.getId() %>">
                            Dettaglio
                        </a>

                    </article>

                <%
                    }
                %>

            </section>

        <%
            }
        %>

    </main>

</body>
</html>