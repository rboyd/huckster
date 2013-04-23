(ns huckster.alert
  (:require [clj-http.client :as client]
            [environ.core :refer [env]]))

(defn sms
  "Sends an SMS alert via Twilio."
  [body]
  (let [twilio-sid    (env :twilio-sid)
        twilio-token  (env :twilio-token)
        twilio-number (env :twilio-number)
        owner-number  (env :owner-number)
        api-base "https://api.twilio.com/2010-04-01/Accounts/"
        url (str api-base twilio-sid "/SMS/Messages.json")]
    (client/post url
                 {:digest-auth [twilio-sid
                               twilio-token]
                  :form-params {"Body" body
                                "From" twilio-number
                                "To"   owner-number}})))
