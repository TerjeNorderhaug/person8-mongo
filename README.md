# Person8

SPA to restore the identity of homeless youth using MongoDB Atlas & Stitch for serverless implementation.

## References

https://clojure.org/
https://clojurescript.org/
https://reagent-project.github.io/
https://getbootstrap.com/docs/4.0/layout/overview/
http://www.material-ui.com
https://expressjs.com/

## Deploy with Docker

Start a local web server in a Docker container:

    docker-compose up release

Access http://localhost:5000 from a browser.

## Run Locally

Requirements: leiningen, heroku, npm

To start a server on your own computer:

    lein do clean, deps, compile
    heroku local web

Point your browser to the displayed local port.
Click on the displayed text to refresh.

## Deploy to Heroku

To start a server on Heroku:

    heroku apps:create
    git push heroku master
    heroku open

This will open the site in your browser.

## Development Workflow

Start figwheel for interactive development with
automatic builds and code loading:

    lein figwheel app server

Wait until Figwheel is ready to connect, then
start a server in another terminal:

    lein run

Open the displayed URL in a browser.
Figwheel will push code changes to the app and server.

Alternatively, to set the local environment from a .env file, start the server with:

    heroku local web

To test the system, execute:

    lein test

## License

Copyright © 2019 Terje Norderhaug

Distributed under the Eclipse Public License either version 1.0
or (at your option) any later version.
