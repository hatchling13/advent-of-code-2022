import zio._
import userUtil.UserUtil.{readTextFile, getNewLine}

object Main extends ZIOAppDefault {

  def run = for {
    newLine <- getNewLine()
    file <- readTextFile("day01.txt")

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
