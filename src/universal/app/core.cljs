(ns app.core
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [<! chan close! alts! timeout put!]]
   [goog.dom :as dom]
   [reagent.core :as reagent
    :refer [atom]]
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre]
   [mount.core :as mount
    :refer [defstate]]
   [util.rflib :as rflib]
   [sdk.okta :as okta]
   [sdk.twilio :as twilio]
   [app.state :as state]
   [app.session :as session]
   [app.view.page
    :refer [page html5]]
   [app.view.view
    :refer [view]]))

(defn scripts [initial]
  [{:src okta/auth-js}
   {:src "/js/out/app.js"}
   (str "main_cljs_fn("
        (pr-str (pr-str initial))
        ")")])

(defn html-content [initial]
  (let [state (session/state initial)]
    (-> state
        (page :scripts (scripts initial)
              :title (if (:brand state) @(:brand state) "HackBench")
              :forkme false)
        (html5))))

(defn static-page []
  (go (html-content state/state)))

(defstate reporting
  :start #(timbre/info "Starting")
  :stop #(timbre/info "Stopping"))

(def use-default-state? false)

(defn activate [initial]
  (let [initial (if use-default-state? state/state initial)]
    (session/initialize initial)
    (let [el (dom/getElement "canvas")
          state (session/subscriptions
                 (map first initial))]
      (reagent/render [#(view state)] el))
    (mount/start)))
