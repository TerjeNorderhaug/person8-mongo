# WellBE

Using WELL and Exonum in medical visits.

Please see src/cljs/api/ for custom clojurescript libraries for Exonum and WELL, and src/cljs/app for the react app. A small Node Express server is in node/server to serve the page.

Implemented as a universal (nee isomporhic) Clojurescript React SPA.
Runs on Heroku using node express, bootstrap, reagent and Kioo templates,
with code shared between frontend and backend, where Figwheel hotloads code changes
to both.

## Requirements

leiningen, heroku, npm

## Run Locally

To start a server on your own computer:

    lein do clean, deps, compile
    lein run

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

Copyright © 2018 Terje Norderhaug

Distributed under the Eclipse Public License either version 1.0 or (atyour option) any later version.
