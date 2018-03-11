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
   [app.core :as app
    :refer [static-page]]))

(def express (nodejs/require "express"))

(defn handler [req res]
  (if (= "https" (aget (.-headers req) "x-forwarded-proto"))
    (.redirect res (str "http://" (.get req "Host") (.-url req)))
    (go
      (.set res "Content-Type" "text/html")
      (.send res (<! (static-page))))))

(defn macchiato [http-router & [opts]]
  (let [route (http/handler http-router opts)]
    (fn [req res next]
      (if-not (route req res)
        (next)))))

(def handlers
  {:root #(timbre/warn "Missing root handler")})

(def routes
  ["/" {"" :root}])

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

(defn server [port success]
  (doto (express)
    (.get "/" handler)
    (.use "/" (macchiato router))
    (.listen port success)))

(defn -main [& mess]
  (let [port (or (.-PORT (.-env js/process)) 1337)]
    (server port
            #(timbre/info (str "Server running at http://127.0.0.1:" port "/")))))

(set! *main-cli-fn* -main)
