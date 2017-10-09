import * as React from "react";
import Leak from "../data/Leak";

export default function LeakRow(props: { leak: Leak, expanded: boolean, onClick: () => void }) {
    if (props.expanded) {
        return (
            <tr className="leakRow--expanded" onClick={props.onClick}>
                <td colSpan={8}>lol</td>
            </tr>
        );
    } else {
        return (
            <tr className="leakRow" onClick={props.onClick}>
                <td><a href={props.leak.href.toString()}>Link</a></td>
                <td>{props.leak.location}</td>
                <td>{props.leak.status}</td>
                <td>{props.leak.size}</td>
                <td>{props.leak.reportedOn}</td>
                <td>{props.leak.fixedOn}</td>
                <td>{props.leak.vendorId}</td>
                <td>{props.leak.source.vendor} - {props.leak.source.name} - {props.leak.source.date}</td>
            </tr>
        );
    }
}
