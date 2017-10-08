import Leak from '../data/Leak'

export default async function getLeaks() {
    return fetch('/api/r0/leaks')
        // Get JSON response
        .then(res => res.json())
        .then(json => json._embedded.leaks)
        // Set href to leak reference
        .then(leaks => {
            leaks.forEach(leak =>
                leak.href = new URL(leak._links.self.href));
                return leaks;
            })
        // Set source via another fetch request
        .then(leaks => Promise.all(leaks.map(async (leak: any) =>
            fetch(leak._links.source.href)
                .then(res => res.json())
                .then(source => { leak.source = source; return leak })
            )));
}