(ns app.routes)

(def routes
  ["/" {"" :root
        "mobile" :mobile
        "okta/" {"redirect" :okta-redirect}}])
