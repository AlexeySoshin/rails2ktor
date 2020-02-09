# Rails2Ktor
Easily convert Rails routes into [Ktor](https://ktor.io/) routes.  
Useful for creating a proxy service, and reimplementing particular routes in Kotlin at your own pace.

## Requirements
* JDK8+
* Ruby 2.5+

## Building
```
./gradlew build
```

## Usage
```
java -jar ./build/libs/rails2ktor.jar <path-to-rails-project>
```

Given `routes.rb`:
```ruby
Rails.application.routes.draw do
  get 'welcome/index'
  get 'health/show'

  resources :articles

  root 'welcome#index'
end
```

The output is:
```kotlin
routing {
    get("/welcome/index") { call.proxy(target) }
    get("/health/show") { call.proxy(target) }
    get("/articles") { call.proxy(target) }
    post("/articles") { call.proxy(target) }
    get("/articles/new") { call.proxy(target) }
    get("/articles/{id}/edit") { call.proxy(target) }
    get("/articles/{id}") { call.proxy(target) }
    patch("/articles/{id}") { call.proxy(target) }
    put("/articles/{id}") { call.proxy(target) }
    delete("/articles/{id}") { call.proxy(target) }
    get("/") { call.proxy(target) }
}
```

Where `call.proxy(target)` proxies the call to `target` host.