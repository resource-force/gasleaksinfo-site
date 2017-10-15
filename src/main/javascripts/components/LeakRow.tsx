import * as React from "react";

import Leak from "../data/Leak";
import * as client from "../helpers/client";

export default class LeakRow extends React.Component<
    { leak: Leak, expanded: boolean, onToggleExpand: () => void, onUpdate: (Leak) => void },
    Leak> {
    public constructor(props) {
        super(props);
        this.state = props.leak;
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    public render() {
        if (this.props.expanded) {
            return (
                <tr className="leakRow--expanded">
                    <td colSpan={8}>
                        <form onSubmit={this.handleSubmit}>
                            <h2>Editing leak #{this.state.id}</h2>
                            <p><label>Location:
                                <input name="location"
                                    value={this.state.location}
                                    onChange={this.handleInputChange} /></label></p>
                            <p><label>Status:
                                <select name="status" value={this.state.status} onChange={this.handleInputChange}>
                                    <option value="FIXED">Fixed</option>
                                    <option value="UNREPAIRED">Unrepaired</option>
                                    <option value="Missing">Missing</option>
                                </select></label></p>
                            <p><input type="submit" value="Submit" /></p>
                        </form>
                    </td>
                </tr>
            );
        } else {
            return (
                <tr className="leakRow" onClick={this.props.onToggleExpand}>
                    <td>{this.props.leak.location}</td>
                    <td>{this.props.leak.status}</td>
                    <td>{this.props.leak.size}</td>
                    <td>{this.props.leak.reportedOn}</td>
                    <td>{this.props.leak.fixedOn}</td>
                    <td>{this.props.leak.vendorId}</td>
                    <td>{this.props.leak.source.vendor} -
                        {this.props.leak.source.name} -
                        {this.props.leak.source.date}</td>
                </tr>
            );
        }
    }
    private handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        this.props.onUpdate(this.state);

    }
    private handleInputChange(event) {
        const target = event.target;
        const value = target.type === "checkbox" ? target.checked : target.value;
        const name: string = target.name;

        this.setState(Object.assign({ [name]: value }));
    }
}
