(ns api.exonum-client)

;; See https://github.com/exonum/exonum-client

(def Exonum (js/require "exonum-client"))

#_
(.hash Exonum #js[0 255 16 8])

(defn new-type [params]
  (.newType Exonum (clj->js params)))

(def User
  (new-type {:size 9
             :fields {:name {:type Exonum.String :size 8 :from 8 :to 8}
                      :age {:type Exonum.Int8 :size 1 :from 8 :to 9}}}))

(def user-data {:name "Tom" :age 3})

(defn serialize-user [data]
  (.serialize User (clj->js data)))

#_
(serialize-user user-data)
