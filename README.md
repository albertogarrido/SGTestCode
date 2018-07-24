# SGTestCode

## task

Create a simple Android-App which shows a list and two buttons below. The list is ordered horizontal.
Clicking the left button creates a new producer.
Clicking the right button creates a new consumer.

A producer adds every 3 seconds a new item to the list.
A consumer removes every 4 seconds an item from the list.
A created producer/consumer will never be removed and will do its job forever.

The user can create producer or consumer how often he wants by clicking the specified buttons.

For this project use Android Studio and Java.
Use suitable ui elements to display the Buttons and List.
This project should be done in about 3 hours.


A few notes:

\- Consumers and producers will last forever unless the process is killed (force stop) or the phone restarted. 

\- For some reason, after a while in my emulator (in a Linux machine), it crashes. The error is in "Parcel" and the message is "too many files", no stack trace though, so I am guessing is an issue with the emulator. Also, this crash never happened on physical devices (oneplus 3 and oneplus 1)

\- If you try the app on the phone be aware that it will get really warm. I added more than 40 of each, producer and consumer, and it was working fine, but the phone was using a lot of resources.

\- I am not saving the state of the list, also, the events sent to the activity on the background wont be captured and the UI activity will resume once opened again.

 
