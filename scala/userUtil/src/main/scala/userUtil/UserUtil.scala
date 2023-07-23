package userUtil

import zio._
import os.Path

object UserUtil {
  val newLine = sys.props("line.separator")
  val path = os.pwd / os.up / "inputs"
  val inputFileNames = ZIO.attempt(os.list(path).map(_.last))

  def readTextFile(filename: String) = for {
    fileNames <- inputFileNames

    filename <- ZIO.getOrFail(
      fileNames.find(name => name.contains(filename))
    )

    file <- ZIO.attempt(os.read(path / filename)).catchAll(err => ZIO.fail(err))
  } yield file
}
