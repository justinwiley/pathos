(ns pathos.core
  (:gen-class))

(use 'inflections.core)
(use 'opennlp.nlp)
(use 'opennlp.treebank)
(require '[iron-mq-clojure.client :as mq])
(require '[clojure.data.json :as json])
(require '[clj-yaml.core :as yaml])


; --- open nlp name processing

(def get-sentences (make-sentence-detector "models/en-sent.bin"))
(def tokenize (make-tokenizer "models/en-token.bin"))
(def names (make-name-finder "models/en-ner-name.bin"))
(def locations (make-name-finder "models/en-ner-location.bin"))
(def dates (make-name-finder "models/en-ner-date.bin"))
(def orgs (make-name-finder "models/en-ner-organization.bin"))

(defn get-all [finder text]
  (distinct (flatten (map finder (map tokenize (get-sentences text))))))

; --- iron.io queue functions

(def api-tokens (yaml/parse-string (slurp "resources/api-tokens.yml")))

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

(defn process-json-message [json-message]
  (let [message (json/read-str json-message :key-fn keyword)]
    {
      :id (:id message)
      :names (get-all names (:comment message))
      :locations (get-all locations (:comment message))
      :dates (get-all dates (:comment message))
      :orgs (get-all orgs (:comment message))
    }))


(defn process-messages [messages] (messages) )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
