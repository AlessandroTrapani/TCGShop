<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera eventuali messaggi passati dalla LoginServlet.
     * errore contiene il messaggio da mostrare se il login fallisce.
     * email contiene l'email già inserita, così l'utente non deve riscriverla.
     */
    String errore = (String) request.getAttribute("errore");
    String email = (String) request.getAttribute("email");

    /*
     * Recupera l'eventuale utente loggato dalla sessione.
     * Se è null significa che l'utente non ha ancora effettuato il login.
     * Questo controllo serve per mostrare Login oppure Logout nel menu.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    if (email == null) {
        email = "";
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Login - TCGShop</title>
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

    <!-- Contenuto principale della pagina login -->
    <main class="contenitore contenuto-pagina">

        <h2>Login</h2>

        <%
            /*
             * Se la Servlet ha passato un errore, lo mostra all'utente.
             */
            if (errore != null) {
        %>
            <p class="messaggio-errore"><%= errore %></p>
        <%
            }
        %>

        <!--
            Form di login.
            I dati vengono inviati con metodo POST alla LoginServlet.
        -->
        <form id="form-login" method="post" action="${pageContext.request.contextPath}/login" novalidate>

            <label for="email">Email</label>
            <input 
                type="email" 
                id="email" 
                name="email" 
                value="<%= email %>">
              <p id="errore-email-login" class="messaggio-errore-form"></p>

            <label for="password">Password</label>
            <input 
                type="password" 
                id="password" 
                name="password">
              <p id="errore-password-login" class="messaggio-errore-form"></p>

            <button class="bottone" type="submit">
                Accedi
            </button>

        </form>

        <p>
            Non hai ancora un account?
            <a href="${pageContext.request.contextPath}/registrazione">Registrati</a>
        </p>

        <!-- Dati di test -->
        <section class="box-info">
            <h3>Utenti di test</h3>
            <p><strong>Utente:</strong> utente@tcgshop.it / utente</p>
            <p><strong>Admin:</strong> admin@tcgshop.it / admin</p>
        </section>

    </main>

<script src="${pageContext.request.contextPath}/scripts/validazione-login.js"></script>
</body>
</html>