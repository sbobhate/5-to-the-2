Sound Module
===========

**@ author Mike Reavey**

README:

Sound Module includes the following classes:

SoundTrack:
    -Includes methods to accomodate life-cycle of android app.
     Sound generation requires several methods to coordinate multiple sound events.
    -Instantiates main game Tracks and SoundEffects.  Tracks use a CyclicBarrier to 
     achieve synchronization.

WelcomeTrack:
    -Similar to SoundTrack, but no thread synchronization.
    -Instantiates non-gameplay Tracks and SoundEffects.  

Track:
    - Handles the logic of persistent, looped gameplay sound.  Uses AudioTrack 
      API methods. 
    - The logic in the Track class iterates through the Samlpe array, writing to 
      an audio buffer which is then played using the AudioTrack API.

SoundEffect:
    - Similar to Track, but no looping.  

Sample:
    - Manages the data for each sound sample.  
    - Each sample is an array of data that stores pitch content and duration.  

SoundPalette:
    - Manages the pitch content used in the module.  


Mike Reavey
reaveym@gmail.com
