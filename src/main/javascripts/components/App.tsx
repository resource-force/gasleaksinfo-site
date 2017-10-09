import * as React from "react";

import Footer from "./Footer";
import Header from "./Header";
import Main from "./Main";

export default class App extends React.PureComponent {
    public render() {
        return (
            <div className="app">
                <Header />
                <Main />
                <Footer />
            </div>
        );
    }
}
