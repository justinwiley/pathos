(ns pathos.core
  (:gen-class))

(use 'opennlp.nlp)
(use 'opennlp.treebank)
;; (use 'inflections.core)
(require '[iron-mq-clojure.client :as mq])
(require '[clojure.data.json :as json])


;; (for [fname ["person" "location" "date" "org"]]
;;   (let [plural-name (plural)])
;;   (def (plural fname) (make-name-finder (str "models/en-ner-" fname ".bin")))
;; )


; --- open nlp name processing

(def get-sentences (make-sentence-detector "models/en-sent.bin"))
(def tokenize (make-tokenizer "models/en-token.bin"))
(def people (make-name-finder "models/en-ner-person.bin"))
(def locations (make-name-finder "models/en-ner-location.bin"))
(def dates (make-name-finder "models/en-ner-date.bin"))
(def orgs (make-name-finder "models/en-ner-organization.bin"))

(defn get-all [finder text]
  (distinct (flatten (map finder (map tokenize (get-sentences text))))))

; --- iron.io queue functions

(def client (mq/create-client "" ""))

(defn get-message [queue client]
  (let [msg (mq/get-message client queue)]
    (if msg
      (do
        (println (get msg "body"))
        (mq/delete-message client queue msg))
      (println "queue is empty"))))

; (mq/post-message client "myqueue" "hello from clojure")


; --- message extraction


(defn process-json-message [raw-json]
  (let [message (json/read-str raw-json  :key-fn keyword)]
    (merge message
           {:names (get-all people (:comment message))
            :locations (get-all locations (:comment message))
            :dates (get-all dates (:comment message))
            :orgs (get-all orgs (:comment message))
            })
  ))

(def raw-json "{\"id\": 111, \"comment\": \"Heres a sample comment about John and Sarah, two people from Arizona.  We stayed in the University of North Carolina waiting room from Monday January 15th 2004.\"}")

(process-json-message raw-json)


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
