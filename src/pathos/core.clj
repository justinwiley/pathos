(ns pathos.core
  (:gen-class)
  (:require [cheshire.core :refer :all]
            [iron-mq-clojure.client :as mq]
            [clj-yaml.core :as yaml])
  (:use [opennlp.nlp])
  (:use [opennlp.treebank])
)

; --- open nlp name processing

(def get-sentences (make-sentence-detector "models/en-sent.bin"))
(def tokenize (make-tokenizer "models/en-token.bin"))
(def names (make-name-finder "models/en-ner-name.bin"))
(def locations (make-name-finder "models/en-ner-location.bin"))
(def dates (make-name-finder "models/en-ner-date.bin"))
(def orgs (make-name-finder "models/en-ner-organization.bin"))

(defn get-all [finder text]
  (distinct (flatten (map finder (map tokenize (get-sentences text))))))

; --- iron.io functions

(def api-tokens (yaml/parse-string (slurp "resources/api-tokens.yml")))
(def ironio (:ironio api-tokens))
(def client (mq/create-client (:token ironio) (:project_id ironio)))
(def receive-queue "new-comments")
(def send-queue "processed-comments")

(defn logit [client msg]
  (let [logmsg {:time (str (new java.util.Date)) :msg msg}]
    (mq/post-message client "log" (generate-string logmsg true))))

(defn get-messages [client queue n]
  (map (fn [m] ())(mq/get-messages [client queue n])))

; --- json transformation

(defn from-json [msgs]
   (map (fn [c] (parse-string c true)) msgs))

(defn to-json [msgs]
   (map (fn [c] (generate-string c)) msgs))

; --- comment processing

(defn extract-text-artifacts [comment-map]
  {
    :text_response_id (:text_response_id comment-map)
    :names (get-all names (:comment comment-map))
    :locations (get-all locations (:comment comment-map))
    :dates (get-all dates (:comment comment-map))
    :orgs (get-all orgs (:comment comment-map))
  })

(defn process-messages [comment-maps]
  (map extract-text-artifacts comment-maps))

; --- main loop

(defn process [client receive-queue send-queue]
  (let [processed-messages (process-messages (from-json (get-messages [client receive-queue 1000])))]
    (mq/post-messages client send-queue (to-json processed-messages))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
