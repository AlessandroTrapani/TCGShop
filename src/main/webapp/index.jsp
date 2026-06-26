<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </nav>
        </div>
    </header>

    <!-- Contenuto principale della homepage -->
    <main class="contenitore contenuto-pagina">
        <h2>Benvenuto su TCGShop</h2>
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