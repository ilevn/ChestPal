# ChestPal

ChestPal is a sorter plugin for [PaperMC](https://papermc.io/) minecraft servers. It automatically sorts all items in
your chest.

## How it works

An intuitive fan-out system with dedicated sender and receiver chests allows you to specify multiple drop-off chests.
These then send all items off to their respective receiver chests.

## Building

ChestPal uses Gradle to manage all of its dependencies and for building the jar file.

#### Requirements

* Java 16 JDK or newer
* Kotlin 1.5 or newer
* Git

#### Compiling from source

```sh
git clone https://github.com/ilevn/ChestPal.git
cd ChestPal/
./gradlew build
# Use shadow to build a fat jar instead.
./gradlew shadowJar
```

## Contributing

Pull requests are always welcome. For major changes, please open an issue first to discuss what you would like to
change.

Please also make sure to update tests as appropriate.

## Licence

ChestPal is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

See the file "LICENCE" for information on the history of this software, terms & conditions for usage, and a DISCLAIMER
OF ALL WARRANTIES.

All trademarks referenced herein are property of their respective holders.
