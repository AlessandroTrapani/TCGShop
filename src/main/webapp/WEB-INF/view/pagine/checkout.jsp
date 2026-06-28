<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera il carrello dalla request.
     * La CheckoutServlet lo passa alla JSP prima del forward.
     */
    Carrello carrello = (Carrello) request.getAttribute("carrello");

    /*
     * Recupera l'utente dalla sessione per mostrare Login/Logout
     * e per eventuali informazioni nella pagina.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    /*
     * Recupera eventuali messaggi e valori già inseriti nel form.
     * Servono quando la Servlet rileva un errore e torna alla JSP.
     */
    String errore = (String) request.getAttribute("errore");
    String emailConsegna = (String) request.getAttribute("emailConsegna");
    String indirizzoSpedizione = (String) request.getAttribute("indirizzoSpedizione");
    String metodoPagamento = (String) request.getAttribute("metodoPagamento");

    if (emailConsegna == null && utenteLoggato != null) {
        emailConsegna = utenteLoggato.getEmail();
    }

    if (emailConsegna == null) {
        emailConsegna = "";
    }

    if (indirizzoSpedizione == null) {
        indirizzoSpedizione = "";
    }

    if (metodoPagamento == null) {
        metodoPagamento = "";
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Checkout - TCGShop</title>
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

    <!-- Contenuto principale della pagina checkout -->
    <main class="contenitore contenuto-pagina">

        <h2>Checkout</h2>

        <%
            if (errore != null) {
        %>
            <p class="messaggio-errore"><%= errore %></p>
        <%
            }
        %>

        <!-- Riepilogo del carrello prima della conferma ordine -->
        <section class="box-info">
            <h3>Riepilogo ordine</h3>

            <%
                if (carrello != null && !carrello.isVuoto()) {
                    for (ElementoCarrello elemento : carrello.getElementi()) {
            %>

                <p>
                    <strong><%= elemento.getProdotto().getNome() %></strong>
                    - quantità: <%= elemento.getQuantita() %>
                    - totale riga: € <%= elemento.getTotaleRiga() %>
                </p>

            <%
                    }
                }
            %>

            <h3>Totale: € <%= carrello != null ? carrello.getTotale() : 0 %></h3>
        </section>

        <!--
            Form di checkout.
            I dati vengono inviati con metodo POST alla CheckoutServlet.
        -->
        <form 
    id="form-checkout"
    method="post" 
    action="${pageContext.request.contextPath}/checkout"
    novalidate>

    <label for="emailConsegna">Email di consegna</label>
    <input 
        type="email" 
        id="emailConsegna" 
        name="emailConsegna" 
        value="<%= emailConsegna %>">

    <p id="errore-email-checkout" class="messaggio-errore-form"></p>

    <label for="indirizzoSpedizione">Indirizzo di spedizione</label>
    <input 
        type="text" 
        id="indirizzoSpedizione" 
        name="indirizzoSpedizione" 
        value="<%= indirizzoSpedizione %>">

    <p id="errore-indirizzo-checkout" class="messaggio-errore-form"></p>

    <label for="metodoPagamento">Metodo di pagamento</label>
    <select id="metodoPagamento" name="metodoPagamento">
        <option value="">Seleziona</option>
        <option value="Carta" <%= "Carta".equals(metodoPagamento) ? "selected" : "" %>>Carta</option>
        <option value="Contanti" <%= "Contanti".equals(metodoPagamento) ? "selected" : "" %>>Contanti</option>
    </select>

    <p id="errore-metodo-checkout" class="messaggio-errore-form"></p>

    <!--
        Messaggio mostrato solo quando l'utente sceglie pagamento in contanti.
    -->
    <p id="messaggio-contanti" class="box-info" style="display: none;">
        Il pagamento sarà effettuato alla consegna.
    </p>

    <!--
        Sezione dati carta.
        Viene mostrata solo quando l'utente seleziona il metodo di pagamento Carta.
    -->
    <div id="sezione-carta" style="display: none;">

        <label for="numeroCarta">Numero carta</label>
        <input 
            type="text" 
            id="numeroCarta" 
            name="numeroCarta" 
            placeholder="Solo simulazione">

        <p id="errore-numero-carta" class="messaggio-errore-form"></p>

        <label for="cvv">CVV</label>
        <input 
            type="text" 
            id="cvv" 
            name="cvv" 
            placeholder="123">

        <p id="errore-cvv" class="messaggio-errore-form"></p>

    </div>

    <button class="bottone" type="submit">
        Conferma ordine
    </button>

</form>

        <a class="bottone" href="${pageContext.request.contextPath}/carrello">
            Torna al carrello
        </a>

    </main>
<script src="${pageContext.request.contextPath}/scripts/validazione-checkout.js"></script>
</body>
</html>