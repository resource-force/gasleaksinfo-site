import * as React from "react";
import { Helmet } from "react-helmet";
import { BrowserRouter, Route } from "react-router-dom";

import Footer from "./Footer";
import Header from "./Header";

import AboutPage from "./AboutPage";
import MainPage from "./MainPage";

export default function App() {
    return (
        <BrowserRouter>
            <div className="app">
                <Helmet>
                </Helmet>
                <Header />
                <div className="main">
                    <Route path="/" exact component={MainPage} />
                    <Route path="/about" exact component={AboutPage} />
                </div>
                <Footer />
            </div>
        </BrowserRouter>
    );
}
