import * as React from 'react';
import LeakTable from './LeakTable';

export default class App extends React.PureComponent {
    render() {
        return (
            <div className="app">
                <h1 className="app__header">Leaks</h1>
                <LeakTable />
            </div>
        );
    }
}