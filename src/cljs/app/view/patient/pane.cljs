(ns app.view.patient.pane)

(defmulti pane (fn [{:keys [stage] :as session}]
                  (if stage @stage)))

(defmethod pane :default [session]
  [:div "Wait..."
   #_
   [ui/linear-progress]])
