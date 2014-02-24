(ns pathos.core-test
  (:require [clojure.test :refer :all]
            [pathos.core :refer :all]))

 (deftest get-all-names-test
   (testing "it should return all names"
     (let [para "Here's an interesting story for you.
           I went to see Dr. John Smith.  He wasn't home.  But Sally Smith was."]
       (is (= ["John Smith", "Sally Smith"], (get-all-names para)))))
   (testing "it should distinct names"
     (let [para "My name is John Valjean.  John Valjean!"]
       (is (= '("John Valjean"), (get-all-names para))))))

