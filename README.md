# leakfixing-site

A Spring Boot + MongoDB web app to track gas leaks in Acton

## Running

Requirements: Gradle, NodeJS + NPM.

1. Run `gradle bootRun` to do a one-time build of bundle.js and start Spring
Boot.
2. Run `npm start` to start the webpack dev server on port 9090.
3. Go to [https://localhost:9090](https://localhost:9090) to view the site.

Also set up a MongoDB instance and configure it in application.properties.
(For local development, Spring Boot will automatically connect to a MongoDB
server on localhost.)

## Development

- `curl -X POST -H "Content-Type: application/json" -d '{"vendor": "NGRID", "name": "xd", "date": "2017-01-01"}' localhost:9090/api/r0/datasets`
- `curl -X POST -H "Content-Type: application/json" -d '{"location": "xd", "status": "FIXED", "size": null, "reportedOn": "2017-01-01", "fixedOn": "2017-01-01", "vendorId": null}' localhost:9090/api/r0/leaks`
- `curl -i -X PUT -H "Content-Type:text/uri-list" -d "http://localhost:9090/api/r0/datasets/1" http://localhost:9090/api/r0/leaks/1/source`

## License

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program. If not, see <http://www.gnu.org/licenses/>.