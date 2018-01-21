# WellBE

Globally distributed continuous healthcare for globetrotters with chronic conditions, applying AI and blockchain to change lives for the better.

Implemented as a universal (nee isomporhic) Clojurescript React SPA. Runs on Docker and Heroku using node express, bootstrap, reagent and Kioo templates, with code shared between frontend and backend, where Figwheel hotloads code changes to both.

## Deploy with Docker

Start a local web server with an etherium blockchain:

    docker blockchain up

Access http://localhost:5000 from a browser.

## Run Locally

Requirements: leiningen, heroku, npm

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
