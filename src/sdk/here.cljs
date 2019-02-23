(ns sdk.here
  "Reagent for here.com"
  (:require
   [reagent.core :as reagent
    :refer [atom]]))

(def here-app {:app_id "Wp7Nt66LvjPRdvnwl5jM"
               :app_code "1htkA4_N2ScaYTdpF-0mSg"}) ; ## TODO: move to env

(def Platform
  (memoize
   (fn []
     (new js/H.service.Platform (clj->js here-app)))))

(defn init-map [element]
  (let [platform (Platform)
        maptypes (.createDefaultLayers platform)
        params {:zoom 10
                :center {:lat 52.5 :lng 13.4}}]
    (new js/H.Map element maptypes.normal.map (clj->js params))))

(defn component-did-mount [{container :container}]
  (init-map (.getElementById js/document container)))

(defn reagent-render [attr]
  [:div.heremap (assoc attr
                       :style {:border "thin solid red"
                               :width "640px" :height "480px"})])

(defn view [{:keys [id] :as attr}]
   (reagent/create-class
    {:display-name id
     :reagent-render #(reagent-render attr)
     :component-did-mount #(component-did-mount {:container id})}))
