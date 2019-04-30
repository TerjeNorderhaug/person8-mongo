(ns sdk.mongo
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async :as async
    :refer [chan <! >! put! close! timeout promise-chan]]
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [util.lib :as lib]
   [util.chan
    :refer [echo]]
   [util.env :as env
    :refer [env]]))

(def stich-sdk-url "https://s3.amazonaws.com/stitch-sdks/js/bundles/4.3.1/stitch.js")

(defonce client
  (memoize
   (fn []
     (js/stitch.Stitch.initializeDefaultAppClient "person8-pzkzk"))))

(defonce db
  (memoize
   (fn []
     (->
      (.getServiceClient (client)
        js/stitch.RemoteMongoClient.factory "mongodb-atlas")
      (.db "database")))))


(defn demo []
  "Working example from MongoDB"
  (timbre/debug "Starting Mongo demo")
  (->
    (.loginWithCredential (.-auth (client))
                          (new js/stitch.AnonymousCredential))
    (.then (fn [user]
             (timbre/debug "Login successful:" user)
             (-> (.collection (db) "personal")
                 (.updateOne #js{:owner_id
                                 (.. (client) -auth -user -id)}
                             #js{"$set" #js{:number 42}}
                             #js{:upsert true}))))
    (.then (fn []
             (timbre/info "[MongoDB Stitch] Connected")
             (-> (.collection (db) "personal")
                 (.find #js{:owner_id (.. (client) -auth -user -id)}
                        #js{:limit 100})
                 (.asArray))))
    (.then (fn [docs]
             (timbre/debug "Found docs:" (js->clj docs))))
    (.catch (fn [err]
              (timbre/error "MongoDB demo failed:" (js->clj err))))))

(defn login-anonymous []
  (let [out (chan)]
    (go
      (->> (new js/stitch.AnonymousCredential)
           (.loginWithCredential (.-auth (client)))
           (.then (fn [user]
                    (timbre/debug "Login successful:" user)
                    (put! out (js->clj user :keywordize-keys true))))
           (.catch (fn [err]
                     (timbre/error "MongoDB anonymous login failed:"
                                   (js->clj err))
                     (close! out)))))
    out))

#_
(login-anonymous)

(defn append-collection [collection-id data]
   (-> (.collection (db) collection-id)
       (.updateOne #js{:owner_id
                       (.. (client) -auth -user -id)}
                   #js{"$set" (clj->js data)}
                   #js{:upsert true})
       (.then (fn []
                (timbre/debug "[MongoDB] updated collection")))
       (.catch (fn [err]
                 (timbre/error "[MongoDB] update collection failed:"
                               (js->clj err))))))

#_
(append-collection "personal" {:number 22})

(defn fetch-collection [collection-id]
  (let [out (chan)]
    (go
      (-> (.collection (db) collection-id)
          (.find #js{:owner_id (.. (client) -auth -user -id)}
                 #js{:limit 100})
          (.asArray)
          (.then (fn [docs]
                   (->> (js->clj docs :keywordize-keys true)
                        (put! out))))
          (.catch (fn [err]
                    (timbre/error "[MongoDB] fetch collection failed:"
                                  (js->clj err))))))
    out))

#_
(echo (fetch-collection "personal"))

(defn watch-collection [collection-id]
  ; Not yet supported in stitch
  (let [out (chan)]
      (-> (.collection (db) collection-id)
          (.watch)
          (.on "change"
               (fn [doc]
                 (timbre/debug "[MongoDB] Collection change" collection-id)
                 (put! out (js->clj doc :keywordize-keys true)))))))

#_
(echo (watch-collection "personal"))

(defn watch-documents [collection-id doc-ids]
  {:pre [seq? doc-ids]}
  (let [out (chan)]
      (-> (.collection (db) collection-id)
          (.watch doc-ids)
          (.on "change"
               (fn [doc]
                 (timbre/debug "[MongoDB] Document change" collection-id)
                 (put! out (js->clj doc :keywordize-keys true)))))))

#_
(go-loop [docs (<! (fetch-collection "personal"))]
  (timbre/debug "Watched documents:" docs)
  (rf/dispatch [:profile :doc (first docs)])
  (let [id (:_id (first docs))
        doc (<! (watch-documents "personal" [id]))]
    (recur [doc])))

#_
(append-collection "personal" {:number 28})
