const User = require("../../models/user");
const bcrypt = require("bcrypt");
const { validateEmail } = require("../../utils/validateEmail");
const { generateLoginToken } = require("../../utils/generateLoginToken");

async function loginController(req, res) {
    const { email, password } = req.body;
    try {
        if (validateEmail(email) && password) {
            const user = await User.findOne({ email: email }).exec();
            if (user) {
                const valid = await bcrypt.compare(password, user.password);
                if (valid) {
                    const token = generateLoginToken(user._id);
                    return res.status(200).json({ message: "Login Successful", token: token });
                }
                else {
                    return res.status(400).json({ message: "Invalid Credentials" });
                }
            }
            else {
                return res.status(400).json({ message: "User is not registered" });
            }
        }
        else {
            return res.status(400).json({ message: "Credentials missing" });
        }
    }
    catch (err) {
        return res.status(500).json({ message: "Internal Server Error" });
    }
}

module.exports = loginController;