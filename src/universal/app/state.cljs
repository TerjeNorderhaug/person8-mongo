(ns app.state
  (:require
   [camel-snake-kebab.core
    :refer [->kebab-case-keyword]]
   [camel-snake-kebab.extras
    :refer [transform-keys]]))

(defn transform-kebab-keys [m]
  (transform-keys ->kebab-case-keyword m))

(def profile-fields
  [{:label "Social Security Card"
    :description "Required for jobs, benefits and more"
    :image "/media/social-security-card.jpg"
    :expandable true
    :edit false
    :share true}
   {:label "Drivers License"
    :description "Required for a job or bank account"
    :image "/media/drivers-license.jpg"
    :expandable true
    :share true}
   {:label "Health Insurance Card"
    :description "Required for healthcare"
    :image "/media/bic-card.png"
    :expandable true
    :share true}
   {:label "Passport"
    :description "Required for ID and flying"
    :image "/media/passport_card.jpg"
    :expandable true
    :share true}
   {:label "Birth Certificate"
    :description "Required for benefits and as identification"
    :image "/media/birth-certificate.jpg"
    :expandable true
    :edit false
    :share true}])



birth-certificate.jpg


(def state
  {:brand "Person8"
   :mode {:current "mobile"
          :options [{:id "split" :title "Split"}
                    {:id "mobile" :title "Mobile"}
                    {:id "dashboard" :title "Dashboard"}]}
   :stage nil
   :profile {:fields profile-fields}
   :mobile {:stage nil}
   :tab {:current "info"
         :options [{:id "about" :title "About"}
                   {:id "main" :title "Main"}
                   {:id "info" :title "Info"}]}})
