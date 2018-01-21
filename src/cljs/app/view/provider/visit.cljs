(ns app.view.provider.visit
  (:require
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [goog.string :as gstring]
   [app.view.provider.pane
    :refer [pane]]))

(defn view [{:keys [itinerary] :as session}]
  [:div
     [:h5
      [ui/font-icon {:class-name "material-icons"
                     :style {:font-size "30"}}
       ; use another icon here
       [ic/action-perm-contact-calendar {:color (color :grey600)
                                         :font-size "8em"}]
       [:span {:style {:margin-left "0.5em"}}
        "Visit"]]]
   [:table.table
     (into [:tbody]
           (for [[ix {:keys [label description cost active] :as item}]
                 (map-indexed vector (if itinerary (:items @itinerary)))]
             ^{:key ix}
             [:tr
              [:td [:input {:type "checkbox"
                            :checked (boolean active)
                            :on-click #(rf/dispatch
                                        [:update [:itinerary :items ix :active] not])}]]
              [:td description]]))]
   [:button.btn.btn-default
    {:type "button" :aria-label "checkout"
     :on-click #(do
                  (rf/dispatch [:stage "checkout"]))}

    "Checkout"]])

(defmethod pane "visit" [session]
  [view session])
