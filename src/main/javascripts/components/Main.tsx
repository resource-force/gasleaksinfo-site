import * as React from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import AboutPage from './AboutPage';
import MainPage from './MainPage';

export default function Main() {
    return (
        <BrowserRouter>
            <div>
                <Route path="/" exact component={MainPage} />
                <Route path="/about" exact component={AboutPage} />
            </div>
        </BrowserRouter>
    );
}