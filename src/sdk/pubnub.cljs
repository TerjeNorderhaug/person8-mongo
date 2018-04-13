(ns sdk.pubnub
  (:require
   [cljsjs.pubnub]
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [util.lib :as lib]
   [util.env :as env
    :refer [env]]))

;; Dependencies: [cljsjs/pubnub "4.1.1-0"]

(def PubNub
  (memoize
   (fn []
     (if (lib/node?)
       (js/require "pubnub")
       js/PubNub))))

(defn new-pubnub [config]
  (let [PN (PubNub)]
    (new PN (clj->js config))))

(def config {:publishKey   (or (env "PUBNUB_PUBLISH_KEY")
                               "pub-c-9fa72b4b-080d-4483-8f26-c10f390749ed")
             :subscribeKey (or (env "PUBNUB_SUBSCRIBE_KEY")
                               "sub-c-51fe3994-092b-11e8-be21-ca57643e6300")})

(def pubnub
  (memoize
   (fn []
     (new-pubnub config))))

(defn add-listener [pubnub args]
  (.addListener pubnub (clj->js args)))

(defn subscribe
  ([args]
   (subscribe (pubnub) args))
  ([pubnub {:keys [channels] :as args}]
   (.subscribe pubnub (clj->js args))))

(defn publish
  ([msg]
   (publish (pubnub) msg))
  ([pubnub msg]
   (.publish pubnub (clj->js msg))))

(defn demo [pubnub]
  (let [channel "demo"]
    (publish pubnub {:channel channel
                     :message "hello"})
    (add-listener pubnub
                  {:status (fn [status]
                             (timbre/info "Pubnub Status:" (js->clj status)))
                   :message (fn [message]
                              (timbre/info "Pubnub Message:" (js->clj message)))})
    (subscribe pubnub {:channels [channel]})))

#_
(demo (pubnub))

(rf/reg-fx
 :pubnub-publish
 (fn [{:as event}]
   (publish pubnub event)))
