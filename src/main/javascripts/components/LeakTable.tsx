import * as React from 'react';
import Leak from '../data/Leak';
import getLeaks from '../helpers/getLeaks';

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
                        <tr key={leak.href.toString()}>
                            <td><a href={leak.href.toString()}>Link</a></td>
                            <td>{leak.location}</td>
                            <td>{leak.status}</td>
                            <td>{leak.size}</td>
                            <td>{leak.reportedOn}</td>
                            <td>{leak.fixedOn}</td>
                            <td>{leak.vendorId}</td>
                            <td>{leak.source.vendor} - {leak.source.name} - {leak.source.date}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        );
    }
}
