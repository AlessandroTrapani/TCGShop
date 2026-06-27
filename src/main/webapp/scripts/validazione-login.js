/*
 * File JavaScript dedicato alla validazione del form di login.
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
     * Recupero gli elementi principali del form di login.
     */
    const formLogin = document.getElementById("form-login");
    const campoEmail = document.getElementById("email");
    const campoPassword = document.getElementById("password");

    const erroreEmail = document.getElementById("errore-email-login");
    const errorePassword = document.getElementById("errore-password-login");

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
     * Controlla che la password non sia vuota.
     * 
     * @returns true se la password è presente, false altrimenti
     */
    function validaPassword() {
        const passwordInserita = campoPassword.value.trim();

        if (passwordInserita === "") {
            errorePassword.textContent = "La password è obbligatoria.";
            campoPassword.classList.add("input-errore");
            return false;
        }

        errorePassword.textContent = "";
        campoPassword.classList.remove("input-errore");
        return true;
    }

    /*
     * Controllo eseguito quando l'utente esce dal campo email.
     * In questo modo il messaggio rosso compare senza dover inviare il form.
     */
    if (campoEmail != null) {
        campoEmail.addEventListener("blur", function () {
            validaEmail();
        });
    }

    /*
     * Controllo eseguito quando l'utente esce dal campo password.
     */
    if (campoPassword != null) {
        campoPassword.addEventListener("blur", function () {
            validaPassword();
        });
    }

    /*
     * Controllo finale al submit del form.
     * Se email o password non sono valide, blocchiamo l'invio.
     */
    if (formLogin != null) {
        formLogin.addEventListener("submit", function (event) {
            const emailValida = validaEmail();
            const passwordValida = validaPassword();

            if (!emailValida || !passwordValida) {
                event.preventDefault();
            }
        });
    }
});