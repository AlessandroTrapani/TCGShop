<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupero eventuali dati e messaggi passati dalla RegistrazioneServlet.
     * Se la registrazione fallisce, questi valori servono per ripopolare il form.
     */
    String errore = (String) request.getAttribute("errore");
    String nome = (String) request.getAttribute("nome");
    String cognome = (String) request.getAttribute("cognome");
    String email = (String) request.getAttribute("email");

    /*
     * Recupero l'eventuale utente loggato dalla sessione.
     * Serve per mostrare Login oppure Logout nel menu.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");

    if (nome == null) {
        nome = "";
    }

    if (cognome == null) {
        cognome = "";
    }

    if (email == null) {
        email = "";
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Registrazione - TCGShop</title>
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

    <!-- Contenuto principale della pagina registrazione -->
    <main class="contenitore contenuto-pagina">

        <h2>Registrazione</h2>

        <%
            /*
             * Se la Servlet ha passato un errore, lo mostro all'utente.
             */
            if (errore != null) {
        %>
            <p class="messaggio-errore"><%= errore %></p>
        <%
            }
        %>

        <!--
            Form di registrazione.
            I dati vengono inviati con metodo POST alla RegistrazioneServlet.
        -->
        <form id="form-registrazione" method="post" action="${pageContext.request.contextPath}/registrazione" novalidate>

            <label for="nome">Nome</label>
<input 
    type="text" 
    id="nome" 
    name="nome" 
    value="<%= nome %>">

<p id="errore-nome-registrazione" class="messaggio-errore-form"></p>

<label for="cognome">Cognome</label>
<input 
    type="text" 
    id="cognome" 
    name="cognome" 
    value="<%= cognome %>">

<p id="errore-cognome-registrazione" class="messaggio-errore-form"></p>

            <label for="email">Email</label>
            <input 
                type="email" 
                id="email" 
                name="email" 
                value="<%= email %>">
                <p id="errore-email-registrazione" class="messaggio-errore-form"></p>

            <label for="password">Password</label>
<input 
    type="password" 
    id="password" 
    name="password">

<p id="errore-password-registrazione" class="messaggio-errore-form"></p>

<label for="confermaPassword">Conferma password</label>
<input 
    type="password" 
    id="confermaPassword" 
    name="confermaPassword">

<p id="errore-conferma-password-registrazione" class="messaggio-errore-form"></p>

            <button class="bottone" type="submit">
                Registrati
            </button>

        </form>

        <p>
            Hai già un account?
            <a href="${pageContext.request.contextPath}/login">Accedi</a>
        </p>

    </main>
<script src="${pageContext.request.contextPath}/scripts/validazione-registrazione.js"></script>
</body>
</html>