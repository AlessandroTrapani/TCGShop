/*
 * File JavaScript dedicato alla validazione del form di registrazione.
 *
 * Controlla i dati lato client usando regex, eventi blur/input,
 * AJAX, JSON e messaggi mostrati direttamente nel DOM, senza usare alert.
 */

document.addEventListener("DOMContentLoaded", function () {

    /*
     * Recupera gli elementi principali del form di registrazione.
     */
    const formRegistrazione = document.getElementById("form-registrazione");

    const campoNome = document.getElementById("nome");
    const campoCognome = document.getElementById("cognome");
    const campoEmail = document.getElementById("email");
    const campoPassword = document.getElementById("password");
    const campoConfermaPassword = document.getElementById("confermaPassword");

    /*
     * Recupera gli elementi dedicati ai messaggi di errore.
     */
    const erroreNome = document.getElementById("errore-nome-registrazione");
    const erroreCognome = document.getElementById("errore-cognome-registrazione");
    const erroreEmail = document.getElementById("errore-email-registrazione");
    const errorePassword = document.getElementById("errore-password-registrazione");
    const erroreConfermaPassword = document.getElementById("errore-conferma-password-registrazione");

    /*
     * Indica se l'email è stata verificata dal server ed è disponibile.
     */
    let emailDisponibile = false;

    /*
     * Regex usata per controllare il formato dell'email.
     */
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    /*
     * Regex usata per nome e cognome.
     * Accetta lettere, lettere accentate, spazi, apostrofi e trattini.
     */
    const regexNome = /^[A-Za-zÀ-ÿ' -]+$/;

    /**
     * Mostra un errore sotto al campo indicato.
     *
     * @param {HTMLElement} campo campo da evidenziare
     * @param {HTMLElement} elementoErrore elemento in cui mostrare il messaggio
     * @param {string} messaggio messaggio da visualizzare
     */
    function mostraErrore(campo, elementoErrore, messaggio) {
        elementoErrore.textContent = messaggio;
        campo.classList.add("input-errore");
    }

    /**
     * Rimuove l'errore dal campo indicato.
     *
     * @param {HTMLElement} campo campo da ripulire
     * @param {HTMLElement} elementoErrore elemento errore da svuotare
     */
    function rimuoviErrore(campo, elementoErrore) {
        elementoErrore.textContent = "";
        campo.classList.remove("input-errore");
    }

    /**
     * Valida il nome.
     *
     * @returns true se il nome è valido, false altrimenti
     */
    function validaNome() {
        const nome = campoNome.value.trim();

        if (nome === "") {
            mostraErrore(campoNome, erroreNome, "Il nome è obbligatorio.");
            return false;
        }

        if (nome.length < 2) {
            mostraErrore(campoNome, erroreNome, "Il nome deve contenere almeno 2 caratteri.");
            return false;
        }

        if (!regexNome.test(nome)) {
            mostraErrore(campoNome, erroreNome, "Il nome può contenere solo lettere.");
            return false;
        }

        rimuoviErrore(campoNome, erroreNome);
        return true;
    }

    /**
     * Valida il cognome.
     *
     * @returns true se il cognome è valido, false altrimenti
     */
    function validaCognome() {
        const cognome = campoCognome.value.trim();

        if (cognome === "") {
            mostraErrore(campoCognome, erroreCognome, "Il cognome è obbligatorio.");
            return false;
        }

        if (cognome.length < 2) {
            mostraErrore(campoCognome, erroreCognome, "Il cognome deve contenere almeno 2 caratteri.");
            return false;
        }

        if (!regexNome.test(cognome)) {
            mostraErrore(campoCognome, erroreCognome, "Il cognome può contenere solo lettere.");
            return false;
        }

        rimuoviErrore(campoCognome, erroreCognome);
        return true;
    }

    /**
     * Valida il formato dell'email.
     *
     * @returns true se il formato email è valido, false altrimenti
     */
    function validaFormatoEmail() {
        const emailInserita = campoEmail.value.trim();

        if (emailInserita === "") {
            mostraErrore(campoEmail, erroreEmail, "L'email è obbligatoria.");
            emailDisponibile = false;
            return false;
        }

        if (!regexEmail.test(emailInserita)) {
            mostraErrore(campoEmail, erroreEmail, "Inserisci un indirizzo email valido.");
            emailDisponibile = false;
            return false;
        }

        rimuoviErrore(campoEmail, erroreEmail);
        return true;
    }

    /**
     * Verifica tramite AJAX se l'email è già presente nel database.
     */
    function verificaEmailAjax() {
        const emailInserita = campoEmail.value.trim();

        /*
         * Controlla prima il formato per evitare chiamate inutili al server.
         */
        if (!validaFormatoEmail()) {
            return;
        }

        fetch("verifica-email?email=" + encodeURIComponent(emailInserita))
            .then(function (response) {
                return response.json();
            })
            .then(function (dati) {
                if (dati.valida) {
                    emailDisponibile = true;
                    rimuoviErrore(campoEmail, erroreEmail);
                } else {
                    emailDisponibile = false;
                    mostraErrore(campoEmail, erroreEmail, dati.messaggio);
                }
            })
            .catch(function () {
                emailDisponibile = false;
                mostraErrore(campoEmail, erroreEmail, "Errore durante la verifica dell'email.");
            });
    }

    /**
     * Valida la password.
     *
     * La password deve avere almeno 5 caratteri.
     *
     * @returns true se la password è valida, false altrimenti
     */
    function validaPassword() {
        const password = campoPassword.value.trim();

        if (password === "") {
            mostraErrore(campoPassword, errorePassword, "La password è obbligatoria.");
            return false;
        }

        if (password.length < 5) {
            mostraErrore(campoPassword, errorePassword, "La password deve contenere almeno 5 caratteri.");
            return false;
        }

        rimuoviErrore(campoPassword, errorePassword);
        return true;
    }

    /**
     * Valida la conferma password.
     *
     * @returns true se la conferma password è valida, false altrimenti
     */
    function validaConfermaPassword() {
        const password = campoPassword.value.trim();
        const confermaPassword = campoConfermaPassword.value.trim();

        if (confermaPassword === "") {
            mostraErrore(campoConfermaPassword, erroreConfermaPassword, "La conferma password è obbligatoria.");
            return false;
        }

        if (password !== confermaPassword) {
            mostraErrore(campoConfermaPassword, erroreConfermaPassword, "Le password non coincidono.");
            return false;
        }

        rimuoviErrore(campoConfermaPassword, erroreConfermaPassword);
        return true;
    }

    /*
     * Associa i controlli all'evento blur.
     * Il messaggio viene mostrato quando l'utente esce dal campo.
     */
    campoNome.addEventListener("blur", validaNome);
    campoCognome.addEventListener("blur", validaCognome);
    campoEmail.addEventListener("blur", verificaEmailAjax);
    campoPassword.addEventListener("blur", function () {
        validaPassword();

        /*
         * Se la conferma è già stata compilata, la ricontrolla.
         */
        if (campoConfermaPassword.value.trim() !== "") {
            validaConfermaPassword();
        }
    });
    campoConfermaPassword.addEventListener("blur", validaConfermaPassword);

    /*
     * Quando l'email cambia, la disponibilità deve essere verificata di nuovo.
     */
    campoEmail.addEventListener("input", function () {
        emailDisponibile = false;
    });

    /*
     * Controlla tutti i campi prima dell'invio del form.
     */
    formRegistrazione.addEventListener("submit", function (event) {
        const nomeValido = validaNome();
        const cognomeValido = validaCognome();
        const emailFormatoValido = validaFormatoEmail();
        const passwordValida = validaPassword();
        const confermaPasswordValida = validaConfermaPassword();

        if (!nomeValido
                || !cognomeValido
                || !emailFormatoValido
                || !emailDisponibile
                || !passwordValida
                || !confermaPasswordValida) {

            event.preventDefault();

            /*
             * Se il formato email è valido ma l'email non è ancora stata
             * verificata, forza la verifica AJAX.
             */
            if (emailFormatoValido && !emailDisponibile) {
                verificaEmailAjax();
            }
        }
    });
});