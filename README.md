# AndroidClient

## Team

**Devs**
- Amit Bharadwaj ([@amitbharadwaj](http://github.com/amitbharadwaj))
- Felipe Duarte ([@fcduarte](http://github.com/fcduarte))
- Jose Montes de Oca ([@josemontesdeoca](http://github.com/josemontesdeoca))

**Designers**
- Jairo Avalos ([@jairoavalos](http://github.com/jairoavalos))
- Tom Gurka ([@tomgurka](http://github.com/tomgurka))

## Setup Instructions
* **Clone the repository**

`git clone https://github.com/CodePath-MAF/AndroidClient.git`

* **Create the config file**

Create `res/values/app_config.xml` and add in the Parse Application & Client ID for the project following this sample:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="parseApplicationId">PARSE_APPLICATION_ID</string>
    <string name="parseClientId">PARSE_CLIENT_ID</string>
</resources>
```

**Note:** This config file will be gitginore.

* **Import Third-party Libraries**

Libraries are included within the `third-party-libs` folder.

## Third-party Libraries

Below we have the list of third-party libraries used on the app:

- [Parse SDK](https://parse.com/docs/android_guide): responsible for doing the data synchronization between the app and remote server
- [ParseLoginUI](https://github.com/ParsePlatform/ParseUI-Android): UI wrapper for log in/sign up services
- [HoloGraphLibrary](https://bitbucket.org/danielnadeau/holographlibrary/wiki/Home): allow beautiful graphs and charts to be easily incorporated into Android
- [Calligraphy](https://github.com/chrisjenx/Calligraphy): custom fonts for Android


## License

All code is distributed under MIT license. See LICENSE file for more information.
