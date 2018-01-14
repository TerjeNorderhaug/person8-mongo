(ns api.well
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]
   [app.lib :as lib]))

;; API Reference
;; https://well-api.joinwell.com/docs/

(def api-root "https://well-api.joinwell.com/api/")

(defn endpoint [& path]
  (apply str api-root path))

(defn provider-home []
  (http/get (endpoint "provider/home")
            {; :with-credentials false
             :headers {"Accept" "application/json"}}))
#_
(lib/echo (provider-home))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn fetch-auth [{:keys [email password partnerid]
                   :as params}]
  (http/post (endpoint "auth")
             {:json-params params}))
#_
(->
 (fetch-auth {:email "provider@demo.com"
              :password "password"
              :partnerid 4})
 (lib/echo))

(def oauth-token ;; can be retrieved with fetch-auth
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL3dlbGwtYXBpLmpvaW53ZWxsLmNvbS9hcGkvYXV0aCIsImlhdCI6MTUxNTg4NzgzNSwiZXhwIjoxNTE1ODk1MDM1LCJuYmYiOjE1MTU4ODc4MzUsImp0aSI6Im9Mb0JRcmx4eTl6aWpWVGIiLCJzdWIiOjF9.rQjjBp1tWyxUnwLYZcFZo57EcBPqJ4h01prfvogtT8M")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PROVIDER API

(defn fetch-patients-list []
  (http/get (endpoint "provider/getAllPatient")
            {:with-credentials? false
             :oauth-token oauth-token
             :json-params nil}))

#_
(-> (fetch-patients-list)
    (lib/echo))

(defn fetch-patient [id]
  (http/get (endpoint "provider/getPatientById/" (str id))
            {:with-credentials? false
             :oauth-token oauth-token}))


#_
(-> (fetch-patient 5)
    (lib/echo))
