<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupero l'eventuale utente loggato dalla sessione.
     * Se è null significa che l'utente non ha ancora effettuato il login.
     * Questo controllo serve per mostrare Login oppure Logout nel menu.
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

    <!-- Contenuto principale della homepage -->
    <main class="contenitore contenuto-pagina">
        <h2>Benvenuto su TCGShop</h2>

        <%
            /*
             * Se l'utente è loggato, mostro un piccolo messaggio di benvenuto
             * usando il nome salvato nell'oggetto Utente presente in sessione.
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