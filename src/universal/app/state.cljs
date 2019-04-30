(ns app.state
  (:require
   [camel-snake-kebab.core
    :refer [->kebab-case-keyword]]
   [camel-snake-kebab.extras
    :refer [transform-keys]]))


(defn transform-kebab-keys [m]
  (transform-keys ->kebab-case-keyword m))

(def profile-fields
  [{:id "social-security-card"
    :label "Social Security Card"
    :description "Required for jobs, benefits and more"
    :image "/media/social-security-card.jpg"
    :expandable true
    :edit false
    :share true}
   {:id "drivers-license"
    :label "Drivers License"
    :description "Required for a job or bank account"
    :image "/media/drivers-license.jpg"
    :expandable true
    :share true}
   {:id "health-insurance-card"
    :label "Health Insurance Card"
    :description "Required for healthcare"
    :image "/media/bic-card.png"
    :expandable true
    :share true}
   {:id "passport"
    :label "Passport"
    :description "Required for ID and flying"
    :image "/media/Passport_card.jpg"
    :expandable true
    :share true}
   {:id "birth-certificate"
    :label "Birth Certificate"
    :description "Required for benefits and as identification"
    :image "/media/birth-certificate.jpg"
    :expandable true
    :edit false
    :share true}])

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
