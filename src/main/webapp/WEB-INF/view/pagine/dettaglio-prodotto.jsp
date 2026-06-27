<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Utente" %>
<%@ page import="model.Carrello" %>

<%
    /*
     * Recupero dalla request il prodotto passato dalla Servlet.
     * Se il prodotto non esiste, la Servlet passa anche un messaggio di errore.
     */
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    String errore = (String) request.getAttribute("errore");

    /*
     * Recupero l'eventuale utente loggato dalla sessione.
     * Serve per mostrare Login oppure Logout nel menu.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    /*
     * Recupero il carrello dalla sessione.
     * Serve per sapere quanti pezzi del prodotto sono già stati aggiunti.
     */
    Carrello carrello = (Carrello) session.getAttribute("carrello");
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

                    <%
    /*
     * Calcolo quanti pezzi del prodotto sono già nel carrello.
     * Se il carrello non esiste ancora, la quantità già presente è 0.
     */
    int quantitaGiaNelCarrello = 0;

    if (carrello != null) {
        quantitaGiaNelCarrello = carrello.getQuantitaProdotto(prodotto.getId());
    }

    /*
     * Calcolo quanti pezzi l'utente può ancora aggiungere.
     * Esempio:
     * disponibilità prodotto = 5
     * già nel carrello = 2
     * quantità ancora aggiungibile = 3
     */
    int quantitaAggiungibile = prodotto.getQuantita() - quantitaGiaNelCarrello;
%>

<%
    /*
     * Se la quantità aggiungibile è maggiore di zero,
     * mostro il form per aggiungere al carrello.
     * Altrimenti mostro un messaggio.
     */
    if (quantitaAggiungibile > 0) {
%>

    <form method="post" action="${pageContext.request.contextPath}/carrello">
        <input type="hidden" name="azione" value="aggiungi">
        <input type="hidden" name="idProdotto" value="<%= prodotto.getId() %>">

        <label for="quantita">Quantità</label>
        <input 
            type="number" 
            id="quantita" 
            name="quantita" 
            value="1" 
            min="1" 
            max="<%= quantitaAggiungibile %>">

        <p>
            Puoi aggiungere ancora massimo 
            <strong><%= quantitaAggiungibile %></strong> pezzi.
        </p>

        <button class="bottone" type="submit">
            Aggiungi al carrello
        </button>
    </form>

<%
    } else {
%>

    <p class="messaggio-errore">
        Hai già aggiunto al carrello tutta la quantità disponibile per questo prodotto.
    </p>

<%
    }
%>

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