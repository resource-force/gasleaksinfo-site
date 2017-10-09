import * as React from 'react';
import Leak from '../data/Leak';
import getLeaks from '../helpers/getLeaks';
import LeakRow from './LeakRow';

export default class LeakTable extends React.Component {
    state = { leaks: Array<Leak>() }
    async componentDidMount() {
        this.setState({ leaks: await getLeaks() });
    }
    render() {
        return (
            <table className="leakTable">
                <thead>
                    <tr>
                        <th>Link</th>
                        <th>Location</th>
                        <th>Status</th>
                        <th>Size</th>
                        <th>Reported on</th>
                        <th>Fixed on</th>
                        <th>Vendor ID</th>
                        <th>Source</th>
                    </tr>
                </thead>
                <tbody>
                    {this.state.leaks.map((leak, i) =>
                        <LeakRow leak={leak} />
                    )}
                </tbody>
            </table>
        );
    }
}
