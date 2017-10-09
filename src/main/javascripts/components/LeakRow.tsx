import * as React from 'react';
import Leak from '../data/Leak';

export default function LeakRow(props: { leak: Leak }) {
    return (
        <tr key={props.leak.href.toString()}>
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