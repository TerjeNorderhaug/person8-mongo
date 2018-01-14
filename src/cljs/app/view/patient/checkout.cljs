(ns app.view.patient.checkout
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
  (let [pay-action #(rf/dispatch [:pay 1.2])]
    [:div {}
     [:h5
      [ui/font-icon {:class-name "material-icons"
                     :style {:font-size "30"}}
       ; use another icon here
       [ic/action-perm-contact-calendar {:color (color :grey600)
                                         :font-size "8em"}]
       [:span {:style {:margin-left "0.5em"}}
        "Checkout"]]]
     [:table.table
      #_
      [ui/table-header
       [ui/table-row]]
      (into [:table-body]
          (for [[id {:keys [label description cost]:as item}]
                (map-indexed vector (if itinerary (:items @itinerary)))]
               ^{:key id}
               [:tr
                #_[ui/table-row-column label]
                [:td description]
                [:td cost]]))
      [:tfoot
        [:tr
         [:td "Total"]
         [:td "13"]]]]
     [:div {:style {:margin-top "1em"
                    :margin-right "1em"
                    :float "right"}}

      [ui/raised-button {:label "Pay with WELL"
                         :primary true
                         :on-click pay-action}]]]))
