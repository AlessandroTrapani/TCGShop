/*
 * File JavaScript dedicato alla validazione del form di checkout.
 *
 * Gestisce:
 * - validazione email con regex
 * - controllo indirizzo obbligatorio
 * - visualizzazione dinamica dei campi carta
 * - messaggio per pagamento in contanti
 * - validazione numero carta e CVV
 * - messaggi di errore nel DOM senza usare alert
 */

document.addEventListener("DOMContentLoaded", function () {

    /*
     * Recupera degli elementi principali del form.
     */
    const formCheckout = document.getElementById("form-checkout");

    const campoEmail = document.getElementById("emailConsegna");
    const campoIndirizzo = document.getElementById("indirizzoSpedizione");
    const campoMetodoPagamento = document.getElementById("metodoPagamento");

    const sezioneCarta = document.getElementById("sezione-carta");
    const messaggioContanti = document.getElementById("messaggio-contanti");

    const campoNumeroCarta = document.getElementById("numeroCarta");
    const campoCvv = document.getElementById("cvv");

    /*
     * Recupero degli elementi dove mostrare i messaggi di errore.
     */
    const erroreEmail = document.getElementById("errore-email-checkout");
    const erroreIndirizzo = document.getElementById("errore-indirizzo-checkout");
    const erroreMetodo = document.getElementById("errore-metodo-checkout");
    const erroreNumeroCarta = document.getElementById("errore-numero-carta");
    const erroreCvv = document.getElementById("errore-cvv");

    /*
     * Regex per controllare il formato dell'email.
     */
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    /*
     * Regex per il numero carta.
     * In questo progetto il pagamento è simulato: controlliamo solo
     * che siano presenti da 12 a 19 cifre.
     */
    const regexNumeroCarta = /^\d{12,19}$/;

    /*
     * Regex per il CVV: esattamente 3 cifre.
     */
    const regexCvv = /^\d{3}$/;

    /**
     * Mostra un errore sotto a un campo e aggiunge il bordo rosso.
     *
     * @param {HTMLElement} campo campo input/select da evidenziare
     * @param {HTMLElement} elementoErrore elemento dove scrivere il messaggio
     * @param {string} messaggio messaggio da mostrare
     */
    function mostraErrore(campo, elementoErrore, messaggio) {
        elementoErrore.textContent = messaggio;
        campo.classList.add("input-errore");
    }

    /**
     * Rimuove l'errore da un campo.
     *
     * @param {HTMLElement} campo campo input/select da ripulire
     * @param {HTMLElement} elementoErrore elemento errore da svuotare
     */
    function rimuoviErrore(campo, elementoErrore) {
        elementoErrore.textContent = "";
        campo.classList.remove("input-errore");
    }

    /**
     * Valida il campo email.
     *
     * @returns true se l'email è valida, false altrimenti
     */
    function validaEmail() {
        const email = campoEmail.value.trim();

        if (email === "") {
            mostraErrore(campoEmail, erroreEmail, "L'email è obbligatoria.");
            return false;
        }

        if (!regexEmail.test(email)) {
            mostraErrore(campoEmail, erroreEmail, "Inserisci un indirizzo email valido.");
            return false;
        }

        rimuoviErrore(campoEmail, erroreEmail);
        return true;
    }

    /**
     * Valida il campo indirizzo.
     *
     * @returns true se l'indirizzo è presente, false altrimenti
     */
    function validaIndirizzo() {
        const indirizzo = campoIndirizzo.value.trim();

        if (indirizzo === "") {
            mostraErrore(campoIndirizzo, erroreIndirizzo, "L'indirizzo di spedizione è obbligatorio.");
            return false;
        }

        rimuoviErrore(campoIndirizzo, erroreIndirizzo);
        return true;
    }

    /**
     * Valida il metodo di pagamento.
     *
     * @returns true se il metodo è valido, false altrimenti
     */
    function validaMetodoPagamento() {
        const metodo = campoMetodoPagamento.value;

        if (metodo === "") {
            mostraErrore(campoMetodoPagamento, erroreMetodo, "Seleziona un metodo di pagamento.");
            return false;
        }

        rimuoviErrore(campoMetodoPagamento, erroreMetodo);
        return true;
    }

    /**
     * Valida i dati della carta.
     * Questo controllo viene eseguito solo se il metodo selezionato è Carta.
     *
     * @returns true se i dati carta sono validi o se il metodo non è Carta
     */
    function validaDatiCarta() {
        const metodo = campoMetodoPagamento.value;

        /*
         * Se il metodo non è Carta, i campi carta non sono obbligatori.
         */
        if (metodo !== "Carta") {
            rimuoviErrore(campoNumeroCarta, erroreNumeroCarta);
            rimuoviErrore(campoCvv, erroreCvv);
            return true;
        }

        let datiCartaValidi = true;

        const numeroCarta = campoNumeroCarta.value.trim();
        const cvv = campoCvv.value.trim();

        if (numeroCarta === "") {
            mostraErrore(campoNumeroCarta, erroreNumeroCarta, "Il numero carta è obbligatorio.");
            datiCartaValidi = false;
        } else if (!regexNumeroCarta.test(numeroCarta)) {
            mostraErrore(campoNumeroCarta, erroreNumeroCarta, "Il numero carta deve contenere da 12 a 19 cifre.");
            datiCartaValidi = false;
        } else {
            rimuoviErrore(campoNumeroCarta, erroreNumeroCarta);
        }

        if (cvv === "") {
            mostraErrore(campoCvv, erroreCvv, "Il CVV è obbligatorio.");
            datiCartaValidi = false;
        } else if (!regexCvv.test(cvv)) {
            mostraErrore(campoCvv, erroreCvv, "Il CVV deve contenere 3 cifre.");
            datiCartaValidi = false;
        } else {
            rimuoviErrore(campoCvv, erroreCvv);
        }

        return datiCartaValidi;
    }

    /**
     * Aggiorna la visualizzazione dei campi in base al metodo di pagamento.
     */
    function aggiornaMetodoPagamento() {
        const metodo = campoMetodoPagamento.value;

        if (metodo === "Carta") {
            sezioneCarta.style.display = "block";
            messaggioContanti.style.display = "none";
        } else if (metodo === "Contanti") {
            sezioneCarta.style.display = "none";
            messaggioContanti.style.display = "block";

            /*
             * Se passo a Contanti, pulisco eventuali errori dei campi carta.
             */
            rimuoviErrore(campoNumeroCarta, erroreNumeroCarta);
            rimuoviErrore(campoCvv, erroreCvv);
        } else {
            sezioneCarta.style.display = "none";
            messaggioContanti.style.display = "none";
        }
    }

    /*
     * Controlli eseguiti quando l'utente esce dai campi.
     */
    campoEmail.addEventListener("blur", function () {
        validaEmail();
    });

    campoIndirizzo.addEventListener("blur", function () {
        validaIndirizzo();
    });

    campoNumeroCarta.addEventListener("blur", function () {
        validaDatiCarta();
    });

    campoCvv.addEventListener("blur", function () {
        validaDatiCarta();
    });

    /*
     * Quando cambia il metodo di pagamento, mostro o nascondo
     * la sezione carta e il messaggio per i contanti.
     */
    campoMetodoPagamento.addEventListener("change", function () {
        aggiornaMetodoPagamento();
        validaMetodoPagamento();
    });

    /*
     * Al caricamento della pagina imposto la visualizzazione corretta.
     * Serve anche quando la Servlet torna alla JSP dopo un errore.
     */
    aggiornaMetodoPagamento();

    /*
     * Controllo finale prima dell'invio del form.
     */
    formCheckout.addEventListener("submit", function (event) {
        const emailValida = validaEmail();
        const indirizzoValido = validaIndirizzo();
        const metodoValido = validaMetodoPagamento();
        const cartaValida = validaDatiCarta();

        if (!emailValida || !indirizzoValido || !metodoValido || !cartaValida) {
            event.preventDefault();
        }
    });
});