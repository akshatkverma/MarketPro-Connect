const { transporter } = require("./transporter");

async function sendEmail(receiver, message) {
    if (receiver && message) {
        try {
            const mailData = {
                from: `Market-Pro Connect <${process.env.EMAIL_USERNAME}>`,
                to: receiver,
                subject: message.subject,
                template: message.template,
                context: message.context,
            };

            await transporter.sendMail(mailData);
            return true;

        } catch (error) {
            console.log(error);
            return false;
        }
    } else {
        return false;
    }
};

module.exports = { sendEmail };