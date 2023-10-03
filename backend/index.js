// ------------- Imports --------------
const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");

const dotenv = require("dotenv");
dotenv.config();

const mongoose = require("mongoose");

const User = require("./models/user");
const registerRoute = require("./controllers/auth/registerController");


// Create the express app object
const app = express();

// ------------- Middlewares --------------
app.use(bodyParser.json({ limit: '10mb' })); // To parse json objects
app.use(bodyParser.urlencoded({ limit: '10mb', extended: true })); // To pass urlencoded messages
app.use(cors()); // Allow Cross Origin Resource Sharing

// -------------- API Routes -----------------
app.use('/api/auth', registerRoute);

// ------------- Database connection and starting the server --------------
const PORT = process.env.PORT || 5000;

/* When "strictQuery" is set to true, Mongoose will throw an error if we try to query a model with undefined fields. 
  This helps ensure that the queries are precise and do not include unintended or misspelled fields.
*/
mongoose.set("strictQuery", true);

mongoose
    .connect(process.env.CONNECTION_URL, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
    })
    .then(() =>
        app.listen(PORT, () => {
            console.log(`Server running at http://localhost:${PORT}`);
        })
    )
    .catch((error) => {
        console.log(error);
    });