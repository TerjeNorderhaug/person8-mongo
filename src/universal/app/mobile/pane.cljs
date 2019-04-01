(ns app.mobile.pane
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
   [sdk.twilio :as twilio]
   [util.lib :as lib
     :refer [pp-element pp-str]]))

(defmulti pane (fn [{:keys [stage] :as session}]
                  (if stage [@stage])))

(defn navigator [{:keys [tab] :as session}]
  [ui/card
   (into [:ul.list-group {:style {:margin-top "1em"}}]
         (for [{:keys [id title] :as item}
               (if tab (:options @tab))]
           [:a.list-group-item
            {:class (if (and tab (= id (:current @tab)))
                      "active")
             :href (str "#tab/" id)}
            title]))])

(defn share-content [content]
  (twilio/send-sms
   {:to ""
    :from ""
    :body content}))

(defn share-button [content]
  (case :raised
    :floating
    [ui/floating-action-button
     {:secondary true}
     (ic/notification-sms)]
    :raised
    [ui/raised-button
     {:label "Share"
      :label-position "before"
      :icon (ic/notification-sms)
      :on-click #(share-content content)}]))

(defn edit-button []
  [ui/raised-button
   {:label ""
    :label-position "before"
    :icon (ic/image-edit)}])

(defn identity-card [{:keys [mobile] :as session}]
  (let [share true
        profile (get-in @mobile [:user :profile])]
    [ui/card {:style {:margin-top "0.2em"}}
     [ui/card-header
      {:title "Name"
       :subtitle "What you prefer to be called"
       :avatar (ic/social-person)}]
     [ui/card-text
      (:firstName profile) " "
      (:lastName profile)]
     [ui/card-actions #_{:style {:position "relative" :right 0}}
      (if share
        [:div {:style {:display "flex"
                       :justify-content "flex-end"
                       :width "100%"
                       :padding 0}}
         [share-button {:text (str (:firstName profile) " " (:lastName profile))}]])]]))

(defn profile-card [{:keys [label description edit share image expandable text]
                     :as item}]
  [ui/card
   [ui/card-header
    {:title label
     :subtitle description
     :act-as-expander expandable
     :show-expandable-button expandable
     :avatar (ic/action-assignment)}]
   (if image
     [ui/card-media {:expandable expandable}
      [:img {:src image}]])
   [ui/card-actions #_{:style {:position "relative" :right 0}}
    (if share
      [:div {:style {:display "flex"
                     :justify-content "flex-end"
                     :width "100%"
                     :padding 0}}
       [share-button (if image {:media image}{:text text})]])
    (if edit
      [:span {:style {:width "1em"}} " "
        [edit-button]])]])




(def develop false)

(defmethod pane :default [{:keys [mobile profile] :as session}]
  [:div
   (if develop [lib/pp-table session][:div])
   (if (= (get-in @mobile [:user :status]) "SUCCESS")
     (into
      [:div [identity-card session]]
      (for [item (:fields @profile)]
        [profile-card item]))
    [:div])])
