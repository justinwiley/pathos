(ns pathos.core-test
  (:require [clojure.test :refer :all]
            [pathos.core :refer :all]
            [cheshire.core :refer :all]
))

  (def expected-text "This is John Valjean and Debbie Valjean from Paris, a city in France.
    We stayed in the University of North Carolina waiting room from January 14th 2004 until January 15th 2004.")

  (def expected-names ["John Valjean", "Debbie Valjean"])
  (def expected-locs ["Paris", "France", "North Carolina"])
  (def expected-orgs ["University of North Carolina"])
  (def expected-dates ["January 14th 2004", "January 15th 2004"])

  (def comment-map {:text_response_id 111, :comment expected-text})
  (def json-comment (generate-string comment-map true))
  (def json-comment2 (generate-string comment-map true))
  (def ironio-message {"id" "5984542125330351602", "body" json-comment, "timeout" 60, "reserved_count" 3, "push_status" {}})
  (def expected-processed-message {:text_response_id (:text_response_id comment-map) :names expected-names, :locations expected-locs :orgs expected-orgs :dates expected-dates})

  ; --- open nlp name processing

  (deftest test-key-phrase-extractprs
    (testing "phrase extractors should return text snippets their names imply"
      (is (= expected-names, (get-all names expected-text)))
      (is (= expected-locs, (get-all locations expected-text)))
      (is (= expected-orgs, (get-all orgs expected-text)))
      (is (= expected-dates, (get-all dates expected-text)))
    )

    (testing "get-all should return distinct elements"
        (let [para "My name is John Valjean.  John Valjean!"]
          (is (= '("John Valjean") (get-all names para))))))

  ; --- iron.io functions

  (deftest test-api-tokens
    (testing "it should read api tokens from disk, define"
      (is (= false (empty? (:ironio api-tokens))))))


  ; --- json transformation



  ; --- comment processing



  (deftest test-extract-text-artifacts
    (testing "it should extract text data from comment map, return processed results"
      (is (= expected-processed-message (extract-text-artifacts comment-map)))))


  ; --- main loop





