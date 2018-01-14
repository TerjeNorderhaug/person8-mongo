(ns app.view.provider.checkout
  (:require
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [goog.string :as gstring]))

(defn view [{:keys [itinerary] :as session}]
  [ui/paper {:style {:padding-left "1em"
                     :padding-right "1em"}}
     [:h5
      [ui/font-icon {:class-name "material-icons"
                     :style {:font-size "30"}}
       ; use another icon here
       [ic/action-perm-contact-calendar {:color (color :grey600)
                                         :font-size "8em"}]
       [:span {:style {:margin-left "0.5em"}}
        "Checkout"]]]
     [:table
      (into [:tbody]
            (for [[id item]
                  (map-indexed vector (if itinerary (:items @itinerary)))]
              ^{:key id}
              [:tr [:td (pr-str item)]]))]])
