# LightSensorRadio
This is an Android app that plays music when it detects light and stops when it gets dark. It also supports a voice command, "dalej," which allows users to skip the current song. For the hot word detection feature to work correctly, the user must generate the appropriate access key and place it in the "HotWordDetector" class. 
Additionally, the app can play advertisements (audio clips given by user) after a set number of songs, simulating a radio station experience.

To function properly, the following folder structure must be created on the phone:
Folder "sfx" that contains: 
- folder "songs"
- folder "pzg" with 2 audio files (named "zgrywus.mp3" and "silence.mp3") - here are audio files that are played just after light going off. File "silence.mp3" is played on loop -it helps when phone is connected to bluetooth speaker.
- folder "night songs" (it is possible to play different set of songs in different part of the day)
- folder "ads"
