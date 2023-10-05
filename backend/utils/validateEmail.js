// Helper function to validate the structure of email
function validateEmail(email) {
    if (email && email.includes("@") && email.includes(".", email.indexOf("@"))) return true;
    else return false;
}

module.exports = { validateEmail };