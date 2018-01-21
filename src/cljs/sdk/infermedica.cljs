(ns sdk.infermedica
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [chan <! >! put! close! timeout promise-chan]]
   [cljs-http.client :as http]
   [taoensso.timbre :as timbre]
   [lib.chan :as chan]))

(def app-id
  (or (aget js/process "env" "INFERMEDICA_APP_ID")
      (timbre/warn "Need to set INFERMEDICA_APP_ID environment var")))

(def app-key
  (or (aget js/process "env" "INFERMEDICA_APP_KEY")
      (timbre/warn "Need to set INFERMEDICA_APP_KEY environment var")))

(defn endpoint [& path]
  (apply str "https://api.infermedica.com/v2/" path))

(defn fetch [path {:as params}]
  (http/post (endpoint path)
             {:with-credentials? false
              :headers {"App-Id" app-id "App-Key" app-key}
              :json-params params
              :channel (chan 1 (map identity))}))

(defn fetch-parse [{:as params}]
  ;; https://developer.infermedica.com/docs/nlp
  (fetch "parse" params))

(defn fetch-diagnosis [{:as params}]
  ;; https://developer.infermedica.com/docs/diagnosis
  (timbre/debug "Fetch diagnosis:" params)
  (fetch "diagnosis" params))

(defn generate-medical-analysis [text]
  (go
   (some->> {:text text
             :concept_type ["symptom"
                            "risk_factor"]}
            (fetch-parse)
            (<!)
            (:body))))

#_
(-> "i feel smoach pain but no couoghing today"
    (generate-medical-analysis)
    (chan/echo))

(defn generate-medical-diagnosis [{:as args}]
  (go
   (some->> args
            (fetch-diagnosis)
            (<!)
            (:body))))
