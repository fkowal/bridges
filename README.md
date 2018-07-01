# Bridges

Generate bindings for Scala types in other programming languages.

Copyright 2017 Dave Gurnell. Licensed [Apache 2.0][license].

[![Build Status](https://travis-ci.org/davegurnell/bridges.svg?branch=develop)](https://travis-ci.org/davegurnell/bridges)
[![Coverage status](https://img.shields.io/codecov/c/github/davegurnell/bridges/develop.svg)](https://codecov.io/github/davegurnell/bridges)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.davegurnell/bridges_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.davegurnell/bridges_2.12)

## Getting Started

Grab the code by adding the following to your `build.sbt`:

~~~ scala
libraryDependencies += "com.davegurnell" %% "bridges" % "<<VERSION>>"
~~~

## Synopsis

### Render Typescript/Flow Declarations for Scala ADTs

Create a simple data model:

~~~ scala
final case class Color(red: Int, green: Int, blue: Int)

sealed abstract class Shape extends Product with Serializable
final case class Circle(radius: Double, color: Color) extends Shape
final case class Rectangle(width: Double, height: Double, color: Color) extends Shape
~~~

Call `declaration[Foo]` to generate a type-declaration for `Foo`.
Call `render[Typescript](...)` to convert a list of declarations as Typescript,
or `render[Flow](...)` to render them as Flow types.

~~~ scala
import bridges._
import bridges.syntax._

render(List(
  declaration[Color],
  declaration[Circle],
  declaration[Rectangle],
  declaration[Shape]
))
// res1: String =
// export type Color = {
//   red: number,
//   green: number,
//   blue: number
// };
//
// export type Circle = {
//   radius: number,
//   color: Color
// };
//
// export type Rectangle = {
//   width: number,
//   height: number,
//   color: Color
// };
//
// export type Shape =
//   ({ type: "Circle" } & Circle) |
//   ({ type: "Rectangle" } & Rectangle);
~~~

### Elm extensions

Support for Elm has been added to the project. You can use the `render` method, as described above, to convert an ADT to an Elm type that compiles in Elm 0.18.x.

Additionally, the following methods are available (with some caveats described below):

- `jsonDecoder` : generates a valid Elm `Decode.Decoder` for your ADT
- `jsonEncoder` : generates a valid Elm `Encoder` for your ADT
- `buildFile` : returns a pair (String, String) where the `first element` is the name of an Elm file, and the `second element` is the content for that file. The file contains the ADT in Elm, the json decoder, and the json encoder. The module can be passed as a parameter, so you can copy the contents to a location of your choice.

Note that `buildFile` has a special behaviour: if you provide a list of `declaration` as the input, the output will be the contents of a single file, including the definitions of all the elements provided. This is done so we can avoid circular references in some scenarios.  


Please note if you want to use any of the json encode or decode generated by the project, you must do the following:

* Your Elm project *must* include the `NoRedInk/elm-decode-pipeline` dependency
* We assume any non-primitive type in your ADT will be generated by Bridge in the same module as the current ADT, to be able to define the right imports for them.
* If your ADT is/has a `CoProduct`, the generated json must distinguish between alternatives using a fields named `type` which encodes the name of the coproduct instance as a `String`. If you use [Circe](https://circe.github.io/circe/), see [this link](https://github.com/circe/circe/pull/429)


[license]: http://www.apache.org/licenses/LICENSE-2.0
