# SGTestCode



A few notes for the reviewer/s:

\- Time of development 4:30h including 3 small breaks.

\- Consumers and producers will last forever unless the process is killed (force stop) or the phone restarted. 

\- For some reason, after a while in my emulator (in a Linux machine), it crashes. The error is in "Parcel" and the message is "too many files", no stack trace though, so I am guessing is an issue with the emulator. Also, this crash never happened on physical devices (oneplus 3 and oneplus 1)

\- If you try the app on the phone be aware that it will get really warm. I added more than 40 of each, producer and consumer, and it was working fine, but the phone was using a lot of resources.

\- I am not saving the state of the list, also, the events sent to the activity on the background wont be captured and the UI activity will resume once opened again.

 