// Imports
const express = require("express");
const router = express.Router();

// Importing the controllers
const registerController = require("../controllers/auth/registerController");

// Route to register a new user
router.post("/register", registerController);

// Route to login a user
// router.post("/login", loginController);

module.exports = router;