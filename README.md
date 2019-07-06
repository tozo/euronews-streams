# Euronews streams

Retrieves all the euronews streams and exposes them on the `/streams` API.

To build the project run `./gradlew build`, the output will be under `build/libs`

Just run `java -jar euronews-0.0.1-SNAPSHOT.jar`

#### Example output of the API

```
{
  "urls": {
    "it": "https://euronews-it-b9-cdn.hexaglobe.net/cb6c87b358f02b12909649fe3d5fa878/5d1bb0b7/euronews/euronews-euronews-website-web-responsive-2/ewnsabritbkp_ita.smil/playlist.m3u8",
    "fa": "https://euronews-en-b11-cdn.hexaglobe.net/38d2a23f6ebbb4645714a8cffcd35d7a/5d1bb0bd/euronews/euronews-euronews-website-web-responsive-2/ewnsabrenbkp_eng.smil/playlist.m3u8",
    etc...
  }
}
```