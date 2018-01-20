(ns app.view.provider.pane)

(defmulti pane (fn [{:keys [pane] :as session}]
                 (if pane @pane)))

(defmethod pane :default [session]
  [:div "Waiting..."])
