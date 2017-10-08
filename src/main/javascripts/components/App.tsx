import * as React from 'react';
import LeakTable from './LeakTable';

export default class App extends React.PureComponent {
    render() {
        return (
            <div>
                <h1>Leaks</h1>
                <LeakTable />
            </div>
        );
    }
}