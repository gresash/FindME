# FindME- IMT3662 Assignment 1
###Author:
####Gresa Shala
###Student ID:
####998900


                                                                                              

                                                       
 
 
###Contents
####1.Application Description	3
####2. State and information storing in local database (SQLite)	3
####3. Discussion of the difference between native Apps and web Apps	4
####4. Explanation of how the app could be extended	4




 
##1.Application Description
 
FindME
FindME is an application that provides the user with current location information. This is achieved by information provided by the device’s GPS sensor and web resources. 
In order to access this information, the app must have a permission to use the device’s location service, and internet. For android API<23 it is sufficient to add these permission requests in the manifest file, and Normal permissions are granted automatically when the app is installed, whereas Dangerous permissions need to be granted by the user when the app is being installed. However, for API Level 23 and above, runtime requests for Dangerous permissions are needed. 
The activity_main gives user access to current location data, and location history, if available. The MapActivity shows the location on the map, and gathers longitude, latitude and altitude data from the GPS provider (if that fails, the most accurate location provider with low battery usage is chosen, if available, whereas current altitude is provided by web resources). Therefore, MapActivity includes components that depend on whether the Location permissions have been granted to the app by the user (Map functionality and GPS provider access). Since location temperature and altitude both need to be shown to the user, and both are provided to the app by web resources (except if GPS provider is enabled, then it provides the location altitude) longitude and latitude data enable the app to obtain from web resources, Info activity deals with them. Longitude and latitude data (if available) is provided to the activity by MapActivity . If the device is not connected to the internet, web resources cannot be accessed, which is why the Info activity can’t be started without connectivity.  


##2. State and information storing in local database (SQLite)
FindME stores the user’s current location and temperature in Gresa_Assignment1.LocationLogs table. Each record is identified by an ID and a timestamp corresponding to the time of data insertion. Connection, data reading and insertion to the database is managed by DbHelper, a subclass of SQLiteOpenHelper. When location and temperature data is obtained by the app, it is inserted into the database (insertion is done in Info activity). The user’s location history is shown in LocationHistory activity, which is inaccesible by the user, unless he has used the app before to see details about their current location. If the app has just been installed, hasn’t been used before on the device,or the needed location data couldn’t be provided by the device (meaning that the app has never obtained data of the user’s current location and temperature), LocationLogs is empty, therefore there’s no location history to be shown.    	
. 

##3. Discussion of the difference between native Apps and web Apps
	
Native Apps are downloadable from an App store, and are launched when the user taps on their icon from within the device’s applications. They have access to device-specific features, such as GPS, camera, gestures and notifications. This enables such apps to gather a big amount of data related to the device and the user, therefore broadening the app’s functionality and usage abilities. Another argument in favor of native Apps is that they aren’t limited to the device’s connectivity. Even if any of the app’s components depends on the device’s connectivity, it is possible to store a certain state of that component on the device, so it can still be accessed by the user, even though it cannot be updated. Since native Apps have less dependence on the device’s connectivity compared to web Apps, they are also faster to be accessed. 
Web Apps on the other hand, are platform-independent Apps. They are written in HTML5,Javascript and other web languages. They’re opened in a browser, and the user has the possibility of “installing” them by creating a bookmark on their browser. Since they exist inside browsers, they are more flexible. As long as the device has connectivity and a browser, no matter what platform it runs on, the App is accessible by it. In addition, the users don’t need to go through the process of installing the app, nor do they need to go through the hassle of updating it whenever there’s a newer version on the App store. All the developer needs to do is update the web page. Also, with web Apps you are given more freedom in relation to the content you want to provide for the user, whereas native Apps must obey content restriction rules and approval fees set by App stores.
In conclusion, if you should to develop a native App or a web App depends on whether the purpose is for the App to get to know more about the user, or getting more users to know the App. Web Apps are more suitable for the latter, since such Apps are easily accessible through a browser. On the other hand, the simplicity of making a web App results in poor interaction with the user, in terms of getting the user engaged in the App. A native App can use notifications and alerts to communicate with the user, therefore getting the latter to spend more time on it. 


##4. Explanation of how the app could be extended
	
In addition to showing the user’s current location data and temperature, FindME could have step-counting functionality, which would require data to be taken from the step counting and step detector sensors. Another feature that could be added is giving the user ability to store photos of the current location, which requires access to the camera.
Being a beginner in mobile App development, this assignment was quite a challenge for me. I expected the hardest part to be getting data from web resources, since they were returned in JSON format, but navigating a JSON object was not as hard as I’d expected, using JsonReader class. A drawback of using web resources is the dependency on device connectivity, since if the information cannot be obtained, the app shouldn’t crash. FindME gets current location temperature from OpenWeatherAPI, which sometimes cannot find the city based on the longitude and latitude data, so I decided to change the query parameters to the location. If the GPS is not enabled, the map can still find the location of the user, but that location isn’t automatically zoomed in, since the map object’s camera positioning method needs longitude and latitude data, which cannot be provided without a location provider enabled. The hard part was the runtime permission request[1] for Location Services, and getting the App to adapt to conditions of missing connectivity or location providers. However, it made me really dig into what is going on in the App, which helped me to better understand the process of how Apps work.
