const User = require("../../models/user");
const { sendEmail } = require("../../email/sendEmail");
const { generateLoginToken } = require("../../utils/generateLoginToken");
const { validateEmail } = require("../../utils/validateEmail");

async function registerController(req, res) {
    console.log("Reached register")
    console.log(req.body)
    const { email, send_otp, verify_and_register, name, password, confirm_password, otp } = req.body;
    try {
        if (send_otp == "true") {
            if (validateEmail(email)) {
                const { status, message } = await sendOtp(email);
                console.log(status, message)
                if (status) return res.status(200).json({ status: true });
                else return res.status(400).json({ status: false });
            }
            else {
                return res.status(400).json({ message: "Invalid email" });
            }
        }
        else if (verify_and_register == "true") {
            if (otp && validateEmail(email)) {
                try {
                    const user = await User.validateOTPAndRegister(email, otp, name, password, confirm_password);
                    const token = generateLoginToken(user._id);
                    console.log(token)
                    return res.status(200).json({ message: "Registered Successfully", token: token });
                }
                catch (err) {
                    console.log(err)
                    return res.status(400).json({ message: err });
                }
            }
            else {
                return res.status(400).json({ message: "Invalid email or otp" });
            }
        }
        else {
            return res.status(400).json({ message: "Invalid register request" });
        }
    } catch (error) {
        return res.status(500).json({ message: "Internal Server Error" });
    }
}


// Helper function to send otp while registration
async function sendOtp(email) {
    let otp = Math.floor(100000 + Math.random() * 900000);
    let otp_validity = new Date();

    // OTP is valid for 10 minutes
    otp_validity.setMinutes(otp_validity.getMinutes() + 10);

    // Create the user
    const user = User({
        email: email,
        otp: otp,
        otp_validity: otp_validity
    });

    let toSendOtp = true;
    try {
        await user.save();
    }
    catch (err) {
        const existingUser = await User.findOne({ email: email }).exec();
        if (existingUser?.name) {
            toSendOtp = false;
            return {
                status: false,
                message: "You are already registered."
            }
        }
        else {
            existingUser.otp = otp;
            existingUser.otp_validity = otp_validity;

            await existingUser.save();
        }
    }
    finally {
        if (toSendOtp) {
            message = {
                subject: "Market-Pro Connect : Registration",
                template: "registrationOtp",
                context: {
                    email: email,
                    otp: otp
                }
            }
            const mailSent = await sendEmail(email, message);

            if (mailSent)
                return {
                    status: true,
                    message: "OTP sent."
                }
            else
                return {
                    status: false,
                    message: "Could not send the email. Please retry."
                }
        }
    }
}


module.exports = registerController;