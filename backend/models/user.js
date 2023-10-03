// This is a mongoose model for the users collection in the database.
// It defines the schema for this collection.

const mongoose = require("mongoose");
const bcrypt = require("bcrypt");

const UserSchema = mongoose.Schema({
    name: {
        type: String,
    },
    email: {
        type: String,
        required: true,
        unique: true,
    },
    password: {
        type: String,
    },
    isSeller: {
        type: Boolean,
        deafult: false
    },
    otp: {
        type: Number,
    },
    otp_validity: {
        type: Date,
    },
});

// Validate OTP
UserSchema.statics.validateOTPAndRegister = function (email, otp, name, password, confirm_password) {
    return new Promise(async (resolve, reject) => {
        try {
            // Find the user by email
            const user = await this.findOne({ email: email }, "otp otp_validity");

            if (!user) {
                reject(`User with email '${email}' not found.`);
                return;
            }

            const now = new Date();
            if (!user.otp) {
                reject(`No OTP found.`);
                return;
            }
            if (!user.otp_validity || new Date(user.otp_validity).getTime() < now.getTime()) {
                reject(`OTP is invalid`);
                return;
            }

            if (otp != user.otp) {
                reject(`OTP is invalid`);
                return;
            }

            if (!name) {
                reject("Name is missing");
                return;
            }
            if (!password) {
                reject("Password is missing");
                return;
            }
            if (!confirm_password) {
                reject("Confirm Password is missing");
                return;
            }
            if (password !== confirm_password) {
                reject("Password and confirm password must match");
                return;
            }

            const hashedPassword = await bcrypt.hash(
                password, 10
            );

            user.name = name;
            user.password = hashedPassword;
            await user.save();

            resolve(user);
            return;
        } catch (error) {
            reject(error); // Reject the promise with the error
            return;
        }
    });
};

const User = new mongoose.model("user", UserSchema);
module.exports = User;