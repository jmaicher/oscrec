OSC Cassette Recorder
=====================
A command-line tool which provides record and playback functionality for OSC messages which are sent as UDP datagram packets.

![](http://i.imgur.com/fr9TTli.gif)

The use case for this tool was to ease the development and test of applications written for multitouch surfaces that use the [TUIO messaging protocol](http://www.tuio.org/) (based on the [OSC protocol](http://opensoundcontrol.org)) for the transmission of touch information.

**Similar tools:** 

* [Throng OSCDeck](https://code.google.com/p/throng/), written in Java, provides a GUI
* [OSCRecordTools](https://github.com/chaosct/OSCRecordTools), written in Python

## Usage

	$ java -jar oscrec.jar --help

	usage: java -jar [..].jar [record|play] OPTIONS

	The OSC Cassette Recorder lets you record and playback OSC packets.
	OPTIONS:
    	--help      Print help information
    	--version   Print version information

	For more help consult the specific command help with `[..] record --help`
	or `[..] play --help`.


## Developers

This project uses maven as build manager. For simple builds, the provided build script can be used:

	$ ./build

## TODO

Although this tool has been used in production, it is still in a very early stage of development. There are a lot of possible improvements which may or may not be implemented in future.

Some of the more glaring things to do:

* **Add unit and integration tests**
* Save the cassettes in a human-readable format


## Credits

This tool has been inspired by [Throng OSCDeck](https://code.google.com/p/throng/), a similar tool written by [Johannes Luderschmidt](http://johannesluderschmidt.de/).


## License

The MIT License

Copyright (c) 2013 Julian Maicher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.