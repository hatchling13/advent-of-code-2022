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

    // Refactor
    inventories = file
      .split(newLine * 2)
      .toList
      .map(inventory =>
        inventory.split(newLine).toList.map(calory => calory.toInt)
      )

    calories = inventories.map(inventory => inventory.sum)

    _ <- Console.printLine(
      s"The Elf carrying the most Calories is carrying ${calories.max} calories!"
    )

    topThree = calories.sortBy(x => x).takeRight(3)

    _ <- Console.printLine(
      s"The top three Elves are carrying ${topThree.sum} calories!"
    )

  } yield ()
}
