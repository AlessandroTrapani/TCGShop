<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Utente" %>
<%@ page import="model.Carrello" %>

<%
    /*
     * Recupera il prodotto e l'eventuale errore passati dalla DettaglioProdottoServlet.
     */
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    String errore = (String) request.getAttribute("errore");

    /*
     * Recupera l'utente loggato dalla sessione.
     * Il valore viene usato per mostrare il menu corretto.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    /*
     * Recupera il carrello dalla sessione.
     * Serve per calcolare quanta quantità del prodotto è già stata aggiunta.
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

    <!-- Contenuto principale della pagina dettaglio prodotto -->
    <main class="contenitore contenuto-pagina">

        <%
            /*
             * Se è presente un errore, mostra il messaggio e il link al catalogo.
             */
            if (errore != null) {
        %>

            <h2>Errore</h2>
            <p class="messaggio-errore"><%= errore %></p>

            <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
                Torna al catalogo
            </a>

        <%
            } else if (prodotto != null) {
        %>

            <section class="dettaglio-prodotto">

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
                    class="immagine-prodotto"
                    src="${pageContext.request.contextPath}/images/prodotti/<%= immagineProdotto %>" 
                    alt="<%= prodotto.getNome() %>">

                <div class="informazioni-prodotto">

                    <h2><%= prodotto.getNome() %></h2>

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
                    <p><strong>Quantità disponibile:</strong> <%= prodotto.getQuantita() %></p>

                    <%
                        /*
                         * Mostra la descrizione solo se presente.
                         */
                        if (prodotto.getDescrizione() != null && !prodotto.getDescrizione().trim().equals("")) {
                    %>
                        <p><%= prodotto.getDescrizione() %></p>
                    <%
                        }
                    %>

                    <%
                        /*
                         * Calcola la quantità del prodotto già presente nel carrello.
                         */
                        int quantitaGiaNelCarrello = 0;

                        if (carrello != null) {
                            quantitaGiaNelCarrello = carrello.getQuantitaProdotto(prodotto.getId());
                        }

                        /*
                         * Calcola quanti pezzi possono ancora essere aggiunti al carrello.
                         */
                        int quantitaAggiungibile = prodotto.getQuantita() - quantitaGiaNelCarrello;
                    %>

                    <%
                        /*
                         * L'admin non deve poter usare il sito come cliente.
                         */
                        if (utenteLoggato != null && "ADMIN".equals(utenteLoggato.getRuolo())) {
                    %>

                        <p class="box-info">
                            Stai visualizzando il prodotto come amministratore.
                            Per modificare il prodotto usa l'area admin.
                        </p>

                        <a class="bottone" href="${pageContext.request.contextPath}/admin/modifica-prodotto?id=<%= prodotto.getId() %>">
                            Modifica in area admin
                        </a>

                    <%
                        } else if (quantitaAggiungibile > 0) {
                    %>

                        <!--
                            Form per aggiungere il prodotto al carrello.
                            I dati vengono inviati alla CarrelloServlet.
                        -->
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

                    <br>

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