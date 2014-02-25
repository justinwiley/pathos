(defproject pathos "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
    [org.apache.opennlp/opennlp-tools "1.5.3"]
    [org.clojure/clojure "1.5.1"]
    [clojure-opennlp "0.3.2"]
    [org.clojure/tools.trace "0.7.6"]
    [iron_mq_clojure "1.0.3"]
    [org.clojure/data.json "0.2.4"]
    [inflections "0.9.5"]
    [midje "1.5.1"]
  ]
  :main ^:skip-aot pathos.core
  :target-path "target/%s"
  :plugins [[lein-midje "3.0.0"]]
  :profiles {
    :uberjar {:aot :all}
    :dev {:dependencies [[midje "1.5.1"]]}
  }
)
