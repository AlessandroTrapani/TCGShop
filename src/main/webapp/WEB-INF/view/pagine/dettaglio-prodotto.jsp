<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Prodotto" %>

<%
    /*
     * Recupero dalla request il prodotto passato dalla Servlet.
     * Se il prodotto non esiste, la Servlet passa anche un messaggio di errore.
     */
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    String errore = (String) request.getAttribute("errore");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Dettaglio prodotto - TCGShop</title>
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
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della pagina dettaglio prodotto -->
    <main class="contenitore contenuto-pagina">

        <%
            if (errore != null) {
        %>

            <h2>Errore</h2>
            <p><%= errore %></p>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Torna al catalogo
            </a>

        <%
            } else if (prodotto != null) {
        %>

            <h2><%= prodotto.getNome() %></h2>

            <!-- Riquadro con le informazioni complete del prodotto -->
            <section class="dettaglio-prodotto">

                <div class="immagine-prodotto">
                    <%
                        /*
                         * Per ora mostriamo solo il nome del file immagine.
                         * Più avanti, se inseriamo immagini reali nella cartella images/prodotti,
                         * potremo visualizzarle con un tag img.
                         */
                        if (prodotto.getImmagine() != null && !prodotto.getImmagine().equals("")) {
                    %>
                        <p><strong>Immagine:</strong> <%= prodotto.getImmagine() %></p>
                    <%
                        } else {
                    %>
                        <p><strong>Immagine:</strong> non disponibile</p>
                    <%
                        }
                    %>
                </div>

                <div class="informazioni-prodotto">
                    <p><strong>Gioco:</strong> <%= prodotto.getGioco() %></p>
                    <p><strong>Categoria:</strong> <%= prodotto.getCategoria() %></p>
                    <p><strong>Rarità:</strong> <%= prodotto.getRarita() %></p>
                    <p><strong>Prezzo:</strong> € <%= prodotto.getPrezzo() %></p>
                    <p><strong>Disponibilità:</strong> <%= prodotto.getQuantita() %> pezzi</p>
                    <p><strong>Stato:</strong> <%= prodotto.getStato() %></p>

                    <h3>Descrizione</h3>
                    <p><%= prodotto.getDescrizione() %></p>

                    <!--
                        Questo form sarà usato più avanti dalla CarrelloServlet.
                        Per ora il link può portare a 404 finché non creiamo /carrello.
                    -->
                    <form method="post" action="${pageContext.request.contextPath}/carrello">
                        <input type="hidden" name="azione" value="aggiungi">
                        <input type="hidden" name="idProdotto" value="<%= prodotto.getId() %>">

                        <label for="quantita">Quantità</label>
                        <input type="number" id="quantita" name="quantita" value="1" min="1" max="<%= prodotto.getQuantita() %>">

                        <button class="bottone" type="submit">
                            Aggiungi al carrello
                        </button>
                    </form>

                    <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                        Torna al catalogo
                    </a>
                </div>

            </section>

        <%
            }
        %>

    </main>

</body>
</html>