(ns app.view.patient.visit
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
   [app.view.patient.pane
    :refer [pane]]))

(defn view [{:keys [providers itinerary] :as session}]
  (let [{:keys [name location avatar clinicname
                user-id partner-id provider-id]
          :as provider}
        (if @providers (first (:providers @providers)))]
    [:div
     [ui/card
      [ui/card-header
       {:title name
        :subtitle clinicname
        :avatar (str "https://well-api.joinwell.com/"
                     avatar)}]
      [ui/card-title {:title "Visit"}]
      [ui/card-text
       [ui/table {:selectable false}
        #_
        [ui/table-header
         [ui/table-row]]
        (into [ui/table-body]
              (for [[ix {:keys [label description cost active]
                         :as item}]
                    (map-indexed vector (if itinerary (:items @itinerary)))
                    :when active]
                ^{:key ix}
                [ui/table-row
                 [ui/table-row-column
                   {:style {:white-space "normal" :word-wrap "break-word"}}
                  description]]))]]]]))

(defmethod pane "visit" [session]
  [view session])
