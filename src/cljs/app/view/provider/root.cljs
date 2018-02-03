(ns app.view.provider.root
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
   [app.view.provider.pane
    :refer [pane]]))

(defn toolbar [{:keys [pane itinerary stage panes]
                :as session}]
 (let [active-class #(if (and pane (= @pane %))
                       "active")]
  [:nav.navbar.navbar-default
   [:div.container-fluid
    [:div.navbar-header
      [:a.navbar-brand "WellBE"]]
    (into
     [:ul.nav.nav-pills]
     (for [{:keys [id title] :as item}
           (if panes @panes)]
       ^{:key id}
       [:li {:role "presentation"
             :class (active-class id)}
        [:a {:on-click #(rf/dispatch [:pane id])}
         title]]))]]))

(defn notification [{:keys [stage] :as session}]
  (let [stage (if stage @stage)]
    [:div.alert.alert-info
     {:role "alert"
      :style {:display (if-not (= stage "payed")
                         "none")}}
     "The patient has paid for the visit with WELL tokens"]))

(defn view [session]
 (let [selected (rf/subscribe [:pane])]
   (fn [{:keys [stage providers] :as session}]
     (let [session (assoc session :pane selected)]
       [ui/mui-theme-provider
        {:mui-theme (get-mui-theme
                     {:palette
                      {:primary1-color "#9DCFE1"
                       :primary2-color (color :deep-blue700)
                       :primary3-color (color :deep-blue200)
                       :alternate-text-color (color :white) ;; used for appbar text
                       :primary-text-color (color :light-black)}})}
        [:div
         [toolbar session]
         [notification session]
         [pane session]]]))))
