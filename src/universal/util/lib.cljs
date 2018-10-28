(ns util.lib
  (:require-macros
   [cljs.core.async.macros
    :refer [go go-loop]])
  (:require
   [cljs.core.async
    :refer [<!]]
   [cljs.pprint
    :refer [pprint]]))

#_
(defn echo [ch]
  (go-loop []
    (when-let [item (<! ch)]
      (println item)
      (recur))))

(defn node? []
  (exists? goog/nodeGlobalRequire))

(defn pp-str [data]
  (with-out-str (pprint data)))

(defn pp-element [data]
  [:pre
   [:code {:style {:overflow-wrap :break-all}}
    (pp-str data)]])

(defn pp-table [{:as data}]
  (into
   [:table.table]
   (for [[k v] data]
     [:tr
      [:th (name k)]
      [:td
       [:code {:style {:white-space "pre-wrap"
                       :font-family "monospace"
                       :overflow-wrap "break-word"}}
            (if v @v)]]])))

(defn deep-merge [original mods]
  (merge-with (fn [x y]
                (cond
                  (map? x)
                  (deep-merge x y)

                  (vector? x)
                  (cond
                    (map? y)
                    (reduce
                     (fn [coll [k v]]
                       (if (map? v)
                         (update coll k #(deep-merge % v))
                         (assoc coll k v)))
                     x y)

                    (vector? y) ;; ## makes sense?
                    (concat x y)

                    :else y)

                  :else y))
              original mods))

#_
(merge {:a 1} {:a 2})

#_
(deep-merge
 {:a ["0" "one" "2"] :b "b" :c "c"}
 {:a {1 "1"} :b 2 :d "d"})

#_
(deep-merge
 {:one "one"}
 {:one "1"})

#_
(deep-merge
 {:a ["0" {:one "one"} "2"] :b "b"}
 {:a {1 {:one "1"}}})
