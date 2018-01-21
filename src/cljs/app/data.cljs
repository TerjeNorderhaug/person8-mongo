(ns app.data
  (:require
   [camel-snake-kebab.core
    :refer [->kebab-case-keyword]]
   [camel-snake-kebab.extras
    :refer [transform-keys]]))

(defn transform-kebab-keys [m]
  (transform-keys ->kebab-case-keyword m))

(def state
  {:mode "split"
   :stage "diagnostic"
   :panes [{:id "dashboard" :title "Dashboard"}
           {:id "visit" :title "Visit"}]
   :pane "dashboard"
   :patient 5
   :diagnostic {}
   :analysis {}
   :diagnosis nil
   :itinerary {:items [{:label "1"
                        :description "Examination and consultation"
                        :cost "5"}
                       {:label "2"
                        :description "Prescription (Prednisone 5mg)"
                        :cost "8"}]}
   :providers {:favourite-count 0
               :providers-count 1
               :providers
               (map transform-kebab-keys
                [{"provider_id" 2
                  "npi" "1467586115"
                  "spi" "6008659032004"
                  "dea" "123456789"
                  "partner_id" 4
                  "user_id" 4
                  "name" "Jen Mississippi"
                  "specialty" "Neurologist"
                  "videoprofile" nil
                  "videoProfileUrl" nil
                  "textprofile" "Dr. Jen has been practicing medicine since 1979 and is verified by the American Osteopathic Board of Internal Medicine with a certified sub specialty in Endocrinology. Dr. Jen earned Bachelor's Degree in Biology and Chemistry from the University of Hartford and earned her Master's Degree in Organic Chemistry from Northeastern University before attending the Des Moines University College of Osteopathic Medicine, where she achieved the distinction."
                  "language" "English"
                  "education" "University of Hartford, Northeastern University, Des Moines University College of Osteopathic Medicine"
                  "experience" "33" "board" "Osteopathic Board of Internal Medicine"
                  "residency" "University of Medicine and Dentistry of New Jersey(UMDNJ)"
                  "location" "Philadelphia, Pennsylvania"
                  "clinicname" "testClinic"
                  "cost" "$25"
                  "livestatus" 1
                  "signature" "data:image/jpeg;base64,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"
                  "created_at" nil
                  "updated_at" nil
                  "avatar" "avatar/18b89da361d685dea07a85bb4c38aac7.jpeg"
                  "is_favourite" 0
                  "chatcounter" 0}])}
   :waiting (map transform-kebab-keys
                 [{"id" 12 "patient_id" 5 "provider_id" 1 "room" nil "status" 0
                   "started_at" nil "ended_at" nil "created_at" "2018-01-13 08:54:43"
                   "updated_at" nil "code" nil "parent_waitingroom_id" 0 "rating" 0
                   "feedback" nil "mint_flag" 0 "mint_file" nil "cancel_visit" 0
                   "fname" "Demo" "lname" "Patient" "providerUserId" 1
                   "avatar" "avatar/0bcd8485d8642fa2f53ea5cd7f572744.jpeg"
                   "stateOfResidency" "CO"}])})
