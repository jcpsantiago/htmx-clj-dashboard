(ns jcpsantiago.ui 
  (:require
    [ring.util.anti-forgery :refer [anti-forgery-field]] 
    [hiccup.page :refer [html5 include-css include-js]]))

(defn spinner
  "SVG spinner used as htmx-indicator
  originally from https://samherbert.net/svg-loaders/"
  [id size]
  [:img {:id id 
         :src "/img/tail-spin.svg" 
         :class (str "w-" size " " 
                     "h-" size " " 
                     "mx-2 htmx-indicator")}])

(defn btn
  "Active button"
  [extra-attrs & elements]
  (let [base-attrs {:class "items-center px-4 py-2 border border-transparent 
                           rounded-md shadow text-sm font-medium text-white 
                           bg-indigo-600 hover:bg-indigo-700 focus:outline-none 
                           focus:ring-2 focus:ring-offset-2"}]
    [:button (conj base-attrs extra-attrs) elements]))

(defn btn-disabled
  "Disabled, greyed-out button with default non-interactive cursor"
  [extra-attrs & elements]
  (let [base-attrs {:disabled true
                    ; FIXME use keywords too
                    :class "items-center px-4 py-2 border-2 
                             rounded-md text-sm font-medium text-gray-500
                             border-gray-500 focus:outline-none cursor-default
                             focus:ring-2 focus:ring-offset-2"}]
    [:button (conj base-attrs extra-attrs) elements]))

(def sm-container-css
  "CSS to produce a content container with a slight drop shadow"
  "px-4 py-3 rounded-md shadow bg-white")

(def main-container-css
  "CSS for the container holding the main content"
  "flex flex-col min-h-screen justify-between mx-20")

(def textinput-css
  "CSS for a textarea input component"
  "font-mono text-xs md:text-sm text-gray-50 
  w-full px-4 py-3 mt-2 rounded-md shadow bg-gray-700
  border-gray-50
  focus:outline-none focus:ring focus:border-indigo-300")
 

(defn compfn
  "Skeleton for a component"
  [html-tag-k css-classes extra-attrs & elements]
  [html-tag-k
   (conj {:class (->> (:class extra-attrs) 
                      (str css-classes " "))}
         extra-attrs)
   elements])

(def sm-container (partial compfn :div sm-container-css))
(def main-container (partial compfn :div main-container-css))
(def textinput (partial compfn :input textinput-css))

(defn ex-form 
 "Example of a form using HTMX AJAX requests and server-side validation"
 []
 [:form {:id "ex-form" :name "ex-form" :hx-post "/postit"
         :hx-indicator "#indicator"}
   (anti-forgery-field)
   [:label {:class "text-md text-gray-500 mb-2" 
            :for "ex-text"} 
    "Type something👇 (only `this is not a test` will work)"]
   (textinput {:id "ex-text" :name "ex-text" :hx-get "/validate" :hx-target "this"
               :type "text"
               :hx-swap "outerHTML" :hx-indicator "#indicator"})])

(defn common-header
  "Creates the header seen in every page"
  [& elements]
  [:header {:class "my-5 mx-20"}
   [:nav {:hx-boost "true"}
    [:div {:class "flex-1 flex items-center justify-left"}
     [:h1 {:class "font-mono 2xl text-indigo-500 mr-8"} 
      [:a {:href "/"} "Mighty dashboard 🦢"]]
     elements]]])

(defn base-page 
  "Skeleton used for every page"
  [header & elements]
  (html5
    {:class "" :lang "en"}
    [:head
     (include-css "https://unpkg.com/tailwindcss@2.0.2/dist/tailwind.min.css")
     (include-js "https://unpkg.com/htmx.org@1.0.2")
     [:title "Lagosta"]
     [:meta
      {:charset "utf-8",
       :name "viewport",
       :content "width=device-width, initial-scale=1.0"}]]
    [:body {:class "bg-gray-800"} ; FIXME should use actual dark mode from tailwind
      header
      [:main {:class "mb-auto"}
       [:section
         elements]]
      [:footer {:class "mt-5"}
        [:p {:class "text-gray-300 text-xs"} ""]]]))
