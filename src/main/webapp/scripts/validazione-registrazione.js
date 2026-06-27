/*
 * File JavaScript dedicato alla validazione del form di registrazione.
 *
 * I controlli vengono eseguiti lato client usando regex, eventi,
 * AJAX, JSON e messaggi mostrati direttamente nel DOM, senza usare alert.
 */

/*
 * Attende il caricamento completo della pagina prima di cercare
 * gli elementi HTML del form.
 */
document.addEventListener("DOMContentLoaded", function () {

    /*
     * Recupero gli elementi principali del form di registrazione.
     */
    const formRegistrazione = document.getElementById("form-registrazione");
    const campoEmail = document.getElementById("email");
    const erroreEmail = document.getElementById("errore-email-registrazione");

    /*
     * Variabile usata per ricordare se l'email è valida anche lato server.
     * Parte da false e diventa true solo quando la Servlet risponde
     * che l'email è disponibile.
     */
    let emailDisponibile = false;

    /*
     * Regex usata per controllare il formato dell'email.
     * Verifica una struttura del tipo testo@testo.estensione.
     */
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    /**
     * Mostra un messaggio di errore sotto al campo email.
     *
     * @param {string} messaggio testo da mostrare all'utente
     */
    function mostraErroreEmail(messaggio) {
        erroreEmail.textContent = messaggio;
        campoEmail.classList.add("input-errore");
        emailDisponibile = false;
    }

    /**
     * Rimuove il messaggio di errore sotto al campo email.
     */
    function rimuoviErroreEmail() {
        erroreEmail.textContent = "";
        campoEmail.classList.remove("input-errore");
    }

    /**
     * Controlla il formato dell'email usando una regex.
     *
     * @returns true se il formato è valido, false altrimenti
     */
    function validaFormatoEmail() {
        const emailInserita = campoEmail.value.trim();

        if (emailInserita === "") {
            mostraErroreEmail("L'email è obbligatoria.");
            return false;
        }

        if (!regexEmail.test(emailInserita)) {
            mostraErroreEmail("Inserisci un indirizzo email valido.");
            return false;
        }

        rimuoviErroreEmail();
        return true;
    }

    /**
     * Verifica tramite AJAX se l'email è già presente nel database.
     * 
     * La richiesta viene inviata alla Servlet /verifica-email.
     * La risposta viene letta come JSON.
     */
    function verificaEmailAjax() {
        const emailInserita = campoEmail.value.trim();

        /*
         * Prima di chiamare il server controllo il formato.
         * Se il formato è sbagliato, evito una richiesta inutile.
         */
        if (!validaFormatoEmail()) {
            return;
        }

        fetch("verifica-email?email=" + encodeURIComponent(emailInserita))
            .then(function (response) {
                return response.json();
            })
            .then(function (dati) {

                /*
                 * La Servlet restituisce un JSON con:
                 * dati.valida
                 * dati.messaggio
                 */
                if (dati.valida) {
                    emailDisponibile = true;
                    rimuoviErroreEmail();
                } else {
                    mostraErroreEmail(dati.messaggio);
                }
            })
            .catch(function () {
                /*
                 * Questo errore gestisce problemi nella chiamata AJAX,
                 * ad esempio Servlet non raggiungibile o risposta non valida.
                 */
                mostraErroreEmail("Errore durante la verifica dell'email.");
            });
    }

    /*
     * Quando l'utente esce dal campo email, controllo prima il formato
     * e poi verifico tramite AJAX se l'email è già registrata.
     */
    if (campoEmail != null && erroreEmail != null) {
        campoEmail.addEventListener("blur", function () {
            verificaEmailAjax();
        });
    }

    /*
     * Quando l'utente modifica l'email, considero di nuovo non valida
     * la disponibilità, perché il valore è cambiato.
     */
    if (campoEmail != null) {
        campoEmail.addEventListener("input", function () {
            emailDisponibile = false;
        });
    }

    /*
     * Al submit del form ripeto i controlli disponibili lato client.
     * Se il formato è errato o l'email risulta già non disponibile,
     * blocco l'invio.
     */
    if (formRegistrazione != null) {
        formRegistrazione.addEventListener("submit", function (event) {

            const formatoValido = validaFormatoEmail();

            if (!formatoValido || !emailDisponibile) {
                event.preventDefault();

                /*
                 * Se il formato è valido ma l'utente non è ancora uscito
                 * dal campo email, forzo la verifica AJAX.
                 */
                if (formatoValido) {
                    verificaEmailAjax();
                }
            }
        });
    }
});