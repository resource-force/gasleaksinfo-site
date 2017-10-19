import * as React from "react";
import {Link} from "react-router-dom";

export default function Footer() {
    return (
        // TODO better message!
        <footer className="footer">
            Made by Kevin Liu, Kunal Sharda, and Aneesh Edara for Resource
            Force. See the <Link to="/about">about page</Link> for more
            details.
        </footer>
    );
}
