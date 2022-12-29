
----------------------------------------------------------------------------------------------------
True Caller Application Sample/Model in Native Android Mobile Application
----------------------------------------------------------------------------------------------------
Requirement:

 - Fetching my contacts and showing in recycler view with checkbox.
 - selecting contacts and saving in shared preference locally.
 - after, while receiving incoming call at same selected number,
 - we need to show the incoming call No & Name in simple dialog, on overlay the incoming call window.

eg: We have to do Similar Mini True Caller Android Native Mobile Application.
    I have given name is like Call Identifier Application.
----------------------------------------------------------------------------------------------------
Project developed steps are given below to understand more: (MVP)

1). we have to show runtime permissions dialogs and get necessary permissions from user first.
2). If runtime permissions are provided from user, we have to start fetching user contacts list from mobile.
3). then, show all the contacts in our app landing page with recyclerview with checkbox UI design.
3.1) selecting contacts and saving in shared preference locally for future call detect purpose.

4). then, we have to get to activate overlay permission for our call identifier app using below settings path:
Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
Uri.parse("package:" + getPackageName()));
startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);

4.1). If overlay permission already given, then, will start background service.

5). If permissions for overlay already provided, then, we have to start background service to detect,
while call receiving or not using below settings in service class:

IntentFilter intentFilter = new IntentFilter();
intentFilter.addAction(android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED);
incomingCallListener = new IncomingCallListener();

6). then, we have to declare incoming call listener and register here in same Background class.

IncomingCallListener incomingCallListener = null;
// Register the broadcast receiver with the intent filter object.
registerReceiver(incomingCallListener, intentFilter);

7). From IncomingCallListener class (broadcast), we have additionally PhoneStateListener class is there.
In this class used to get state and mobile no.

8). after, while receiving incoming call at same selected number from listview,
9). then, we need to show the incoming call No & Name in simple dialog, on Top of the incoming call phone speaking window.

----------------------------------------------------------------------------------------------------
Android Studio 4.1.2, Java
https://github.com/innovationindiaforall/Android-POC-Apps
Android, Studio, Java, Kotlin , Room DB, Recyclerview, CardView, BroadCast Listener, Service, Contacts Fetching
----------------------------------------------------------------------------------------------------
