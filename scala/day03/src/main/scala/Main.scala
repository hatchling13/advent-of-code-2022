import zio._
import userUtil.UserUtil.{readTextFile, newLine}

object Main extends ZIOAppDefault {
  def appearanceSet(rucksack: String): Set[Char] = {
    var frequency = Set[Char]()

    rucksack.foreach(item => {
      frequency += item
    })

    return frequency
  }

  def convert(char: Char): Int = {
    return char.isLower match {
      case true  => char.toInt - 'a'.toInt + 1
      case false => char.toInt - 'A'.toInt + 1 + 26
    }
  }

  override def run = for {
    file <- readTextFile("day03.txt")

    rucksacks = for {
      line <- file.split(newLine).toList
      first = line.take(line.length() / 2)
      second = line.takeRight(line.length() / 2)
    } yield (first, second)

    priorities = for {
      (compartment1, compartment2) <- rucksacks
      appearance1 = appearanceSet(compartment1)
      appearance2 = appearanceSet(compartment2)
      intersect = appearance1 & appearance2
      priority = convert(intersect.head)
    } yield priority

    _ <- Console.printLine(s"The sum of the priorities is ${priorities.sum}!")
  } yield ()
}
