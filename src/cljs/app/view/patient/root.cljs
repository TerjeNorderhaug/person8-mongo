(ns app.view.patient.root
  (:require
   [goog.string :as gstring]
   [reagent.core :as reagent
     :refer [atom]]
   [re-frame.core :as rf]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as material
     :refer [get-mui-theme color]]
   [cljs-react-material-ui.reagent :as ui]
   [cljs-react-material-ui.icons :as ic]
   [app.view.patient.checkout :as checkout]))

(defn view [{:keys [stage] :as session}]
  (case (if stage @stage)
    ("checkout")
    [checkout/view session]
    [:div "Missing patient view for "
     (if stage @stage)]))
