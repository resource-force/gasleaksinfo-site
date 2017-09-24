# leakfixing-site

A Spring Boot + MongoDB web app to track gas leaks in Acton

## Running

Requirements: Gradle, NodeJS + NPM.

1. Run `gradle bootRun` to do a one-time build of bundle.js and start Spring
Boot.
2. Run `npm run watch` to start the webpack dev server on port 9090.
3. Go to [https://localhost:9090](https://localhost:9090) to view the site.

Also set up a MongoDB instance and configure it in application.properties.
(For local development, Spring Boot will automatically connect to a MongoDB
server on localhost.)

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