import * as React from "react";
import {Link} from "react-router-dom";

export default function Header() {
    return (
        <header className="header">
            <h1 className="header__siteName"><Link to="/">Gasleaks.info</Link></h1>
            <p className="header__slogan">Mapping Acton's hidden gas leaks</p>
        </header>
    );
}
