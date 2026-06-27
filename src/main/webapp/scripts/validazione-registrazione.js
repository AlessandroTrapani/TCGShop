/*
 * File JavaScript dedicato alla validazione del form di registrazione.
 *
 * I controlli vengono eseguiti lato client usando regex, eventi
 * e messaggi mostrati direttamente nel DOM, senza usare alert.
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
     * Regex usata per controllare il formato dell'email.
     * Verifica una struttura del tipo testo@testo.estensione.
     */
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    /*
     * Controlla se l'email inserita è valida.
     *
     * @returns true se l'email è valida, false altrimenti
     */
    function validaEmail() {
        const emailInserita = campoEmail.value.trim();

        if (emailInserita === "") {
            erroreEmail.textContent = "L'email è obbligatoria.";
            campoEmail.classList.add("input-errore");
            return false;
        }

        if (!regexEmail.test(emailInserita)) {
            erroreEmail.textContent = "Inserisci un indirizzo email valido.";
            campoEmail.classList.add("input-errore");
            return false;
        }

        erroreEmail.textContent = "";
        campoEmail.classList.remove("input-errore");
        return true;
    }

    /*
     * Controllo eseguito quando l'utente esce dal campo email.
     * In questo modo il messaggio rosso compare senza inviare il form.
     */
    if (campoEmail != null && erroreEmail != null) {
        campoEmail.addEventListener("blur", function () {
            validaEmail();
        });
    }

    /*
     * Controllo finale al submit del form.
     * Se l'email non è valida, blocchiamo l'invio del form.
     */
    if (formRegistrazione != null) {
        formRegistrazione.addEventListener("submit", function (event) {
            const emailValida = validaEmail();

            if (!emailValida) {
                event.preventDefault();
            }
        });
    }
});