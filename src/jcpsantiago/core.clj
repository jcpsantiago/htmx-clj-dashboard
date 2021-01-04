(ns jcpsantiago.core
  (:require
    [jcpsantiago.ui :as ui]
    [compojure.core :refer [defroutes GET]]
    [org.httpkit.server :as server]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [ring.middleware.params :refer [wrap-params]]
    [hiccup.core :refer [html]])
  (:gen-class))

(defroutes all-routes
  (GET "/" [_] 
       (ui/base-page
        (ui/common-header)
        (ui/main-container {:id "main-container"}
         [:div {:class "grid grid-cols-1 md:grid-cols-5 gap-2"}
          [:div {:class "col-span-1"}
           (ui/ex-form)
           [:div {:class "flex flex-row-reverse mt-2"}
             (ui/btn-disabled {:id "uuid-form-submit-btn"} "Can't send it yet..")
             (ui/spinner "indicator" 8)]]])))

  (GET "/validate" req
       (let [v (get-in req [:params :ex-text])] 
        (println "Requesting for validation")
        (prn req)
        (if (= v "this is not a test")
          (html
            (ui/textinput {:id "ex-text" 
                           :name "ex-text" 
                           :type "text"
                           :hx-target "this"
                           :hx-get "/validate" 
                           :hx-swap "outerHTML"
                           :value v})
            (ui/btn {:id "uuid-form-submit-btn" 
                     :hx-swap-oob "true"}
                    "Post it like it's hot🔥"))
          (html
            (ui/textinput {:id "ex-text" 
                           :name "ex-text" 
                           :type "text"
                           :hx-target "this"
                           :hx-get "/validate" 
                           :hx-swap "outerHTML"
                           :value v})
            (ui/btn-disabled {:id "uuid-form-submit-btn" 
                              :hx-swap-oob "true"}
                             "almost there..."))))))
  ; (POST "/postit" [_]
  ;       (html5 (ui/ex-form "")
  ;              ())))

(defn -main
  [& _]
  (println "Starting server")
  (server/run-server 
   (-> all-routes
       (wrap-defaults site-defaults)
       wrap-params) 
   {:port 8080}))
