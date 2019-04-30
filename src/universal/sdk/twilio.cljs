(ns sdk.twilio
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [mount.core :as mount
    :refer [defstate]]
   [cljs.core.async :as async
     :refer [chan <! >! put! close! timeout promise-chan]]
   [cljs-http.client :as http]
   [util.chan :as chan]
   [util.lib :as lib]
   [util.env :as env
    :refer [env]]))

(def twilio-client
  (memoize
   (fn [sid token]
     (if (and (lib/node?) sid token)
       (if-let [twilio (js/require "twilio")]
         (twilio sid token))))))

(def account-sid (if (lib/node?)(env "TWILIO_SID")))
(def auth-token  (if (lib/node?)(env "TWILIO_TOKEN")))

(def client (twilio-client account-sid auth-token))

(defn send-sms [{:keys [from to body media] :as msg}]
  (timbre/info "Send SMS via Twilio:" msg)
  (if (lib/node?)
    (-> (.messages client)
        (.create (-> (clojure.set/rename-keys msg {:media :mediaUrl})
                     (clj->js)))
        (.then (fn [res] (timbre/info "Sent SMS using Twilio"))))
    #_
    (http/post (endpoint path)
             {:with-credentials? false
              :headers {"App-Id" app-id "App-Key" app-key}
              :json-params params
              :channel (chan 1 (map identity))})))
