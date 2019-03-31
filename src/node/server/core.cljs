(ns server.core
  (:require-macros
   [cljs.core.async.macros :as m
    :refer [go go-loop alt!]])
  (:require
   [polyfill.compat]
   [cljs.nodejs :as nodejs]
   [cljs.core.async :as async
    :refer [chan close! timeout put!]]
   [reagent.core :as reagent
    :refer [atom]]
   [taoensso.timbre :as timbre]
   [bidi.bidi :as bidi]
   [macchiato.http :as http]
   [macchiato.middleware.resource :as resource]
   [macchiato.util.response :as r]
   [mount.core :as mount
    :refer [defstate]]
   [app.state :as state]
   [app.routes :as routes
    :refer [routes]]
   [app.core :as app
    :refer [static-page]]))

(defstate express :start (nodejs/require "express"))

(def cors (nodejs/require "cors"))

(defn handler [req res]
  (if false #_(= "https" (aget (.-headers req) "x-forwarded-proto"))
    (.redirect res (str "http://" (.get req "Host") (.-url req)))
    (go
      (.set res "Content-Type" "text/html")
      (.send res (<! (static-page))))))

(defn macchiato-handler [http-router & [opts]]
  (let [route (http/handler http-router opts)]
    (fn [req res next]
      (if-not (route req res)
        (next)))))

(def handlers
  {:root #(timbre/warn "Missing root handler")
   :mobile (fn [req res]
              (-> (assoc-in state/state
                            [:mode :current]
                            "mobile")
                  (app/html-content)
                  (r/ok)
                  (r/content-type "text/html")
                  (res)))
   :okta-redirect (fn [req res]
                    (timbre/info "Okta Redirect")
                    (-> "Okta Redirect"
                         (r/ok)
                         (r/content-type "text/html")
                         (res)))})



(defn bidi-router [routes handlers & [raise]]
  (fn [req res]
    (if-let [{:keys [handler route-params]}
             (bidi.bidi/match-route* routes (:uri req) req)]
      (if-let [route (get handlers handler)]
        (route (assoc req :route-params route-params) res)))))

(def router
  (-> (bidi-router routes handlers)
      #_(wrap-defaults)
      (resource/wrap-resource "resources/public")))

(defn server [express-app port success]
  (doto express-app
    (.use (cors))
    (.get "/" handler)
    (.use "/" (macchiato-handler router))
    (.listen port success)))

(defn -main [& mess]
  (mount/start)
  (let [port (or (.-PORT (.-env js/process)) 1337)
        express-app (@express)
        success #(timbre/info (str "Server running at http://127.0.0.1:" port "/"))]
    (server express-app port success)))

(set! *main-cli-fn* -main)
