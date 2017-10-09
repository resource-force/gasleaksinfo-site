import * as React from "react";
import Leak from "../data/Leak";
import getLeaks from "../helpers/getLeaks";
import LeakRow from "./LeakRow";

export default class LeakTable extends React.Component {
    public state = { leaks: Array<Leak>(), expandedRow: -1 };
    public async componentDidMount() {
        this.setState({ leaks: await getLeaks() });
    }
    public render() {
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
                        <LeakRow
                            key={leak.href.toString()}
                            leak={leak}
                            expanded={i === this.state.expandedRow}
                            onClick={() => this.expandRow(i)} />,
                    )}
                </tbody>
            </table>
        );
    }
    private expandRow(rowNumber) {
        this.setState(Object.assign(
            this.state,
            {
                expandedRow: this.state.expandedRow === rowNumber ? -1 : rowNumber,
            }));
    }
}
