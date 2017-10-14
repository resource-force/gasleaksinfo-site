import "normalize-scss";
import "../stylesheets/main.scss";

import * as React from "react";
import * as ReactDOM from "react-dom";
import App from "./components/App";
import api from "./helpers/api";

ReactDOM.render(
    <App />,
    document.getElementById("react"),
);
