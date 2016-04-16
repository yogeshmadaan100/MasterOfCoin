Master Of Coin
===================================

This repository is basically an assignment in which I have to create an Android application that has following features.

- Should use the jsonblob api described [here](https://jsonblob.com/api) and [data](http://jsonblob.com/570de811e4b01190df5dafec)
- A view where user can see all the transactions
- By default each expense is unverifed. User should be able to put each transaction in three possible states:
    - verified, if he thinks everything is fine
    - unverified, if a transaction was earlier marked as verified or fraud
    - fraud, if the user thinks this transaction is a fraudulent activity
- These states should be pushed to jsonblob too.
- The view should be auto-updating. If the json data at the source changes, so should the view, without any user interaction
- Do not use any local database on android side
- There are just two categories of data: recharge and taxi. Bonus if you can use this information to make a better UI

Pre-requisites
--------------
- Android SDK 21 or Higher
- Build Tools version 21.1.2
- Android Support AppCompat 22.2.0
- Android Support Annotations 22.2.0
- Android Support GridLayout 22.2.0
- Android Support CardView 22.2.0
- Android Support Design 22.2.0
- Android Support RecyclerView 22.2.0
* [ButterKnife](https://github.com/JakeWharton/butterknife)
* [Retrofit](https://github.com/square/retrofit)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)



Getting Started
---------------
This sample uses the Gradle build system.  To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Support
-------
Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.

License
-------
Copyright 2015 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.

