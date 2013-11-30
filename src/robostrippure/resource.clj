(ns robostrippure.resource
  (:require [net.cgrand.enlive-html :as enlive]
            [clj-http.client :as http-client]))

(defn- fetch [location]
  (enlive/html-resource (java.io.StringReader. (:body (http-client/get location)))))

(defn- extract* [elem modifiers]
  (if (empty? modifiers) elem (reduce #(%2 %1) elem modifiers)))

(defn- extract [html size selectors & modifiers]
  (let [matches (enlive/select html selectors)]
    (case size
      :all (map #(extract* % modifiers) matches)
      :last (extract* (last matches) modifiers)
      (extract* (first matches) modifiers))))

(defn resource [location & props]
  (let [html (fetch location)]
    (reduce
     #(assoc %1 (first %2) (apply extract html (rest %2)))
     {:parsed html}
     props)))

(defn content
  ([] (content " "))
  ([sep] (fn [e] (clojure.string/join sep (:content e)))))

(defn attr [n]
  (fn [e] (get (:attrs e) n)))

(defmacro defresource [name & props]
  `(defn ~name [location#] (delay (resource location# ~@props))))


