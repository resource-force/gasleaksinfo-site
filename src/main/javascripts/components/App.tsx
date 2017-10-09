import * as React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import AboutPage from './AboutPage';
import MainPage from './MainPage';
import Footer from './Footer';

export default class App extends React.PureComponent {
    render() {
        return (
            <BrowserRouter>
                <div className="app">
                    <h1 className="app__header">Gasleaks.info</h1>
                    <Route path="/" exact component={MainPage} />
                    <Route path="/about" exact component={AboutPage} />
                    <Footer />
                </div>
            </BrowserRouter>
        );
    }
}