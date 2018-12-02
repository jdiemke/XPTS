## XPTS
[![Build Status](https://travis-ci.org/jdiemke/XPTS.svg?branch=master)](https://travis-ci.org/jdiemke/XPTS)
[![GitHub stars](https://img.shields.io/github/stars/jdiemke/XPTS.svg)](https://github.com/jdiemke/XPTS/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/jdiemke/XPTS.svg)](https://github.com/jdiemke/XPTS/network)
[![GitHub issues](https://img.shields.io/github/issues/jdiemke/XPTS.svg)](https://github.com/jdiemke/XPTS/issues)
[![GitHub license](https://img.shields.io/github/license/jdiemke/XPTS.svg)](https://github.com/jdiemke/XPTS/blob/master/LICENSE)
[![Twitter](https://img.shields.io/twitter/url/https/github.com/jdiemke/XPTS.svg?style=social)](https://twitter.com/intent/tweet?text=Wow:&url=https%3A%2F%2Fgithub.com%2Fjdiemke%2FXPTS)

XPTS is a tool for creating procedural textures using the operator stacking paradigm first introduced by the demogroup farbrausch.

### How to get
Type the following command into your shell:
```bash
git clone https://github.com/jdiemke/XPTS.git
```
This will create a copy of the repository in your current working directory.
### What's included
Within the download you'll find the following directories and files. You'll see something like this:
```
XPTS/
├── images/
├── plugins/
├── textures/
├── src/
├── LICENSE
├── README.md
└── build.gradle
```
### How to build
XPTS uses Gradle as a build tool. If you want to build a distribution, then it is sufficient to locate into the project's root directory and type the following command into your shell:
```bash
gradle distribution
```
This will cause Gradle to build `XPTS.zip` in `build/distribution/`.
### Dependencies
XPTS has the following dependencies:
-   JOGL 2.3.2
-   GlueGen 2.3.2
-   Substance Look and Feel 6.0
-   SwingLabs SwingX 1.6.1
-   [RSyntaxTextArea 2.5.8](https://github.com/bobbylight/RSyntaxTextArea) - A syntax highlighting, code folding text editor for Java Swing applications.
-   JGoodies Forms 1.0.5

### Gallery
![Texture Gallery](https://raw.githubusercontent.com/jdiemke/XPTS/master/images/texture-gallery.png "Texture Gallery")
### License
XPTS is protected by the very permissive MIT license. This means you can do anything you want with the code with some minor restrictions related to attribution and liability (see the license below for more details). Nevertheless, it is prefered, but not necessary, that you share your enhancements concerning the project's source code.
```
The MIT License (MIT)

Copyright (c) 2016 Johannes Diemke

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
