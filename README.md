# Lost Pet on Android
Simple Android/Kotlin App for posting lost pets. Here we define the details/framework specific layer in a [Clean Architecture](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html) approach, the business layer is defined [here](https://github.com/drodobyte/lostpet). This branch features a MVI approach.
### Packages 
* **Presentation**, It contains Android specifics implementing views and user interaction, and also the presenters, which are adapters to the use-cases from business model layer and the android specific framework.
* **Service**, doubles or mocked service classes implementing the service interface defined in the business model layer.
* **util & app**, utility and helper classes
