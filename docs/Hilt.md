# Implementation of Hilt in Retrofit

## Step 1: Update build.gradle - Adding dependencies

First, add the `hilt-android-gradle-plugin` plugin to your project's root `build.gradle` file:

```groovy
plugins {
  ...
  id 'com.google.dagger.hilt.android' version '2.44' apply false
}
```

Then, apply the Gradle plugin and add these dependencies in your `app/build.gradle` file:

```groovy
...
// Hilt
apply plugin: 'kotlin-kapt'
apply plugin: 'dagger.hilt.android.plugin'

android {
    ...
}

dependencies {
    ...
    // Hilt DI
    def hilt_version = "2.44"
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-compiler:$hilt_version"
}

// Allow references to generated code
kapt {
  correctErrorTypes true
}
```

## Step 2: Add Internet permission to your manifest file.

- Internet permission(uses-permission) is required for accessing API.
- Application class should be referred in the android:name attribute.

```xml

<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.example.demoapp">

    <uses-permission android:name="android.permission.INTERNET"/>

    <application
        android:name=".BaseApplication"
        android:usesCleartextTraffic="true"
...
```

Here along with the internet permission, we have also added `android:usesCleartextTraffic=”true”` because we are going to an endpoint which has http protocol instead of https protocol.

## Step 3: Define package structure

```bash
│   BaseApplication.kt
├───api
│       ApiHelper.kt
│       ApiHelperImpl.kt
│       ApiService.kt
├───di
│       AppModule.kt
├───models
│       Employee.kt
│       EmployeeResponse.kt
├───other
│       Constants.kt
│       Resource.kt
│       Status.kt
├───repository
│       MainRepository.kt
└───ui
        EmployeeAdapter.kt
        MainActivity.kt
        MainViewModel.kt
```

We will make our classes according to this hierarchy. Now let’s move ahead!

## Step 4: Create an Application class.

First we start with an `Application` class. Now we will create an application class named `BaseApplication` or `MainApplication`

```kotlin
@HiltAndroidApp
class MainApplication : Application()
```

## Step 5: Inject dependencies into Android classes

Once Hilt is set up in your Application class and an application-level component is available, Hilt can provide dependencies to other Android classes that have the `@AndroidEntryPoint` annotation:

```kotlin
@AndroidEntryPoint
class ExampleActivity : AppCompatActivity() { ... }
```

## Step 6: Define model classes

```kotlin
data class Employee(
    val employee_age: String?="",
    val employee_name: String?="",
    val employee_salary: String?="",
    val id: String?="",
    val profile_image: String?=""
)
```

According to the incoming JSON object we have created out model classes

```kotlin
data class EmployeeResponse(
    val data: List<Employee>?=null,
    val status: String? = "",
    val message: String? = ""
)
```

## Step 7: Create a api package classes and interfaces

Define the Employees list API's in the Service.

```kotlin
interface ApiService {
    @GET("employees")
    suspend fun getEmployees():Response<EmployeeResponse>
}
```

## Step 8: Create a repository

Inside repository package we will create a MainRepository class which will call the function of ApiHelper to get the response.

Create an Repository class which returns the list of employees network response.

```kotlin
class MainRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getEmployee() = apiService.getEmployees()
}
```

Here also we made getEmployee() function suspended as ApiHelper function will also return a suspended function.

## Step 9: Create AppModule object

Now it’s time to create a module object which will provide required instances as a dependency to other classes and maintain singleton behavior. We will now create an AppModule object as follows:

```kotlin
@Module
@InstallIn(ApplicationComponent::class)
object AppModule{

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor =HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

}
```

## Step 10: Setup utility files

Now we will create some utility files to retrieve data inside other package.

[`Resource.kt`](https://github.com/raystatic/HiltExample/blob/master/app/src/main/java/com/example/demoapp/other/Resource.kt)

[`Status.kt`](https://github.com/raystatic/HiltExample/blob/master/app/src/main/java/com/example/demoapp/other/Status.kt)

These classes will be used to wrap our data to be used in a generic way into our UI.

## Step 11: Implement UI
 
Now we have to implement our UI for that create a MainViewModel class inside ui package which should extend ViewModel class like this:

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val employeeData = MutableLiveData<List<Employee>?>()

    fun getEmployees() = employeeData

    init {
        loadEmployees()
    }
}
```