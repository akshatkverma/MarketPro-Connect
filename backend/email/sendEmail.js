const { transporter } = require("./transporter");

function sendEmail(receiver, message) {
    if (receiver && message) {
        try {
            const mailData = {
                from: `Market-Pro Connect <${process.env.EMAIL_USERNAME}>`,
                to: receiver,
                subject: message.subject,
                template: message.template,
                context: message.context,
            };

            transporter.sendMail(mailData, (err, info) => {
                if (err) {
                    console.log(err);
                    return false;
                }
                else {
                    console.log("Mail sent.");
                }
            });
            return true;
        } catch (error) {
            console.log(error);
        }
    } else {
        return false;
    }
};

module.exports = { sendEmail };