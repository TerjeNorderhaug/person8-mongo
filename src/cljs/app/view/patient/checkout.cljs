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
   [goog.string :as gstring]
   [app.view.patient.pane
     :refer [pane]]))

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
     [ui/table
      #_
      [ui/table-header
       [ui/table-row]]
      (into [ui/table-body]
          (for [[id {:keys [label description cost]:as item}]
                (map-indexed vector (if itinerary (:items @itinerary)))]
               ^{:key id}
               [ui/table-row
                #_[ui/table-row-column label]
                [ui/table-row-column
                 {:style {:white-space "normal" :word-wrap "break-word"}}
                 description]
                [ui/table-row-column cost]]))
      [ui/table-footer
       [ui/table-row
        [ui/table-row-column "Total"]
        [ui/table-row-column "13"]]]]
     [:div {:style {:margin-top "1em"
                    :margin-right "1em"
                    :float "right"}}

      [ui/raised-button {:label "Pay Tokens"
                         :primary true
                         :on-click pay-action}]]]))

(defmethod pane "checkout" [session]
  [view session])
