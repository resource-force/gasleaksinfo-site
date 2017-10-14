import Leak from "../data/Leak";

import api from "../helpers/api";

export async function getLeaks() {
    return api("/api/r0/leaks")
        // Get JSON response
        .then((res) => res.entity._embedded.leaks)
        // Set href to leak reference
        .then((leaks) => {
            leaks.forEach((leak) => leak.href = new URL(leak._links.self.href));
            return leaks;
        })
        // Set source via another fetch request
        .then((leaks) => Promise.all(leaks.map(async (leak: any) =>
            fetch(leak._links.source.href)
                .then((res) => res.json())
                .then((source) => { leak.source = source; return leak; }),
            )));
}
