(ns app.view.patient.diagnostic
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
   [lib.chan :as chan]
   [app.view.patient.pane
     :refer [pane]]))

#_
(-> "i feel smoach pain but no couoghing today"
    (infermedica/generate-medical-analysis)
    (chan/echo))

(defn next-button [{:keys [diagnostic] :as session}
                   & {:keys [label action effect]
                      :or {label "Next"}}]

  [ui/raised-button
   {:label label
    :primary true
    :on-click #(do
                 (timbre/debug "Clicked next")
                 (when effect
                   (effect %))
                 (if action
                   (action %)
                   (let [active (or (if diagnostic (:active @diagnostic)) 0)]
                     (rf/dispatch
                      [:assign [:diagnostic :active] (inc active)]))))}])

(defn back-button [{:keys [diagnostic] :as session}
                   & {:keys [label]
                      :or {label "Back"}}]
  [ui/flat-button
   {:label label
    :secondary true
    :on-click #(let [active (or (if diagnostic (:active @diagnostic)) 0)]
                 (rf/dispatch
                  [:assign [:diagnostic :active] (dec active)]))}])

(defn alert-step [_]
  (let [desc (reagent/atom nil)]
    (fn [{:keys [diagnostic] :as session}]
      [:div
       [:p "Hi! Sensor data indicate you could "
        "be coming down with something... "
        "Please answer a few question to get that settled. "
        "How well do you feel today?"]
       [ui/text-field
        {:floating-label-text "How well are you today?"
         :multi-line true
         :value (or @desc "")
         :on-change #(reset! desc (-> % .-target .-value))
         :hint-text "Describe your condition and eventual symptoms"}]
       [next-button session
        :effect #(rf/dispatch [:diagnostic/analyze @desc])]])))

(defn analysis-step [{:keys [analysis] :as session}]
  [:div
   [:p]
   #_ (if analysis (pr-str @analysis))
   #_ [:p "Sounds like you have 100+ in fever and some pain..."]
   (if analysis
     [:p "Sounds like you experience:"])
   [:div
    (into
     [ui/list]
     (for [{:keys [id common_name name choice_id] :as item}
           (if analysis (:mentions @analysis))]
       ^{:key id}
       [ui/list-item
        {:primary-text (str (or common_name name)
                            (if-not (= choice_id "present")
                              " (not present)"))
         #_:secondary-text #_(or type "")}]))]
   [:div {:style {:width "100%"}}
    [back-button session :label "Change"]
    [:span {:style {:width "1em"}}]
    [next-button session
     :effect #(rf/dispatch [:diagnostic/diagnose
                            {:evidence (:mentions @analysis)}])
     :label "Continue"]]])


(defn diagnosis-step [{:keys [diagnosis] :as session}]
  [:div
   [:div
    (into
     [ui/list]
     (for [{:keys [id common_name name choice_id] :as item}
           (if diagnosis (:mentions @diagnosis))]
       ^{:key id}
       [ui/list-item
        {:primary-text (str (or common_name name))
         :secondary-text (pr-str item)}]))]
   #_[:p "You could be coming down with a flu"]
   [:p "Recommend checking in with a doctor asap."]
   [:p "Best matching physicians nearby:"]
   [ui/list
    [ui/list-item "Doctor Cough"]
    [ui/list-item "Doctor Sharp"]]
   [next-button session
    :label "Schedule"
    :action #(rf/dispatch [:stage "schedule"])]])

(defn view [{:keys [diagnostic] :as session}]
  [:div
   [ui/stepper {:orientation "vertical"
                :active-step (or (if diagnostic (:active @diagnostic)) 0)}
     [ui/step
      [ui/step-label "Health Alert"]
      [ui/step-content
       [alert-step session]]]
     [ui/step
      [ui/step-label "Analysis"]
      [ui/step-content
       [analysis-step session]]]
     [ui/step
      [ui/step-label "Remedy"]
      [ui/step-content
       [diagnosis-step session]]]]])

(defmethod pane "diagnostic" [session]
  [view session])
