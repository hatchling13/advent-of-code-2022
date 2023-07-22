import zio._
import os.Path

object Main extends ZIOAppDefault {
  val newLine = sys.props("line.separator")
  val path = os.pwd / os.up / "inputs"
  val inputFileNames = ZIO.attempt(os.list(path).map(_.last))

  def run = for {
    fileNames <- inputFileNames

    filename <- ZIO.getOrFail(
      fileNames.find(name => name.contains("day01.txt"))
    )

    file <- ZIO.attempt(os.read(path / filename)).catchAll(err => ZIO.fail(err))

    calories = for {
      splited <- file.split(newLine * 2).toList
      inventory = splited.split(newLine)
      calory = for {
        value <- inventory
      } yield value.toInt
      result = calory.sum
    } yield result

    _ <- Console.printLine(
      s"The Elf carrying the most Calories is carrying ${calories.max} calories!"
    )

    topThree = calories.sortBy(x => x).takeRight(3)

    _ <- Console.printLine(
      s"The top three Elves are carrying ${topThree.sum} calories!"
    )

  } yield ()
}
