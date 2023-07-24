package userUtil

import zio._
import os.Path

object UserUtil {
  val path = os.pwd / os.up / "inputs"
  val inputFileNames = ZIO.attempt(os.list(path).map(_.last))

  val newLineTask = System.property("line.separator")

  def getNewLine() = for {
    newLineProp <- newLineTask

    result <- ZIO.getOrFail(newLineProp)
  } yield result

  def readTextFile(filename: String) = for {
    fileNames <- inputFileNames

    filename <- ZIO.getOrFail(
      fileNames.find(name => name.contains(filename))
    )

    file <- ZIO.attempt(os.read(path / filename)).catchAll(err => ZIO.fail(err))
  } yield file
}
