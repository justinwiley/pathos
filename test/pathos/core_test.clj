(ns pathos.core-test
  (:require [clojure.test :refer :all]
            [pathos.core :refer :all]
            [clojure.data.json :as json]))

  (def expected-comment "This is John Valjean and Debbie Valjean from Paris, a city in France.
    We stayed in the University of North Carolina waiting room from January 14th 2004 until January 15th 2004.")

  (def expected-names ["John Valjean", "Debbie Valjean"])
  (def expected-locs ["Paris", "France", "North Carolina"])
  (def expected-orgs ["University of North Carolina"])
  (def expected-dates ["January 14th 2004", "January 15th 2004"])

  (def message {:id 111, :comment expected-comment})
  (def json-message (json/write-str message))
  (def expected-processed-message {:id (:id message) :names expected-names, :locations expected-locs :orgs expected-orgs :dates expected-dates})

  (deftest test-key-phrase-extractprs
    (testing "phrase extractors should return text snippets their names imply"
      (is (= expected-names, (get-all names expected-comment)))
      (is (= expected-locs, (get-all locations expected-comment)))
      (is (= expected-orgs, (get-all orgs expected-comment)))
      (is (= expected-dates, (get-all dates expected-comment)))
    )

    (testing "get-all should return distinct elements"
        (let [para "My name is John Valjean.  John Valjean!"]
          (is (= '("John Valjean") (get-all names para))))))

  (deftest test-process-json-message
   (testing "it should extract text data from json, return processed results"
     (is (= expected-processed-message (process-json-message json-message)))))


  (deftest test-api-tokens
    (testing "it should read api tokens from disk, define"
      (is (= false (empty? (:ironio api-tokens))))))
