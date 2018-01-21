(ns app.view.provider.standby
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
   [app.view.provider.pane
     :refer [pane]]))

(defn view [{:keys [waiting stage] :as session}]
 [:div.well
  [:table.table.waiting
   [:thead [:caption {:style {:width "100%"}}
            "Waiting Room"]]
   (into [:tbody]
         (for [[id {:keys [fname lname avatar
                           state-of-residency]
                    :as patient}]
               (map-indexed vector
                            (concat
                             (if waiting @waiting)
                             (if (and stage
                                      (= @stage "schedule")
                                      []))))]
            ^{:key id}
            [:tr
             [:td
              [:img.avatar {:src (str "https://well-api.joinwell.com/"
                                  avatar)}]]
             [:td
               {:style {:text-align "left"}}
               (str fname lname)]]))]])

(defmethod pane "dashboard" [session]
  [view session])
