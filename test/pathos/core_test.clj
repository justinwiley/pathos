(ns pathos.core-test
  (:require [clojure.test :refer :all]
            [pathos.core :refer :all]))

 (deftest test-get-all-names
   (testing "it should return all names"
     (let [para "Here's an interesting story for you.
           I went to see Dr. John Smith.  He wasn't home.  But Sally Smith was."]
       (is (= ["John Smith", "Sally Smith"], (get-all-names para)))))
   (testing "it should distinct names"
     (let [para "My name is John Valjean.  John Valjean!"]
       (is (= '("John Valjean"), (get-all-names para))))))

  (deftest test-process-json-message
   (testing "it should extract text data from json, return processed results"
     (let [raw-json "{\"id\": 111, \"comment\": \"Heres a sample comment about John and Sara\"}"]
       (is (= {:id 111 :names ["John" "Sara"]}, (process-json-message raw-json))))))
