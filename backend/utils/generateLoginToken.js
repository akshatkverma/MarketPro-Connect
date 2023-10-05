const jwt = require("jsonwebtoken");

const generateLoginToken = (user_id) => {
    let jwtSecretKey = process.env.SECRET_KEY;
    let data = {
        time: Date(),
        user_id: user_id
    };

    const token = jwt.sign(data, jwtSecretKey, { expiresIn: '30d' });
    return token;
};

module.exports = { generateLoginToken }