/*
 * File JavaScript dedicato alla validazione dei form prodotto dell'area admin.
 *
 * Controlla i campi principali del prodotto e mostra eventuali errori
 * direttamente nel DOM, senza usare alert.
 */

document.addEventListener("DOMContentLoaded", function () {

    /*
     * Recupera il form prodotto.
     * Lo stesso script viene usato sia per nuovo-prodotto.jsp
     * sia per modifica-prodotto.jsp.
     */
    const formProdotto = document.getElementById("form-prodotto-admin");

    if (formProdotto == null) {
        return;
    }

    /*
     * Recupera i campi principali del form.
     */
    const campoNome = document.getElementById("nome");
    const campoGioco = document.getElementById("gioco");
    const campoCategoria = document.getElementById("categoria");
    const campoPrezzo = document.getElementById("prezzo");
    const campoQuantita = document.getElementById("quantita");

    /*
     * Recupera gli elementi usati per mostrare i messaggi di errore.
     */
    const erroreNome = document.getElementById("errore-nome-prodotto");
    const erroreGioco = document.getElementById("errore-gioco-prodotto");
    const erroreCategoria = document.getElementById("errore-categoria-prodotto");
    const errorePrezzo = document.getElementById("errore-prezzo-prodotto");
    const erroreQuantita = document.getElementById("errore-quantita-prodotto");

    /**
     * Mostra un messaggio di errore sotto al campo indicato.
     *
     * @param {HTMLElement} campo campo da evidenziare
     * @param {HTMLElement} elementoErrore elemento in cui scrivere l'errore
     * @param {string} messaggio messaggio da mostrare
     */
    function mostraErrore(campo, elementoErrore, messaggio) {
        elementoErrore.textContent = messaggio;
        campo.classList.add("input-errore");
    }

    /**
     * Rimuove il messaggio di errore da un campo.
     *
     * @param {HTMLElement} campo campo da ripulire
     * @param {HTMLElement} elementoErrore elemento errore da svuotare
     */
    function rimuoviErrore(campo, elementoErrore) {
        elementoErrore.textContent = "";
        campo.classList.remove("input-errore");
    }

    /**
     * Valida il nome del prodotto.
     *
     * @returns true se il nome è valido, false altrimenti
     */
    function validaNome() {
        const nome = campoNome.value.trim();

        if (nome === "") {
            mostraErrore(campoNome, erroreNome, "Il nome prodotto è obbligatorio.");
            return false;
        }

        if (nome.length < 2) {
            mostraErrore(campoNome, erroreNome, "Il nome prodotto deve contenere almeno 2 caratteri.");
            return false;
        }

        rimuoviErrore(campoNome, erroreNome);
        return true;
    }

    /**
     * Valida il gioco selezionato.
     *
     * @returns true se il gioco è valido, false altrimenti
     */
    function validaGioco() {
        const gioco = campoGioco.value;

        if (gioco === "") {
            mostraErrore(campoGioco, erroreGioco, "Seleziona un gioco.");
            return false;
        }

        rimuoviErrore(campoGioco, erroreGioco);
        return true;
    }

    /**
     * Valida la categoria selezionata.
     *
     * @returns true se la categoria è valida, false altrimenti
     */
    function validaCategoria() {
        const categoria = campoCategoria.value;

        if (categoria === "") {
            mostraErrore(campoCategoria, erroreCategoria, "Seleziona una categoria.");
            return false;
        }

        rimuoviErrore(campoCategoria, erroreCategoria);
        return true;
    }

    /**
     * Valida il prezzo.
     *
     * Sono accettati sia valori con punto sia valori con virgola.
     *
     * @returns true se il prezzo è valido, false altrimenti
     */
    function validaPrezzo() {
        const prezzoTesto = campoPrezzo.value.trim().replace(",", ".");
        const prezzo = parseFloat(prezzoTesto);

        if (prezzoTesto === "") {
            mostraErrore(campoPrezzo, errorePrezzo, "Il prezzo è obbligatorio.");
            return false;
        }

        if (isNaN(prezzo)) {
            mostraErrore(campoPrezzo, errorePrezzo, "Il prezzo deve essere un numero.");
            return false;
        }

        if (prezzo <= 0) {
            mostraErrore(campoPrezzo, errorePrezzo, "Il prezzo deve essere maggiore di zero.");
            return false;
        }

        rimuoviErrore(campoPrezzo, errorePrezzo);
        return true;
    }

    /**
     * Valida la quantità.
     *
     * @returns true se la quantità è valida, false altrimenti
     */
    function validaQuantita() {
        const quantitaTesto = campoQuantita.value.trim();
        const quantita = parseInt(quantitaTesto, 10);

        if (quantitaTesto === "") {
            mostraErrore(campoQuantita, erroreQuantita, "La quantità è obbligatoria.");
            return false;
        }

        if (isNaN(quantita)) {
            mostraErrore(campoQuantita, erroreQuantita, "La quantità deve essere un numero intero.");
            return false;
        }

        if (quantita < 0) {
            mostraErrore(campoQuantita, erroreQuantita, "La quantità non può essere negativa.");
            return false;
        }

        rimuoviErrore(campoQuantita, erroreQuantita);
        return true;
    }

    /*
     * Controlli eseguiti quando l'utente esce dai campi.
     */
    campoNome.addEventListener("blur", validaNome);
    campoGioco.addEventListener("change", validaGioco);
    campoCategoria.addEventListener("change", validaCategoria);
    campoPrezzo.addEventListener("blur", validaPrezzo);
    campoQuantita.addEventListener("blur", validaQuantita);

    /*
     * Controllo finale prima dell'invio del form.
     */
    formProdotto.addEventListener("submit", function (event) {
        const nomeValido = validaNome();
        const giocoValido = validaGioco();
        const categoriaValida = validaCategoria();
        const prezzoValido = validaPrezzo();
        const quantitaValida = validaQuantita();

        if (!nomeValido || !giocoValido || !categoriaValida || !prezzoValido || !quantitaValida) {
            event.preventDefault();
        }
    });
});