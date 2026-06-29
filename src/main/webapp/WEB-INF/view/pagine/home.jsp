<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera l'eventuale utente loggato dalla sessione.
     * Il valore viene usato per mostrare il menu corretto.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>TCGShop - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/stile.css">
</head>
<body>

    <!-- Intestazione principale del sito -->
    <header class="intestazione-sito">
        <div class="contenitore">
            <h1>TCGShop</h1>

            <!-- Menu principale pubblico -->
            <nav class="menu-principale">
                <a href="${pageContext.request.contextPath}/home">Home</a>
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

    <!-- Contenuto principale della homepage -->
    <main class="contenitore contenuto-pagina">
        <h2>Benvenuto su TCGShop</h2>

        <%
            /*
             * Se l'utente è loggato, mostra un messaggio di benvenuto.
             */
            if (utenteLoggato != null) {
        %>
            <p>
                Ciao <strong><%= utenteLoggato.getNome() %></strong>, sei connesso a TCGShop.
            </p>
        <%
            }
        %>

        <p>
            TCGShop è un e-commerce simulato dedicato ai prodotti TCG:
            box, bustine, carte singole e accessori.
        </p>

        <a class="bottone" href="${pageContext.request.contextPath}/catalogo">
            Vai al catalogo
        </a>
    </main>

</body>
</html>