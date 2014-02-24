(ns pathos.core
  (:gen-class))

(use 'opennlp.nlp)
(use 'opennlp.treebank)

(def get-sentences (make-sentence-detector "models/en-sent.bin"))
(def tokenize (make-tokenizer "models/en-token.bin"))
(def name-find (make-name-finder "models/namefind/en-ner-person.bin"))

(defn get-all-names [para]
  (distinct (flatten (map name-find (map tokenize (get-sentences para))))))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
