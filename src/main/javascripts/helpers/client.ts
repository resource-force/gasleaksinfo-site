import Leak from "../data/Leak";

import api from "../helpers/api";

export async function getLeaks() {
    const response = await api("/api/r0/leaks");
    return await Promise.all(response.entity._embedded.leaks
        .map((leak) => Object.assign(leak, { href: new URL(leak._links.self.href) }))
        .map(async (leak) => {
            const source = await api(leak._links.source.href);
            return Object.assign(leak, { source: source.entity });
        }));
}
