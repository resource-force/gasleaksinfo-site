import Leak from "../data/Leak";

import api from "../helpers/api";

const API_ROOT = "/api/r0";

export async function getLeaks() {
    const response = await api(`${API_ROOT}/leaks`);
    return await Promise.all(response.entity._embedded.leaks
        // HACK: find better way to get leak id
        .map((leak, i) => Object.assign(leak, { id: i + 1 }))
        .map(async (leak) => {
            const source = await api(leak._links.source.href);
            return Object.assign(leak, { source: source.entity });
        }));
}
export async function updateLeak(id: number, data: Leak) {
    return await api({
        entity: data,
        headers: { "Content-Type": "application/json" },
        method: "PUT",
        path: `${API_ROOT}/leaks/${id}`,
    });
}
export async function addLeak(data: Leak) {
    // pass
}
