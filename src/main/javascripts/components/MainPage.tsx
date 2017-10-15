import * as React from "react";
import LeakTable from "./LeakTable";

export default class AboutPage extends React.PureComponent {
    public render() {
        return (
            <div>
                <h1>Gas Leaks Administrative Interface</h1>
                <LeakTable />
            </div>
        );
    }
}
