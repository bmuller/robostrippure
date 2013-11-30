# robostrippure

A Clojure library designed to treat a website as though it provided a REST interface.

## Usage
```clojure
(ns examples.yellowpages
  (:use [robostrippure.resource]))

(defresource yellowpage
  [:phone :first [:p.phone] (content "-")])

(defresource yellowpagelist
  [:urls :all [:a.url] (attr :href)]
  [:businesses :all [:a.url] (attr :href) yellowpage])

(defn ypurl [city name]
  (let [clean #(-> % (clojure.string/replace " " "-") (clojure.string/replace #",*\.*" ""))]
    (str "http://www.yellowpages.com/" (clean city) "/" (clean name))))

(defn -main [& args]
  (println @(first (:businesses @(yellowpagelist (ypurl "Washington, D.C." "Quarry House Tavern"))))))
```
