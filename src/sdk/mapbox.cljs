(ns sdk.mapbox
  "Reagent for https://www.mapbox.com"
  (:require
   [reagent.core :as reagent
    :refer [atom]]))

(def map-obj (atom {}))

(defn map-did-mount [{:keys [container] :as config}]
  {:pre [container]}
  (->> (js/initMap (clj->js config))
       (swap! map-obj assoc container)))

(defn map-reagent-render [attr]
  [:div attr])

(defn view [{:keys [id mapbox] :as attr}]
  (reagent/create-class
   {:component-did-mount #(map-did-mount
                           (assoc mapbox :container id))
    :display-name id
    :reagent-render #(map-reagent-render (dissoc attr :mapbox))}))

(defn example []
          [view {:id map-id
                 :mapbox {:style "mapbox://styles/mapbox/light-v9"
                          :center [-122.422 37.8265]
                          :zoom 18.0
                          :pitch 75
                          :bearing -17.6
                          :hash true}
                 :style {:border "thin solid grey"
                         :width "100%"
                         :height "40em”}}])