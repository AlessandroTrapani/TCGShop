<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Utente" %>

<%
    /*
     * Recupera l'utente loggato dalla sessione.
     * In questa pagina dovrebbe essere sempre un amministratore,
     * perché l'accesso è protetto da FiltroAdmin.
     */
    Utente utenteLoggato = (Utente) session.getAttribute("utenteLoggato");
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Admin - TCGShop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/stile.css">
</head>
<body>

    <!-- Intestazione principale dell'area admin -->
    <header class="intestazione-sito">
        <div class="contenitore">
            <h1>TCGShop - Area Admin</h1>

            <!-- Menu principale amministratore -->
            <nav class="menu-principale">
                <a href="${pageContext.request.contextPath}/index.jsp">Sito</a>
                <a href="${pageContext.request.contextPath}/admin/home">Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/prodotti">Prodotti</a>
                <a href="${pageContext.request.contextPath}/admin/ordini">Ordini</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della dashboard admin -->
    <main class="contenitore contenuto-pagina">

        <h2>Dashboard amministratore</h2>

        <%
            /*
             * Mostra un messaggio di benvenuto usando il nome dell'admin loggato.
             */
            if (utenteLoggato != null) {
        %>
            <p>
                Benvenuto, <strong><%= utenteLoggato.getNome() %></strong>.
            </p>
        <%
            }
        %>

        <p>
            Da questa area è possibile gestire i prodotti del catalogo
            e gli ordini effettuati dagli utenti.
        </p>

        <!-- Collegamenti principali dell'area amministratore -->
        <section class="griglia-prodotti">

            <article class="card-prodotto">
                <h3>Gestione prodotti</h3>
                <p>
                    Visualizza, inserisce, modifica ed elimina logicamente
                    i prodotti presenti nel catalogo.
                </p>

                <a class="bottone" href="${pageContext.request.contextPath}/admin/prodotti">
                    Vai ai prodotti
                </a>
            </article>

            <article class="card-prodotto">
                <h3>Gestione ordini</h3>
                <p>
                    Visualizza gli ordini degli utenti, consulta i dettagli
                    e aggiorna lo stato degli ordini.
                </p>

                <a class="bottone" href="${pageContext.request.contextPath}/admin/ordini">
                    Vai agli ordini
                </a>
            </article>

        </section>

    </main>

</body>
</html>