(ns app.json
  (:require
   [goog.net.XhrIo :as xhr]))

(defn fetch-json [uri cb]
  (xhr/send uri (fn [e]
                  (let [target (.-target e)]
                    (if (.isSuccess target)
                      (-> target .getResponseJson js->clj cb)
                      (cb nil {:status (-> target .getStatus)
                               :explanation (-> target .getStatusText)}))))))
