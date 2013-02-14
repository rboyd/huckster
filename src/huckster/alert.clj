(ns huckster.alert
  (:require [clj-http.client :as client]
            [immutant.registry :as registry]))

(defn sms
  "Sends an SMS alert via Twilio."
  [body]
  (let [config        (registry/get :config)
        twilio-sid    (:twilio-sid config)
        twilio-token  (:twilio-token config)
        twilio-number (:twilio-number config)
        owner-number  (:owner-number config)
        api-base "https://api.twilio.com/2010-04-01/Accounts/"
        url (str api-base twilio-sid "/SMS/Messages.json")]
    (client/post url
                 {:digest-auth [twilio-sid
                               twilio-token]
                  :form-params {"Body" body
                                "From" twilio-number
                                "To"   owner-number}})))
