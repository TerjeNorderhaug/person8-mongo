(ns app.view.provider.pane)

(defmulti pane (fn [{:keys [pane] :as session}]
                 (if pane @pane)))

(defmethod pane :default [session]
  [:div {:style {:padding-top "3em"
                 :margin-left "25%"
                 :width "50%"}}
   [:div.progress
    [:div.progress-bar.progress-bar-animated
     {:role "progress-bar"
      :aria-valuenow "100"
      :aria-valuemin "0"
      :aria-valuemax "100"
      :style {:width "100%"}}]]])
