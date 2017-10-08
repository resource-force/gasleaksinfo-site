import * as React from 'react';
import Leak from './Leak';

export default class App extends React.Component {
    state = { leaks: Array<Leak>() }
    async componentDidMount() {
        const leaks = await fetch('/api/r0/leaks')
            .then(res => res.json())
            .then(json => json._embedded.leaks);
        this.setState({
             leaks: await Promise.all(leaks.map(async (leak: any) => {
                leak.source = await fetch(leak._links.source.href).then(res => res.json());
                return leak;
            }
        ))});
    }
    render() {
        console.log(this.state)
        return (
            <div>
                <h1>Leaks</h1>
                <table>
                    <thead>
                        <tr>
                            <th>Status</th>
                            <th>Location</th>
                            <th>Reported on</th>
                            <th>Fixed on</th>
                            <th>Source</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.state.leaks.map((leak, i) =>
                        <tr key={i}>
                            <td>{leak.status}</td>
                            <td>{leak.location}</td>
                            <td>{leak.reportedOn}</td>
                            <td>{leak.fixedOn}</td>
                            <td>{leak.source.vendor} - {leak.source.name} - {leak.source.date}</td>
                        </tr>)}
                    </tbody>
                </table>
            </div>
        );
    }
}