import * as React from "react";

import Leak from "../data/Leak";
import * as client from "../helpers/client";
import LeakRow from "./LeakRow";

enum FocusState {
    LOADING,
    EXPANDED,
    NONE,
}

export default class LeakTable extends React.Component {
    public state = { leaks: Array<Leak>(), focusState: FocusState.NONE, focusedRow: -1 };
    public async componentDidMount() {
        this.setState({ leaks: await client.getLeaks() });
    }
    public render() {
        return (
            <table className="leakTable">
                <thead>
                    <tr>
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
                    {this.state.leaks.map((leak, i) => {
                        if (this.state.focusState === FocusState.LOADING) {
                            return (
                                <tr key={leak.id}><td colSpan={7}>UPDATING</td></tr>
                            );
                        } else {
                            return (
                                <LeakRow
                                    key={leak.id}
                                    leak={leak}
                                    expanded={i === this.state.focusedRow}
                                    onToggleExpand={() => this.toggleRowFocus(FocusState.EXPANDED, i)}
                                    onUpdate={(newLeak) => this.updateLeak(i, newLeak)} />
                            );
                        }
                    })}
                </tbody>
            </table>
        );
    }
    private toggleRowFocus(focus: FocusState, rowNumber?: number) {
        if (rowNumber == null) {
            rowNumber = this.state.focusedRow;
        }
        this.setState({
            focusState: focus,
            focusedRow: this.state.focusedRow === rowNumber ? -1 : rowNumber,
        });
    }
    private async updateLeak(rowNumber, newLeak) {
        this.toggleRowFocus(FocusState.LOADING, rowNumber);
        await client.updateLeak(newLeak.id, newLeak);
        // update state
        this.state.leaks[rowNumber] = newLeak;
        this.setState({
            leaks: this.state.leaks,
        });
        this.toggleRowFocus(FocusState.NONE);
    }
}
